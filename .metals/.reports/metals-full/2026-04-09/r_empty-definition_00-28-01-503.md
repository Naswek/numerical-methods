error id: file://<WORKSPACE>/lab2/backend/src/main/scala/solver/library/Library.scala:
file://<WORKSPACE>/lab2/backend/src/main/scala/solver/library/Library.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -PolynomialFunctions.p13.
	 -PolynomialFunctions.p13#
	 -PolynomialFunctions.p13().
	 -scala/Predef.PolynomialFunctions.p13.
	 -scala/Predef.PolynomialFunctions.p13#
	 -scala/Predef.PolynomialFunctions.p13().
offset: 802
uri: file://<WORKSPACE>/lab2/backend/src/main/scala/solver/library/Library.scala
text:
```scala
package solver.library

import solver.library.functions.{FunctionPack, Functions, Method1D}
import solver.library.systems.{SystemPack, Systems, SystemMethod}
import solver.methods.{ChordMethod, SecantMethod, SimpleIterationMethod, SystemNewton}

object Library {
    val functionPack: Map[Int, FunctionPack] = Map(
        1 -> PolynomialFunctions.p1,
        2 -> PolynomialFunctions.p2,
        3 -> PolynomialFunctions.p3,
        4 -> PolynomialFunctions.p4,
        5 -> PolynomialFunctions.p5,
        6 -> PolynomialFunctions.p6,
        7 -> PolynomialFunctions.p7,
        8 -> PolynomialFunctions.p8,
        9 -> PolynomialFunctions.p9,
        10 -> PolynomialFunctions.p10,
        11 -> PolynomialFunctions.p11,
        12 -> PolynomialFunctions.p12,
        13 -> PolynomialFunctions.p13@@,
        14 -> PolynomialFunctions.p14,
        15 -> PolynomialFunctions.p15,
        16 -> PolynomialFunctions.p16,
        17 -> PolynomialFunctions.p17,
        18 -> PolynomialFunctions.p18,
        19 -> PolynomialFunctions.p19,
        20 -> PolynomialFunctions.p20,
        21 -> PolynomialFunctions.p21,
        22 -> PolynomialFunctions.p22,
        23 -> PolynomialFunctions.p23,
        24 -> PolynomialFunctions.p24,
        25 -> PolynomialFunctions.p25,
        26 -> PolynomialFunctions.p26
    )

    val systemPack: Map[Int, SystemPack] = Map(
        1 -> Systems.sys1,
        2 -> Systems.sys2,
        3 -> Systems.sys3,
        4 -> Systems.sys4,
        5 -> Systems.sys5,
        6 -> Systems.sys6,
        7 -> Systems.sys7,
        8 -> Systems.sys8,
        9 -> Systems.sys9,
        10 -> Systems.sys10,
        11 -> Systems.sys11,
        12 -> Systems.sys12,
        13 -> Systems.sys13,
        14 -> Systems.sys14,
        15 -> Systems.sys15,
        16 -> Systems.sys16,
        17 -> Systems.sys17,
        18 -> Systems.sys18,
        19 -> Systems.sys19,
        20 -> Systems.sys20,
        21 -> Systems.sys21,
        22 -> Systems.sys22,
        23 -> Systems.sys23,
        24 -> Systems.sys24,
        25 -> Systems.sys25,
        26 -> Systems.sys26
    )

    val functionMethods: Map[Int, Method1D] = Map(
        1 -> new ChordMethod,
        2 -> new SecantMethod,
        3 -> new SimpleIterationMethod
    )

    val systemMethods: Map[Int, SystemMethod] = Map(
        1 -> new SystemNewton
    )
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 