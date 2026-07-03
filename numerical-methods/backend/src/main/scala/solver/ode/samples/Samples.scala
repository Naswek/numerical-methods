package solver.ode.samples

object Samples {
  val functions = List(
    "y' = x + y",
    "y' = y - x^2 + 1",
    "y' = x*y",
    "y' = -2y",
    "y' = sin(x)"
  )

  val methods = List(
    "Метод Эйлера",
    "Усовершенствованный метод Эйлера",
    "Метод Милна"
  )
}
