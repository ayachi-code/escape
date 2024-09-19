// scalastyle:off

package escape.game

import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PFont, PImage}
import escape.logic._
import ddf.minim.{AudioPlayer, Minim}
import engine.GameBase
import processing.event.KeyEvent
import escape.logic.buttons.RectangleButton

class GameOver(PApplet: PApplet, sounds: Map[String, Audio]) extends GameBase(PApplet) with Scene {

  val mono: PFont = PApplet.createFont("src/escape/assets/fonts/horror.ttf", 200)
  val fontNumber: PFont = PApplet.createFont("src/escape/assets/fonts/number.ttf", 75)
  val button = new RectangleButton(PApplet, Point(PApplet.width.toFloat / 2 - PApplet.width.toFloat / 4, PApplet.height.toFloat / 2 + PApplet.height.toFloat / 5), PApplet.width.toFloat / 2, 50, "  Restart", mono)
  var stateStart: Boolean = true

  def run(surface: processing.core.PSurface, state: GameStateManager): GameStateManager = {
    if (stateStart) {
      if (sounds != null) sounds("background").play()
      if (sounds != null) sounds("background").loop()
      if (sounds != null) sounds("gameOver").play()
      stateStart = false
    }

    setBackground((0,0,0))
    button.display()

    resetTextAlign()
    drawText("Game over",Point(PApplet.width.toFloat / 2 - 200, 175), (255, 36, 0), mono, 105)
    drawText("Depth ",Point(PApplet.width.toFloat / 2 - 120, 300), (255, 0, 0), mono, 75)
    drawText(":",Point(PApplet.width.toFloat / 2 + 50, 200 + 75), (255, 0, 0), fontNumber, 75)
    drawText(state.score.toString,Point(PApplet.width.toFloat / 2 + 100, 200 + 95), (255, 0, 0), fontNumber, 75)


    if (button.pressed()) {
      if (sounds != null) sounds("clickAudio").play()
      sleep(100)
      stateStart = true
      if (sounds != null) sounds("gameOver").pause()
      if (sounds != null) sounds("background").pause()
      return state.copy(scene = "start")
    }
    state
  }

  def keyEvent(event: KeyEvent): Unit = {}
}