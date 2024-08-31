// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game


import engine.GameBase
import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PFont, PImage}
import tetris.logic._
import ddf.minim.{AudioPlayer, Minim}


class gameOver(PApplet: PApplet, min: Minim) extends Scene {

  val mono = PApplet.createFont("src/tetris/assets/horror.ttf", 200)
  val fontNumber = PApplet.createFont("src/tetris/assets/number.ttf", 75)

  val button = new Button(PApplet, Point(PApplet.width / 2 - PApplet.width / 4, PApplet.height / 2 + PApplet.height / 5), PApplet.width / 2, 50, "  Restart", mono)

  val minmin = new Minim(this)
  val audio = minmin.loadFile("src/tetris/assets/main.mp3")
  val clickAudio = minmin.loadFile("src/tetris/assets/audioClick2.mp3")
  val gameOverAudio = minmin.loadFile("src/tetris/assets/scaryman.mp3")


  def run(): Unit = {
    PApplet.background(0) // clears old frame
    button.display()

    PApplet.fill(255, 36, 0)
    PApplet.textFont(mono)
    PApplet.textSize(105)
    PApplet.text("Game over", PApplet.width / 2 - 200, 175)

    PApplet.textFont(mono)
    PApplet.textSize(75)
    PApplet.fill(255, 0, 0)
    PApplet.text("Depth ", PApplet.width / 2 - 120, 200 + 100)

    PApplet.textFont(fontNumber)
    PApplet.textSize(75)
    PApplet.fill(255, 0, 0)
    PApplet.text(":", PApplet.width / 2 + 50, 200 + 75)

    PApplet.textFont(fontNumber)
    PApplet.textSize(75)
    PApplet.fill(255, 0, 0)
    PApplet.text("0", PApplet.width / 2 + 90, 200 + 95)

    if (button.pressed()) {
      println(clickAudio.getVolume)
      clickAudio.play()
      println("change")
      clickAudio.rewind()
    }
  }
}



//  override def setup(): Unit = {
//
//    // Fonts are loaded lazily, so when we call text()
//    // for the first time, there is significant lag.
//    // This prevents it from happening during gameplay.
//
//    text("", 0, 0)
//
//    surface.setTitle("Escape");
//
//
//    mono = createFont("src/tetris/assets/horror.ttf", 200)
//    fontNumber = createFont("src/tetris/assets/number.ttf", 75)
//
//    button = new Button(this, Point(width/2 - width/4,height/2 + height/5),width/2,50, "  Restart", mono)
//
//    minmin = new Minim(this)
//    audio = minmin.loadFile("src/tetris/assets/main.mp3")
//    clickAudio = minmin.loadFile("src/tetris/assets/audioClick2.mp3")
//    gameOverAudio = minmin.loadFile("src/tetris/assets/scaryman.mp3")
//
//    audio.loop()
//    gameOverAudio.play()
//  }
//
//}

//object gameOver {
//  def main(args:Array[String]): Unit = {
//    PApplet.main("tetris.game.gameOver")
//  }
//}