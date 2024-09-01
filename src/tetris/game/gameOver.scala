// scalastyle:off

package tetris.game

import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PFont, PImage}
import tetris.logic._
import ddf.minim.{AudioPlayer, Minim}
import engine.GameBase
import processing.event.KeyEvent


class gameOver(PApplet: PApplet, min: Minim, state: GameStateManager) extends GameBase(PApplet) with Scene {

  var mono = PApplet.createFont("src/tetris/assets/horror.ttf", 200)
  var fontNumber = PApplet.createFont("src/tetris/assets/number.ttf", 75)

  var button = new Button(PApplet, Point(PApplet.width / 2 - PApplet.width / 4, PApplet.height / 2 + PApplet.height / 5), PApplet.width / 2, 50, "  Restart", mono)

  var minmin = new Minim(this)
  var backgroundAudio = min.loadFile("src/tetris/assets/main.mp3")
  var clickAudio = min.loadFile("src/tetris/assets/audioClick2.mp3")
  var gameOverAudio = min.loadFile("src/tetris/assets/scaryman.mp3")

  var stateStart: Boolean = true


  def keyEvent(event: KeyEvent): Unit = {

  }


  def run(surface: processing.core.PSurface, state: GameStateManager): GameStateManager = {

    if (stateStart) {
      backgroundAudio.rewind()
      gameOverAudio.rewind()
      backgroundAudio.loop()
      gameOverAudio.play()
      stateStart = false
      println(button)
    }

    PApplet.background(0)
    button.display()

    PApplet.fill(255, 36, 0)
    PApplet.textFont(mono)
    PApplet.textSize(105)
    PApplet.textAlign(PConstants.LEFT, PConstants.BASELINE)
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
    PApplet.text(state.score.toString, PApplet.width / 2 + 90, 200 + 95)

    if (button.pressed()) {
      clickAudio.play()
      clickAudio.rewind()
      state.setGameState("start")
      PApplet.delay(100)
      stateStart = true
      gameOverAudio.pause()
      backgroundAudio.pause()
    }
    state
  }
}