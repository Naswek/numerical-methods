package solver.ode.models

case class OdeRequest(
  functionId: Int,
  x0: Double,
  y0: Double,
  xn: Double,
  h: Double,
  epsilon: Double
)
