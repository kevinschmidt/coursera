package kvstore

import akka.actor.{ OneForOneStrategy, Props, ActorRef, Actor }
import kvstore.Arbiter._
import scala.collection.immutable.Queue
import akka.actor.SupervisorStrategy.Restart
import scala.annotation.tailrec
import akka.pattern.{ ask, pipe }
import akka.actor.Terminated
import scala.concurrent.duration._
import akka.actor.PoisonPill
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.util.Timeout
import akka.actor.ActorLogging

object Replica {
  sealed trait Operation {
    def key: String
    def id: Long
  }
  case class Insert(key: String, value: String, id: Long) extends Operation
  case class Remove(key: String, id: Long) extends Operation
  case class Get(key: String, id: Long) extends Operation

  sealed trait OperationReply
  case class OperationAck(id: Long) extends OperationReply
  case class OperationFailed(id: Long) extends OperationReply
  case class GetResult(key: String, valueOption: Option[String], id: Long) extends OperationReply

  case object RetryPersist
  case class CheckReplication(id: Long)

  def props(arbiter: ActorRef, persistenceProps: Props): Props = Props(new Replica(arbiter, persistenceProps))
}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor with ActorLogging {
  import Replica._
  import Replicator._
  import Persistence._
  import context.dispatcher

  context.system.scheduler.schedule(10.milliseconds, 10.milliseconds, self, RetryPersist)
  override val supervisorStrategy = OneForOneStrategy() {
    case _: PersistenceException => Restart
  }
  val persistence = context.actorOf(persistenceProps)

  var kv = Map.empty[String, String]
  var currentPersist: Option[(ActorRef, Persist)] = None
  var expectedSeq = 0

  // a map from secondary replicas to replicators
  var secondaries = Map.empty[ActorRef, ActorRef]
  // a map of operation id to sender and replicators with outstanding acks
  var replicating = Map.empty[Long, (ActorRef, Set[ActorRef])]

  override def preStart() {
    arbiter ! Join
  }

  def receive = {
    case JoinedPrimary   => {
      processReplicas(Set(self))
      context.become(leader)
    }
    case JoinedSecondary => context.become(replica)
  }

  /* Behavior for the replica role. */
  val replica: Receive = {
    case Get(key, id) => getValue(sender, key, id)
    case Snapshot(key, valueOption, seq) => processSnapshot(key, valueOption, seq)
    case Persisted(key, seq) => ackSnapshot(key, seq)
    case RetryPersist => retrySnapshot()
  }

  /* Behavior for  the leader role. */
  val leader: Receive = replica.orElse {
    case Replicas(replicas) => processReplicas(replicas).foreach( replayData(_) )
    case Insert(key, value, id) => startReplication(sender, key, Some(value), id)
    case Remove(key, id) => startReplication(sender, key, None, id)
    case Replicated(key, id) => {
      if (id != -1) {
        registerReplication(id, sender)
      }
    }
    case CheckReplication(id) => checkReplication(id)
  }

  private[this] def getValue(requester: ActorRef, key: String, id: Long) {
    requester ! GetResult(key, kv.get(key), id)
  }

  private[this] def processSnapshot(key: String, valueOption: Option[String], seq: Long) {
    seq.compare(expectedSeq) match {
      case cmp if cmp > 0 => log.warning(s"ignoring future sequence number [req/exc]: [$seq/$expectedSeq] [$key:$valueOption]")
      case cmp if cmp < 0 => {
        log.warning(s"ignoring outdated sequence number [req/exc]: [$seq/$expectedSeq] [$key:$valueOption]")
        sender ! SnapshotAck(key, seq)
      }
      case _ => {
        log.info(s"processing [$seq] [$key:$valueOption]")
        valueOption match {
          case Some(value) => kv += (key -> value)
          case None => kv -= key
        }
        persistValue(sender, key, valueOption, seq)
      }
    }
  }

  private[this] def ackSnapshot(key: String, seq: Long) {
    currentPersist.foreach { persist =>
      expectedSeq += 1
      persist._1 ! SnapshotAck(key, seq)
    }
    currentPersist = None
  }

  private[this] def retrySnapshot() {
    currentPersist.foreach { persist =>
      persistence ! persist._2
    }
  }

  private[this] def persistValue(requester: ActorRef, key: String, valueOption: Option[String], seq: Long) {
    val persist = Persist(key, valueOption, seq)
    currentPersist = Some((sender, persist))
    persistence ! persist
  }

  private[this] def startReplication(requester: ActorRef, key: String, valueOption: Option[String], id: Long) {
    secondaries.values.foreach { replicator =>
      replicator ! Replicate(key, valueOption, id)
    }
    replicating += (id -> (requester, secondaries.values.toSet))
    context.system.scheduler.scheduleOnce(1.seconds, self, CheckReplication(id))
  }

  private[this] def registerReplication(id: Long, replicator: ActorRef) {
    replicating.get(id).foreach { entry =>
      var (sender, reps) = entry
      reps -= replicator
      if (reps.isEmpty) {
        replicating -= id
        sender ! OperationAck(id)
      } else {
        replicating += (id -> (sender, reps))
      }
    }
  }

  private[this] def stopReplication(replicator: ActorRef) {
    replicating.keys.foreach { id =>
      registerReplication(id, replicator)
    }
  }

  private[this] def checkReplication(id: Long) {
    replicating.get(id).foreach { replication =>
      val (sender, replicas) = replication
      sender ! OperationFailed(id)
      replicating -= id
    }
  }

  private[this] def processReplicas(replicas: Set[ActorRef]) = {
    // kill replicators for removed replicas
    (secondaries.keys.toSet -- replicas) foreach { replica =>
      stopReplication(secondaries(replica))
      secondaries(replica) ! PoisonPill
      secondaries -= replica
    }
    // add new replicators
    val newReplicas = (replicas -- secondaries.keys)
    newReplicas.foreach { replica =>
      val replicator = context.actorOf(Replicator.props(replica))
      secondaries += (replica -> replicator)
    }
    newReplicas
  }

  private[this] def replayData(replica: ActorRef) {
    kv foreach { entry =>
      val (key, value) = entry
      secondaries(replica) ! Replicate(key, Some(value), -1)
    }
  }
}
