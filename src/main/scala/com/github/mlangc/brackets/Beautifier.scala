package com.github.mlangc.brackets

import scala.IndexedSeq
import scala.util.matching.Regex

case class Brackets(open: String, close: String) {
  require(open != close, "Opening and closing brackets must differ")
}

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
 * A configurable beautifier implemented for, but not limited to, viewing Scala ASTs.
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
    s"${classOf[Beautifier].getSimpleName}($brackets, $indent, $maxNestingBeforeLineBreak)"
  }

  private def format(trees: IndexedSeq[StrTree], cindent: Int = 0): String = {
    trees.foldLeft("") { (acc, tree) =>
      val breakLine = tree.height > maxNestingBeforeLineBreak
      val nindent = if (breakLine) cindent + indent else cindent
      val (beforeCloseingBrace, closingBrace) = {
        val subtrees = tree.subtrees
        if (subtrees.isEmpty) {
          (IndexedSeq(), None)
        } else if (subtrees.size == 1) {
          if (!isClosingBrace(subtrees(0))) (IndexedSeq(subtrees(0)), None)
          else (IndexedSeq(), Some(subtrees(0)))
        } else if (isClosingBrace(subtrees.last)) {
          (subtrees.init, Some(subtrees.last))
        } else {
          (subtrees, None)
        }
      }

      val closeExpr = closingBrace.map { brace =>
        (if (breakLine) "\n" + (" " * cindent) else "") + brace.str
      }.getOrElse("")

      val subtreeStr = {
        if (beforeCloseingBrace.isEmpty) None
        else Some(format(beforeCloseingBrace, nindent))
      }.map { str =>
        (if (breakLine) "\n" + (" " * nindent) else "") + str
      }.getOrElse("")

      acc + tree.str + subtreeStr + closeExpr
    }
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
      val partitions = parts.foldLeft(IndexedSeq[(String, IndexedSeq[String], Int)]()) { (acc, str) =>
        val ((cstr, cseq, clevel), replace) = {
          if (acc.isEmpty || acc.last._3 == 0) {
            ((str, IndexedSeq(), 0), false)
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
        val nlevel = math.max(clevel + levelDelta, 0)
        val firstOpeningBracket = levelDelta == 1 && clevel == 0
        val nseq = if (!strHandled) cseq :+ str else cseq
        val nstr = if (firstOpeningBracket && !strHandled) cstr + str else cstr
        val nacc = (nstr, nseq, nlevel)

        if (acc.isEmpty) IndexedSeq(nacc)
        else if (replace) acc.init :+ nacc
        else acc :+ nacc
      }

      partitions.foldLeft(IndexedSeq[StrTree]()) { (acc, part) =>
        val (str, parts, _) = part
        acc :+ StrTree(str, toStringTrees(parts))
      }
    }
  }

  private def split(input: String): IndexedSeq[String] = {
    rxSplit.split(input)
  }
}
