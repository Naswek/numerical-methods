package solver.ode.functions

case class OdePack(
  f: (Double, Double) => Double,
  exact: (Double, Double, Double) => Double
)
