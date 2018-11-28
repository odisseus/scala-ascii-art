package wtf.shekels.alice

import com.sksamuel.scrimage.RGBColor

object Util {

  case class RGBColorWithLightness(color: RGBColor) extends AnyVal{
    def lightness: Double = color.toHSL.lightness * 255.0
  }

  implicit def withLightness(color: RGBColor): RGBColorWithLightness = RGBColorWithLightness(color)

}
