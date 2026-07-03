package routes.formats

import spray.json._
import DefaultJsonProtocol._
import solver.core.Message
import solver.ode.models._

object JsonFormatsOde extends DefaultJsonProtocol {
  implicit val messageFormat: RootJsonFormat[Message] = new RootJsonFormat[Message] {
    def write(obj: Message): JsValue = JsString(obj.toString)
    def read(json: JsValue): Message = json match {
      case JsString(value) => Message.valueOf(value)
      case _ => throw DeserializationException("Сообщение должно быть стринг")
    }
  }

  implicit val odePointFormat: RootJsonFormat[OdePoint] =
    jsonFormat5(OdePoint.apply)

  implicit val odeResultFormat: RootJsonFormat[OdeResult] =
    jsonFormat5(OdeResult.apply)

  implicit val odeRequestFormat: RootJsonFormat[OdeRequest] =
    jsonFormat6(OdeRequest.apply)

  implicit val odeResponseFormat: RootJsonFormat[OdeResponse] =
    jsonFormat4(OdeResponse.apply)

  implicit val samplesResponseFormat: RootJsonFormat[SamplesResponse] =
    jsonFormat2(SamplesResponse.apply)
}
