// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import engine.GameBase
import processing.core.{PApplet, PConstants, PFont, PImage}
import ddf.minim.{AudioPlayer, Minim}
import processing.event.KeyEvent

import java.awt.event.KeyEvent.{VK_A, VK_DOWN, VK_LEFT, VK_RIGHT, VK_S, VK_SPACE, VK_UP, VK_V}


class driver extends PApplet{
  var gameState = new GameStateManager
  gameState.setGameState("game")

  var allScenes = Map[String, Scene]()

  def resz(width: Int, heigth: Int): Unit = {
    surface.setSize(100,100)
  }

  override def draw(): Unit = {
    allScenes(gameState.currentGameState).run(surface)
  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    size(470, 540)
  }

  override def keyPressed(event: KeyEvent): Unit = {
    allScenes(gameState.currentGameState).keyEvent(event)
  }

  override def setup(): Unit = {
    text("", 0, 0)

    surface.setTitle("Escape");
    val min = new Minim(this)

    frame.setResizable(true)

    var assets = Map[String, PImage]("ghost" -> loadImage("src/tetris/assets/ghost.png"), "coin" -> loadImage("src/tetris/assets/x.png"), "clock" -> loadImage("src/tetris/assets/clocko.png"), "sword" -> loadImage("src/tetris/assets/weapons/sword/sword_1.png")   )

    allScenes = Map[String, Scene]("start" -> new mainMenu(this, min, gameState), "gameOver" -> new gameOver(this, min, gameState), "game" -> new TetrisGame(this, min, gameState, assets))

  }
}

object driver {
  def main(args: Array[String]): Unit = {
    PApplet.main("tetris.game.driver")
  }
}