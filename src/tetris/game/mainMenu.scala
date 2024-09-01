// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import engine.graphics.Point
import processing.core.PApplet
import tetris.logic._
import ddf.minim.Minim
import engine.GameBase
import processing.event.KeyEvent

class mainMenu(PApplet: PApplet, minmin: Minim, state: GameStateManager) extends Scene {

  val mono = PApplet.createFont("src/tetris/assets/horror.ttf", 200)
  val fontNumber = PApplet.createFont("src/tetris/assets/number.ttf", 75)
  val button = new Button(PApplet, Point(PApplet.width/2 - PApplet.width/4,PApplet.height/2 + PApplet.height/5),PApplet.width/2,50, "Descend", mono)

  val backgroundAudio = minmin.loadFile("src/tetris/assets/main.mp3")
  var clickAudio = minmin.loadFile("src/tetris/assets/audioClick2.mp3")

  backgroundAudio.loop()

  var audioStartState = false

  def run(surface: processing.core.PSurface, state: GameStateManager) : GameStateManager = {

    if (!audioStartState) {
      audioStartState = true
      backgroundAudio.rewind()
      backgroundAudio.play()
    }

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
    PApplet.text(state.highScore.toString,PApplet.width / 2 + 150,200 + 95)

    if (button.pressed()) {
      clickAudio.play()
      clickAudio.rewind()
      backgroundAudio.pause()
      state.setGameState("game")
      PApplet.delay(100)
      audioStartState = false
    }
    state
  }

  override def keyEvent(event: KeyEvent): Unit = {

  }

}