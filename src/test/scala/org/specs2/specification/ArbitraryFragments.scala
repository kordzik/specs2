package org.specs2
package specification

import org.scalacheck._
import Arbitrary._
import main.Arguments
import control.LazyParameters._

trait ArbitraryFragments extends execute.StandardResults with PredefinedFragments {

  implicit def arbitraryFragment: Arbitrary[Fragment] = Arbitrary {
    Gen.frequency ( 
      (1, Gen.value(Step(success))), 
      (1, Gen.value(SpecStart("specStart"))),
      (1, Gen.value(SpecEnd("specEnd"))),
      (10, Gen.value(Text("text"))),
      (8, Gen.value(Example("ex1", success))),
      (1, Gen.value(end)),
      (2, Gen.value(p)),
      (1, Gen.value(br)))
  }
  implicit def arbitraryFragments: Arbitrary[Fragments] = Arbitrary {

    def genFragments(sz: Int): Gen[Fragments] = for (l <- Gen.listOfN(sz, arbitrary[Fragment])) yield Fragments(l)(Arguments())
    def sizedList(sz: Int): Gen[Fragments] = {
      if (sz <= 0) genFragments(1)
      else genFragments(sz)
    }
    Gen.sized(sz => sizedList(sz))
  }
}