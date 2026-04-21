package solver.nonlinear.models

import solver.nonlinear.samples.SystemSample  

case class FunctionResponse(
    x: Double, 
    fx: Double, 
    iterations: Int
)

case class SystemResponse(
    x: Array[Double], 
    fx: Array[Double], 
    iterations: Int
)

case class SamplesResponse(
  functions: List[String],
  systems: List[SystemSample],
  methods: MethodsResponse
)

case class MethodsResponse(
  function: List[String],
  system: List[String]
)