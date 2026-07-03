package routes.formats

import spray.json._
import DefaultJsonProtocol._
import solver.matrix.models._

object JsonFormatsMatrix extends DefaultJsonProtocol:

  implicit val matrixRequestFormat: RootJsonFormat[MatrixRequest] =
    jsonFormat4(MatrixRequest)

  implicit val matrixResponseFormat: RootJsonFormat[MatrixResponse] =
    jsonFormat11(MatrixResponse)

  implicit val samplesResponseFormat: RootJsonFormat[SamplesResponse] =
    jsonFormat1(SamplesResponse)
