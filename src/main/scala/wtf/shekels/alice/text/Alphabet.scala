package wtf.shekels.alice.text

import scala.collection.immutable

/**
  * Collection of our selected characters + some functionality
  */
class Alphabet(val alphabet: String, val font: String = "Roboto") {

  /**
    * List of character objects for each character in `alphabet`
    */
  val characters: immutable.IndexedSeq[AsciiCharacter] = alphabet.map(c => new AsciiCharacter(c.toString, font))

  /**
    * Generates a map that pairs each character to its lightness value, used later to determine what character to use
    */
  def getMap: Map[Double, String] = {
    characters.map(c => c.lightness -> c.char).toMap
  }

}
