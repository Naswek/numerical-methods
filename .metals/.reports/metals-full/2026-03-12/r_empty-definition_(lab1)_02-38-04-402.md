error id: file://<WORKSPACE>/lab1/src/main/scala/Main.scala:
file://<WORKSPACE>/lab1/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -Colors.readMatrixFromGeneration.
	 -Colors.readMatrixFromGeneration#
	 -Colors.readMatrixFromGeneration().
	 -IOHelper.readMatrixFromGeneration.
	 -IOHelper.readMatrixFromGeneration#
	 -IOHelper.readMatrixFromGeneration().
	 -readMatrixFromGeneration.
	 -readMatrixFromGeneration#
	 -readMatrixFromGeneration().
	 -scala/Predef.readMatrixFromGeneration.
	 -scala/Predef.readMatrixFromGeneration#
	 -scala/Predef.readMatrixFromGeneration().
offset: 652
uri: file://<WORKSPACE>/lab1/src/main/scala/Main.scala
text:
```scala
import Colors._
import IOHelper._

object Main extends App {

  var running = true
  while (running) {
    printInfo("Выберите способ ввода матрицы:\n1. Клавиатура\n2. Файл\n3. Генерация\n4. Выход")
    val choice = readInt(1, 4)

    val matrixOpt = choice match {
      case 1 =>
        printInfo("Введите размерность матрицы (количество строк) до 20 включительно:") 
        val dim = readInt(2, 20)
        Some(readMatrixFromKeyboard(dim))
      case 2 => readMatrixFromFile()
      case 3 =>         
        printInfo("Введите размерность матрицы (количество строк) до 20 включительно:") 
        val dim = readInt(2, 20)
        Some(readMatri@@xFromGeneration(dim))
      case 4 =>
        running = false
        None
    }

    matrixOpt.foreach { matrix =>
      val solver = new MatrixSolver(matrix)
      val (c, d) = solver.toJacobiForm(matrix)
      printInfo("Проверяем диагональное преобладание и условие сходимости...")
      print(solver.makeDiagonallyDominant(matrix))
      print(solver.matrixNorm(c))
      if (solver.makeDiagonallyDominant(matrix) && solver.matrixNorm(c)) {
        printSuccess("Начинаем итерации метода Якоби...")
        solver.runJacobiIterationsWithFormula(c, d)
        
      }
    }
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 