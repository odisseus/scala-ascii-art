package wtf.shekels.alice.image

import org.scalatest.{FlatSpec, Matchers}
import wtf.shekels.alice.text.Alphabet

import scala.io.{Codec, Source}

class ImageTransformerTest extends FlatSpec with Matchers {

  behavior of classOf[ImageTransformer].toString

  it should "correctly convert a sample image" in {
    // Given
    val chars = "-#"
    val alphabet = new Alphabet(chars, "Monospace")
    val sourcePath = getClass.getResource("/star.png").getPath

    // When
    val imageTransformer = new ImageTransformer(path = sourcePath, compressionFactor = 8, alphabet = alphabet)
    val result = imageTransformer.toString

    // Then
    val expectedResult = Source.fromURL(getClass.getResource("/star.txt"))(Codec.UTF8)
    result shouldBe expectedResult.mkString
  }

  it should "correctly process image dimension that aren't divisble by compression factor" in {
    // Given
    val chars = "."
    val alphabet = new Alphabet(chars, "Monospace")
    val sourcePath = getClass.getResource("/9x9black.png").getPath

    // When
    val imageTransformer = new ImageTransformer(path = sourcePath, compressionFactor = 8, alphabet = alphabet)
    val result = imageTransformer.toString

    // Then
    val expectedResult = Source.fromURL(getClass.getResource("/9x9black.txt"))(Codec.UTF8)
    result shouldBe expectedResult.mkString
  }

  it should "map white color to whitespace even if the alphabet contains very light characters" in {
    // Given
    val chars = ".`_#"
    val alphabet = new Alphabet(chars, "Monospace")
    val sourcePath = getClass.getResource("/stripes.png").getPath

    // When
    val imageTransformer = new ImageTransformer(path = sourcePath, compressionFactor = 8, alphabet = alphabet)
    val result = imageTransformer.toString

    // Then
    val expectedResult = Source.fromURL(getClass.getResource("/stripes.txt"))(Codec.UTF8)
    result shouldBe expectedResult.mkString
  }


}
