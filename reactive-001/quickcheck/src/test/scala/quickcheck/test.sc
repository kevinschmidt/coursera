package quickcheck

object test {
  println("Welcome to the Scala worksheet")
  
  val s = new QuickCheckHeap with BinomialHeap
  val t = new QuickCheckHeap with Bogus3BinomialHeap
  s.insert(1, s)
  t
}