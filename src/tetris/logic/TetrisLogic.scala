package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.logic.TetrisLogic._


class TetrisLogic(val randomGen: RandomGenerator,
                  val gridDims : Dimensions,
                  val initialBoard: Seq[Seq[CellType]], val mazeDim: Dimensions) {


  var gameState = GameState(Point(0, 0), gameDone = false, 0, false)

  val maze = new Maze(10,10)

  val mazeGrid = maze.generateMaze()

  println("debug")

  def this(random: RandomGenerator, gridDims : Dimensions) =
    this(random, gridDims, makeEmptyBoard(gridDims), Dimensions(30,30))

  def this() =
    this(new ScalaRandomGen(), DefaultDims, makeEmptyBoard(DefaultDims), Dimensions(30,30))

  // TODO implement me
  def rotateLeft(): Unit = ()

  // TODO implement me
  def rotateRight(): Unit = ()

  def moveUp(): Unit = {
    if (isMovePossible(gameState.playerPosition, 'n')) {
      mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
      mazeGrid(gameState.playerPosition.y - 1)(gameState.playerPosition.x).setPlayer(true)
      gameState = gameState.copy(playerPosition = Point(gameState.playerPosition.x, gameState.playerPosition.y - 1), gameDone = false)

      if (mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).isCoin && mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).isPlayerOn) {
        gameState = gameState.copy(score = gameState.score + 1)
        mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).isCoin = false
      }

      checkCoinCollision()

    }
  }

  // TODO implement me
  def moveLeft(): Unit = {
    if (isMovePossible(gameState.playerPosition, 'w')) {
      mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
      mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x - 1).setPlayer(true)
      gameState = gameState.copy(playerPosition = Point(gameState.playerPosition.x - 1, gameState.playerPosition.y), gameDone = false)

      checkCoinCollision()

    }
  }

  def checkCoinCollision(): Unit = {
    if (mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).isCoin && mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).isPlayerOn) {
      gameState = gameState.copy(score = gameState.score + 1)
      mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).isCoin = false
    }
  }

  // TODO implement me
  def moveRight(): Unit = {
    if (isMovePossible(gameState.playerPosition, 'e')) {
      mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
      mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x + 1).setPlayer(true)
      gameState = gameState.copy(playerPosition = Point(gameState.playerPosition.x + 1, gameState.playerPosition.y), gameDone = false)

      checkCoinCollision()
    }
  }


  def isMovePossible(point: Point, move: Char): Boolean = {
    move match {
      case 's' => !(point.x < 0 || point.y + 1 < 0 || point.x > mazeGrid.length - 1 || point.y + 1 > mazeGrid.length - 1) && !mazeGrid(point.y + 1)(point.x).walls('n') && !mazeGrid(point.y)(point.x).walls('s')
      case 'n' => !(point.x < 0 || point.y - 1 < 0 || point.x > mazeGrid.length - 1 || point.y - 1 > mazeGrid.length - 1) && !mazeGrid(point.y - 1)(point.x).walls('s') && !mazeGrid(point.y)(point.x).walls('n')
      case 'e' => !(point.x + 1 < 0 || point.y < 0 || point.x + 1 > mazeGrid.length - 1|| point.y > mazeGrid.length - 1) && !mazeGrid(point.y)(point.x + 1).walls('w') && !mazeGrid(point.y)(point.x).walls('e')
      case 'w' => !(point.x - 1 < 0 || point.y < 0 || point.x - 1 > mazeGrid.length - 1 || point.y > mazeGrid.length - 1) && !mazeGrid(point.y)(point.x - 1).walls('e') && !mazeGrid(point.y)(point.x).walls('w')
    }

  }


  def moveDown(): Unit = {
    if (isMovePossible(gameState.playerPosition, 's')) {
      mazeGrid(gameState.playerPosition.y)(gameState.playerPosition.x).setPlayer(false)
      mazeGrid(gameState.playerPosition.y + 1)(gameState.playerPosition.x).setPlayer(true)
      gameState = gameState.copy(playerPosition = Point(gameState.playerPosition.x, gameState.playerPosition.y + 1), gameDone = false)

      checkCoinCollision()

    }
  }

  def leaveRoom(): Unit = {
    if (gameState.playerPosition == maze.portalLocation) gameState = gameState.copy(gameDone = true)
  }

  def isGameOver: Boolean = gameState.gameDone

  def getWalls(p : Point): List[Char] = {
    mazeGrid(p.y)(p.x).getWalls()
  }

  def getCellType(p : Point): CellType = {

//    if (mazeGrid(p.y)(p.x).isCoin && mazeGrid(p.y)(p.x).isPlayerOn) {
//      gameState = gameState.copy(score = gameState.score + 1)
//      mazeGrid(p.y)(p.x).isCoin = false
//      return Empty
//    }

    if (mazeGrid(p.y)(p.x).isPlayerOn && mazeGrid(p.y)(p.x).isPortal) {
      return PlayerOnDoor
    }


    if (mazeGrid(p.y)(p.x).isPlayerOn) {
      return PlayerCell
    }

    if (mazeGrid(p.y)(p.x).isPortal) {
      return Portal
    }

    if (mazeGrid(p.y)(p.x).isCoin) {
      return Coin
    }


    Empty
  }
}

object TetrisLogic {

  val FramesPerSecond: Int = 5 // change this to speed up or slow down the game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller


  def makeEmptyBoard(gridDims : Dimensions): Seq[Seq[CellType]] = {
    val emptyLine = Seq.fill(gridDims.width)(MazeCell)
    Seq.fill(gridDims.height)(emptyLine)
  }

  val DefaultWidth: Int = 30
  val NrTopInvisibleLines: Int = 4
  val DefaultVisibleHeight: Int = 33
  val DefaultHeight: Int = DefaultVisibleHeight //+ NrTopInvisibleLines
  val DefaultDims : Dimensions = Dimensions(width = DefaultWidth, height = DefaultHeight)
  val mazeDims : Dimensions = Dimensions(width = 30, height = 30)

  def apply() = new TetrisLogic(new ScalaRandomGen(),
    DefaultDims,
    makeEmptyBoard(DefaultDims), mazeDims)

}