package solver.ode.models

case class OdePoint(
  i: Int,
  x: Double,
  y: Double,
  exactY: Double,
  error: Double
)
