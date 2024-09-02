// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import engine.graphics.{Point, Rectangle}
import processing.core.{PApplet, PFont, PImage}
import tetris.logic._
import ddf.minim.Minim
import engine.GameBase
import processing.event.KeyEvent
import tetris.logic.buttons._

class mainMenu(PApplet: PApplet, minmin: Minim, state: GameStateManager) extends GameBase(PApplet) with Scene {

  val mono: PFont = PApplet.createFont("src/tetris/assets/horror.ttf", 200)
  val fontNumber: PFont = PApplet.createFont("src/tetris/assets/number.ttf", 75)


  val startGameButton = new RectangleButton(PApplet, Point(PApplet.width/2 - PApplet.width/4,PApplet.height/2 + PApplet.height/5),PApplet.width/2,50, "Descend", mono)

  val audioTurnOnButton = new ImageButton(PApplet, Point(PApplet.width - 100, PApplet.height - 100), 100, 100, PApplet.loadImage("src/tetris/assets/audioImages/audio.png"))
  val audioTurnOffButton = new ImageButton(PApplet, Point(PApplet.width - 100, PApplet.height - 100), 100, 100, PApplet.loadImage("src/tetris/assets/audioImages/noAudio.png"))


  val backgroundAudio: Audio = new Audio("src/tetris/assets/main.mp3", minmin)
  val clickAudio: Audio = new Audio("src/tetris/assets/audioClick2.mp3", minmin)

  backgroundAudio.loop()

  var audioStartState = false
  var audioEnabledState = true


  def run(surface: processing.core.PSurface, state: GameStateManager) : GameStateManager = {

    if (!audioStartState && state.audioEnabled) {
      audioStartState = true
      backgroundAudio.play()
    }

    PApplet.background(0)
    startGameButton.display()

    if (state.audioEnabled) {
      audioTurnOffButton.display()
      audioTurnOffButton.enabled = true
      audioTurnOnButton.enabled = false

      if (!audioEnabledState) {
        backgroundAudio.continue()
        audioEnabledState = true
      }
    } else {
      audioTurnOnButton.display()
      audioTurnOffButton.enabled = false
      audioTurnOnButton.enabled = true

      backgroundAudio.pause()
      clickAudio.pause()

      audioEnabledState = false
    }

    drawText("Escape", Point(PApplet.width / 2 - 200,200), (255,36,0), mono, 150)
    drawText("Highscore ", Point(50, 200 + 100), (255,0,0), mono, 75)
    drawText(":", Point(PApplet.width / 2 + 125, 200 + 75), (255,0,0), fontNumber, 75)
    drawText(state.highScore.toString, Point(PApplet.width / 2 + 150, 200 + 95), (255,0,0), fontNumber, 75)

    if (startGameButton.pressed()) {
      clickAudio.play()
      backgroundAudio.pause()
      PApplet.delay(100)
      audioStartState = false
      return state.copy(scene = "game")
    }

    if (audioTurnOnButton.pressed()) {
      state.audioEnabled = true
      delay(100)
    }
    if (audioTurnOffButton.pressed()) {
      state.audioEnabled = false
      delay(100)
    }

    state
  }

  override def keyEvent(event: KeyEvent): Unit = {}
}