package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class Gauss extends Interpolator {
  override def name: String = "Многочлен Гаусса"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    val n = points.size
    val h = points(1).x - points(0).x
    
    // Проверка на шаг h
    if (!points.sliding(2).forall(p => math.abs((p(1).x - p(0).x) - h) < 1e-9))
      return InterpolationResult(name, 0, Seq.empty, Message.BadParameters)

    // Находим индекс x0 - ближайшего узла
    val midIdx = points.zipWithIndex.minBy(p => math.abs(p._1.x - targetX))._2
    val t = (targetX - points(midIdx).x) / h
    val table = DifferenceTable.finite(points.map(_.y))

    var res = points(midIdx).y
    var tPart = 1.0
    var factorial = 1.0

    if (targetX >= points(midIdx).x) {
      // ПЕРВАЯ формула (вперед): y0, Δy0, Δ²y-1, Δ³y-1, Δ⁴y-2...
      for (i <- 1 until math.min(midIdx + 1, n - midIdx)) {
        // k = 1 -> Δy0, k = 2 -> Δ²y-1, k = 3 -> Δ³y-1...
        // Четные шаги: идем вверх по индексу y. Нечетные: остаемся.
        
        // k = 1
        tPart *= (t - (i - 1))
        factorial *= (2 * i - 1)
        res += (tPart * table(midIdx)(2 * i - 1)) / factorial
        
        // k = 2
        tPart *= (t + i)
        factorial *= (2 * i)
        res += (tPart * table(midIdx - i)(2 * i)) / factorial
      }
      InterpolationResult("Гаусс (вперед)", res, table, Message.Success)
    } else {
      // ВТОРАЯ формула (назад): y0, Δy-1, Δ²y-1, Δ³y-2, Δ⁴y-2...
      for (i <- 1 until math.min(midIdx + 1, n - midIdx)) {
        // k = 1
        tPart *= (t + (i - 1))
        factorial *= (2 * i - 1)
        res += (tPart * table(midIdx - i)(2 * i - 1)) / factorial
        
        // k = 2
        tPart *= (t - i)
        factorial *= (2 * i)
        res += (tPart * table(midIdx - i)(2 * i)) / factorial
      }
      InterpolationResult("Гаусс (назад)", res, table, Message.Success)
    }
  }
}