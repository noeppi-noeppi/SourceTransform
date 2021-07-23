package io.github.noeppi_noeppi.tools.sourcetransform.transform

import java.util.Locale

sealed abstract class Case {
  def apply(str: String): String
}

object Case {
  
  // Gets case and transform to camel case
  def findCase(str: String): (Case, String) = {
    if (str.toUpperCase(Locale.ROOT) == str) {
      (UPPER_SNAKE_CASE, str.toLowerCase(Locale.ROOT))
    } else {
      if (str.contains("_")) {
        if (str.nonEmpty && str.head.isUpper) {
          (CAPITALIZED_SNAKE_CASE, str.toLowerCase(Locale.ROOT))
        } else {
          (SNAKE_CASE, str.toLowerCase(Locale.ROOT))
        }
      } else {
        if (str.nonEmpty && str.head.isUpper) {
          (UPPER_CAMEL_CASE, transformAnyCamelCase(str))
        } else {
          (CAMEL_CASE, transformAnyCamelCase(str))
        }
      }
    }
  }
  
  private def transformAnyCamelCase(str: String): String = {
    val sb = new StringBuilder
    for (c <- str) {
      if (c.isUpper) {
        sb.append('_')
      }
      sb.append(c.toLower)
    }
    sb.toString().dropWhile(_ == '_')
  }
  
  case object CAMEL_CASE extends Case {
    override def apply(str: String): String = {
      var upper = false
      val sb = new StringBuilder
      for (c <- str) {
        if (c == '_') {
          upper = true
        } else if (upper) {
          sb.append(c.toUpper)
          upper = false
        } else {
          sb.append(c)
        }
      }
      sb.toString()
    }
  }
  
  case object UPPER_CAMEL_CASE extends Case {
    override def apply(str: String): String = {
      val r = CAMEL_CASE(str)
      if (r.nonEmpty) "" + r.head.toUpper + r.tail else r
    }
  }
  
  case object SNAKE_CASE extends Case {
    override def apply(str: String): String = str
  }
  
  case object UPPER_SNAKE_CASE extends Case {
    override def apply(str: String): String = str.toUpperCase(Locale.ROOT)
  }
  
  case object CAPITALIZED_SNAKE_CASE extends Case {
    override def apply(str: String): String = {
      var upper = true
      val sb = new StringBuilder
      for (c <- str) {
        if (c == '_') {
          upper = true
          sb.append('_')
        } else if (upper) {
          sb.append(c.toUpper)
          upper = false
        } else {
          sb.append(c)
        }
      }
      sb.toString()
    }
  }
}
