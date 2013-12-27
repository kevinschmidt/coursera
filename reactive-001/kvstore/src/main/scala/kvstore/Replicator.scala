package kvstore

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import scala.concurrent.duration._
import scala.actors.Scheduler
import akka.actor.DefaultScheduler
import akka.actor.DefaultScheduler

object Replicator {
  case class Replicate(key: String, valueOption: Option[String], id: Long)
  case class Replicated(key: String, id: Long)

  case class Snapshot(key: String, valueOption: Option[String], seq: Long)
  case class SnapshotAck(key: String, seq: Long)

  case object RetryAll

  def props(replica: ActorRef): Props = Props(new Replicator(replica))
}

class Replicator(val replica: ActorRef) extends Actor {
  import Replicator._
  import Replica._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */
  context.system.scheduler.schedule(100.milliseconds, 100.milliseconds, self, RetryAll)

  // map from sequence number to pair of sender and request
  var acks = Map.empty[Long, (ActorRef, Replicate)]
  // a sequence of not-yet-sent snapshots (you can disregard this if not implementing batching)
  var pending = Vector.empty[Snapshot]

  var _seqCounter = 0L
  def nextSeq = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }

  /* Behavior for the Replicator. */
  def receive: Receive = {
    case r: Replicate => {
      val seq = nextSeq
      acks += (seq -> (sender, r))
      replica ! Snapshot(r.key, r.valueOption, seq)
    }
    case SnapshotAck(key, seq) => {
      acks.get(seq).foreach { ackData =>
        val (sender, Replicate(key, valueOption, id)) = ackData
        sender ! Replicated(key, id)
      }
      acks -= seq
    }
    case RetryAll => {
      acks.foreach { ackData =>
        val (seq, (sender, Replicate(key, valueOption, id))) = ackData
        replica ! Snapshot(key, valueOption, seq)
      }
    }
  }

}
