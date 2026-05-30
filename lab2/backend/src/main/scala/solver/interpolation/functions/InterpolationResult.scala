package solver.interpolation.functions

import solver.core.Message

case class InterpolationResult(
  methodName: String,
  value: Double,                    // Значение в искомой точке X
  differenceTable: Seq[Seq[Double]], // Таблица (конечных или разделенных разностей)
  message: Message
)