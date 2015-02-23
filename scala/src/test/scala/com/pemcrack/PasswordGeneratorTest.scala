package com.pemcrack

import org.specs2.mutable._

class PasswordGeneratorTest extends Specification {
  val alphabet = "abc"

  "PasswordGenerator with zero depth" should {
    val generator = new PasswordGenerator(alphabet, "prefix", 0)

    "have 1 combination" in {
      generator.combinations must beEqualTo(BigDecimal(1))
    }
    "have combination equal to prefix" in {
      generator.iterator.hasNext must beTrue
      generator.iterator.next must beEqualTo("prefix")
    }
    "have empty subset" in {
      generator.split.length must beEqualTo(0)
    }
  }

  "PasswordGenerator with one depth" should {
    val generator = new PasswordGenerator(alphabet, "prefix", 1)

    "have combinations equal to alphabet length" in {
      generator.combinations must beEqualTo(BigDecimal(3))
    }
    "have first combination equal to prefix plus first alphabet character" in {
      val iterator = generator.iterator
      iterator.hasNext must beTrue
      iterator.next must beEqualTo("prefixa")
    }
    "have three subsets" in {
      generator.split.length must beEqualTo(3)
    }
    "have subsets with depth of zero" in {
      val subsets = generator.split
      subsets(0).combinations must beEqualTo(1)
    }
  }

  "PasswordGenerator with two depth" should {
    val generator = new PasswordGenerator(alphabet, "prefix", 2)

    "have combinations equal to alphabet length squared" in {
      generator.combinations must beEqualTo(BigDecimal(Math.pow(alphabet.length, 2)))
      val iterator = generator.iterator
      iterator.hasNext must beTrue
      iterator.next.equals("prefixaa") must beTrue
      iterator.next.equals("prefixab") must beTrue
      iterator.next.equals("prefixac") must beTrue
      iterator.next.equals("prefixba") must beTrue
      iterator.next.equals("prefixbb") must beTrue
      iterator.next.equals("prefixbc") must beTrue
      iterator.next.equals("prefixca") must beTrue
      iterator.next.equals("prefixcb") must beTrue
      iterator.next.equals("prefixcc") must beTrue
      iterator.hasNext must beFalse
    }
  }
}
