package com.github.mlangc.brackets

object Brackets {
  def apply(str: String): Brackets = str.toCharArray() match {
    case Array(open, close) => Brackets(open.toString, close.toString)
  }
}

case class Brackets(open: String, close: String) {
  require(open != close, "Opening and closing brackets must differ")
}