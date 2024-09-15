// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off
package escape.game

import engine.GameBase
import ddf.minim.{AudioPlayer, Minim}


import java.awt.event.KeyEvent._
import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants, PImage, PSurface}
import processing.event.KeyEvent
import escape.logic._
import escape.game.EscapeGame._
import escape.logic.{Point => GridPoint}

import java.io
import java.io.{File, FileWriter}
import scala.util.Random



class EscapeGame(PApplet: PApplet, min: Minim, assets: Map[String, PImage],  val backgroundSounds: List[Audio], soundEffects: Map[String, Audio]) extends GameBase(PApplet) with Scene{

  private val gameLogic: EscapeLogic = new EscapeLogic(min, soundEffects)
  private var mazeDims: Dimensions = gameLogic.mazeDim
  var rand = new Random()


  private val updateTimer = new UpdateTimer(EscapeLogic.FramesPerSecond.toFloat)

  var gridDims: Dimensions = gameLogic.gridDims

  private var widthInPixels: Int = (WidthCellInPixels * gridDims.width).ceil.toInt
  private var heightInPixels: Int = (HeightCellInPixels * gridDims.height).ceil.toInt
  private var screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)

  private var changeState = false

  private var time: Int = millis()
  private var timeAttack : Int = _

  updateTimer.init()


  var audioStartState = false
  private var bgAudio : Audio = null
  private var backgroundMusic: List[Audio] = backgroundSounds
  private var startedAnimation : Boolean = false


  def menu(): Unit = {
    setFillColor(169, 139, 53)
    drawTextCentered("Gold: " + gameLogic.gameState.player.gold, 23, Point(45, ((screenArea.height / gridDims.height) * 3) / 2))
    drawTextCentered("Depth: " + gameLogic.gameState.level, 23, Point(screenArea.width - 49, ((screenArea.height / gridDims.height) * 3) / 2))
    if (gameLogic.gameState.player.gotKey) drawSprite(Rectangle(Point(100,(screenArea.height / gridDims.height) - 20), 45,45), assets("key"))

    for (i <- 0 until gameLogic.gameState.player.hp) drawSprite(Rectangle(Point((150 + i * 35).toFloat, (screenArea.height / gridDims.height.toFloat) - 20), 45, 45), assets("heart"))

    setFillColor(255, 0, 0)
    drawTextCentered(gameLogic.gameState.timeLeft.toString, 23, Point(screenArea.width - 49 - 100, ((screenArea.height / gridDims.height) * 3) / 2 + 5))
  }

  def weaponMenu(): Unit = {
    setFillColor(192,192,192)
    drawTextCentered("Weapons: ", 23, Point(60, screenArea.height - 15))

    drawTextCentered(gameLogic.gameState.player.playersWeapons.length.toString, 23, Point(150, ((screenArea.height - 15))))
    drawSprite(Rectangle(Point(150 + 10, screenArea.height - 35), 30,30), assets("sword"))
  }

  private def showAttackAnimation(): Unit = gameLogic.gameState.player.playersWeapons.last.animation(gameLogic.maze)

  private def setAudio(state: GameStateManager): Unit = {
    gameLogic.audioEnabled = state.audioEnabled

    if (!audioStartState && state.audioEnabled) {
      audioStartState = true
      val index : Int = if (backgroundMusic.length <= 1) rand.nextInt(backgroundMusic.length) else rand.nextInt(backgroundMusic.length - 1)
      if (bgAudio != null) bgAudio.stop()
      bgAudio = backgroundMusic(index)
      backgroundMusic = backgroundMusic.patch(index, Nil, 1)
      bgAudio.loop()
    }

  }

  private def resetWindowStates(width: Int, height: Int, player: Player, mazeDimension : Dimensions): Unit = {
    gameLogic.maze = Maze(width,height, player)
    gameLogic.mazeGrid = gameLogic.maze.generateMaze()

    gridDims = Dimensions(gameLogic.maze.width * 3, gameLogic.maze.height * 3 + 6)
    mazeDims = mazeDimension

    widthInPixels = (WidthCellInPixels * gridDims.width).ceil.toInt
    heightInPixels = (HeightCellInPixels * gridDims.height).ceil.toInt
    screenArea = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)

  }


  def run(surface: processing.core.PSurface, state: GameStateManager): GameStateManager = {
    setAudio(state)
    setBackground((0,0,0))
    updateState(surface)
    setAudio(state)

    menu()
    weaponMenu()
    drawGrid()

    if (gameLogic.gameState.attackAnimation && !startedAnimation) {
      showAttackAnimation()
      timeAttack = millis() // Start of attack
      startedAnimation = true
    }

    if (gameLogic.gameState.attackAnimation && millis() - timeAttack >= 100) {
      gameLogic.finishAttack()
      timeAttack = millis()
      startedAnimation = false
    }

    if (gameLogic.immunityCooldownActive && millis() - time >= 1000) gameLogic.immunityCooldownActive = false

    gameLogic.ghostHit(state)

    if (gameLogic.gameState.gameDone) {
      state.scene = "gameOver"
      state.score = gameLogic.gameState.level

      resetWindowStates(10, 10, new Player, gameLogic.mazeDim)
      gameLogic.gameState = gameLogic.gameState.copy(gameDone = false, player = new Player, level = 0, timeLeft = 20, attackAnimation = false)

      if (state.score > state.highScore) {
        state.highScore = state.score
        val fileWriter = new FileWriter(new File("src/escape/logic/highscore"))
        fileWriter.write(state.score.toString)
        fileWriter.close()
      }

      surface.setSize(470, 540)

      if (state.audioEnabled) bgAudio.pause()
      audioStartState = false

      if (backgroundSounds != null) backgroundMusic = List[Audio](new Audio("src/escape/assets/dungeonOST/bg1.mp3", min), new Audio("src/escape/assets/dungeonOST/bg2.mp3", min), new Audio("src/escape/assets/dungeonOST/bg3.mp3",min), new Audio("src/escape/assets/dungeonOST/bg4.mp3",min), new Audio("src/escape/assets/dungeonOST/bg5.mp3",min), new Audio("src/escape/assets/dungeonOST/bg6.mp3", min))

      return state
    }

    if(millis() - time >= 1000){
      gameLogic.maze.enemyPath()
      gameLogic.gameState = gameLogic.gameState.copy(timeLeft = gameLogic.gameState.timeLeft - 1)
      time = millis();
    }
    state
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
      val smallSizeSprite = Rectangle(Point((area.left + area.right - area.width/2) / 2, (area.top + area.bottom - area.width / 2) / 2), area.width / 2, area.height / 2)

      typeOfCell.foreach {
        case PlayerCell => drawPlayer(area)
        case Portal => drawSprite(area, assets("door"))
        case Coin => drawSprite(smallSizeSprite, assets("coin"))
        case OpenPortal => {
          drawOpenDoor(area)
          changeState = true
        }
        case Key => drawSprite(area, assets("key"))
        case Clock => drawSprite(smallSizeSprite, assets("clock"))
        case Enemy => drawSprite(smallSizeSprite, assets("ghost"))
        case SwordAttack => drawAttackSword(area, gameLogic, assets)
        case SwordCell => drawSprite(smallSizeSprite, assets("sword"))
        case Heart => drawSprite(area, assets("heart"))
        case _ => Empty
      }
      drawMazeCell(area, walls)
    }

  }


  def keyEvent(event: KeyEvent): Unit = {

    event.getKeyCode match {
      case VK_UP if !gameLogic.gameState.attackAnimation => gameLogic.moveUp()
      case VK_DOWN if !gameLogic.gameState.attackAnimation  => gameLogic.moveDown()
      case VK_LEFT if !gameLogic.gameState.attackAnimation  => gameLogic.moveLeft()
      case VK_RIGHT if !gameLogic.gameState.attackAnimation => gameLogic.moveRight()
      case VK_SPACE => gameLogic.leaveRoom()
      case VK_V if !gameLogic.gameState.attackAnimation => gameLogic.attack()  //changeState = true
      case _        => ()
    }
  }

  def updateState(surface: PSurface): Unit = {
    if (updateTimer.timeForNextFrame()) {
      if (changeState) {
        delay(1000)
        val newSize = gameLogic.difficultyCurve(gameLogic.gameState.level + 1)
        if (newSize.width != gameLogic.maze.width) audioStartState = false // Make sure we get a new background audio when we increase maze size

        resetWindowStates(newSize.width, newSize.height, gameLogic.gameState.player, Dimensions(newSize.width * 3, newSize.height * 3))

        gameLogic.gameState.player.nextRound()
        gameLogic.gameState = gameLogic.gameState.copy(timeLeft = 20, transits = false, level = gameLogic.gameState.level + 1)


        surface.setSize(widthInPixels, heightInPixels)
        changeState = false
      }
      updateTimer.advanceFrame()
    }
  }

}

object EscapeGame {
  val WidthCellInPixels: Double = 15 * EscapeLogic.DrawSizeFactor
  val HeightCellInPixels: Double = WidthCellInPixels
}