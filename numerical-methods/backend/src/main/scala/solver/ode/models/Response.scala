package solver.ode.models

case class OdeResponse(
  success: Boolean,
  results: Seq[OdeResult],
  bestMethod: String,
  message: String
)

case class SamplesResponse(
  functions: List[String],
  methods: List[String]
)
