package quickcheck

object test {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(77); 
  println("Welcome to the Scala worksheet");$skip(50); 
  
  val s = new QuickCheckHeap with BinomialHeap;System.out.println("""s  : quickcheck.QuickCheckHeap with quickcheck.BinomialHeap = """ + $show(s ));$skip(53); 
  val t = new QuickCheckHeap with Bogus3BinomialHeap;System.out.println("""t  : quickcheck.QuickCheckHeap with quickcheck.Bogus3BinomialHeap = """ + $show(t ));$skip(17); val res$0 = 
  s.insert(1, s);System.out.println("""res0: <error> = """ + $show(res$0));$skip(4); val res$1 = 
  t;System.out.println("""res1: quickcheck.QuickCheckHeap with quickcheck.Bogus3BinomialHeap = """ + $show(res$1))}
}
