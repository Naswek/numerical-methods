file://<WORKSPACE>/lab1/src/main/scala/IOHelper.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:
	 -Colors.printError.
	 -Colors.printError#
	 -Colors.printError().
	 -printError.
	 -printError#
	 -printError().
	 -scala/Predef.printError.
	 -scala/Predef.printError#
	 -scala/Predef.printError().
offset: 464
uri: file://<WORKSPACE>/lab1/src/main/scala/IOHelper.scala
text:
```scala
import scala.io.StdIn.readLine
import java.io.File
import Colors._

object IOHelper {

  def readDouble(min: Double = Double.MinValue, max: Double = Double.MaxValue): Double = {
    val input = readLine()
    try {
      val value = input.toDouble
      if (value >= min && value <= max) value
      else {
        printError(s"Введите число от $min до $max")
        readDouble(min, max)
      }
    } catch {
      case _: NumberFormatException =>
        printE@@rror("Введите корректное число (например 3.14)")
        readDouble(min, max)
    }
  }

  def readNumbers(expectedLength: Option[Int] = None): List[Double] = {
    while (true) {
      val input = readLine()
      try {
        val row = input.split("\\s+").map(_.toDouble).toList
        if (expectedLength.isEmpty || row.length == expectedLength.get) return row
        else printError(s"Нужно ввести ровно ${expectedLength.get} чисел")
      } catch {
        case _: NumberFormatException => printError("Введите только числа через пробел")
      }
    }
    Nil
  }

  def readMatrixFromKeyboard(dimension: Int): Array[Array[Double]] = {
    val matrix = Array.ofDim[Double](dimension, dimension + 1)
    println("Введите строки матрицы, разделяя коэффициенты пробелом:")
    for (i <- 0 until dimension) {
      matrix(i) = readNumbers(Some(dimension + 1)).toArray
    }
    matrix
  }

  def readMatrixFromFile(): Option[Array[Array[Double]]] = {
    print("Введите название файла: ")
    val fileName = readLine()
    val file = new File(fileName)
    if (!file.exists() || !file.isFile) {
      printError("Файл не найден")
      return None
    }

    val lines = scala.io.Source.fromFile(file).getLines().toArray
    val dimension = lines(0).toInt
    val matrix = Array.ofDim[Double](dimension, dimension + 1)
    try {
      for (i <- 0 until dimension) {
        val row = lines(i + 1).split("\\s+").map(_.toDouble)
        if (row.length != dimension + 1) {
          printError("Неверное количество элементов в строке")
          return None
        }
        matrix(i) = row
      }
    } catch {
      case _: NumberFormatException =>
        printError("Ошибка в формате чисел")
        return None
    }
    Some(matrix)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 