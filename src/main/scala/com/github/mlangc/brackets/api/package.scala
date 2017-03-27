package com.github.mlangc.brackets

import com.github.mlangc.brackets.impl.Beautifier
import com.github.mlangc.brackets.impl.DefaultBeautifier

package object api {
  implicit def defaultBeautifier: Beautifier = DefaultBeautifier

  def beautify(input: String)(implicit beautifier: Beautifier): String = {
    beautifier.format(input)
  }
}
