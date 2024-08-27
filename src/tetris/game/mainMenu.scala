// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

//import java.awt.event
import java.awt.event.KeyEvent._
import engine.GameBase
import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PFont, PImage}
import tetris.logic._
import ddf.minim
import ddf.minim.{AudioPlayer, Minim}


class mainMenu extends GameBase {

  var mono : PFont = null
  var fontNumber : PFont = _


  var button : Button = _
  var bg : PImage = _
  var minmin : ddf.minim.Minim = _
  var audio : AudioPlayer = _
  var clickAudio : AudioPlayer = _


  override def draw(): Unit = {
    background(bg) // clears old frame
    button.display()

    fill(255, 36, 0)
    textFont(mono)
    textSize(150)
    text("Escape",width / 2 - 200,200)

    textFont(mono)
    textSize(75)
    fill(255,0,0)
    text("Highscore ",50,200 + 100)

    textFont(fontNumber)
    textSize(75)
    fill(255,0,0)
    text(":",width / 2 + 100,200 + 75)

    textFont(fontNumber)
    textSize(75)
    fill(255,0,0)
    text("0",width / 2 + 125,200 + 95)

    if (button.pressed()) {
      println(clickAudio.getVolume)
      clickAudio.play()
      println("change")
      clickAudio.rewind()
    }
  }



  /** Method that calls handlers for different key press events.
   * You may add extra functionality for other keys here.
   * See [[event.KeyEvent]] for all defined keycodes.
   *
   * @param event The key press event to handle
   */

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    // If line below gives errors try size(widthInPixels, heightInPixels, PConstants.P2D)
    size(500, 500)
  }

  override def setup(): Unit = {

    // Fonts are loaded lazily, so when we call text()
    // for the first time, there is significant lag.
    // This prevents it from happening during gameplay.

    text("", 0, 0)

    surface.setTitle("Escape");


    mono = createFont("src/tetris/assets/horror.ttf", 200)
    fontNumber = createFont("src/tetris/assets/number.ttf", 75)
    bg = loadImage("src/tetris/assets/cc1.png")
    button = new Button(this, Point(width/2 - width/4,height/2 + height/5),width/2,50, "Ascend", mono)

    minmin = new Minim(this)
    audio = minmin.loadFile("src/tetris/assets/main.mp3")
    clickAudio = minmin.loadFile("src/tetris/assets/audioClick2.mp3")

    audio.loop()
  }

}

object mainMenu {
//  val WidthCellInPixels: Double = 15 * 1
//  val HeightCellInPixels: Double = WidthCellInPixels

  def main(args:Array[String]): Unit = {
    PApplet.main("tetris.game.mainMenu")
  }

}