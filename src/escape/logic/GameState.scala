package escape.logic

import scala.collection.mutable.ArrayBuffer

case class GameState(attackAnimation: Boolean,  player: Player, timeLeft: Int, gameDone: Boolean, leaveRoomButtonPressed: Boolean, level: Int, transits: Boolean, mazeGrid : ArrayBuffer[ArrayBuffer[Cell]]) {
  def getCellTypes(p : Point): Array[CellType] = {
    var array = Array[CellType]()

    if (mazeGrid(p.y)(p.x).isAttacked) array = array :+ SwordAttack

    if (mazeGrid(p.y)(p.x).isPortal) {
      if (transits) {
        array = array :+ OpenPortal
        return array
      }
      else array = array :+ Portal
    }

    if (mazeGrid(p.y)(p.x).isHeart) array = array :+ Heart
    if (mazeGrid(p.y)(p.x).isWeapon) array = array :+ SwordCell
    if (mazeGrid(p.y)(p.x).isEnemyOn) array = array :+ Enemy
    if (mazeGrid(p.y)(p.x).isClock) array = array :+ Clock
    if (mazeGrid(p.y)(p.x).isKey) array = array :+ Key
    if (mazeGrid(p.y)(p.x).isPlayerOn) array = array :+ PlayerCell
    if (mazeGrid(p.y)(p.x).isCoin) array = array :+ Coin

    array
  }

  def difficultyCurve(level: Int): Dimensions = {
    if (level >= 0 && level <= 3) return Dimensions(10, 10)
    if (level >= 4 && level <= 6) return Dimensions(12, 12)
    if (level >= 7 && level <= 10) return Dimensions(15, 15)
    if (level >= 11 && level <= 13) return Dimensions(16, 16)
    if (level >= 14 && level <= 16) return Dimensions(17, 17)

    Dimensions(18, 18)
  }

  def isGameOver: Boolean = if (timeLeft <= 0 && !transits || player.hp <= 0) true else false

  def getWalls(p : Point): List[Char] = {
    mazeGrid(p.y)(p.x).getWalls
  }


}