package com.github.mlangc.demo

import com.github.mlangc.brackets.{DefaultBeautifier => beautifier}
import scala.tools.reflect.ToolBox
import scala.reflect.runtime.universe._

object PcAsts {
  private def classLoader = getClass.getClassLoader
  private val toolbox = runtimeMirror(classLoader).mkToolBox()

  def format(scalaSnippet: String): String = {
    beautifier.format {
      val tree = toolbox.typecheck(toolbox.parse(scalaSnippet))
      showRaw(tree)
    }
  }
}
