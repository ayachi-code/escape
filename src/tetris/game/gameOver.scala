// scalastyle:off

package tetris.game

import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PFont, PImage}
import tetris.logic._
import ddf.minim.{AudioPlayer, Minim}
import engine.GameBase
import processing.event.KeyEvent


class gameOver(PApplet: PApplet, min: Minim, state: GameStateManager) extends GameBase(PApplet) with Scene {

  val mono: PFont = PApplet.createFont("src/tetris/assets/horror.ttf", 200)
  val fontNumber: PFont = PApplet.createFont("src/tetris/assets/number.ttf", 75)

  val button = new Button(PApplet, Point(PApplet.width / 2 - PApplet.width / 4, PApplet.height / 2 + PApplet.height / 5), PApplet.width / 2, 50, "  Restart", mono)

  val backgroundAudio = new Audio("src/tetris/assets/main.mp3", min)
  val clickAudio = new Audio("src/tetris/assets/audioClick2.mp3", min)
  val gameOverAudio = new Audio("src/tetris/assets/scaryman.mp3", min)

  var stateStart: Boolean = true


  def run(surface: processing.core.PSurface, state: GameStateManager): GameStateManager = {

    if (stateStart) {
      backgroundAudio.play()
      backgroundAudio.loop()
      gameOverAudio.play()
      stateStart = false
      println(button)
    }

    PApplet.background(0)
    button.display()

    drawText("Game over",Point(PApplet.width / 2 - 200, 175), (255, 36, 0), mono, 105)
    drawText("Depth ",Point(PApplet.width / 2 - 120, 300), (255, 0, 0), mono, 75)
    drawText(":",Point( PApplet.width / 2 + 50, 200 + 75), (255, 0, 0), fontNumber, 75)
    drawText(state.score.toString,Point( PApplet.width / 2 + 100, 200 + 95), (255, 0, 0), fontNumber, 75)


    if (button.pressed()) {
      clickAudio.play()
      state.setGameState("start")
      PApplet.delay(100)
      stateStart = true
      gameOverAudio.pause()
      backgroundAudio.pause()
    }
    state
  }

  def keyEvent(event: KeyEvent): Unit = {}

}