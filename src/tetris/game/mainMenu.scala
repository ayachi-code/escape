// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game


import engine.graphics.Point
import processing.core.PApplet
import tetris.logic._
import ddf.minim.Minim


class mainMenu(PApplet: PApplet, minmin: Minim) extends Scene {

  val mono = PApplet.createFont("src/tetris/assets/horror.ttf", 200)
  val fontNumber = PApplet.createFont("src/tetris/assets/number.ttf", 75)
  val button = new Button(PApplet, Point(PApplet.width/2 - PApplet.width/4,PApplet.height/2 + PApplet.height/5),PApplet.width/2,50, "Descend", mono)

  val backgroundAudio = minmin.loadFile("src/tetris/assets/main.mp3")
  var clickAudio = minmin.loadFile("src/tetris/assets/audioClick2.mp3")

  backgroundAudio.loop()

  def run(): Unit = {
    PApplet.background(0)
    button.display()

    PApplet.fill(255, 36, 0)
    PApplet.textFont(mono)
    PApplet.textSize(150)
    PApplet.text("Escape",PApplet.width / 2 - 200,200)

    PApplet.textFont(mono)
    PApplet.textSize(75)
    PApplet.fill(255,0,0)
    PApplet.text("Highscore ",50,200 + 100)

    PApplet.textFont(fontNumber)
    PApplet.textSize(75)
    PApplet.fill(255,0,0)
    PApplet.text(":",PApplet.width / 2 + 100,200 + 75)

    PApplet.textFont(fontNumber)
    PApplet.textSize(75)
    PApplet.fill(255,0,0)
    PApplet.text("0",PApplet.width / 2 + 150,200 + 95)

    if (button.pressed()) {
      clickAudio.play()
      println("change")
      clickAudio.rewind()
    }
  }

}

object mainMenu {
  def main(args: Array[String]): Unit = {
    PApplet.main("tetris.game.mainMenu")
  }
}