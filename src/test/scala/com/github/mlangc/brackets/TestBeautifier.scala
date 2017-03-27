package com.github.mlangc.brackets

import com.github.mlangc.UnitTest

object TestBeautifier {
  private val TypicalBeautifier = new Beautifier(Brackets("()"), 2, 1)
  private val CompactBeautifier = new Beautifier(Brackets("()"), 1, 3)
  private val ElaborateBeautifier = new Beautifier(Brackets("()"), 4, 0)
}

class TestBeautifier extends UnitTest {
import TestBeautifier._

  "Test a typical beautifier with" - {
    "an empty string" in {
      testWithTypicalBeauty("", "")
    }

    "a string without any braces" in {
      testWithTypicalBeauty("braceless", "braceless")
    }

    "a string with braces, that should be left alone" in {
      testWithTypicalBeauty("test()", "test()")
    }

    "a simple expression" in {
      testWithTypicalBeauty(
        "Expr(Literal(Constant(1)))",
     """|Expr(
        |  Literal(
        |    Constant(1)
        |  )
        |)""".stripMargin)
    }

    "with a more elaborate expression" in {
      testWithTypicalBeauty(
        """Expr(Apply(Select(Ident(scala.collection.immutable.List), TermName("apply")), List(Literal(Constant(1)), Literal(Constant(2)))))""",
       """|Expr(
          |  Apply(
          |    Select(
          |      Ident(scala.collection.immutable.List), TermName("apply")
          |    ), List(
          |      Literal(
          |        Constant(1)
          |      ), Literal(
          |        Constant(2)
          |      )
          |    )
          |  )
          |)""".stripMargin)
    }

    "Test a compact beautifier with" - {
      "1 :: Nil" in {
      testWithCompactBeauty(
        """Expr(Block(List(ValDef(Modifiers(SYNTHETIC | ARTIFACT), TermName("x$1"), TypeTree(), Literal(Constant(1)))), Apply(Select(Ident(scala.collection.immutable.Nil), TermName("$colon$colon")), List(Ident(TermName("x$1"))))))""",
       """|Expr(
          | Block(
          |  List(
          |   ValDef(Modifiers(SYNTHETIC | ARTIFACT), TermName("x$1"), TypeTree(), Literal(Constant(1)))
          |  ), Apply(
          |   Select(Ident(scala.collection.immutable.Nil), TermName("$colon$colon")), List(Ident(TermName("x$1")))
          |  )
          | )
          |)""".stripMargin)
      }
    }

    "Test an elaborate beautifier with" - {
      "an almost trivial example" in {
        testWithElaborateBeauty(
          "()",
       """|(
          |)""".stripMargin)
      }

      "a very easy example" in {
         testWithElaborateBeauty(
            "(a, b)",
          """|(
             |    a, b
             |)""".stripMargin)
        }
      }

      "a single open bracket" in {
         testWithElaborateBeauty("(", "(")
      }

      "a single closed bracket" in {
         testWithElaborateBeauty(")", ")")
      }

      "an epxression missing a closed bracket" in {
         testWithElaborateBeauty(
            "(a * (b + c",
          """|(
             |    a * (
             |        b + c""".stripMargin)
      }

      "an epxression that starts with a stray closed bracket" in {
         testWithElaborateBeauty(
            ")(a * (b + c))",
          """|)(
             |    a * (
             |        b + c
             |    )
             |)""".stripMargin)
      }

      "a very simple expression that starts with a closing bracket" in {
         testWithElaborateBeauty(
            ")()",
          """|)(
             |)""".stripMargin)
      }

      "a completely b0rked expression" in {
         testWithElaborateBeauty(
            ")())))f(a, b))(",
          """|)(
             |))))f(
             |    a, b
             |))(""".stripMargin)
      }
  }

  private def testWithElaborateBeauty(input: String, expected: String) {
    testWith(ElaborateBeautifier, input, expected)
  }

  private def testWithTypicalBeauty(input: String, expected: String) {
    testWith(TypicalBeautifier, input, expected)
  }

  private def testWithCompactBeauty(input: String, expected: String) {
    testWith(CompactBeautifier, input, expected)
  }

  private def testWith(beauty: Beautifier, input: String, expected: String) = {
    beauty.format(input) should be (expected)
  }
}
