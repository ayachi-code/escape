// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import processing.core.{PApplet, PImage}
import ddf.minim.Minim
import processing.event.KeyEvent
import tetris.logic.Audio

import javax.sound.sampled.AudioSystem.getMixerInfo

//TODO: clean code --> timer beetje beneden

class driver extends PApplet{
  var gameState: GameStateManager = GameStateManager("start", 0, 0, audioEnabled = true, audioSupport = true)

  var allScenes: Map[String, Scene] = Map[String, Scene]()

  override def draw(): Unit = gameState = allScenes(gameState.scene).run(surface, gameState)

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    size(470, 540)
  }

  override def keyPressed(event: KeyEvent): Unit = allScenes(gameState.scene).keyEvent(event)

  override def setup(): Unit = {
    text("", 0, 0)

    surface.setTitle("Escape")

    val assets = Map[String, PImage]("ghost" -> loadImage("src/tetris/assets/sprite/ghost.png"), "coin" -> loadImage("src/tetris/assets/sprite/coin.png"), "clock" -> loadImage("src/tetris/assets/sprite/clock.png"), "sword" -> loadImage("src/tetris/assets/weapons/sword/sword_1.png"))

    val mixers = getMixerInfo
    if (mixers.length <= 0) { // No audio port found if true
      gameState = gameState.copy(audioSupport = false, audioEnabled = false)
      allScenes = Map[String, Scene]("start" -> new mainMenu(this, null, gameState, null), "gameOver" -> new gameOver(this, null, null), "game" -> new TetrisGame(this, null, assets, null, null))
    } else {

      val min = new Minim(this)

      val mainMenuSounds : Map[String, Audio] = Map[String, Audio]("background" -> new Audio("src/tetris/assets/soundEffects/main.mp3", min), "clickAudio" -> new Audio("src/tetris/assets/soundEffects/audioClick2.mp3", min))
      val gameOverSounds : Map[String, Audio] = Map[String, Audio]("background" -> new Audio("src/tetris/assets/soundEffects/main.mp3", min), "clickAudio" -> new Audio("src/tetris/assets/soundEffects/audioClick2.mp3", min),  "gameOver" -> new Audio("src/tetris/assets/soundEffects/scaryman.mp3", min))
      val backgroundAudios : List[Audio] = List[Audio](new Audio("src/tetris/assets/dungeonOST/bg1.mp3", min), new Audio("src/tetris/assets/dungeonOST/bg2.mp3", min), new Audio("src/tetris/assets/dungeonOST/bg3.mp3",min), new Audio("src/tetris/assets/dungeonOST/bg4.mp3",min), new Audio("src/tetris/assets/dungeonOST/bg5.mp3",min), new Audio("src/tetris/assets/dungeonOST/bg6.mp3", min))
      val gameSound : Map[String, Audio] = Map[String, Audio]("hit" -> new Audio("src/tetris/assets/soundEffects/hit.mp3", min), "clock" -> new Audio("src/tetris/assets/soundEffects/clock.mp3", min), "coin" -> new Audio("src/tetris/assets/soundEffects/coin.mp3", min), "loot" -> new Audio("src/tetris/assets/soundEffects/loot.mp3", min), "openDoor" -> new Audio("src/tetris/assets/soundEffects/opendoor.mp3", min), "attack" -> new Audio("src/tetris/assets/soundEffects/attack.mp3", min), "hp" -> new Audio("src/tetris/assets/soundEffects/hp.mp3", min))

      allScenes = Map[String, Scene]("start" -> new mainMenu(this, min, gameState, mainMenuSounds), "gameOver" -> new gameOver(this, min, gameOverSounds), "game" -> new TetrisGame(this, min, assets, backgroundAudios, gameSound))
    }

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