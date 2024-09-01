package tetris.logic

import ddf.minim.{AudioPlayer, Minim}


//Morgen
//TODO: 9 coin reward + Highscore


//TODO: Gamestate manager + progression levels + timer + highscore + voorkom dat als je knop niet kan ingedrukt houden om te lopen

class TetrisLogic(minim: Minim) {


  var gameState: GameState = GameState(false, new Player, 20, gameDone = false, leaveRoomButtonPressed = false, 0, transits = false)

  var maze = new Maze(10,10, gameState.player)

  var mazeGrid = maze.generateMaze()

  var gridDims = Dimensions(maze.width * 3, maze.height * 3 + 6)

  var mazeDim = Dimensions(maze.width * 3, maze.height * 3)

  val clockSF = minim.loadFile("src/tetris/assets/soundeffects/clock.mp3")
  val coinSF = minim.loadFile("src/tetris/assets/soundeffects/coin.mp3")
  val lootSF = minim.loadFile("src/tetris/assets/soundeffects/loot.mp3")
  val openDoorSF = minim.loadFile("src/tetris/assets/soundeffects/opendoor.mp3")
  val attackSF = minim.loadFile("src/tetris/assets/soundeffects/attack.mp3")
  val hpSF = minim.loadFile("src/tetris/assets/soundeffects/hp.mp3")



  // TODO implement me
  def rotateLeft(): Unit = ()

  // TODO implement me
  def rotateRight(): Unit = ()

  def attack(): Unit = {
    if (gameState.player.playersWeapons.length > 0 && !gameState.attackAnimation) {
      println(gameState.attackAnimation)
      gameState.player.playersWeapons(gameState.player.playersWeapons.length - 1).attack(maze)
      gameState = gameState.copy(attackAnimation = true)
      attackSF.play()
      attackSF.rewind()
    }
    1
  }

  // 10,10 = first stage(1 -- 3), 12,12 = second Stage (4 -- 6) 15,15 = third Stage ( 7 -- 10) fourth Stage = (11 - 13) 16,16  Fifth Stage (14 -- 16) 17,17, Final stage(6) (17 --> 100000) 20,17
  def difficultyCurve(level: Int): Dimensions = {
//    if (level == 2) return Dimensions(20, 20)
    if (level >= 0 && level <= 3) return Dimensions(10, 10)
    if (level >= 4 && level <= 6) return Dimensions(12, 12)
    if (level >= 7 && level <= 10) return Dimensions(15, 15)
    if (level >= 11 && level <= 13) return Dimensions(16, 16)
    if (level >= 14 && level <= 16) return Dimensions(17, 17)

    Dimensions(18, 18)
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

      maze.playerPosition = gameState.player.position

      if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isCoin && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
        gameState.player.increaseScore(1)
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
      coinSF.play()
      coinSF.rewind()
      gameState.player.increaseScore(1)
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

      maze.playerPosition = gameState.player.position

      checkCollisions()
    }
  }

  def checkClockCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isClock && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isClock = false
      gameState = gameState.copy(timeLeft = gameState.timeLeft + 6)
      clockSF.play()
      clockSF.rewind()
    }
  }

  def checkSwordCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isWeapon && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn && gameState.player.playersWeapons.length <= 9) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isWeapon = false
      gameState.player.playersWeapons = gameState.player.playersWeapons :+ Sword(gameState.player)
      lootSF.play()
      lootSF.rewind()
    }
  }

  def checkHeartCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isHeart && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn && gameState.player.playersWeapons.length <= 9) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isHeart = false
      gameState.player.setHp(gameState.player.hp + 1)
      hpSF.play()
      hpSF.rewind()
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
      openDoorSF.play()
      openDoorSF.rewind()
      gameState = gameState.copy(transits = true)
    }
  }


  def checkKeyCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isKey && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      gameState.player.keyState(true)
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isKey = false
      lootSF.play()
      lootSF.rewind()
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

    array
  }
}

object TetrisLogic {

  val FramesPerSecond: Int = 60 // change this to speed up or slow down the game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller


  def makeEmptyBoard(gridDims : Dimensions): Seq[Seq[CellType]] = {
    val emptyLine = Seq.fill(gridDims.width)(MazeCell)
    Seq.fill(gridDims.height)(emptyLine)
  }

  val DefaultWidth: Int = 30
  //val NrTopInvisibleLines: Int = 4
  val DefaultVisibleHeight: Int = 36

//  def apply() = new TetrisLogic()

}