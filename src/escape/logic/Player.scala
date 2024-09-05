package escape.logic

import engine.graphics.Color

import scala.collection.mutable.Stack

class Player() {
  var playersWeapons: List[Weapon] = List[Weapon](Sword(this), Sword(this), Sword(this), Sword(this), Sword(this), Sword(this), Sword(this), Sword(this), Sword(this))
  val maxHP : Int = 3

  var gold : Int = 0
  var hp : Int = 3
  var gotKey : Boolean = false
  var position : Point = Point(0,0)

  case class attackPair(point: Point, direction: Char)

  def possibleAttack(maze: Maze): List[attackPair] = {
    var positions: List[attackPair] = List[attackPair]()

    if (maze.inBound(Point(position.x + 1, position.y))  && !maze.mazeCells(position.y)(position.x).walls('e') &&  !maze.mazeCells(position.y)(position.x + 1).walls('w') )  positions = positions :+  attackPair((Point(position.x + 1, position.y)), 'e')

    if (maze.inBound(Point(position.x, position.y + 1)) && !maze.mazeCells(position.y)(position.x).walls('s') &&  !maze.mazeCells(position.y + 1)(position.x).walls('n')) positions = positions :+ attackPair((Point(position.x, position.y + 1)), 's')

    if (maze.inBound(Point(position.x - 1, position.y)) && !maze.mazeCells(position.y)(position.x).walls('w') &&  !maze.mazeCells(position.y)(position.x - 1).walls('e')) positions = positions :+ attackPair((Point(position.x - 1, position.y)), 'w')

    if (maze.inBound(Point(position.x, position.y - 1)) && !maze.mazeCells(position.y)(position.x).walls('n') &&  !maze.mazeCells(position.y - 1)(position.x).walls('s')) positions = positions :+ attackPair((Point(position.x, position.y - 1)), 'n')

    positions
  }

  def move(direction: Char): Unit = {
    direction match {
      case 'n' => position = Point(position.x, position.y - 1)
      case 'e' => position = Point(position.x + 1, position.y)
      case 's' => position = Point(position.x, position.y + 1)
      case 'w' => position = Point(position.x - 1, position.y)
    }
  }

  def increaseScore(increase: Int): Unit = {
    gold += increase
  }

  def keyState(state: Boolean): Unit = {
    gotKey = state
  }

  def setHp(newValue: Int): Unit = {
    hp = newValue
  }

  def nextRound(): Unit = {
    gotKey = false
    position = Point(0,0)
  }

}
