package tetris.logic

import engine.graphics.Color

import scala.collection.mutable.Stack

class Player() {
  var playersWeapons: List[Weapon] = List[Weapon](Sword(this), Sword(this), Sword(this))

  var gold : Int = 0
  var hp : Int = 3
  var gotKey : Boolean = false
  var position : Point = Point(0,0)

  def possibleAttack(maze: Maze): List[Point] = {
    var positions: List[Point] = List[Point]()

    if (maze.inBound(Point(position.x + 1, position.y))  && !maze.mazeCells(position.y)(position.x).walls('e') &&  !maze.mazeCells(position.y)(position.x + 1).walls('w') )  positions = positions :+ (Point(position.x + 1, position.y))

    if (maze.inBound(Point(position.x, position.y + 1)) && !maze.mazeCells(position.y)(position.x).walls('s') &&  !maze.mazeCells(position.y + 1)(position.x).walls('n')) positions = positions :+ (Point(position.x, position.y + 1))

    if (maze.inBound(Point(position.x - 1, position.y)) && !maze.mazeCells(position.y)(position.x).walls('w') &&  !maze.mazeCells(position.y)(position.x - 1).walls('e')) positions = positions :+ (Point(position.x - 1, position.y))

    if (maze.inBound(Point(position.x, position.y - 1)) && !maze.mazeCells(position.y)(position.x).walls('n') &&  !maze.mazeCells(position.y - 1)(position.x).walls('s')) positions = positions :+ (Point(position.x, position.y - 1))

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
