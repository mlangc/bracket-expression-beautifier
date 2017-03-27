package com.github.mlangc.demo

import com.github.mlangc.brackets.{DefaultBeautifier => beautifier}
import scala.meta.parsers.Parse
import scala.meta._

object MetaAsts {
  def format[U <: Tree](scalaSnippet: String)(implicit parse: Parse[U]): String = {
    beautifier.format {
      scalaSnippet.parse[U].get.structure.toString
    }
  }
}
