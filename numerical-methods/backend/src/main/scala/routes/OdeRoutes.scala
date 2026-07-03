package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import solver.core.Message
import solver.ode.Library
import solver.ode.models._
import solver.ode.samples.Samples
import routes.formats.JsonFormatsOde._

object OdeRoutes {
  val routes: Route =
    pathPrefix("ode") {
      concat(
        path("function") {
          post {
            entity(as[OdeRequest]) { request =>
              val response =
                if (!Library.functions.contains(request.functionId)) {
                  OdeResponse(
                    success = false,
                    results = Seq.empty,
                    bestMethod = "",
                    message = Message.BadId.toString
                  )
                } else if (!isValid(request)) {
                  OdeResponse(
                    success = false,
                    results = Seq.empty,
                    bestMethod = "",
                    message = Message.BadParameters.toString
                  )
                } else {
                  val pack = Library.functions(request.functionId)
                  val results = Library.odeMethods.values.map { method =>
                    method.solve(pack, request.x0, request.y0, request.xn, request.h, request.epsilon)
                  }.toSeq
                  val successfulResults = results.filter(_.message == Message.Success)
                  val bestMethodName =
                    if (successfulResults.nonEmpty) successfulResults.minBy(_.maxError).methodName
                    else "Нет подходящего метода"

                  OdeResponse(
                    success = true,
                    results = results,
                    bestMethod = bestMethodName,
                    message = Message.Success.toString
                  )
                }

              complete(response)
            }
          }
        },
        path("samples") {
          get {
            complete(SamplesResponse(
              functions = Samples.functions,
              methods = Samples.methods
            ))
          }
        },
        path("test") {
          get {
            complete("Ит воркс!")
          }
        }
      )
    }

  private def isValid(request: OdeRequest): Boolean = {
    val rawSteps = (request.xn - request.x0) / request.h
    val roundedSteps = math.round(rawSteps)

    request.h > 0 &&
      request.epsilon > 0 &&
      request.xn > request.x0 &&
      rawSteps.isFinite &&
      math.abs(rawSteps - roundedSteps) < 1e-9 &&
      roundedSteps > 0 &&
      roundedSteps <= 150
  }
}
