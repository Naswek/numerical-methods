package solver.ode.functions

import scala.math.*

object Functions {
  val p1: OdePack = OdePack(
    f = (x, y) => x + y,
    exact = (x, x0, y0) => {
      val c = (y0 + x0 + 1) * exp(-x0)
      c * exp(x) - x - 1
    }
  )

  val p2: OdePack = OdePack(
    f = (x, y) => y - x * x + 1,
    exact = (x, x0, y0) => {
      val particular = (t: Double) => t * t + 2 * t + 1
      val c = (y0 - particular(x0)) * exp(-x0)
      particular(x) + c * exp(x)
    }
  )

  val p3: OdePack = OdePack(
    f = (x, y) => x * y,
    exact = (x, x0, y0) => y0 * exp((x * x - x0 * x0) / 2)
  )

  val p4: OdePack = OdePack(
    f = (_, y) => -2 * y,
    exact = (x, x0, y0) => y0 * exp(-2 * (x - x0))
  )

  val p5: OdePack = OdePack(
    f = (x, _) => sin(x),
    exact = (x, x0, y0) => y0 - cos(x) + cos(x0)
  )
}
