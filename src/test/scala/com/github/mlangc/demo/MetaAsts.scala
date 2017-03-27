package com.github.mlangc.demo

import scala.meta.parsers.Parse
import scala.meta._

import com.github.mlangc.brackets.api._

object MetaAsts {
  def format[U <: Tree](scalaSnippet: String)(implicit parse: Parse[U]): String = {
    beautify {
      scalaSnippet.parse[U].get.structure.toString
    }
  }
}
