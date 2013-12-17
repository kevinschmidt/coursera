/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package actorbintree

import akka.actor._
import scala.collection.immutable.Queue

object BinaryTreeSet {

  trait Operation {
    def requester: ActorRef
    def id: Int
    def elem: Int
  }

  trait OperationReply {
    def id: Int
  }

  /** Request with identifier `id` to insert an element `elem` into the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Insert(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to check whether an element `elem` is present
    * in the tree. The actor at reference `requester` should be notified when
    * this operation is completed.
    */
  case class Contains(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to remove the element `elem` from the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Remove(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request to perform garbage collection*/
  case object GC

  /** Holds the answer to the Contains request with identifier `id`.
    * `result` is true if and only if the element is present in the tree.
    */
  case class ContainsResult(id: Int, result: Boolean) extends OperationReply
  
  /** Message to signal successful completion of an insert or remove operation. */
  case class OperationFinished(id: Int) extends OperationReply

}


class BinaryTreeSet extends Actor with ActorLogging {
  import BinaryTreeSet._
  import BinaryTreeNode._

  def createRoot: ActorRef = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = true))

  var root = createRoot

  // optional
  var pendingQueue = Queue.empty[Operation]

  // optional
  def receive = normal

  // optional
  /** Accepts `Operation` and `GC` messages. */
  val normal: Receive = { 
    case opr: Operation => root ! opr
    case GC => {
      val newRoot = createRoot
      root ! CopyTo(newRoot)
      context.become(garbageCollecting(newRoot))
    }
  }

  // optional
  /** Handles messages while garbage collection is performed.
    * `newRoot` is the root of the new binary tree where we want to copy
    * all non-removed elements into.
    */
  def garbageCollecting(newRoot: ActorRef): Receive = {
    case opr: Operation => pendingQueue :+= opr
    case CopyFinished => {
      root ! PoisonPill
      root = newRoot
      pendingQueue.foreach( root ! _ )
      pendingQueue = Queue.empty[Operation]
      context.become(normal)
    }
  }

}

object BinaryTreeNode {
  trait Position

  case object Left extends Position
  case object Right extends Position

  case class CopyTo(treeNode: ActorRef)
  case object CopyFinished

  def props(elem: Int, initiallyRemoved: Boolean) = Props(classOf[BinaryTreeNode],  elem, initiallyRemoved)
}

class BinaryTreeNode(val elem: Int, initiallyRemoved: Boolean) extends Actor with ActorLogging {
  import BinaryTreeNode._
  import BinaryTreeSet._

  var subtrees = Map[Position, ActorRef]()
  var removed = initiallyRemoved

  // optional
  def receive = normal

  // optional
  /** Handles `Operation` messages and `CopyTo` requests. */
  val normal: Receive = {
    case opr: Operation => opr.elem.compare(elem) match { 
      case cmp if cmp < 0 => passMessage(Left, opr)
      case cmp if cmp > 0 => passMessage(Right, opr)
      case _ => opr match {
        case Insert(requester, id, elem) => {
          removed = false
          requester ! OperationFinished(id)
        }
        case Contains(requester, id, elem) => requester ! ContainsResult(id, !removed)
        case Remove(requester, id, elem) => {
          removed = true
          requester ! OperationFinished(id)
        }
      }
    }
    case CopyTo(newRoot) => {
      var expected = Set.empty[ActorRef]
      subtrees.values.foreach { node =>
        node ! CopyTo(newRoot)
        expected += node
      }
      if (!removed) newRoot ! Insert(self, 0, elem)
      context.become(copying(expected, removed))
    }
  }

  private[this] def passMessage(pos: Position, opr: Operation) = {
    if ( subtrees.contains(pos) ) {
      subtrees(pos) ! opr
    } else {
      opr match {
        case Insert(requester, id, elem) => {
          subtrees += (pos -> context.actorOf(props(elem, false)))
          requester ! OperationFinished(id)
        }
        case Contains(requester, id, elem) => requester ! ContainsResult(id, false)
        case Remove(requester, id, elem) => requester ! OperationFinished(id)
      }
    }
  }

  // optional
  /** `expected` is the set of ActorRefs whose replies we are waiting for,
    * `insertConfirmed` tracks whether the copy of this node to the new tree has been confirmed.
    */
  def copying(expected: Set[ActorRef], insertConfirmed: Boolean): Receive = {
    if (expected.isEmpty && insertConfirmed) {
      context.parent ! CopyFinished
      normal
    } else {
      case OperationFinished(id) => context.become(copying(expected, true))
      case CopyFinished => {
        sender ! PoisonPill
        context.become(copying(expected - sender, insertConfirmed))
      }
    }
  }

}
