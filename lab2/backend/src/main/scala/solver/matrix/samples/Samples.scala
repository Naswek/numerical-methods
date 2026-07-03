package solver.matrix.samples

import solver.matrix.Library

object Samples:
  val methods: List[String] =
    Library.matrixMethods.toSeq.sortBy(_._1).map(_._2.name).toList
