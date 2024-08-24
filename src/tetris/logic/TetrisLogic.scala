package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.logic.TetrisLogic._

//Morgen
//TODO: Add difficulty curve + 9 gold coin reward

//TODO: Gamestate manager + progression levels + timer + highscore + voorkom dat als je knop niet kan ingedrukt houden om te lopen

class TetrisLogic(val randomGen: RandomGenerator,
                  val gridDims : Dimensions,
                  val initialBoard: Seq[Seq[CellType]], val mazeDim: Dimensions) {


  var gameState: GameState = GameState(false, new Player, 20, gameDone = false, leaveRoomButtonPressed = false, 1, transits = false)


  var maze = new Maze(10,10, gameState.player)

  var mazeGrid = maze.generateMaze()

  println("debug")

  def this(random: RandomGenerator, gridDims : Dimensions) =
    this(random, gridDims, makeEmptyBoard(gridDims), Dimensions(30,30))

  def this() =
    this(new ScalaRandomGen(), DefaultDims, makeEmptyBoard(DefaultDims), Dimensions(30,30))

  // TODO implement me
  def rotateLeft(): Unit = ()

  // TODO implement me
  def rotateRight(): Unit = ()

  def attack(): Unit = {
    if (gameState.player.playersWeapons.length > 0 && !gameState.attackAnimation) {
      println(gameState.attackAnimation)
      gameState.player.playersWeapons(gameState.player.playersWeapons.length - 1).attack(maze)
//      gameState.player.playersWeapons = gameState.player.playersWeapons.dropRight(1)
      gameState = gameState.copy(attackAnimation = true)
    }
    1
  }

  def enemyPath(p: Point): Unit = {
    mazeGrid(p.y)(p.x).isEnemyOn = false
    var z = enemyPath(p)
    mazeGrid(p.y)(p.x).isEnemyOn = true
  }

  def moveUp(): Unit = {
    if (isMovePossible(gameState.player.position, 'n')) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).setPlayer(false)
      mazeGrid(gameState.player.position.y - 1)(gameState.player.position.x).setPlayer(true)
      gameState.player.move('n')
      //gameState = gameState.copy()//gameState.player.copy(position = )// Point(gameState.player.position.x, gameState.player.position.y - 1))) // Point(gameState.player.position.x, gameState.player.position.y - 1), gameDone = false)

      maze.playerPosition = gameState.player.position

      if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isCoin && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
        gameState.player.increaseScore(1)
        //        gameState = gameState.copy(player = gameState.player.gold + 1)
        mazeGrid(gameState.player.position.y)(gameState.player.position.x).isCoin = false
      }

      checkCollisions()

    }
  }

  // TODO implement me
  def moveLeft(): Unit = {
    if (isMovePossible(gameState.player.position, 'w')) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).setPlayer(false)
      mazeGrid(gameState.player.position.y)(gameState.player.position.x - 1).setPlayer(true)
      gameState.player.move('w')
      //      gameState = gameState.copy(playerPosition = Point(gameState.player.position.x - 1, gameState.player.position.y), gameDone = false)

      maze.playerPosition = gameState.player.position


      checkCollisions()

    }
  }

  def checkCoinCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isCoin && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      gameState.player.increaseScore(1)
      //      gameState = gameState.copy(score = gameState.score + 1)
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isCoin = false
    }
  }

  def moveRight(): Unit = {
    if (isMovePossible(gameState.player.position, 'e')) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).setPlayer(false)
      mazeGrid(gameState.player.position.y)(gameState.player.position.x + 1).setPlayer(true)
      gameState.player.move('e')

      maze.playerPosition = gameState.player.position

      checkCollisions()
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
    if (isMovePossible(gameState.player.position, 's')) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).setPlayer(false)
      mazeGrid(gameState.player.position.y + 1)(gameState.player.position.x).setPlayer(true)
      gameState.player.move('s')
      //      gameState = gameState.copy(playerPosition = Point(gameState.player.position.x, gameState.player.position.y + 1), gameDone = false)

      maze.playerPosition = gameState.player.position

      checkCollisions()
    }
  }

  def checkClockCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isClock && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isClock = false
      gameState = gameState.copy(timeLeft = gameState.timeLeft + 6)
    }
  }

  def checkSwordCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isWeapon && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn && gameState.player.playersWeapons.length <= 9) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isWeapon = false
      gameState.player.playersWeapons = gameState.player.playersWeapons :+ Sword(gameState.player)
    }
  }

  def checkHeartCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isHeart && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn && gameState.player.playersWeapons.length <= 9) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isHeart = false
      gameState.player.setHp(gameState.player.hp + 1)
    }
  }

  def checkCollisions(): Unit = {
    checkKeyCollision()
    checkCoinCollision()
    checkClockCollision()
    checkSwordCollision()
    checkHeartCollision()
  }

  def leaveRoom(): Unit = {
    if (gameState.player.position == maze.portalLocation && gameState.player.gotKey) {
      gameState = gameState.copy(transits = true)
    }
  }


  def checkKeyCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isKey && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      gameState.player.keyState(true)
      //      gameState = gameState.copy(gotKey = true)
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isKey = false
    }
  }

  def isGameOver: Boolean = {
    if (gameState.timeLeft <= 0 && !gameState.transits) {
      return true
    }
    false
  }

  def getWalls(p : Point): List[Char] = {
    mazeGrid(p.y)(p.x).getWalls()
  }

  def getCellType(p : Point): Array[CellType] = {

    var array = Array[CellType]()

    if (mazeGrid(p.y)(p.x).isPortal) {
        if (gameState.transits) {
          array = array :+ OpenPortal
          return array
        }
        else array = array :+ Portal
    }

    if (mazeGrid(p.y)(p.x).isHeart) array = array :+ Heart //return Heart
    if (mazeGrid(p.y)(p.x).isAttacked) array = array :+ SwordAttack //return SwordAttack
    if (mazeGrid(p.y)(p.x).isWeapon) array = array :+ SwordCell  //return SwordCell

    if (mazeGrid(p.y)(p.x).isEnemyOn) array = array :+ Enemy //return Enemy

    if (mazeGrid(p.y)(p.x).isClock) array = array :+ Clock //return Clock

    if (mazeGrid(p.y)(p.x).isKey) array = array :+ Key //return Key

    if (mazeGrid(p.y)(p.x).isPlayerOn) array = array :+ PlayerCell
    //return PlayerCell

    if (mazeGrid(p.y)(p.x).isCoin) array = array :+ Coin //return Coin




//
//    if (mazeGrid(p.y)(p.x).isPortal) {
//      if (gameState.transits) {
//        return OpenPortal
//      }
//      return Portal
//    }


    array
  }
}

object TetrisLogic {

  val FramesPerSecond: Int = 120 // change this to speed up or slow down the game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller


  def makeEmptyBoard(gridDims : Dimensions): Seq[Seq[CellType]] = {
    val emptyLine = Seq.fill(gridDims.width)(MazeCell)
    Seq.fill(gridDims.height)(emptyLine)
  }

  val DefaultWidth: Int = 30
  val NrTopInvisibleLines: Int = 4
  val DefaultVisibleHeight: Int = 36
  val DefaultHeight: Int = DefaultVisibleHeight //+ NrTopInvisibleLines
  val DefaultDims : Dimensions = Dimensions(width = DefaultWidth, height = DefaultHeight)
  val mazeDims : Dimensions = Dimensions(width = 30, height = 30)

  def apply() = new TetrisLogic(new ScalaRandomGen(),
    DefaultDims,
    makeEmptyBoard(DefaultDims), mazeDims)

}