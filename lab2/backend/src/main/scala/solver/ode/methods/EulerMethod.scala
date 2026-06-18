package solver.ode.methods

import solver.core.Message
import solver.ode.functions.{OdeMethod, OdePack}
import solver.ode.models.{OdePoint, OdeResult}

class Euler extends OdeMethod {
  override val name: String = "Метод Эйлера"
  override val order: Int = 1

  override def solve(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, epsilon: Double): OdeResult = {
    val ys = Euler.calculate(pack.f, x0, y0, xn, h)
    val points = Euler.toPoints(ys, pack, x0, y0, h)
    val rungeError = Euler.rungeError(pack, x0, y0, xn, h, order)

    OdeResult(
      methodName = name,
      points = points,
      maxError = if (points.isEmpty) 0 else points.map(_.error).max,
      rungeError = rungeError,
      message = if (rungeError <= epsilon) Message.Success else Message.MethodDoesNotConverge
    )
  }
}

object Euler {
  def calculate(f: (Double, Double) => Double, x0: Double, y0: Double, xn: Double, h: Double): Seq[Double] = {
    val steps = math.round((xn - x0) / h).toInt
    val ys = scala.collection.mutable.ArrayBuffer(y0)

    for (i <- 0 until steps) {
      val x = x0 + i * h
      val y = ys.last
      ys += y + h * f(x, y)
    }

    ys.toSeq
  }

  def toPoints(ys: Seq[Double], pack: OdePack, x0: Double, y0: Double, h: Double): Seq[OdePoint] =
    ys.zipWithIndex.map { case (y, i) =>
      val x = x0 + i * h
      val exactY = pack.exact(x, x0, y0)
      OdePoint(i, x, y, exactY, math.abs(exactY - y))
    }

  def rungeError(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, order: Int): Double = {
    val yH = calculate(pack.f, x0, y0, xn, h).lastOption.getOrElse(y0)
    val yHalf = calculate(pack.f, x0, y0, xn, h / 2).lastOption.getOrElse(y0)
    math.abs(yH - yHalf) / (math.pow(2, order) - 1)
  }
}
