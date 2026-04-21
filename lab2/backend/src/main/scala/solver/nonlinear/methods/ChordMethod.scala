package solver.nonlinear.methods

import solver.nonlinear.functions.{FunctionPack, Method1D, FunctionResult}

class ChordMethod extends Method1D {
  override def solve(functionPack: FunctionPack, a: Double, b: Double, epsilon: Double): FunctionResult = {
    val fa = functionPack.f(a)
    val fb = functionPack.f(b)

    if (fa * fb > 0) {
      return new FunctionResult(0, 0, 0, Some("В данном промежутке нет корня."))
    }

    val f2a = functionPack.d2f(a)
    val (fix, x0) = if (fa * f2a > 0) {
      (a, b)
    } else {
      (b, a)
    }
    
    val fFix = functionPack.f(fix) 
    var x = x0
    var fx = functionPack.f(x)
    var iterations = 0

    while ((math.abs(fx)) > epsilon && iterations < 1000) {
        x = x - (fix - x) /( fFix - fx) * fx
        fx = functionPack.f(x)
        iterations += 1
    }
    new FunctionResult(x, fx, iterations, if (iterations >= 1000) Some("Превышено максимальное количество итераций") else None)
  }
}