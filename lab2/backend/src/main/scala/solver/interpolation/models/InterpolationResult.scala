package solver.interpolation.models

import solver.core.Message

case class InterpolationResult(
  methodName: String,
  value: Double,                    
  differenceTable: Seq[Seq[Double]], 
  message: Message,
  isExtrapolated: Boolean = false,
  equation: String = ""
)

object InterpolationResult {
  def formatDouble(d: Double): String = {
    if (d.isInfinite || d.isNaN) "0"
    else {
      val s = f"$d%.15f".replace(",", ".")
      val clean = s.replaceAll("0+$", "")
      if (clean.endsWith(".")) clean.substring(0, clean.length - 1)
      else if (clean.isEmpty) "0"
      else clean
    }
  }

  def getLagrangeEquation(points: Seq[Point]): String = {
    val n = points.size
    if (n == 0) return ""

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    val terms = for (i <- 0 until n) yield {
      val num = (0 until n).filter(_ != i).map(j => formatXMinus(points(j).x)).mkString("*")
      val den = (0 until n).filter(_ != i).map(j => points(i).x - points(j).x).product
      val coeff = points(i).y / den
      val coeffStr = formatDouble(coeff)
      val coeffSigned = if (coeffStr.startsWith("-")) coeffStr else "+" + coeffStr

      if (coeff == 0) ""
      else if (num.isEmpty) coeffSigned
      else s"$coeffSigned*$num"
    }
    val filteredTerms = terms.filter(_.nonEmpty)
    if (filteredTerms.isEmpty) "y = 0"
    else {
      val eq = filteredTerms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }
}

