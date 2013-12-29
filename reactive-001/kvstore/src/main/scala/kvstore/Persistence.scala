package kvstore

import akka.actor.{Props, Actor}
import scala.util.Random
import java.util.concurrent.atomic.AtomicInteger
import akka.actor.ActorLogging

object Persistence {
  case class Persist(key: String, valueOption: Option[String], id: Long)
  case class Persisted(key: String, id: Long)

  class PersistenceException extends Exception("Persistence failure") {
    setStackTrace(Array.empty[StackTraceElement])
  }

  def props(flaky: Boolean): Props = Props(classOf[Persistence], flaky)
}

class Persistence(flaky: Boolean) extends Actor with ActorLogging {
  import Persistence._

  override def preRestart(reason: Throwable, message: Option[Any]) = {
    reason match {
      case _: PersistenceException => {
        log.warning(s"restart with message [$message]")
      }
    }
  }

  def receive = {
    case Persist(key, _, id) =>
      if (!flaky || Random.nextBoolean()) sender ! Persisted(key, id)
      else throw new PersistenceException
  }

}
