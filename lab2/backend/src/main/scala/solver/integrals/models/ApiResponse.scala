package solver.integrals.models

case class FunctionApiResponse(
  status: Message,
  data: Option[Integral],
  error: Option[String]
)

case class SystemApiResponse(
  status: Message,
  data: Option[SystemResponse],
  error: Option[String]
)