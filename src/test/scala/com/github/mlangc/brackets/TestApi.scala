package com.github.mlangc.brackets

import com.github.mlangc.UnitTest
import com.github.mlangc.brackets.impl.Beautifier
import com.github.mlangc.brackets.impl.Brackets

class TestApi extends UnitTest {
  "Make sure that we can beautify" - {
    "with default settings" - {
      import com.github.mlangc.brackets.api._

      "looking at trivial examples" in {
        assert(beautify("") == "")
        assert(beautify("xxx") == "xxx")
        assert(beautify("()") == "()")
      }

      "looking at typical examples" in {
        assert {
          beautify("""Defn.Val(Nil, Seq(Pat.Var.Term(Term.Name("x"))), None, Lit(42))""") ==
            """Defn.Val(
              |  Nil, Seq(Pat.Var.Term(Term.Name("x"))), None, Lit(42)
              |)""".stripMargin
        }
      }
    }

    "with special settings" - {
      import com.github.mlangc.brackets.api._
      implicit val defaultBeautifier = new Beautifier(Brackets("[]"), indent = 4)

      "looking at trivial examples" in {
        assert(beautify("") == "")
        assert(beautify("[]") == "[]")
      }

      "looking at an example that should not be modified" in {
        val shouldNotBeModified = """Defn.Val(Nil, Seq(Pat.Var.Term(Term.Name("x"))), None, Lit(42))"""
        assert(beautify(shouldNotBeModified) == shouldNotBeModified)
      }

      "looking at a string that should be adapted" in {
        val input = "[[[ [ [ !! ] ] ]]]"
        val expected = """[
                          |    [
                          |        [ [ [ !! ] ] ]
                          |    ]
                          |]""".stripMargin

        assert(beautify(input) == expected)
      }
    }
  }
}
