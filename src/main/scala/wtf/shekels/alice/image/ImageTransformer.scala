package wtf.shekels.alice.image

import java.io.File
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.{GrayscaleFilter, InvertFilter}
import wtf.shekels.alice.Util.withLightness
import wtf.shekels.alice.text.Alphabet

/**
  * Transforms an image file into ascii art
  * @param path String path to image
  * @param compressionFactor Integer for how scaled down the output should be (10 x 10 img w/ factor of 2 comes out 5x5)
  * @param alphabet Alphabet object of chosen characters with calculated lightness values
  * @param lightnessModifier Integer used to offset the lightness value when image is read. Adding a negative modifier removes most _'s.
  * @param invert Whether or not the image should be inverted, default true
  */
class ImageTransformer(val path: String,
                       val compressionFactor: Int,
                       val alphabet: Alphabet,
                       val lightnessModifier: Int = 0,
                       val invert: Boolean = true) {

  val file: File = new File(path)
  val map: Map[Double, String] = alphabet.getMap
  val lowest: Double = map.keys.reduceLeft((l, r) => if (l < r) l else r)

  private def applyFilters(block: Image): Image = {
    val grayscale = block.filter(GrayscaleFilter)
    if (invert) grayscale.filter(InvertFilter) else grayscale
  }

  private def toBlocks(source: Image, preferredBlockSize: Int): List[List[Image]] = {
    Iterator.range(0, source.height, preferredBlockSize).map { y =>
      Iterator.range(0, source.width, preferredBlockSize).map { x =>
        val blockWidth = Math.min(source.width - x, preferredBlockSize)
        val blockHeight = Math.min(source.height - y, preferredBlockSize)
        Image.wrapAwt(source.awt.getSubimage(x, y, blockWidth, blockHeight))
      }.toList
    }.toList
  }

  private def toString(block: Image): String = {
    // Small function to get the closest character for the block's lightness value
    val getClosest = (n: Double, coll: List[Double]) => coll.minBy(v => Math.abs(v - n))

    // Gets our character or potentially spaces. Characters are duplicated width-wise because text has a vertically biased aspect ratio.
    val getChar = (n: Double) => {
      if (n >= lowest) {
        map.get(getClosest(n, map.keys.toList)) match {
          case Some(x) => x + x
          case None => "  "
        }
      } else {
        "  "
      }
    }

    // Get lightness values and offset with our modifier if we chose to use one
    val pixelLightness: List[Double] = block.pixels.map(p => p.toColor.lightness + lightnessModifier).toList
    val blockLightness = pixelLightness.sum / pixelLightness.length
    getChar(blockLightness)
  }

  override def toString: String = {
    val source = Image.fromFile(file)
    val blocks = toBlocks(source, compressionFactor)
    blocks.map(_.map(applyFilters).map(toString(_)).mkString).mkString("\n")
  }

  /**
    * Print the image
    */
  def print(): Unit = {
    println(this.toString)
  }
}