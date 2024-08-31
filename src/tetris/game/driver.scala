// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

//import java.awt.event
import java.awt.event.KeyEvent._
import engine.GameBase
import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PFont, PImage}
import tetris.logic._
import ddf.minim
import ddf.minim.{AudioPlayer, Minim}


class driver extends GameBase {
  var gameState = new gameStateManager
  gameState.setGameState("start")

  var allScenes = Map[String, Scene]()

  val escapeGameScene = new TetrisGame


  override def draw(): Unit = {
    allScenes(gameState.currentGameState).run()
  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    // If line below gives errors try size(widthInPixels, heightInPixels, PConstants.P2D)
    size(500, 500)
  }

  override def setup(): Unit = {
    text("", 0, 0)
    surface.setTitle("Escape");
    var min = new Minim(this)
    allScenes = Map[String, Scene]("start" -> new mainMenu(this, min), "gameOver" -> new gameOver(this, min))

  }

}

object driver {
  def main(args: Array[String]): Unit = {
    PApplet.main("tetris.game.driver")
  }
}