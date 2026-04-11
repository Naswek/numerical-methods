file://<WORKSPACE>/lab1/src/main/scala/MatrixSolver.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -Colors.maxAttempts.
	 -Colors.maxAttempts#
	 -Colors.maxAttempts().
	 -IOHelper.maxAttempts.
	 -IOHelper.maxAttempts#
	 -IOHelper.maxAttempts().
	 -maxAttempts.
	 -maxAttempts#
	 -maxAttempts().
	 -scala/Predef.maxAttempts.
	 -scala/Predef.maxAttempts#
	 -scala/Predef.maxAttempts().
offset: 881
uri: file://<WORKSPACE>/lab1/src/main/scala/MatrixSolver.scala
text:
```scala
class MatrixSolver {

    import Colors._
    import IOHelper._

    def checkConvergence(matrix: Array[Array[Double]]): Boolean =
        var n = matrix.length
        for (i <- 0 until n) {
            var diag = math.abs(matrix(i)(i))
            val others = (i until n)
                .filter(_ != i)
                .map(j => math.abs(matrix(i)(j)))
                .sum
            if (diag <= others) return false;
        }   
        true

    def trySwap(matrix: Array[Array[Double]]): Array[Array[Double]] =
        var n = matrix.length
        for (i <- 0 until n) {
            val maxRow = (i until n).maxBy(r => math.abs(matrix(r)(i)))
            val tempRow = matrix(i) 
            matrix(i) = matrix(maxRow)
            matrix(maxRow) =  tempRow
        }
        matrix

    def makeDiagonallyDominant(matrix: Array[Array[Double]]): Boolean =
        maxAtte@@mpts = matrix.length
        for (i <- 0 until n) {
            if (checkConvergence(matrix)) {
                return true
            }
            trySwap(matrix)
        }
        printError("не удалось добиться диагонального преобладания")
        false
    
    def toJacobiForm(matrix: Array[Array[Double]]): (Array[Array[Double]], Array[Double]) = {
        val n = matrix.length
        val C = Array.fill(n, n)(0.0)
        val d = Array.fill(n)(0.0)

        for (i <- 0 until n) {
            val a_ii = matrix(i)(i)

            if (a_ii == 0.0) {
            throw new IllegalArgumentException(s"Нулевой диагональный элемент в строке $i")
            }

            for (j <- 0 until n) {
            if (i != j) {
                C(i)(j) = -matrix(i)(j) / a_ii
            }
            }
            
            d(i) = matrix(i)(n) / a_ii 
        }

        printSuccess("Получившаяся матрица C:")
        C.foreach(row => printInfo(row.mkString(" ")))
        printSuccess(s"Вектор d: ${d.mkString(" ")}")
        (C, d)
    }

    def getApproximationVector(dimension: Integer): (Double, Array[Double]) =
        printInfo("Напишите эпсилон: ")
        var epsilon = readDouble()
        var rowX0 = requestInitialApproximation(dimension)
        (epsilon, rowX0)

    def requestInitialApproximation(dimension: Integer): Array[Double] =
        printInfo(s"Напишите начальное приближение c размерностью $dimension, формат ввода: x1 x2 x3 ... xn")
        readNumbers(dimension)

    def runJacobiIterationsWithFormula(C: Array[Array[Double]], d: Array[Double], printErrors: Boolean = false): Unit = {
        val n = C.length

        def idx(num: Int): String = {
            val sub = "₀₁₂₃₄₅₆₇₈₉"
            num.toString.map(c => sub(c.asDigit)).mkString
        }

        for (i <- 0 until n) {
            val parts = (0 until n).flatMap { j =>
            if (C(i)(j) != 0) {
                val sign = if (C(i)(j) >= 0) "+" else "-"
                Some(s" $sign ${math.abs(C(i)(j))}·x${idx(j+1)}^k")
            } else None
            }.mkString

            val signC = if (d(i) >= 0) "+" else "-"
            val formula = s"x${idx(i+1)}^(k+1) =$parts $signC ${math.abs(d(i))}"
            printSuccess(formula)
        }

        val (epsilon, x0) = getApproximationVector(n)
        var row0 = x0
        var newRow = Array.fill(n)(0.0)
        var diff = epsilon + 1
        var count = 0

        while (diff > epsilon) {
            newRow = iteration(C, row0, d)
            count += 1
            diff = (row0 zip newRow).map { case (a, b) => math.abs(a - b) }.max

            if (printErrors) {
            val diffVector = (row0 zip newRow).map { case (a, b) => math.abs(a - b) }
            printSuccess(s"Погрешности на итерации $count: ${diffVector.mkString(", ")}")
            }

            row0 = newRow
        }

        printSuccess(
            s"""Введенный эпсилон: $epsilon
Полученная строка: ${newRow.mkString(", ")}
Количество итераций: $count
Полученный max: ${newRow.max}""".stripMargin)
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 