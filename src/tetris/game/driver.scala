// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import processing.core.{PApplet, PImage}
import ddf.minim.{Minim}
import processing.event.KeyEvent
import javax.sound.sampled.AudioSystem.getMixerInfo

//TODO: Clean code --> Audio class, load all assets once!, Error handling(no audio) + no audio mode

class driver extends PApplet{
  var gameState = GameStateManager("start", 0, 0, audioEnabled = true)
//  gameState.setGameState("start")

  var allScenes: Map[String, Scene] = Map[String, Scene]()

  override def draw(): Unit = {
    gameState = allScenes(gameState.scene).run(surface, gameState)
    var x = 11
  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    size(470, 540)
  }

  override def keyPressed(event: KeyEvent): Unit = {
    allScenes(gameState.scene).keyEvent(event)
  }

  override def setup(): Unit = {
    text("", 0, 0)

    surface.setTitle("Escape")
    val min = new Minim(this) // Try catch here for audio

    val assets = Map[String, PImage]("ghost" -> loadImage("src/tetris/assets/ghost.png"), "coin" -> loadImage("src/tetris/assets/x.png"), "clock" -> loadImage("src/tetris/assets/clocko.png"), "sword" -> loadImage("src/tetris/assets/weapons/sword/sword_1.png"))

    allScenes = Map[String, Scene]("start" -> new mainMenu(this, min, gameState), "gameOver" -> new gameOver(this, min, gameState), "game" -> new TetrisGame(this, min, gameState, assets))


    val mixers = getMixerInfo // If lenght is 0, no possible output for audio found

    val highScoreFile = scala.io.Source.fromFile("src/tetris/logic/highscore")
    val oldHighScore = highScoreFile.mkString
    gameState = gameState.copy(highScore = oldHighScore.toInt )
    highScoreFile.close()

  }
}

object driver {
  def main(args: Array[String]): Unit = {
    PApplet.main("tetris.game.driver")
  }
}