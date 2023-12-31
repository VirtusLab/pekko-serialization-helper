package org.random.project

import org.apache.pekko.persistence.typed.scaladsl.Effect

import scala.annotation.StaticAnnotation

sealed trait Data

object Data {
  class TestAnn(val info1: Int, val info2: Int = 0) extends StaticAnnotation

  class ClassTest(val a: String, var b: Int, c: Double) extends Data
  case class GenericsTest(val a: Map[String, Int], var b: Option[Int], c: Tuple3[Int, Int, Int]) extends Data

  case class AdditionalData(a: Int)
  case class ClassWithAdditionData(ad: AdditionalData) extends Data

  @TestAnn(info1 = 1, info2 = 2)
  @TestAnn(1)
  sealed trait TraitWithAnnotation extends Data {
    val a: Long
    val b: String
    val c: AdditionalData
    def someMethod: Boolean = ???
  }

  sealed trait DeepTrait extends Data
  sealed trait DeeperTrait extends Data
  case class DeepestClass(a: Int) extends DeeperTrait
}
