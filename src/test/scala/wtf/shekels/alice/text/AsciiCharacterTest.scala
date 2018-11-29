package wtf.shekels.alice.text

import org.scalatest.{FlatSpec, Inspectors, Matchers}

class AsciiCharacterTest extends FlatSpec with Matchers with Inspectors {

  behavior of classOf[AsciiCharacter].toString

  val fontName = "Monospace"

  it should "map the whitespace character to lightness level of 0" in {
    (new AsciiCharacter(' ', fontName)).lightness shouldBe 0.0
  }

  it should "map all the printable characters to lightness levels in (0; 255]" in {
    // Given
    // See https://en.wikipedia.org/wiki/ASCII#Printable_characters
    val printableAsciiCharacters = (0x21 to 0x7e).map(_.toChar)

    forAll(printableAsciiCharacters){ c =>
      // When
      val lightness = (new AsciiCharacter(c, fontName)).lightness
      // Then
      lightness should be > 0.0
      lightness should be <= 255.0
    }

  }

}
