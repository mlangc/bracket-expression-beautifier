package com.github.mlangc.demo

import com.github.mlangc.brackets.DefaultBeautifier
import scala.tools.reflect.ToolBox
import scala.reflect.runtime.universe._

object Asts {
  private def beautifier = DefaultBeautifier
  private val toolbox = runtimeMirror(getClass.getClassLoader).mkToolBox()

  def format(scalaSnippet: String): String = {
    beautifier.format {
      showRaw(toolbox.parse(scalaSnippet))
    }
  }
}
