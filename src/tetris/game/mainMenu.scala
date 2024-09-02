// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import engine.graphics.Point
import processing.core.{PApplet, PFont}
import tetris.logic._
import ddf.minim.Minim
import engine.GameBase
import processing.event.KeyEvent

class mainMenu(PApplet: PApplet, minmin: Minim, state: GameStateManager) extends GameBase(PApplet) with Scene {

  val mono: PFont = PApplet.createFont("src/tetris/assets/horror.ttf", 200)
  val fontNumber: PFont = PApplet.createFont("src/tetris/assets/number.ttf", 75)
  val button = new Button(PApplet, Point(PApplet.width/2 - PApplet.width/4,PApplet.height/2 + PApplet.height/5),PApplet.width/2,50, "Descend", mono)

  val backgroundAudio: Audio = new Audio("src/tetris/assets/main.mp3", minmin)
  val clickAudio: Audio = new Audio("src/tetris/assets/audioClick2.mp3", minmin)

  backgroundAudio.loop()

  var audioStartState = false

  def run(surface: processing.core.PSurface, state: GameStateManager) : GameStateManager = {

    if (!audioStartState) {
      audioStartState = true
      backgroundAudio.play()
    }

    PApplet.background(0)
    button.display()

    drawText("Escape", Point(PApplet.width / 2 - 200,200), (255,36,0), mono, 150)
    drawText("Highscore ", Point(50, 200 + 100), (255,0,0), mono, 75)
    drawText(":", Point(PApplet.width / 2 + 125, 200 + 75), (255,0,0), fontNumber, 75)
    drawText(state.highScore.toString, Point(PApplet.width / 2 + 150, 200 + 95), (255,0,0), fontNumber, 75)

    if (button.pressed()) {
      clickAudio.play()
      backgroundAudio.pause()
      state.setGameState("gameOver")
      PApplet.delay(100)
      audioStartState = false
    }
    state
  }

  override def keyEvent(event: KeyEvent): Unit = {}
}