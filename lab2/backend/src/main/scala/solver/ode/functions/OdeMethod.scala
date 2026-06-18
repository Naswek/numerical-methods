package solver.ode.functions

import solver.ode.models.OdeResult

trait OdeMethod {
  def name: String
  def order: Int
  def solve(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, epsilon: Double): OdeResult

  protected def buildGrid(x0: Double, xn: Double, h: Double): Seq[Double] = {
    val steps = math.round((xn - x0) / h).toInt
    (0 to steps).map(i => x0 + i * h)
  }
}
