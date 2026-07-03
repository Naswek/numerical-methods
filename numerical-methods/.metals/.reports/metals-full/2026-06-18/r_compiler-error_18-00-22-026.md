error id: A176F47E5A4A5263EDCAA12C1D540806
file://<WORKSPACE>/backend/src/main/scala/solver/ode/methods/ImprovedEulerMethod.scala
### java.lang.NullPointerException: Cannot invoke "dotty.tools.dotc.parsing.Scanners$Region.isOutermost()" because the return value of "dotty.tools.dotc.parsing.Scanners$Scanner.currentRegion()" is null

occurred in the presentation compiler.



action parameters:
uri: file://<WORKSPACE>/backend/src/main/scala/solver/ode/methods/ImprovedEulerMethod.scala
text:
```scala
package solver.ode.methods

import solver.core.Message
import solver.ode.functions.{OdeMethod, OdePack}
import solver.ode.models.OdeResult

class ImprovedEuler extends OdeMethod {
  override val name: String = "Усовершенствованный метод Эйлера"
  override val order: Int = 2

  override def solve(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, epsilon: Double): OdeResult = {
    val ys = ImprovedEuler.calculate(pack.f, x0, y0, xn, h)
    val points = Euler.toPoints(ys, pack, x0, y0, h)
    val rungeError = ImprovedEuler.rungeError(pack, x0, y0, xn, h, order)

    OdeResult(
      methodName = name,
      points = points,
      maxError = if (points.isEmpty) 0 else points.map(_.error).max,
      rungeError = rungeError,
      message = if (rungeError Message.Success
    )
  }
}

object ImprovedEuler {
  def calculate(f: (Double, Double) => Double, x0: Double, y0: Double, xn: Double, h: Double): Seq[Double] = {
    val steps = math.round((xn - x0) / h).toInt
    val ys = scala.collection.mutable.ArrayBuffer(y0)

    for (i <- 0 until steps) {
      val x = x0 + i * h
      val y = ys.last
      val predictor = y + h * f(x, y)
      val corrected = y + h / 2 * (f(x, y) + f(x + h, predictor))
      ys += corrected
    }

    ys.toSeq
  }

  def rungeError(pack: OdePack, x0: Double, y0: Double, xn: Double, h: Double, order: Int): Double = {
    val yH = calculate(pack.f, x0, y0, xn, h).lastOption.getOrElse(y0)
    val yHalf = calculate(pack.f, x0, y0, xn, h / 2).lastOption.getOrElse(y0)
    math.abs(yH - yHalf) / (math.pow(2, order) - 1)
  }
}

```


presentation compiler configuration:
Scala version: 3.3.7-bin-nonbootstrapped
Classpath:
<HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.7/scala3-library_3-3.3.7.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.16/scala-library-2.13.16.jar [exists ]
Options:





#### Error stacktrace:

```
dotty.tools.dotc.parsing.Scanners$Scanner.adjustSepRegions(Scanners.scala:352)
	dotty.tools.dotc.parsing.Scanners$Scanner.nextToken(Scanners.scala:397)
	dotty.tools.dotc.parsing.Parsers$Parser.accept(Parsers.scala:319)
	dotty.tools.dotc.parsing.Parsers$Parser.enclosed(Parsers.scala:583)
	dotty.tools.dotc.parsing.Parsers$Parser.inBraces(Parsers.scala:602)
	dotty.tools.dotc.parsing.Parsers$Parser.inBracesOrIndented(Parsers.scala:616)
	dotty.tools.dotc.parsing.Parsers$Parser.inDefScopeBraces(Parsers.scala:619)
	dotty.tools.dotc.parsing.Parsers$Parser.templateBody(Parsers.scala:4215)
	dotty.tools.dotc.parsing.Parsers$Parser.templateBodyOpt(Parsers.scala:4208)
	dotty.tools.dotc.parsing.Parsers$Parser.template(Parsers.scala:4185)
	dotty.tools.dotc.parsing.Parsers$Parser.templateOpt(Parsers.scala:4193)
	dotty.tools.dotc.parsing.Parsers$Parser.classDefRest(Parsers.scala:3905)
	dotty.tools.dotc.parsing.Parsers$Parser.classDef(Parsers.scala:3900)
	dotty.tools.dotc.parsing.Parsers$Parser.tmplDef(Parsers.scala:3876)
	dotty.tools.dotc.parsing.Parsers$Parser.defOrDcl(Parsers.scala:3660)
	dotty.tools.dotc.parsing.Parsers$Parser.topStatSeq(Parsers.scala:4272)
	dotty.tools.dotc.parsing.Parsers$Parser.topstats$1(Parsers.scala:4466)
	dotty.tools.dotc.parsing.Parsers$Parser.topstats$1(Parsers.scala:4460)
	dotty.tools.dotc.parsing.Parsers$Parser.compilationUnit$$anonfun$1(Parsers.scala:4471)
	dotty.tools.dotc.parsing.Parsers$Parser.checkNoEscapingPlaceholders(Parsers.scala:527)
	dotty.tools.dotc.parsing.Parsers$Parser.compilationUnit(Parsers.scala:4476)
	dotty.tools.dotc.parsing.Parsers$Parser.parse(Parsers.scala:200)
	dotty.tools.dotc.parsing.Parser.parse$$anonfun$1(ParserPhase.scala:32)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:467)
	dotty.tools.dotc.parsing.Parser.parse(ParserPhase.scala:40)
	dotty.tools.dotc.parsing.Parser.$anonfun$2(ParserPhase.scala:52)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:479)
	scala.collection.Iterator$$anon$9.hasNext(Iterator.scala:583)
	scala.collection.immutable.List.prependedAll(List.scala:152)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.IterableOps$WithFilter.map(Iterable.scala:900)
	dotty.tools.dotc.parsing.Parser.runOn(ParserPhase.scala:51)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:351)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1324)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:344)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:384)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:393)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:69)
	dotty.tools.dotc.Run.compileUnits(Run.scala:393)
	dotty.tools.dotc.Run.compileSources(Run.scala:297)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:161)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:357)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:158)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:149)
	scala.meta.internal.pc.CompilerAccess.$anonfun$1(CompilerAccess.scala:93)
	scala.meta.internal.pc.CompilerAccess.onCompilerJobQueue$$anonfun$1(CompilerAccess.scala:210)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:153)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1583)
```
#### Short summary: 

java.lang.NullPointerException: Cannot invoke "dotty.tools.dotc.parsing.Scanners$Region.isOutermost()" because the return value of "dotty.tools.dotc.parsing.Scanners$Scanner.currentRegion()" is null