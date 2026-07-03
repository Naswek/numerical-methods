package solver.matrix.methods

import solver.core.Message
import solver.matrix.models.MatrixResult

object JacobiMethod extends MatrixMethod:
  override val name: String = "Метод простых итераций (Якоби)"

  private val PivotTolerance = 1e-12

  override def solve(
      matrix: Array[Array[Double]],
      epsilon: Double,
      initialApproximation: Array[Double],
      maxIterations: Int
  ): MatrixResult =
    validate(matrix, epsilon, initialApproximation, maxIterations) match
      case Left(message) => failed(epsilon, message)
      case Right(n) =>
        val source = matrix.map(_.clone)

        reorderForConvergence(source, n) match
          case None => failed(epsilon, Message.MethodDoesNotConverge)
          case Some(reordered) =>
            toJacobiForm(reordered, n) match
              case None => failed(epsilon, Message.DivisionByZero)
              case Some((c, d)) =>
                val norm = c.map(row => row.map(math.abs).sum).max
                if norm >= 1.0 then failed(epsilon, Message.MethodDoesNotConverge)
                else iterate(c, d, reordered, epsilon, initialApproximation.clone, maxIterations)

  private def validate(
      matrix: Array[Array[Double]],
      epsilon: Double,
      initialApproximation: Array[Double],
      maxIterations: Int
  ): Either[Message, Int] =
    if matrix == null || initialApproximation == null then Left(Message.BadParameters)
    else if !epsilon.isFinite || epsilon <= 0.0 || epsilon >= 1.0 then Left(Message.BadParameters)
    else if maxIterations <= 0 || maxIterations > 100000 then Left(Message.BadParameters)
    else
      val n = matrix.length
      if n < 2 || n > 20 then Left(Message.BadParameters)
      else if initialApproximation.length != n || !initialApproximation.forall(_.isFinite) then
        Left(Message.BadParameters)
      else if matrix.exists(row => row == null || row.length != n + 1 || !row.forall(_.isFinite)) then
        Left(Message.BadParameters)
      else Right(n)

  private def reorderForConvergence(matrix: Array[Array[Double]], n: Int): Option[Array[Array[Double]]] =
    if matrix.indices.forall(i => rowConvergesAt(matrix(i), i, n)) then Some(matrix.map(_.clone))
    else
      findConvergentPermutation(matrix, n).map { permutation =>
        permutation.map(rowIndex => matrix(rowIndex).clone)
      }

  private def findConvergentPermutation(matrix: Array[Array[Double]], n: Int): Option[Array[Int]] =
    val candidates = Array.tabulate(n) { column =>
      matrix.indices.filter(rowIndex => rowConvergesAt(matrix(rowIndex), column, n)).toList
    }

    if candidates.exists(_.isEmpty) then None
    else
      val columnOrder = (0 until n).sortBy(column => candidates(column).length)
      val usedRows = Array.fill(n)(false)
      val permutation = Array.fill(n)(-1)

      def search(orderIndex: Int): Boolean =
        if orderIndex == columnOrder.length then true
        else
          val column = columnOrder(orderIndex)
          val orderedCandidates = candidates(column).sortBy(rowIndex => -math.abs(matrix(rowIndex)(column)))

          orderedCandidates.exists { rowIndex =>
            if usedRows(rowIndex) then false
            else
              usedRows(rowIndex) = true
              permutation(column) = rowIndex

              val solved = search(orderIndex + 1)
              if !solved then
                usedRows(rowIndex) = false
                permutation(column) = -1
              solved
          }

      if search(0) then Some(permutation) else None

  private def rowConvergesAt(row: Array[Double], diagonalIndex: Int, n: Int): Boolean =
    val diagonal = math.abs(row(diagonalIndex))
    val others = (0 until n).filter(_ != diagonalIndex).map(j => math.abs(row(j))).sum
    diagonal > PivotTolerance && diagonal > others + PivotTolerance

  private def toJacobiForm(
      matrix: Array[Array[Double]],
      n: Int
  ): Option[(Array[Array[Double]], Array[Double])] =
    if matrix.indices.exists(i => math.abs(matrix(i)(i)) <= PivotTolerance) then None
    else
      val c = Array.fill(n, n)(0.0)
      val d = Array.fill(n)(0.0)

      for i <- 0 until n do
        val diagonal = matrix(i)(i)

        for j <- 0 until n do
          if i != j then c(i)(j) = -matrix(i)(j) / diagonal

        d(i) = matrix(i)(n) / diagonal

      Some((c, d))

  private def iterate(
      c: Array[Array[Double]],
      d: Array[Double],
      reorderedMatrix: Array[Array[Double]],
      epsilon: Double,
      initialApproximation: Array[Double],
      maxIterations: Int
  ): MatrixResult =
    val n = c.length
    var current = initialApproximation
    var next = Array.fill(n)(0.0)
    var errors = Array.fill(n)(Double.PositiveInfinity)
    var maxError = Double.PositiveInfinity
    var iterations = 0

    while iterations < maxIterations && maxError > epsilon do
      next = Array.tabulate(n) { i =>
        (0 until n).map(j => c(i)(j) * current(j)).sum + d(i)
      }

      if !next.forall(_.isFinite) then
        return MatrixResult(
          success = false,
          solution = Array.empty[Double],
          iterations = iterations,
          epsilon = epsilon,
          maxError = maxError,
          errors = Array.empty[Double],
          c = c,
          d = d,
          reorderedMatrix = reorderedMatrix,
          formulas = formulas(c, d),
          message = Message.MethodDoesNotConverge
        )

      errors = (current zip next).map { case (oldValue, newValue) => math.abs(oldValue - newValue) }
      maxError = errors.max
      current = next
      iterations += 1

    val message =
      if maxError <= epsilon then Message.Success
      else Message.IterationLimitExceeded

    MatrixResult(
      success = message == Message.Success,
      solution = current,
      iterations = iterations,
      epsilon = epsilon,
      maxError = maxError,
      errors = errors,
      c = c,
      d = d,
      reorderedMatrix = reorderedMatrix,
      formulas = formulas(c, d),
      message = message
    )

  private def formulas(c: Array[Array[Double]], d: Array[Double]): List[String] =
    c.indices.map { i =>
      val terms = c(i).indices.flatMap { j =>
        val coefficient = c(i)(j)
        if math.abs(coefficient) <= PivotTolerance then None
        else
          val sign = if coefficient >= 0 then "+" else "-"
          Some(s" $sign ${formatNumber(math.abs(coefficient))}*x${j + 1}^k")
      }.mkString

      val freeSign = if d(i) >= 0 then "+" else "-"
      s"x${i + 1}^(k+1) =$terms $freeSign ${formatNumber(math.abs(d(i)))}"
    }.toList

  private def formatNumber(value: Double): String =
    val rounded = BigDecimal(value).setScale(10, BigDecimal.RoundingMode.HALF_UP).bigDecimal.stripTrailingZeros
    rounded.toPlainString

  private def failed(epsilon: Double, message: Message): MatrixResult =
    MatrixResult(
      success = false,
      solution = Array.empty[Double],
      iterations = 0,
      epsilon = if epsilon.isFinite then epsilon else 0.0,
      maxError = 0.0,
      errors = Array.empty[Double],
      c = Array.empty[Array[Double]],
      d = Array.empty[Double],
      reorderedMatrix = Array.empty[Array[Double]],
      formulas = List.empty,
      message = message
    )
