package solver.matrix.models

case class MatrixRequest(
    matrix: Array[Array[Double]],
    epsilon: Double,
    initialApproximation: Array[Double],
    maxIterations: Int
)
