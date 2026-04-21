package solver.integrals.integrals

trait IntegrationMethod {
  def solve(integral: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult
}