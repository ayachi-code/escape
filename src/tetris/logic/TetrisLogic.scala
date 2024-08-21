package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.logic.TetrisLogic._


// TODO: Add player that can move and win condition


class TetrisLogic(val randomGen: RandomGenerator,
                  val gridDims : Dimensions,
                  val initialBoard: Seq[Seq[CellType]]) {


  var gameState = GameState(Point(0, 0), gameDone = false)

  val mazeGrid = new Maze(10,10).generateMaze()

  println("debug")

  def this(random: RandomGenerator, gridDims : Dimensions) =
    this(random, gridDims, makeEmptyBoard(gridDims))

  def this() =
    this(new ScalaRandomGen(), DefaultDims, makeEmptyBoard(DefaultDims))

  // TODO implement me
  def rotateLeft(): Unit = ()

  // TODO implement me
  def rotateRight(): Unit = ()


  def moveUp(): Unit = {
    mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
    mazeGrid(gameState.playerPosition.y - 1)(gameState.playerPosition.x).setPlayer(true)
    gameState = GameState(playerPosition = Point(gameState.playerPosition.x, gameState.playerPosition.y - 1), gameDone = false)
  }

  // TODO implement me
  def moveLeft(): Unit = {
    mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
    mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x - 1).setPlayer(true)
    gameState = GameState(playerPosition = Point(gameState.playerPosition.x - 1, gameState.playerPosition.y), gameDone = false)
  }

  // TODO implement me
  def moveRight(): Unit = {
    mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
    mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x + 1).setPlayer(true)
    gameState = GameState(playerPosition = Point(gameState.playerPosition.x + 1, gameState.playerPosition.y), gameDone = false)
  }

  // TODO implement me
  def moveDown(): Unit = {
    mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
    mazeGrid(gameState.playerPosition.y + 1)(gameState.playerPosition.x).setPlayer(true)
    gameState = GameState(playerPosition = Point(gameState.playerPosition.x, gameState.playerPosition.y + 1), gameDone = false)
  }

  // TODO implement me
  def doHardDrop(): Unit = ()

  // TODO implement me
  def isGameOver: Boolean = false

  def getWalls(p : Point): List[Char] = {
    mazeGrid(p.y)(p.x).getWalls()
  }


  // TODO implement me
  def getCellType(p : Point): CellType = {
    if (mazeGrid(p.y)(p.x).isPlayerOn) {
      return PlayerCell
    }
    Empty
  }
}

object TetrisLogic {

  val FramesPerSecond: Int = 5 // change this to speed up or slow down the game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller


  var maze = new Maze(10,10).generateMaze()


  def makeEmptyBoard(gridDims : Dimensions): Seq[Seq[CellType]] = {
    val emptyLine = Seq.fill(gridDims.width)(MazeCell)
    Seq.fill(gridDims.height)(emptyLine)
  }


  val DefaultWidth: Int = 30
  val NrTopInvisibleLines: Int = 4
  val DefaultVisibleHeight: Int = 30
  val DefaultHeight: Int = DefaultVisibleHeight //+ NrTopInvisibleLines
  val DefaultDims : Dimensions = Dimensions(width = DefaultWidth, height = DefaultHeight)


  def apply() = new TetrisLogic(new ScalaRandomGen(),
    DefaultDims,
    makeEmptyBoard(DefaultDims))

}