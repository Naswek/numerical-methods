package solver.matrix

import solver.matrix.methods.{JacobiMethod, MatrixMethod}

object Library:
  val matrixMethods: Map[Int, MatrixMethod] = Map(
    0 -> JacobiMethod
  )
