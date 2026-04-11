file://<WORKSPACE>/lab1/src/main/scala/MatrixSolver.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -Colors.colorPrinter.printInfo.
	 -Colors.colorPrinter.printInfo#
	 -Colors.colorPrinter.printInfo().
	 -colorPrinter/printInfo.
	 -colorPrinter/printInfo#
	 -colorPrinter/printInfo().
	 -scala/Predef.colorPrinter.printInfo.
	 -scala/Predef.colorPrinter.printInfo#
	 -scala/Predef.colorPrinter.printInfo().
offset: 1805
uri: file://<WORKSPACE>/lab1/src/main/scala/MatrixSolver.scala
text:
```scala
class MatrixSolver {

    import Colors._

    def checkConvergence(matrix: Array[Array[Double]]): Boolean =
        n = matrix.length
        for (i <- 0 until n) {
            var diag = math.abs(matrix(i)(i))
            val others = (o until n)
                .filter(_ != i)
                .map(j => math.abs(matrix(i)(j)))
                .sum
            if (diag <= others) return false;
        }   
        true

    def trySwap(matrix: Array[Array[Double]]): Array[Array[Double]] =
        n = matrix.length
        for (i <- 0 until n) {
            val maxRow = (i until n).maxBy(r => math.abs(matrix(r)(i)))
            val tempRow = matrix(i) 
            matrix(i) = matrix(maxRow)
            matrix(maxRow) =  tempRow
        }
        matrix

    def makeDiagonallyDominant(matrix: Array[Array[Double]]): Boolean =
        maxAttempts = matrix.length
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
        C.foreach(row => colorPrinter.@@printInfo(row.mkString(" ")))
        colorPrinter.printSuccess(s"Вектор d: ${d.mkString(" ")}")
        (C, d)
    }

    def requestInitialApproximation(dimension: Integer): Array[Double] =
            colorPrinter.printInfo(s"Напишите начальное приближение c размерностью $dimension, формат ввода: x1 x2 x3 ... xn")
            rowX0 = inputNumbers()

}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 