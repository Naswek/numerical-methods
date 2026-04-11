file://<WORKSPACE>/lab1/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -Colors.matrix.
	 -Colors.matrix#
	 -Colors.matrix().
	 -IOHelper.matrix.
	 -IOHelper.matrix#
	 -IOHelper.matrix().
	 -matrix.
	 -matrix#
	 -matrix().
	 -scala/Predef.matrix.
	 -scala/Predef.matrix#
	 -scala/Predef.matrix().
offset: 521
uri: file://<WORKSPACE>/lab1/src/main/scala/Main.scala
text:
```scala
import Colors._
import IOHelper._

object Main extends App {

  var running = true
  while (running) {
    printInfo("Выберите способ ввода матрицы:\n1. Клавиатура\n2. Файл\n3. Выход")
    val choice = readDouble(1, 3)

    val matrixOpt = choice match {
      case 1 =>
        val dim = readDouble(1, 20)
        Some(readMatrixFromKeyboard(dim.toInt))
      case 2 => readMatrixFromFile()
      case 3 =>
        running = false
        None
    }

    matrixOpt.foreach { mat =>
      val solver = new MatrixSolver(ma@@trix)
      if (solver.makeDiagonallyDominant()) {
        val (C, d) = solver.buildJacobiForm()
        solver.matrixNorm(C)
        println("Введите начальное приближение:")
        val x0 = readNumbers(Some(mat.length)).toArray

        var iterations = x0
        val epsilon = 1e-6
        var diff = Double.MaxValue
        var count = 0

        while (diff > epsilon) {
          val newIter = solver.iterate(C, iterations, d)
          diff = (iterations zip newIter).map{ case (a,b) => math.abs(a-b)}.max
          iterations = newIter
          count += 1
        }
        printSuccess(s"Решение после $count итераций: ${iterations.mkString(", ")}")
      }
    }
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 