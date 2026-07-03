package solver.matrix.models

case class MatrixResponse(
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
    message: String
)

case class SamplesResponse(
    methods: List[String]
)
