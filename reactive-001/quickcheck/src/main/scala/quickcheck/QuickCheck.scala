package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._
import scala.math._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }
  
  property("min2") = forAll { (a: Int, b: Int) =>
    val h = insert(b, insert(a, empty))
    findMin(h) == min(a, b)
  }
  
  property("delete1") = forAll { a: Int =>
    val h = deleteMin(insert(a, empty))
    isEmpty(h)
  }
  
  property("delete2") = forAll { h: H =>
    (! isEmpty(h) ) ==> {
      val result = findMin(h)
      val h2 = deleteMin(h)
      isEmpty(h2) || result <= findMin(h2)
    }
  }
  
  property("delete3") = forAll { (a: Int, b: Int, c: Int) =>
    val h = insert(c, insert(b, insert(a, empty)))
    val result = deleteMin(deleteMin(h))
    findMin(result) == max(max(a, b), c)
  }
  
  property("findMin1") = forAll { h: H =>
    val result = captureMin(h)
    result == result.sorted
  }
  
  property("findMin2") = forAll { (h1: H, h2: H) =>
    val result = meld(h1, h2)
    findMin(result) == min(findMin(h1), findMin(h2)) 
  }
  
  def captureMin(heap: H) : List[Int]  = {
    if (isEmpty(heap)) {
      Nil
    } else {
      findMin(heap) :: captureMin(deleteMin(heap))
    }
  }

  lazy val genHeap: Gen[H] = for {
    i <- arbitrary[Int]
    h <- oneOf(value(empty), genHeap)
  } yield insert(i, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

}
