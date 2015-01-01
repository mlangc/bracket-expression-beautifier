package at.lnet.brackets

import scala.util.matching.Regex
import scala.IndexedSeq

case class Brackets(open: String, close: String)

object Brackets {
  def apply(str: String): Brackets = str.toCharArray() match {
    case Array(open, close) => Brackets(open.toString, close.toString)
  }
}

object DefaultBeautifier extends Beautifier {

}

object Beautifier {
  /*
   * These trees are meant to be built like this:
   *
   *  - f(x):
   *    "f("
   *     /\
   *    /  \
   *  "x"  ")"
   *
   * -f(g(a), h(b)):
   *
   *      "f("
   *      /\-----\
   *     /  \    ")"
   *  "g(" ",h("
   *   /\    |\
   *  /  \   | \
   * "a" ") "b" ")"
   */
  private case class StrTree(str: String, subtrees: IndexedSeq[StrTree] = IndexedSeq()) {
    def isLeaf: Boolean = subtrees.isEmpty

    lazy val height: Int = {
      if (isLeaf) 0
      else 1 + subtrees.map(_.height).max
    }
  }
}

/**
 * A configurable beautifier for implemented for, but not limited to, viewing Scala ASTs.
 *
 * @param brackets usually "(" and ")"
 * @param indent the amount of indent after line breaks
 * @param maxHeightBeforeLineBreak defines how many nested "()" expressions are allowed, before a line break is inserted.
 */
class Beautifier(brackets: Brackets = Brackets("()"), indent: Int = 2, maxNestingBeforeLineBreak: Int = 3) {
  import Beautifier._

  require(indent > 0)
  require(maxNestingBeforeLineBreak >= 0)

  private val rxSplit = {
    val o = Regex.quote(brackets.open)
    val c = Regex.quote(brackets.close)
    s"""(?<=$o)|(?=$o)|(?<=$c)|(?=$c)""".r
  }

  def format(input: String): String = {
    format(toStringTrees(input))
  }

  override def toString = {
    "${(classOf[Beautifier].getSimpleName}, $brackets, $indent, $maxNestingBeforeLineBreak)"
  }

  private def format(trees: IndexedSeq[StrTree]): String = {
    def go(trees: IndexedSeq[StrTree], clevel: Int = 0, cindent: Int = 0): String = {
      trees.foldLeft("") { (acc, tree) =>
        val breakLine = tree.height > maxNestingBeforeLineBreak
        val nindent = if (breakLine) cindent + indent else cindent
        val nlevel = clevel + 1
        val (subtreesWoClosingBrace, closingBrace) = {
          val subs = tree.subtrees
          if (subs.isEmpty) {
            (IndexedSeq(), None)
          } else if (subs.size == 1) {
            if (!isClosingBrace(subs(0))) (IndexedSeq(subs(0)), None)
            else (IndexedSeq(), Some(subs(0)))
          } else if (isClosingBrace(subs.last)) {
            (subs.init, Some(subs.last))
          } else {
            (subs, None)
          }
        }

        val closeExpr = closingBrace.map { brace =>
          (if (breakLine) "\n" + (" " * cindent) else "") + brace.str
        }.getOrElse("")

        val subtreeStr = {
          if (subtreesWoClosingBrace.isEmpty) None
          else Some(go(subtreesWoClosingBrace, nlevel, nindent))
        }.map { str =>
          (if (breakLine) "\n" + (" " * nindent) else "") + str
        }.getOrElse("")

        acc + tree.str + subtreeStr + closeExpr
      }
    }
    go(trees)
  }

  private def isClosingBrace(tree: StrTree): Boolean = {
    tree.isLeaf && tree.str == brackets.close
  }

  private def toStringTrees(input: String): IndexedSeq[StrTree] = {
    val parts = split(input)
    toStringTrees(parts)
  }

  private def toStringTrees(parts: IndexedSeq[String]): IndexedSeq[StrTree] = {
    if (parts.isEmpty) {
      IndexedSeq()
    } else if (parts.size == 1) {
      IndexedSeq(StrTree(parts.head))
    } else {
      val partitions = parts.foldLeft(IndexedSeq[(String, IndexedSeq[String], Option[Int])]()) { (acc, str) =>
        val ((cstr, cseq, clevel), replace) = {
          if (acc.isEmpty || acc.last._3 == Some(0)) {
            ((str, IndexedSeq(), None), false)
          } else {
            (acc.last, true)
          }
        }

        val levelDelta = {
          if (str == brackets.open) 1
          else if (str == brackets.close) -1
          else 0
        }

        val strHandled = !replace
        val nlevel = clevel.orElse(Some(0)).map(_ + levelDelta)
        val firstOpeningBracket = levelDelta == 1 && clevel == None
        val nseq = if (!strHandled) cseq :+ str else cseq
        val nstr = if (firstOpeningBracket && !strHandled) cstr + str else cstr
        val nacc = (nstr, nseq, nlevel)

        if (acc.isEmpty) IndexedSeq(nacc)
        else if (replace) acc.init :+ nacc
        else acc :+ nacc
      }

      partitions.foldLeft(IndexedSeq[StrTree]()) { (acc, part) =>
        val (str, parts, level) = part
        acc :+ StrTree(str, toStringTrees(parts))
      }
    }
  }

  private def split(input: String): IndexedSeq[String] = {
    rxSplit.split(input)
  }
}
