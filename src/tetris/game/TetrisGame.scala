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
  var mazeDims: Dimensions = gameLogic.mazeDim

  val updateTimer = new UpdateTimer(TetrisLogic.FramesPerSecond.toFloat)

  var gridDims: Dimensions = gameLogic.gridDims


  var widthInPixels: Int = (WidthCellInPixels * gridDims.width).ceil.toInt
  var heightInPixels: Int = (HeightCellInPixels * gridDims.height).ceil.toInt

  var screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)

  var changeState = false
  var time = 0

  var food : PImage = null
  var coin: PImage = null
  var clock: PImage = null
  var sword: PImage = null



  var immunityCooldownActive = false

  var immunityCooldown = 0


  def menu(): Unit = {
    fill(169, 139, 53)
    drawTextCentered("Gold: " + gameLogic.gameState.player.gold, 23, Point(45, ((screenArea.height / gridDims.height) * 3) / 2))
    drawTextCentered("Depth: " + gameLogic.gameState.level, 23, Point(screenArea.width - 49, ((screenArea.height / gridDims.height) * 3) / 2))
    if (gameLogic.gameState.player.gotKey) {
      drawKey(Rectangle(Point(100,(screenArea.height / gridDims.height) - 20), 45,45))
    }

    for (i <- 0 until gameLogic.gameState.player.hp) {
      drawHeart(Rectangle(Point(150 + i * 35,(screenArea.height / gridDims.height) - 20), 45,45))
    }

    fill(255, 0, 0)
    drawTextCentered(gameLogic.gameState.timeLeft.toString, 23, Point(screenArea.width - 49 - 100, ((screenArea.height / gridDims.height) * 3) / 2))

  }

  def weaponMenu(): Unit = {
    fill(192,192,192)
    drawTextCentered("Weapons: ", 23, Point(60, ((screenArea.height - 15))))

    drawTextCentered(gameLogic.gameState.player.playersWeapons.length.toString, 23, Point(150, ((screenArea.height - 15))))
    drawWeapon(Rectangle(Point(150 + 10, screenArea.height - 45), 30,30))
  }

  def showAttackAnimation(): Unit = {
    gameLogic.gameState.player.playersWeapons.last.animation(gameLogic.maze)
  }

  override def draw(): Unit = {
//    background(48,25,52) // clears old frame
    background(0)
    updateState()

    if (gameLogic.gameState.attackAnimation) {
      showAttackAnimation()
    }

    drawGrid()
    menu()
    weaponMenu()


    if (gameLogic.gameState.attackAnimation && millis() - time >= 500) {
      gameLogic.gameState = gameLogic.gameState.copy(attackAnimation = false)
      gameLogic.maze.mazeCells(gameLogic.gameState.player.playersWeapons.last.attackCell.y)(gameLogic.gameState.player.playersWeapons.last.attackCell.x).isAttacked = false
      gameLogic.gameState.player.playersWeapons = gameLogic.gameState.player.playersWeapons.dropRight(1)
    }

    if (immunityCooldownActive && millis() - time >= 1000) {
        immunityCooldownActive = false
    }

    gameLogic.maze.enemys.foreach(enmy => {
      if (enmy.point == gameLogic.gameState.player.position) {
        if (!immunityCooldownActive) {
          gameLogic.gameState.player.setHp(gameLogic.gameState.player.hp - 1)
          immunityCooldownActive = true
      }}
    })

    if (gameLogic.isGameOver) drawGameOverScreen()

//    gameLogic.maze.enemyPath()

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

    def drawCell(area: Rectangle, walls: List[Char], typeOfCell: Array[CellType]): Unit = {
      typeOfCell.foreach {
        case PlayerCell => drawPlayer(area)
        case Portal => drawPortal(area)
        case Coin => drawEnemy(area, coin)
        case PlayerOnDoor => drawPlayerOnDoor(area)
        case OpenPortal => {
          drawOpenDoor(area)
          changeState = true
        }
        case Key => drawKey(area)
        case Clock => drawEnemy(area, clock)
        case Enemy => drawEnemy(area, food)
        case SwordCell => drawEnemy(area, sword)
        case SwordAttack => drawAttackSword(area, gameLogic)
        case Heart => drawHeart(area)
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
      case VK_UP if !gameLogic.gameState.attackAnimation    => gameLogic.moveUp()
      case VK_DOWN if !gameLogic.gameState.attackAnimation  => gameLogic.moveDown()
      case VK_LEFT if !gameLogic.gameState.attackAnimation  => gameLogic.moveLeft()
      case VK_RIGHT if !gameLogic.gameState.attackAnimation => gameLogic.moveRight()
      case VK_SPACE => gameLogic.leaveRoom()
      case VK_V if !gameLogic.gameState.attackAnimation => gameLogic.attack()
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
    coin = loadImage("src/tetris/assets/x.png")
    clock = loadImage("src/tetris/assets/clocko.png")
    sword = loadImage("src/tetris/assets/weapons/sword/sword_1.png")



    // This should be called last, since the game
    // clock is officially ticking at this point
    updateTimer.init()

    time = millis()
  }

  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame()) {
      if (changeState) {
        delay(1000)

        // 10,10 = first stage(1 -- 3), 12,12 = second Stage (4 -- 6) 15,15 = third Stage ( 7 -- 10) fourth Stage = (11 - 13) 16,16  Fifth Stage (14 -- 16) 17,17, Final stage(6) (17 --> 100000) 20,17


        val newSize = gameLogic.difficultyCurve(gameLogic.gameState.level + 1)
        gameLogic.maze = Maze(newSize.width,newSize.height, gameLogic.gameState.player)
        gameLogic.mazeGrid = gameLogic.maze.generateMaze()
        gameLogic.gameState.player.nextRound()

        gridDims = Dimensions(gameLogic.maze.width * 3, gameLogic.maze.height * 3 + 6)
        mazeDims = Dimensions(gameLogic.maze.width * 3, gameLogic.maze.height * 3)
        gameLogic.gameState = gameLogic.gameState.copy(timeLeft = 20, transits = false, level = gameLogic.gameState.level + 1)


        widthInPixels = (WidthCellInPixels * gridDims.width).ceil.toInt
        heightInPixels = (HeightCellInPixels * gridDims.height).ceil.toInt
        screenArea = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)
        surface.setSize(widthInPixels, heightInPixels)

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