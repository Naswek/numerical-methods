error id: file://<WORKSPACE>/lab2/backend/src/main/scala/routes/SolverRoutes.scala:`<error>`#`<error>`.
file://<WORKSPACE>/lab2/backend/src/main/scala/routes/SolverRoutes.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -akka/http/scaladsl/server/Directives.result.message.
	 -akka/http/scaladsl/marshallers/sprayjson/SprayJsonSupport.result.message.
	 -spray/json/result/message.
	 -DefaultJsonProtocol.result.message.
	 -models/result/message.
	 -result/message.
	 -scala/Predef.result.message.
offset: 1362
uri: file://<WORKSPACE>/lab2/backend/src/main/scala/routes/SolverRoutes.scala
text:
```scala
package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import DefaultJsonProtocol._
import solver.library.Library
import solver.library.samples.Samples
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._


object SolverRoutes {

implicit val functionRequestFormat = jsonFormat5(FunctionRequest)
implicit val systemRequestFormat = jsonFormat4(SystemRequest)

  val routes: Route =
    pathPrefix("solve") {
      concat(
        path("function") {
          powst {
            entity(as[FunctionRequest]) { request =>

              val resultOpt =
                for {
                  pack <- Library.functionPack.get(request.functionId)
                  solver <- Library.functionMethods.get(request.methodId)
                } yield solver.solve(pack, request.a, request.b, request.epsilon)

              resultOpt match {

                case Some(result) =>
                  complete(
                    JsObject(
                      "success" -> JsBoolean(true),
                      "x" -> JsNumber(result.x),
                      "fx" -> JsNumber(result.fx),
                      "iterations" -> JsNumber(result.iterations),
                      "message" -> JsString(result.m@@essage.getOrElse("OK"))
                    )
                  )

                case None =>
                  complete(
                    JsObject(
                      "success" -> JsBoolean(false),
                      "error" -> JsString("Неверный id функции или метода")
                    )
                  )
              }
            }
          }
        },

        path("system") {
          post {
            entity(as[SystemRequest]) { request =>

              val resultOpt =
                for {
                  pack <- Library.systemPack.get(request.systemId)
                  solver <- Library.systemMethods.get(request.methodId)
                } yield solver.solve(pack, request.x0, request.epsilon)

              resultOpt match {

                case Some(result) =>
                  complete(
                    JsObject(
                      "success" -> JsBoolean(true),
                      "x" -> JsArray(result.x.map(JsNumber(_)).toVector),
                      "fx" -> JsArray(result.fx.map(JsNumber(_)).toVector),
                      "iterations" -> JsNumber(result.iterations),
                      "message" -> JsString(result.message.getOrElse("OK"))
                    )
                  )

                case None =>
                  complete(
                    JsObject(
                      "success" -> JsBoolean(false),
                      "error" -> JsString("Неверный id системы или метода")
                    )
                  )
              }
            }
          }
        },
        path("samples") {
          get {
            complete(
              JsObject(
                "functions" -> JsArray(
                  Samples.functions.map(JsString(_)).toVector
                ),
                "systems" -> JsArray(
                  Samples.systems.map { s =>
                    JsArray(JsString(s.eq1), JsString(s.eq2))
                  }.toVector
                )
              )
            )
          }
        },
            path("test") {
    get {
        complete("OK")
    }
    }
      )
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 