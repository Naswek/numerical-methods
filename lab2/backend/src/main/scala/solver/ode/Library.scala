package solver.ode

import solver.ode.functions.{Functions, OdeMethod, OdePack}
import solver.ode.methods.{Euler, ImprovedEuler, Milne}

object Library {
  val functions: Map[Int, OdePack] = Map(
    0 -> Functions.p1,
    1 -> Functions.p2,
    2 -> Functions.p3,
    3 -> Functions.p4,
    4 -> Functions.p5
  )

  val odeMethods: Map[Int, OdeMethod] = Map(
    0 -> new Euler,
    1 -> new ImprovedEuler,
    2 -> new Milne
  )
}
