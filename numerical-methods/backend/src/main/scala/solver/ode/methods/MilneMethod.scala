package solver.ode.methods

import solver.core.Message
import solver.ode.functions.{OdeMethod, OdePack}
import solver.ode.models.OdeResult

class Milne extends OdeMethod {
  override val name: String = "Метод Милна"
  override val order: Int = 4

  override def solve(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, epsilon: Double): OdeResult = {
    val ys = Milne.calculate(pack.f, x0, y0, xn, h, epsilon)
    val points = Euler.toPoints(ys, pack, x0, y0, h)
    val maxError = if (points.isEmpty) 0 else points.map(_.error).max

    OdeResult(
      methodName = name,
      points = points,
      maxError = maxError,
      rungeError = 0,
      message = Message.Success 
    )
  }
}

object Milne {
  private val MaxCorrections = 100

  def calculate(f: (Double, Double) => Double, x0: Double, y0: Double, xn: Double, h: Double, epsilon: Double): Seq[Double] = {
    val steps = math.round((xn - x0) / h).toInt
    if (steps < 4) return RungeKutta4.calculate(f, x0, y0, xn, h)

    val ys = scala.collection.mutable.ArrayBuffer.from(RungeKutta4.calculate(f, x0, y0, x0 + 3 * h, h))

    for (i <- 4 to steps) {
      val x = x0 + i * h
      val f1 = f(x0 + (i - 3) * h, ys(i - 3))
      val f2 = f(x0 + (i - 2) * h, ys(i - 2))
      val f3 = f(x0 + (i - 1) * h, ys(i - 1))
      val predicted = ys(i - 4) + 4 * h / 3 * (2 * f1 - f2 + 2 * f3)

      var corrected = ys(i - 2) + h / 3 * (f2 + 4 * f3 + f(x, predicted))
      var previous = predicted
      var corrections = 0

      while (math.abs(corrected - previous) > epsilon && corrections < MaxCorrections) {
        previous = corrected
        corrected = ys(i - 2) + h / 3 * (f2 + 4 * f3 + f(x, previous))
        corrections += 1
      }

      ys += corrected
    }

    ys.toSeq
  }
}

object RungeKutta4 {
  def calculate(f: (Double, Double) => Double, x0: Double, y0: Double, xn: Double, h: Double): Seq[Double] = {
    val steps = math.round((xn - x0) / h).toInt
    val ys = scala.collection.mutable.ArrayBuffer(y0)

    for (i <- 0 until steps) {
      val x = x0 + i * h
      val y = ys.last
      val k1 = h * f(x, y)
      val k2 = h * f(x + h / 2, y + k1 / 2)
      val k3 = h * f(x + h / 2, y + k2 / 2)
      val k4 = h * f(x + h, y + k3)
      ys += y + (k1 + 2 * k2 + 2 * k3 + k4) / 6
    }

    ys.toSeq
  }
}
