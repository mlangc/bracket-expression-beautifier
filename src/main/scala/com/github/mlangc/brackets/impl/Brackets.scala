package com.github.mlangc.brackets.impl

object Brackets {
  def apply(str: String): Brackets = str.toCharArray() match {
    case Array(open, close) => Brackets(open.toString, close.toString)
    case _ => throw new IllegalArgumentException(s"Cannot make brackets from '$str'")
  }
}

case class Brackets(open: String, close: String) {
  require(open != close, "Opening and closing brackets must differ")
}