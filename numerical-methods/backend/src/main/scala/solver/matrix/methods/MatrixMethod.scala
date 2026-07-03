package solver.matrix.methods

import solver.matrix.models.MatrixResult

trait MatrixMethod:
  def name: String

  def solve(
      matrix: Array[Array[Double]],
      epsilon: Double,
      initialApproximation: Array[Double],
      maxIterations: Int
  ): MatrixResult
