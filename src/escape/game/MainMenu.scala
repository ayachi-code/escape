// scalastyle:off

package escape.game

import engine.graphics.{Point, Rectangle}
import processing.core.{PApplet, PFont, PImage}
import escape.logic._
import ddf.minim.Minim
import engine.GameBase
import processing.event.KeyEvent
import escape.logic.buttons._

class MainMenu(PApplet: PApplet, sounds: Map[String, Audio] ) extends GameBase(PApplet) with Scene {

  val mono: PFont = PApplet.createFont("src/escape/assets/fonts/horror.ttf", 200)
  val fontNumber: PFont = PApplet.createFont("src/escape/assets/fonts/number.ttf", 75)

  private val startGameButton = new RectangleButton(PApplet, Point(PApplet.width.toFloat/2 - PApplet.width.toFloat/4,PApplet.height.toFloat/2 + PApplet.height.toFloat/5),PApplet.width.toFloat/2,50, "Descend", mono)

  private val audioTurnOnButton = new ImageButton(PApplet, Point(PApplet.width.toFloat - 100, PApplet.height.toFloat - 100), 100, 100, PApplet.loadImage("src/escape/assets/audioImages/audio.png"))
  private val audioTurnOffButton = new ImageButton(PApplet, Point(PApplet.width.toFloat - 100, PApplet.height.toFloat - 100), 100, 100, PApplet.loadImage("src/escape/assets/audioImages/noAudio.png"))

  if (sounds != null) sounds("background").loop()

  var audioStartState = false
  private var audioEnabledState = true

  def setAudio(state: GameStateManager): Unit = {
    if (state.audioEnabled && state.audioSupport) {
      audioTurnOffButton.display()
      audioTurnOffButton.enabled = true
      audioTurnOnButton.enabled = false

      if (!audioEnabledState) {
        if (sounds != null)  sounds("background").continue()
        audioEnabledState = true
      }
    } else if (!state.audioEnabled && state.audioSupport) {
      audioTurnOnButton.display()
      audioTurnOffButton.enabled = false
      audioTurnOnButton.enabled = true

      if (sounds != null) sounds("background").pause()
      if (sounds != null) sounds("clickAudio").pause()

      audioEnabledState = false
    }
  }


  def run(surface: processing.core.PSurface, state: GameStateManager) : GameStateManager = {

    if (!audioStartState && state.audioEnabled) {
      audioStartState = true
      if (sounds != null) sounds("background").play()
    }

    setBackground((0, 0, 0))
    startGameButton.display()
    setAudio(state)

    drawText("Escape", Point(PApplet.width.toFloat / 2 - 200,200), (255,36,0), mono, 150)
    drawText("Highscore ", Point(50, 200 + 100), (255,0,0), mono, 75)
    drawText(":", Point(PApplet.width.toFloat / 2 + 125, 200 + 75), (255,0,0), fontNumber, 75)
    drawText(state.highScore.toString, Point(PApplet.width.toFloat / 2 + 150, 200 + 95), (255,0,0), fontNumber, 75)

    if (startGameButton.pressed()) {
      if (sounds != null) sounds("clickAudio").play()
      if (sounds != null) sounds("background").pause()
      sleep(100)
      audioStartState = false
      return state.copy(scene = "game")
    }

    if (audioTurnOnButton.pressed()) {
      state.audioEnabled = true
      sleep(250)
    }

    if (audioTurnOffButton.pressed()) {
      state.audioEnabled = false
      sleep(250)
    }

    state
  }

  override def keyEvent(event: KeyEvent): Unit = {}
}