package escape.logic

import ddf.minim.Minim
import escape.game.GameStateManager

import scala.collection.mutable.ArrayBuffer


class EscapeLogic(soundEffects: Map[String, Audio]) {


  var player : Player = new Player
  var maze: Maze = Maze(10,10, player)
  var mazeGrid: ArrayBuffer[ArrayBuffer[Cell]] = maze.generateMaze()

  var gameState: GameState = GameState(false, player, 20, gameDone = false, leaveRoomButtonPressed = false, 0, transits = false, mazeGrid)

  var gridDims: Dimensions = Dimensions(maze.width * 3, maze.height * 3 + 6)
  var mazeDim: Dimensions = Dimensions(maze.width * 3, maze.height * 3)

  var audioEnabled : Boolean = true
  var immunityCooldownActive = false

  def finishAttack(): Unit = {
    gameState = gameState.copy(attackAnimation = false)
    maze.mazeCells(gameState.player.playersWeapons.last.attackCell.y)(gameState.player.playersWeapons.last.attackCell.x).isAttacked = false
    gameState.player.playersWeapons = gameState.player.playersWeapons.dropRight(1)
  }

  def attack(): Unit = {
    if (gameState.player.playersWeapons.nonEmpty && !gameState.attackAnimation) {
      gameState.player.playersWeapons.last.attack(maze)
      gameState = gameState.copy(attackAnimation = true)
      if (audioEnabled) soundEffects("attack").play()
    }
  }

  def ghostHit() : Unit = {
    maze.enemys.foreach(enemy => {
      if (enemy.point == gameState.player.position) {
        if (!immunityCooldownActive) {
          gameState.player.setHp(gameState.player.hp - 1)
          immunityCooldownActive = true
        }}
    })
  }

  def moveUp(): Unit = {
    if (isMovePossible(gameState.player.position, 'n')) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).setPlayer(false)
      mazeGrid(gameState.player.position.y - 1)(gameState.player.position.x).setPlayer(true)
      gameState.player.move('n')
      maze.playerPosition = gameState.player.position
      checkCollisions()

    }
  }

  def moveLeft(): Unit = {
    if (isMovePossible(gameState.player.position, 'w')) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).setPlayer(false)
      mazeGrid(gameState.player.position.y)(gameState.player.position.x - 1).setPlayer(true)
      gameState.player.move('w')
      maze.playerPosition = gameState.player.position
      checkCollisions()
    }
  }

  def checkCoinCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isCoin && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      if (audioEnabled) soundEffects("coin").play()
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

      if (audioEnabled) soundEffects("clock").play()
    }
  }

  def checkSwordCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isWeapon && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn && gameState.player.playersWeapons.length <= 9) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isWeapon = false
      gameState.player.playersWeapons = gameState.player.playersWeapons :+ Sword(gameState.player)

      if (audioEnabled) soundEffects("loot").play()
    }
  }

  def checkHeartCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isHeart && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isHeart = false
      gameState.player.setHp(gameState.player.hp + 1)

      if (audioEnabled) soundEffects("hp").play()
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
      if (audioEnabled) soundEffects("openDoor").play()
      gameState = gameState.copy(transits = true)
    }
  }

  def checkKeyCollision(): Unit = {
    if (mazeGrid(gameState.player.position.y)(gameState.player.position.x).isKey && mazeGrid(gameState.player.position.y)(gameState.player.position.x).isPlayerOn) {
      gameState.player.keyState(true)
      mazeGrid(gameState.player.position.y)(gameState.player.position.x).isKey = false

      if (audioEnabled) soundEffects("loot").play()
    }
  }

  def getCellTypes(p : Point): Array[CellType] = gameState.getCellTypes(p)
}

object EscapeLogic {

  val FramesPerSecond: Int = 10 // change this to speed up or slow down the game
  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  val DefaultWidth: Int = 30
  val DefaultVisibleHeight: Int = 36
}