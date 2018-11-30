package wtf.shekels.alice.text

import java.awt.image.BufferedImage
import java.awt.{Color, Font, Graphics2D}

import com.sksamuel.scrimage.Image
import wtf.shekels.alice.Util._

class AsciiCharacter(val char: Char, val fontName: String) {

  private lazy val renderedImage: BufferedImage = {
    val fontSize = 75

    // Create new grayscale image to write our character to
    // Add generous margins to make sure that ascenders or descenders aren't clipped
    val image: BufferedImage = new BufferedImage(fontSize * 5, fontSize * 5, BufferedImage.TYPE_BYTE_GRAY)

    // Get graphics context for the image so we can do stuff to it
    val graphics: Graphics2D = image.createGraphics

    // Set a white background
    graphics.setColor(Color.WHITE)
    graphics.fillRect(0, 0, image.getWidth, image.getHeight)

    // Write our character in the middle of the image in black
    graphics.setColor(Color.BLACK)
    val fnt = new Font(fontName, Font.PLAIN, fontSize)
    graphics.setFont(fnt)
    graphics.drawString(char.toString, image.getWidth / 2, image.getHeight / 2)

    // Clip the part where the actual character has been rendered
    val logicalCharacterBounds = fnt.createGlyphVector(graphics.getFontRenderContext, Array(char)).getLogicalBounds.getBounds
    image.getSubimage(
      logicalCharacterBounds.x + image.getWidth / 2,
      logicalCharacterBounds.y + image.getHeight / 2,
      logicalCharacterBounds.width,
      logicalCharacterBounds.height
    )
  }

  lazy val lightness: Double = {
    val pixels = Image.wrapAwt(renderedImage).pixels
    val values = pixels.map(p => 255 - p.toColor.lightness)
    values.sum / values.length
  }

}
