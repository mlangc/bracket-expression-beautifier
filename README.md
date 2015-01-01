# Bracket Expression Beautifier
*A simple beautifier for bracket expressions, implemented for, but not limited to, Scala ASTs.*

## Usage

Clone the repository, cd into it and run

	$ sbt console

Now you can use the beautifier like this:

    scala> val beauty = at.lnet.brackets.DefaultBeautifier
    beauty: at.lnet.brackets.DefaultBeautifier.type = Beautifier, Brackets((,)), 2, 3)
    scala> beauty.format("(2 * (3 + f(a, g(h(k(l(x) + 1) + 1) + 1) + 1)*5))")
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
    import scala.reflect.runtime.universe._
    scala> beauty.format(showRaw(reify { 3 + Seq(Seq(1, 2), Seq()).flatten.sum }))
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
	

