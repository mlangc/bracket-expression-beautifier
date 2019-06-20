# Bracket Expression Beautifier
*A simple beautifier for bracket expressions, implemented for, but not limited to, Scala ASTs.*

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mlangc/bracket-expression-beautifier_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mlangc/bracket-expression-beautifier_2.11)

## Usage

### General

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

## For Quickly Dumping a few ASTs to the Console

Clone the repo, cd into it and run 

    $ sbt test:console
 
This starts an [Ammonite-REPL](http://www.lihaoyi.com/Ammonite/#Ammonite-REPL) with a few predefined imports.
Use `MetaAsts.format` to print [scala.meta](http://scalameta.org/) ASTs and `NscAsts.format` to print ASTs from the
[Scala Compiler](http://www.scala-lang.org/api/2.12.1/scala-compiler/scala/tools/nsc/ast/Trees.html).

A sample session could look like

    Welcome to the Ammonite Repl 0.8.2
    @ MetaAsts.format(q"for(i <- 1.to(42)) yield i*i") 
    res0: String = """
    Term.ForYield(
      Seq(
        Enumerator.Generator(
          Pat.Var.Term(Term.Name("i")), Term.Apply(Term.Select(Lit(1), Term.Name("to")), Seq(Lit(42)))
        )
      ), Term.ApplyInfix(Term.Name("i"), Term.Name("*"), Nil, Seq(Term.Name("i")))
    )
    """
    @ NscAsts.format("for(i <- 1.to(42)) yield i*i") 
    res1: String = """
    Apply(
      Apply(
        TypeApply(
          Select(
            Apply(
              Select(
                Apply(
                  Select(
                    Select(This(TypeName("scala")), scala.Predef), TermName("intWrapper")
                  ), List(Literal(Constant(1)))
                ), TermName("to")
              ), List(Literal(Constant(42)))
            ), TermName("map")
          ), List(TypeTree(), TypeTree())
        ), List(
          Function(
            List(ValDef(Modifiers(PARAM), TermName("i"), TypeTree(), EmptyTree)), Apply(
              Select(Ident(TermName("i")), TermName("$times")), List(Ident(TermName("i")))
            )
          )
        )
      ), List(
        TypeApply(
    ...

