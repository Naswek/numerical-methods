package solver.matrix.models

import solver.core.Message

case class MatrixResult(
    success: Boolean,
    solution: Array[Double],
    iterations: Int,
    epsilon: Double,
    maxError: Double,
    errors: Array[Double],
    c: Array[Array[Double]],
    d: Array[Double],
    reorderedMatrix: Array[Array[Double]],
    formulas: List[String],
    message: Message
)
