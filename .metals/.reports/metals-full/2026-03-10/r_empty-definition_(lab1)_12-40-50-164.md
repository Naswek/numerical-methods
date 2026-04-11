file://<WORKSPACE>/lab1/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -Colors.solver.buildJacobiForm.
	 -Colors.solver.buildJacobiForm#
	 -Colors.solver.buildJacobiForm().
	 -IOHelper.solver.buildJacobiForm.
	 -IOHelper.solver.buildJacobiForm#
	 -IOHelper.solver.buildJacobiForm().
	 -solver/buildJacobiForm.
	 -solver/buildJacobiForm#
	 -solver/buildJacobiForm().
	 -scala/Predef.solver.buildJacobiForm.
	 -scala/Predef.solver.buildJacobiForm#
	 -scala/Predef.solver.buildJacobiForm().
offset: 614
uri: file://<WORKSPACE>/lab1/src/main/scala/Main.scala
text:
```scala
import Colors._
import IOHelper._

object Main extends App {

  var running = true
  while (running) {
    printInfo("Выберите способ ввода матрицы:\n1. Клавиатура\n2. Файл\n3. Выход")
    val choice = readDouble()

    val matrixOpt = choice match {
      case 1 =>
        val dim = readDouble()
        Some(readMatrixFromKeyboard(dim.toInt))
      case 2 => readMatrixFromFile()
      case 3 =>
        running = false
        None
    }

    matrixOpt.foreach { matrix =>
      val solver = new MatrixSolver(matrix)
      if (solver.makeDiagonallyDominant(matrix)) {
        val (C, d) = solver.buildJacobiFor@@m()
        solver.matrixNorm(C)
        println("Введите начальное приближение:")
        val x0 = readNumbers(Some(matrix.length)).toArray
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