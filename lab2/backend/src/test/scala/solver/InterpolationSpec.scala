package solver.interpolation

import org.scalatest.funsuite.AnyFunSuite
import solver.interpolation.methods._
import solver.interpolation.models.Point
import solver.core.Message

class InterpolationSpec extends AnyFunSuite {
  test("Метод Стирлинга возвращает NotEnoughPoints для одного узла") {
    val points = Seq(Point(0.0, 3.0)) 
    val solver = new Stirling()
    val result = solver.solve(points, 1.0)
    assert(result.message == Message.NotEnoughPoints)
    println(s"Случай с одной точкой: value = ${result.value}, message = ${result.message}")
  }

  test("Метод Стирлинга точно возвращает значение в существующем узле сетки") {
    val points = (0 to 10).map(i => Point(i * 0.2, 2.0 * (i * 0.2) + 3))
    val solver = new Stirling()
    val result = solver.solve(points, 1.0)
    assert(result.value == 5.0)
    assert(result.message == Message.Success)
    println(s"Точное попадание (targetX = 1.0): value = ${result.value}, message = ${result.message}")
  }

  test("Метод Стирлинга интерполирует значение между узлами сетки") {
    val points = (0 to 10).map(i => Point(i * 0.2, 2.0 * (i * 0.2) + 3))
    val solver = new Stirling()
    val result = solver.solve(points, 1.1)
    assert(result.value == 5.2)
    assert(result.message == Message.Success)
    println(s"Промежуточная точка (targetX = 1.1): value = ${result.value}, message = ${result.message}")
  }

  test("Логика определения экстраполяции отличает внутренние и внешние точки") {
    val points = (0 to 5).map(i => Point(i.toDouble, i.toDouble * 2))
    
    val targetXInside = 2.5
    val targetXOutsideLeft = -0.5
    val targetXOutsideRight = 5.5
    
    val isExtrapolatedInside = targetXInside < points.head.x || targetXInside > points.last.x
    val isExtrapolatedOutsideLeft = targetXOutsideLeft < points.head.x || targetXOutsideLeft > points.last.x
    val isExtrapolatedOutsideRight = targetXOutsideRight < points.head.x || targetXOutsideRight > points.last.x
    
    assert(!isExtrapolatedInside)
    assert(isExtrapolatedOutsideLeft)
    assert(isExtrapolatedOutsideRight)
  }

  test("Логика определения неопределённых точек находит ln(x) на [-1, 1]") {
    val f: Double => Double = math.log
    
    def hasUndefinedPoints(func: Double => Double, a: Double, b: Double, h: Double): Boolean = {
      if (a > b || h <= 0) return false
      (BigDecimal(a) to BigDecimal(b) by BigDecimal(h)).exists { x =>
        val yVal = func(x.toDouble)
        yVal.isNaN || yVal.isInfinite
      }
    }
    
    val undefinedRes = hasUndefinedPoints(f, -1.0, 1.0, 0.2)
    val definedRes = hasUndefinedPoints(f, 0.1, 1.1, 0.2)
    
    assert(undefinedRes)
    assert(!definedRes)
  }
}
