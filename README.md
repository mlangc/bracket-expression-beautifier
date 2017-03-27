# Bracket Expression Beautifier
*A simple beautifier for bracket expressions, implemented for, but not limited to, Scala ASTs.*

## Usage

    scala> import com.github.mlangc.brackets.api._
    scala> beautify("(2 * (3 + f(a, g(h(k(l(x) + 1) + 1) + 1) + 1)*5))")
    res0: String =
    (
      2 * (
        3 + f(
          a, g(
            h(k(l(x) + 1) + 1) + 1
          ) + 1
        )*5
      )
    )
    scala> import scala.reflect.runtime.universe._
    scala> beautify(showRaw(reify { 3 + Seq(Seq(1, 2), Seq()).flatten.sum }))
    res4: String =
    Expr(
      Apply(
        Select(Literal(Constant(3)), TermName("$plus")), List(
          Apply(
            Select(
              Apply(
                Select(
                  Apply(
                    Select(Ident(scala.collection.Seq), TermName("apply")), List(
                      Apply(
                        Select(Ident(scala.collection.Seq), TermName("apply")), List(Literal(Constant(1)), Literal(Constant(2)))
                      ), Apply(Select(Ident(scala.collection.Seq), TermName("apply")), List())
                    )
                  ), TermName("flatten")
                ), List(Select(Ident(scala.Predef), TermName("$conforms")))
              ), TermName("sum")
            ), List(Select(Ident(scala.math.Numeric), TermName("IntIsIntegral")))
          )
        )
      )
    )

If you want to add the beautifier as a dependency to your project, you can do so by add the following line to your build.sbt:

	libraryDependencies += "com.github.mlangc" %% "bracket-expression-beautifier" % "2.0.0"

The library is available for Scala-2.11 and Scala-2.12.

