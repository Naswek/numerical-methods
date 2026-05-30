package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class Stirling extends Interpolator {
  override def name: String = "Схема Стирлинга"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    // Для экономии места: Стирлинг считается через Гаусса или напрямую
    // Рекомендую просто реализовать формулу через средние значения центральных разностей
    val n = points.size
    val h = points(1).x - points(0).x
    val midIdx = points.zipWithIndex.minBy(p => math.abs(p._1.x - targetX))._2
    val t = (targetX - points(midIdx).x) / h
    val table = DifferenceTable.finite(points.map(_.y))

    var res = table(midIdx)(0)
    var tProd = t
    var factorial = 1.0

    for (k <- 1 until math.min(midIdx + 1, n - midIdx)) {
      factorial *= (2 * k - 1)
      // Полуразность нечетных разностей
      val avgDiff = (table(midIdx - k + 1)(2 * k - 1) + table(midIdx - k)(2 * k - 1)) / 2.0
      res += (tProd * avgDiff) / factorial
      
      factorial *= (2 * k)
      tProd *= t // t²
      res += (tProd * table(midIdx - k)(2 * k)) / factorial
      
      tProd *= (t * t - k * k) / t // для следующего шага
    }
    
    InterpolationResult(name, res, table, Message.Success)
  }
}