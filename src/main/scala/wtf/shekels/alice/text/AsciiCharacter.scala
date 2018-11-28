package wtf.shekels.alice.text

import java.awt.image.BufferedImage
import java.awt.{Color, Font, Graphics}

import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.filter.GrayscaleFilter
import wtf.shekels.alice.Util._

class AsciiCharacter(val char: String, val font: String) {

  private lazy val renderedImage: Image = {
    // Create new grayscale image to write our character to
    val image: BufferedImage = new BufferedImage(45, 70, BufferedImage.TYPE_BYTE_GRAY)

    // Get graphics context for the image so we can do stuff to it
    val graphics: Graphics = image.getGraphics

    // Set a white background
    graphics.setColor(Color.WHITE)
    graphics.fillRect(0, 0, 45, 75)

    // Write our character in the middle of the image in black
    graphics.setColor(Color.BLACK)
    graphics.setFont(new Font(font, Font.PLAIN, 75))
    graphics.drawString(char, 0, 60)

    Image.wrapAwt(image)
  }

  lazy val lightness: Double = {
    val pixels = renderedImage.filter(GrayscaleFilter).pixels
    val values = pixels.map(p => 255 - p.toColor.lightness)
    values.sum / values.length
  }

}
