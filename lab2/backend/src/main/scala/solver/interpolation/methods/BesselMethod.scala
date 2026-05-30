package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class Bessel extends Interpolator {
  override def name: String = "Схема Бесселя"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    val n = points.size
    if (n < 2) return InterpolationResult(name, 0, Seq.empty, Message.NotEnoughPoints)

    // 1. Проверка на равные промежутки (шаг h)
    val h = points(1).x - points(0).x
    if (!points.sliding(2).forall(p => math.abs((p(1).x - p(0).x) - h) < 1e-9))
      return InterpolationResult(name, 0, Seq.empty, Message.BadParameters)

    val table = DifferenceTable.finite(points.map(_.y))

    // 2. Находим x0 такой, что x0 <= targetX < x1
    var i0 = points.indexWhere(_.x > targetX) - 1
    
    // Обработка краев (экстраполяция)
    if (i0 < 0) i0 = 0
    if (i0 > n - 2) i0 = n - 2

    val t = (targetX - points(i0).x) / h
    
    // Формула Бесселя:
    // B(t) = (y0 + y1)/2 + (t - 0.5)Δy0 + t(t-1)/2! * (Δ²y-1 + Δ²y0)/2 + ...
    
    // Нулевой и первый порядки (база)
    var res = (table(i0)(0) + table(i0 + 1)(0)) / 2.0
    res += (t - 0.5) * table(i0)(1)

    var factorial = 1.0
    var tProd = t * (t - 1.0)

    // Итерация по высшим порядкам
    // k - номер пары разностей
    for (k <- 1 until n / 2) {
      val evenOrder = 2 * k // Четная разность
      
      // Проверка границ таблицы для центральных разностей
      if (i0 - k >= 0 && i0 - k + 1 < n - evenOrder) {
        factorial *= evenOrder
        val avgEvenDiff = (table(i0 - k)(evenOrder) + table(i0 - k + 1)(evenOrder)) / 2.0
        res += (tProd * avgEvenDiff) / factorial

        val oddOrder = 2 * k + 1 // Нечетная разность
        if (i0 - k >= 0 && i0 - k < n - oddOrder) {
          factorial *= oddOrder
          res += ((t - 0.5) * tProd * table(i0 - k)(oddOrder)) / factorial
        }
      }

      // Подготовка множителя t(t-1) для следующего шага: (t+k)(t-k-1)
      tProd *= (t + k) * (t - k - 1)
    }

    InterpolationResult(name, res, table, Message.Success)
  }
}