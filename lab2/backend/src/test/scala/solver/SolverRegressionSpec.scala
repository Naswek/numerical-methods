package solver

import org.scalatest.funsuite.AnyFunSuite
import solver.core.Message
import solver.integrals.integrals.IntegralsPack
import solver.interpolation.models.{Point => InterpolationPoint}
import solver.nonlinear.functions.FunctionPack
import solver.nonlinear.systems.SystemPack
import solver.ode.Library as OdeLibrary
import solver.ode.models.OdeResult

class SolverRegressionSpec extends AnyFunSuite {
  private val eps = 1e-6

  test("Методы нелинейных уравнений находят простой корень и метод хорд отсекает интервал без корня") {
    val line = FunctionPack(
      f = x => x - 2.0,
      d1f = _ => 1.0,
      d2f = _ => 0.0
    )

    solver.nonlinear.Library.functionMethods.values.foreach { method =>
      val result = method.solve(line, 0.0, 4.0, eps)
      assert(result.message == Message.Success)
      assert(math.abs(result.x - 2.0) <= eps)
      assert(math.abs(result.fx) <= eps)
    }

    val noRoot = solver.nonlinear.Library.functionMethods(0).solve(line, 3.0, 4.0, eps)
    assert(noRoot.message == Message.NoRootInInterval)
  }

  test("Метод Ньютона для систем возвращает Success для уже сошедшейся и нормально сходящейся системы") {
    val system = SystemPack(
      f = x => Array(x(0) - 1.0, x(1) + 2.0),
      jacobian = _ => Array(Array(1.0, 0.0), Array(0.0, 1.0))
    )
    val method = solver.nonlinear.Library.systemMethods(0)

    val alreadyRoot = method.solve(system, Array(1.0, -2.0), eps)
    assert(alreadyRoot.message == Message.Success)
    assert(alreadyRoot.iterations == 0)

    val converged = method.solve(system, Array(0.0, 0.0), eps)
    assert(converged.message == Message.Success)
    assert(converged.fx.forall(v => math.abs(v) <= eps))

    val badRequest = method.solve(system, Array(1.0), eps)
    assert(badRequest.message == Message.BadParameters)
  }

  test("Все методы интегрирования достаточно точно считают постоянную функцию") {
    val constant = IntegralsPack(_ => 2.0)

    solver.integrals.Library.integralsMethods.values.foreach { method =>
      val result = method.solve(constant, 0.0, 3.0, 1e-4)
      assert(result.message == Message.Success)
      assert(math.abs(result.value - 6.0) <= 1e-9)
    }
  }

  test("Методы интегрирования сообщают о несобственном интеграле около особенности") {
    val singular = IntegralsPack(x => 1.0 / (x * x))
    val result = solver.integrals.Library.integralsMethods(1).solve(singular, -1.0, 1.0, 1e-4)

    assert(Set(Message.IntegralDoesNotExist, Message.IntervalWasChanged).contains(result.message))
  }

  test("Все методы интерполяции воспроизводят линейную функцию на равноотстоящей сетке") {
    val points = (0 to 6).map(i => InterpolationPoint(i.toDouble, 2.0 * i + 3.0))

    solver.interpolation.Library.interpolationFunctions.values.foreach { method =>
      val result = method.solve(points, 2.5)
      assert(result.message == Message.Success, method.name)
      assert(math.abs(result.value - 8.0) <= 1e-8, method.name)
    }
  }

  test("Конечно-разностные методы интерполяции отклоняют неравномерную сетку") {
    val points = Seq(
      InterpolationPoint(0.0, 1.0),
      InterpolationPoint(1.0, 3.0),
      InterpolationPoint(3.0, 9.0)
    )

    Seq(2, 3, 4, 5, 6).foreach { methodId =>
      val result = solver.interpolation.Library.interpolationFunctions(methodId).solve(points, 2.0)
      assert(result.message == Message.BadParameters)
    }
  }

  test("Все модели аппроксимации возвращают Success на подходящих корректных данных") {
    val cases = Seq(
      0 -> (0 to 5).map(x => solver.approximation.functions.Point(x, 0.5 * x * x * x - 2 * x * x + x)),
      1 -> (0 to 5).map(x => solver.approximation.functions.Point(x, 2.0 * math.exp(0.5 * x))),
      2 -> (1 to 6).map(x => solver.approximation.functions.Point(x, 3.0 * math.pow(x, 1.5))),
      3 -> (0 to 5).map(x => solver.approximation.functions.Point(x, 2.0 * x + 3.0)),
      4 -> (1 to 6).map(x => solver.approximation.functions.Point(x, 3.0 * math.log(x) + 2.0)),
      5 -> (0 to 5).map(x => solver.approximation.functions.Point(x, x * x - 4.0 * x + 1.0))
    )

    cases.foreach { case (methodId, points) =>
      val result = solver.approximation.Library.approximationFunctions(methodId).solve(points)
      assert(result.message == Message.Success, result.methodName)
      assert(result.mse <= 1e-8, result.methodName)
    }
  }

  test("Все методы ОДУ возвращают конечные успешные результаты при мягкой точности") {
    val pack = OdeLibrary.functions(0)

    OdeLibrary.odeMethods.values.foreach { method =>
      val result: OdeResult = method.solve(pack, 0.0, 1.0, 1.0, 0.1, 1.0)
      assert(result.message == Message.Success, method.name)
      assert(result.points.nonEmpty, method.name)
      assert(result.points.forall(p => p.x.isFinite && p.y.isFinite && p.exactY.isFinite), method.name)
    }
  }

  test("Списки samples соответствуют зарегистрированным backend-библиотекам") {
    assert(solver.integrals.samples.Samples.integrals.size == solver.integrals.Library.integralsPack.size)
    assert(solver.integrals.samples.Samples.integralsMethods.size == solver.integrals.Library.integralsMethods.size)

    assert(solver.approximation.samples.Samples.functions.size == solver.approximation.Library.functions.size)
    assert(solver.approximation.samples.Samples.approximators.size == solver.approximation.Library.approximationFunctions.size)

    assert(solver.interpolation.samples.Samples.functions.size == solver.interpolation.Library.functions.size)
    assert(solver.interpolation.samples.Samples.interpolators.size == solver.interpolation.Library.interpolationFunctions.size)

    assert(solver.ode.samples.Samples.functions.size == solver.ode.Library.functions.size)
    assert(solver.ode.samples.Samples.methods.size == solver.ode.Library.odeMethods.size)

    assert(solver.nonlinear.samples.Samples.functions.size == solver.nonlinear.Library.functionPack.size)
    assert(solver.nonlinear.samples.Samples.systems.size == solver.nonlinear.Library.systemPack.size)
  }
}
