// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import java.awt.event
import java.awt.event.KeyEvent._
import engine.GameBase
import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PImage}
import processing.event.KeyEvent
import tetris.logic._
import tetris.game.TetrisGame._
import tetris.logic
import tetris.logic.{Point => GridPoint}

class TetrisGame extends GameBase {

  var gameLogic : TetrisLogic = TetrisLogic()
  val mazeDims: Dimensions = gameLogic.mazeDim
  val updateTimer = new UpdateTimer(TetrisLogic.FramesPerSecond.toFloat)

  val gridDims : Dimensions = gameLogic.gridDims
  val widthInPixels: Int = (WidthCellInPixels * gridDims.width).ceil.toInt
  val heightInPixels: Int = (HeightCellInPixels * gridDims.height).ceil.toInt
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)

  var changeState = false

  var waitBo = 1000
  var time = 0

  var food : PImage = null

  var immunityCooldownActive = false


  var immunityCooldown = 0


  def menu(): Unit = {
    fill(169, 139, 53)
    drawTextCentered("Gold: " + gameLogic.gameState.score, 23, Point(45, ((screenArea.height / gridDims.height) * 3) / 2))
    drawTextCentered("Depth: " + gameLogic.gameState.level, 23, Point(screenArea.width - 49, ((screenArea.height / gridDims.height) * 3) / 2))
    if (gameLogic.gameState.gotKey) {
      drawKey(Rectangle(Point(100,(screenArea.height / gridDims.height) - 20), 45,45))
    }

    for (i <- 0 until gameLogic.gameState.lives) {
      drawHeart(Rectangle(Point(150 + i * 35,(screenArea.height / gridDims.height) - 20), 45,45))
    }

    fill(255, 0, 0)
    drawTextCentered(gameLogic.gameState.timeLeft.toString, 23, Point(screenArea.width - 49 - 100, ((screenArea.height / gridDims.height) * 3) / 2))

  }

  override def draw(): Unit = {
    background(48,25,52) // clears old frame
    updateState()
    drawGrid()
    menu()

    if (immunityCooldownActive && millis() - time >= 1000) {
        immunityCooldownActive = false
    }

    gameLogic.maze.enemys.foreach(enmy => {
      if (enmy.point == gameLogic.gameState.playerPosition) {
        if (!immunityCooldownActive) {
          gameLogic.gameState = gameLogic.gameState.copy(lives = gameLogic.gameState.lives - 1)
          immunityCooldownActive = true
      }}
    })

//    if (millis() - time >= 500) {
//      gameLogic.maze.enemyPath()
//    }

    if (gameLogic.isGameOver) drawGameOverScreen()

    if(millis() - time >= 1000){
      gameLogic.maze.enemyPath()
      gameLogic.gameState = gameLogic.gameState.copy(timeLeft = gameLogic.gameState.timeLeft - 1)
      time = millis();
    }
  }

  def drawGameOverScreen(): Unit = {
    setFillColor(Color.Red)
    drawTextCentered("GAME OVER!", 20, screenArea.center)
  }

  def drawGrid(): Unit = {

    val widthPerCell = (screenArea.width / gridDims.width) * 3
    val heightPerCell = (screenArea.height / gridDims.height) * 3

    for (p <- mazeDims.allPointsInside) {
      drawCell(getCell(p), gameLogic.getWalls(p), gameLogic.getCellType(p))  // s
    }

    def getCell(p : GridPoint): Rectangle = {
      val leftUp = Point(screenArea.left + p.x * widthPerCell,
        screenArea.top + p.y * heightPerCell + heightPerCell)

      Rectangle(leftUp, widthPerCell, heightPerCell)
    }

    def drawCell(area: Rectangle, walls: List[Char], typeOfCell: CellType): Unit = {
      typeOfCell match {
        case PlayerCell => drawPlayer(area)
        case Portal => drawPortal(area)
        case Coin => drawCoin(area)
        case PlayerOnDoor => drawPlayerOnDoor(area)
        case OpenPortal => {
          drawOpenDoor(area)
          changeState = true
        }
        case Key => drawKey(area)
        case Clock => drawClock(area)
        case Enemy => drawEnemy(area, food)
        case _ => Empty
      }
      drawMazeCell(area, walls)
    }

  }


  override def keyReleased(event: KeyEvent): Unit = {
    event.getKeyCode match {
      case VK_S     => {
        println("test")
      }
      case _        => ()
    }
  }

  /** Method that calls handlers for different key press events.
   * You may add extra functionality for other keys here.
   * See [[event.KeyEvent]] for all defined keycodes.
   *
   * @param event The key press event to handle
   */
  override def keyPressed(event: KeyEvent): Unit = {

    event.getKeyCode match {
      case VK_A     => gameLogic.rotateLeft()
      case VK_S     => gameLogic.rotateRight()
      case VK_UP    => gameLogic.moveUp()
      case VK_DOWN  => gameLogic.moveDown()
      case VK_LEFT  => gameLogic.moveLeft()
      case VK_RIGHT => gameLogic.moveRight()
      case VK_SPACE => gameLogic.leaveRoom()
      case _        => ()
    }

  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    // If line below gives errors try size(widthInPixels, heightInPixels, PConstants.P2D)
    size(widthInPixels, heightInPixels)
  }

  override def setup(): Unit = {

    // Fonts are loaded lazily, so when we call text()
    // for the first time, there is significant lag.
    // This prevents it from happening during gameplay.
    text("", 0, 0)

    food = loadImage("src/tetris/assets/ghost.png")

    // This should be called last, since the game
    // clock is officially ticking at this point
    updateTimer.init()

    time = millis()
  }

  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame()) {
      if (changeState) {
        delay(1000)
        gameLogic.maze = new Maze(10,10)
        gameLogic.mazeGrid = gameLogic.maze.generateMaze()
        gameLogic.gameState = gameLogic.gameState.copy(timeLeft = 20, gotKey = false,level = gameLogic.gameState.level + 1,transits = false, playerPosition = tetris.logic.Point(0,0))
        changeState = false
      }
      updateTimer.advanceFrame()
    }
  }

}

object TetrisGame {
  val WidthCellInPixels: Double = 15 * TetrisLogic.DrawSizeFactor
  val HeightCellInPixels: Double = WidthCellInPixels

  def main(args:Array[String]): Unit = {
    PApplet.main("tetris.game.TetrisGame")
  }

}