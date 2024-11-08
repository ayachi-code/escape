// scalastyle:off

package escape.game

import processing.core.{PApplet, PImage}
import ddf.minim.Minim
import processing.event.KeyEvent
import escape.logic.Audio

import javax.sound.sampled.AudioSystem.getMixerInfo

class Driver extends PApplet {
  var gameState: GameStateManager = GameStateManager(
    scene = "start",
    score = 0,
    highScore = 0,
    audioEnabled = true,
    audioSupport = true
  )

  private var allScenes: Map[String, Scene] = Map[String, Scene]()

  override def draw(): Unit = gameState = allScenes(gameState.scene).run(surface, gameState)

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    size(470, 540)
  }

  override def keyPressed(event: KeyEvent): Unit = allScenes(gameState.scene).keyEvent(event)

  override def setup(): Unit = {
    text("", 0, 0)

    surface.setTitle("Escape")

    surface.setSize(470, 540)

    val assets = Map[String, PImage]("player" -> loadImage("src/escape/assets/sprite/mainC.png"), "swordEast" -> loadImage("src/escape/assets/weapons/sword/attackEast.png"), "swordWest" -> loadImage("src/escape/assets/weapons/sword/attackWest.png"), "swordSouth" -> loadImage("src/escape/assets/weapons/sword/attackSouth.png"), "swordNorth" -> loadImage("src/escape/assets/weapons/sword/attackNorth.png"), "door" -> loadImage("src/escape/assets/sprite/door.png"), "ghost" -> loadImage("src/escape/assets/sprite/ghost.png"), "coin" -> loadImage("src/escape/assets/sprite/coin.png"), "clock" -> loadImage("src/escape/assets/sprite/clock.png"), "sword" -> loadImage("src/escape/assets/weapons/sword/sword_1.png"), "key" -> loadImage("src/escape/assets/sprite/key.png"), "heart" -> loadImage("src/escape/assets/sprite/heart.png") )

    val mixers = getMixerInfo
    // This is to make sure devices without external speakers can play the game
    if (mixers.length <= 0) {
      gameState = gameState.copy(audioSupport = false, audioEnabled = false)
      allScenes = Map[String, Scene](
        "start" -> new MainMenu(this,null),
        "gameOver" -> new GameOver(this, null),
        "game" -> new EscapeGame(this, null, assets, null, null))
    } else {
      val min = new Minim(this)

      // The assets are initially loaded at the start of the game (setup)
      val mainMenuSounds: Map[String, Audio] = Map[String, Audio](
        "background" -> new Audio("src/escape/assets/soundEffects/main.mp3", min),
        "clickAudio" -> new Audio("src/escape/assets/soundEffects/audioClick2.mp3", min))

      val gameOverSounds: Map[String, Audio] = Map[String, Audio](
        "background" -> new Audio("src/escape/assets/soundEffects/main.mp3", min),
        "clickAudio" -> new Audio("src/escape/assets/soundEffects/audioClick2.mp3", min),
        "gameOver" -> new Audio("src/escape/assets/soundEffects/scaryman.mp3", min))

      val backgroundAudios: List[Audio] = List[Audio](
        new Audio("src/escape/assets/dungeonOST/bg1.mp3", min),
        new Audio("src/escape/assets/dungeonOST/bg2.mp3", min),
        new Audio("src/escape/assets/dungeonOST/bg3.mp3",min),
        new Audio("src/escape/assets/dungeonOST/bg4.mp3",min),
        new Audio("src/escape/assets/dungeonOST/bg5.mp3",min),
        new Audio("src/escape/assets/dungeonOST/bg6.mp3", min))

      val gameSound: Map[String, Audio] = Map[String, Audio](
        "hit" -> new Audio("src/escape/assets/soundEffects/hit.mp3", min),
        "clock" -> new Audio("src/escape/assets/soundEffects/clock.mp3", min),
        "coin" -> new Audio("src/escape/assets/soundEffects/coin.mp3", min),
        "loot" -> new Audio("src/escape/assets/soundEffects/loot.mp3", min),
        "openDoor" -> new Audio("src/escape/assets/soundEffects/opendoor.mp3", min),
        "attack" -> new Audio("src/escape/assets/soundEffects/attack.mp3", min),
        "hp" -> new Audio("src/escape/assets/soundEffects/hp.mp3", min))

      allScenes = Map[String, Scene](
        "start" -> new MainMenu(this, mainMenuSounds),
        "gameOver" -> new GameOver(this, gameOverSounds),
        "game" -> new EscapeGame(this, min, assets, backgroundAudios, gameSound))
    }

    val highScoreFile = scala.io.Source.fromFile("src/escape/logic/highscore")
    val oldHighScore = highScoreFile.mkString
    gameState = gameState.copy(highScore = oldHighScore.toInt)
    highScoreFile.close()
  }

}

object Driver   {
  def main(args: Array[String]): Unit = {
    PApplet.main("escape.game.Driver")
  }
}