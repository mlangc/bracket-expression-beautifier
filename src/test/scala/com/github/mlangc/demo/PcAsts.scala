package com.github.mlangc.demo

import scala.tools.reflect.ToolBox
import scala.reflect.runtime.universe._

import com.github.mlangc.brackets.api._

object PcAsts {
  private def classLoader = getClass.getClassLoader
  private val toolbox = runtimeMirror(classLoader).mkToolBox()

  def format(scalaSnippet: String): String = {
    beautify {
      val tree = toolbox.typecheck(toolbox.parse(scalaSnippet))
      showRaw(tree)
    }
  }
}
