package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import routes.formats.JsonFormatsMatrix._
import solver.core.Message
import solver.matrix.Library
import solver.matrix.models._
import solver.matrix.samples.Samples

object MatrixRoutes:

  val routes: Route =
    pathPrefix("matrix") {
      concat(
        path("function") {
          post {
            entity(as[MatrixRequest]) { request =>
              Library.matrixMethods.get(0) match
                case Some(method) =>
                  val result = method.solve(
                    request.matrix,
                    request.epsilon,
                    request.initialApproximation,
                    request.maxIterations
                  )

                  complete(
                    MatrixResponse(
                      success = result.success,
                      solution = result.solution,
                      iterations = result.iterations,
                      epsilon = result.epsilon,
                      maxError = result.maxError,
                      errors = result.errors,
                      c = result.c,
                      d = result.d,
                      reorderedMatrix = result.reorderedMatrix,
                      formulas = result.formulas,
                      message = result.message.toString
                    )
                  )

                case None =>
                  complete(
                    MatrixResponse(
                      success = false,
                      solution = Array.empty[Double],
                      iterations = 0,
                      epsilon = request.epsilon,
                      maxError = 0.0,
                      errors = Array.empty[Double],
                      c = Array.empty[Array[Double]],
                      d = Array.empty[Double],
                      reorderedMatrix = Array.empty[Array[Double]],
                      formulas = List.empty,
                      message = Message.BadId.toString
                    )
                  )
            }
          }
        },
        path("samples") {
          get {
            complete(SamplesResponse(methods = Samples.methods))
          }
        }
      )
    }
