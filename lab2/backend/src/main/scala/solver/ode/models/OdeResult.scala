package solver.ode.models

import solver.core.Message

case class OdeResult(
  methodName: String,
  points: Seq[OdePoint],
  maxError: Double,
  rungeError: Double,
  message: Message
)
