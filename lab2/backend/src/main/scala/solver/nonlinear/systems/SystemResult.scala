package solver.nonlinear.systems  

case class SystemResult(
  x: Array[Double],
  fx: Array[Double],
  iterations: Int,
  message: Option[String]
)