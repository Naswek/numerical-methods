package solver.ode.methods

import solver.core.Message
import solver.ode.functions.{OdeMethod, OdePack}
import solver.ode.models.OdeResult

class ImprovedEuler extends OdeMethod {
  override val name: String = "Усовершенствованный метод Эйлера"
  override val order: Int = 2

  override def solve(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, epsilon: Double): OdeResult = {
    val ys = ImprovedEuler.calculate(pack.f, x0, y0, xn, h)
    val points = Euler.toPoints(ys, pack, x0, y0, h)
    val rungeError = ImprovedEuler.rungeError(pack, x0, y0, xn, h, order)

    OdeResult(
      methodName = name,
      points = points,
      maxError = if (points.isEmpty) 0 else points.map(_.error).max,
      rungeError = rungeError,
      message = Message.Success
    )
  }
}

object ImprovedEuler {
  def calculate(f: (Double, Double) => Double, x0: Double, y0: Double, xn: Double, h: Double): Seq[Double] = {
    val steps = math.round((xn - x0) / h).toInt
    val ys = scala.collection.mutable.ArrayBuffer(y0)

    for (i <- 0 until steps) {
      val x = x0 + i * h
      val y = ys.last
      val predictor = y + h * f(x, y)
      val corrected = y + h / 2 * (f(x, y) + f(x + h, predictor))
      ys += corrected
    }

    ys.toSeq
  }

  def rungeError(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, order: Int): Double = {
    val yH = calculate(pack.f, x0, y0, xn, h).lastOption.getOrElse(y0)
    val yHalf = calculate(pack.f, x0, y0, xn, h / 2).lastOption.getOrElse(y0)
    math.abs(yH - yHalf) / (math.pow(2, order) - 1)
  }
}
