package com.github.mlangc.brackets

import org.scalameter.api._
import org.scalameter.picklers.noPickler._

object BeautifierBenchmark extends Bench.LocalTime {
  private case class InputContainer(strs: Seq[String]) {
    override def toString: String = s"Input[${strs.size} strings]"
  }

  private val strings = Gen.single("input")(InputContainer(
    Seq(
      """Expr(Apply(Select(Apply(Apply(Select(Apply(Select(Ident(scala.collection.immutable.List), TermName("apply")), List(Select(Ident(scala.collection.immutable.Nil), TermName("size")), Select(Apply(Select(Ident(scala.collection.Seq), TermName("apply")), List()), TermName("size")), Select(Apply(Select(Select(Ident(scala.Predef), TermName("Map")), TermName("apply")), List()), TermName("size")))), TermName("map")), List(Function(List(ValDef(Modifiers(PARAM | SYNTHETIC), TermName("x$1"), TypeTree(), EmptyTree)), Apply(Select(Ident(TermName("x$1")), TermName("$times")), List(Literal(Constant(2))))))), List(Select(Ident(scala.collection.immutable.List), TermName("canBuildFrom")))), TermName("sum")), List(Select(Ident(scala.math.Numeric), TermName("IntIsIntegral")))))""",
      """Expr(Apply(Select(Apply(Select(Apply(Select(Ident(scala.Some), TermName("apply")), List(Literal(Constant("")))), TermName("getOrElse")), List(Literal(Constant("a")))), TermName("$plus")), List(Literal(Constant("b")))))""",
      """Expr(Apply(Select(Ident(scala.collection.immutable.List), TermName("apply")), List(Select(Ident(scala.collection.immutable.Nil), TermName("size")), Select(Apply(Select(Ident(scala.collection.Seq), TermName("apply")), List()), TermName("size")), Select(Apply(Select(Select(Ident(scala.Predef), TermName("Map")), TermName("apply")), List()), TermName("size")))))""",
      """Expr(Apply(Select(Apply(Select(Select(Literal(Constant(3)), TermName("toLong")), TermName("hashCode")), List()), TermName("$plus")), List(Apply(Select(Literal(Constant("")), TermName("hashCode")), List()))))""",
      """Expr(Apply(Select(Apply(Select(Ident(scala.math.package), TermName("pow")), List(Literal(Constant(3.141592653589793)), Literal(Constant(2.0)))), TermName("$div")), List(Literal(Constant(2)))))"""
    )))

  performance of "DefaultTreeBeautifier" in {
    measure method "format" in {
      using(strings) config(exec.benchRuns -> 500)  in { case InputContainer(strs) =>
        strs.map(DefaultBeautifier.format)
      }
    }
  }
}
