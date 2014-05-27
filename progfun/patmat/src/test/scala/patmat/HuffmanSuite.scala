package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("times for hello world") {
    assert(times(string2Chars("hello, world")) === List(('e', 1), (' ', 1), (',', 1), ('h', 1), ('r', 1), ('w', 1), ('d', 1), ('o', 2), ('l', 3)))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }

  test("combine of some leaf list with weight test") {
    val leaflist = List(Leaf('e', 2), Leaf('t', 3), Leaf('x', 4), Leaf('z', 7))
    assert(combine(leaflist) === List(Leaf('x',4), Fork(Leaf('e',2),Leaf('t',3),List('e', 't'),5), Leaf('z',7)))
  }

  test("until is working") {
    val leaflist = List(Leaf('e', 2), Leaf('t', 3), Leaf('x', 4), Leaf('z', 7))
    assert(until(singleton, combine)(leaflist) === Fork(
      Leaf('z', 7),
      Fork(
        Leaf('x', 4),
        Fork(
          Leaf('e',2),
          Leaf('t',3),
          List('e', 't'),
          5
        ),
        List('x', 'e', 't'),
        9
      ),
      List('z', 'x', 'e', 't'),
      16
    )
  )}

  test("create code tree from chars") {
    assert(createCodeTree(string2Chars("hollole")) === Fork(
      Leaf('l', 3),
      Fork(
        Fork(
          Leaf('e', 1),
          Leaf('h', 1),
          List('e', 'h'),
          2
        ),
        Leaf('o', 2),
        List('e', 'h', 'o'),
        4
      ),
      List('l', 'e', 'h', 'o'),
      7
    ))
  }

  test("decode french secret") {
    assert(decodedSecret === string2Chars("huffmanestcool"))
  }

  test("encode french secret") {
    assert(encode(frenchCode)(string2Chars("huffmanestcool")) === secret)
  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }

  test("quickEncode french secret") {
    assert(quickEncode(frenchCode)(string2Chars("huffmanestcool")) === secret)
  }
}
