package escape.logic

import collection.mutable.Stack
import collection.mutable.ArrayBuffer
import scala.collection.mutable
import scala.util.Random

case class Maze(width: Int, height: Int, player: Player) {
  val area: Int = width * height
  var mazeCells: ArrayBuffer[ArrayBuffer[Cell]] = ArrayBuffer[ArrayBuffer[Cell]]()
  private var visitedCells: Int = 1

  val rand = new Random()
  var playerPosition: Point = Point(0, 0)
  val portalLocation: Point = Point(width - 1, height - 1)
  var enemyCap: Int = math.ceil(math.pow(2, width / 5)).toInt
  var enemys: Array[Enemy] = Array[Enemy]()

  private def generateMechanics(): Unit = {
    generateCoins()
    generateClock()
    generateEnemy()
    generateWeapons()
    generateHeart()
    val uniqueKeyLocation: Point = uniqueLocation()
    mazeCells(uniqueKeyLocation.y)(uniqueKeyLocation.x).isKey = true
  }

  private def initMaze(): Unit = {
    val roof = ArrayBuffer[Cell]().append(Cell(Point(0, 0)).addWall('n').addWall('w'))
    for (i <- 1 until width - 1) roof.append(Cell(Point(i, 0)).addWall('n'))
    mazeCells.append(roof.append(Cell(Point(width - 1, 0)).addWall('n').addWall('e')))

    for (i <- 1 until height - 1) {
      val row: ArrayBuffer[Cell] = ArrayBuffer[Cell]()
      for (j <- 0 until width) {
        if (j == 0) {
          row.append(Cell(Point(j, i)).addWall('w'))
        } else if (j == width - 1) {
          row.append(Cell(Point(j, i)).addWall('e'))
        } else {
          row.append(Cell(Point(j, i)))
        }
      }
      mazeCells.append(row)
    }

    val floor = ArrayBuffer[Cell]().append(Cell(Point(0, height - 1)).addWall('s').addWall('w'))
    for (l <- 1 until width - 1) floor.append(Cell(Point(l, height - 1)).addWall('s'))
    mazeCells.append(floor.append(Cell(Point(width - 1, height - 1)).addWall('s').addWall('e')))

    mazeCells(height - 1)(width - 1).isPortal = true
    generateMechanics()
  }

  case class Direction(position: Point, neigb: Stack[Point])

  def enemyPath(): Unit = {
    enemys.foreach(enemy => {
      mazeCells(enemy.point.y)(enemy.point.x).enemyIds = mazeCells(enemy.point.y)(enemy.point.x).enemyIds.filterNot(elm => elm == enemy.id)

      enemy.destination = playerPosition

      mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy = mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy :+ enemy.id
      val neighbours = enemy.stack.top.neigb

      var state: Boolean = true
      while (state) {
        if (neighbours.length <= 0) {
          mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy = mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy.filterNot(elm => elm == enemy.id)
          if (enemy.stack.nonEmpty) {
            enemy.stack.pop()
            mazeCells(enemy.point.y)(enemy.point.x).isEnemyOn = false
            enemy.point = enemy.stack.top.position

            mazeCells(enemy.stack.top.position.y)(enemy.stack.top.position.x).isEnemyOn = true
            mazeCells(enemy.point.y)(enemy.point.x).enemyIds = mazeCells(enemy.point.y)(enemy.point.x).enemyIds :+ enemy.id
          } else {
            enemy.stack.push(Direction(enemy.point, enemy.possibleMove()))
          }
          state = false
        }

        if (neighbours.nonEmpty) {
          if (!mazeCells(neighbours.top.y)(neighbours.top.x).visitByEnemy.contains(enemy.id) && neighbours.nonEmpty) {
            mazeCells(enemy.point.y)(enemy.point.x).isEnemyOn = false
            val move = neighbours.top
            enemy.point = move
            mazeCells(move.y)(move.x).isEnemyOn = true

            mazeCells(enemy.point.y)(enemy.point.x).enemyIds = mazeCells(enemy.point.y)(enemy.point.x).enemyIds :+ enemy.id

            enemy.stack.top.neigb.pop()
            enemy.stack.push(Direction(move, enemy.possibleMove()))
            state = false
          } else {
            neighbours.pop()
          }
        }
      }
    })
  }

  def inBound(point: Point): Boolean = {
    if (point.x >= 0 && point.x <= width - 1 && point.y <= height - 1 && point.y >= 0) {
      true
    } else {
      false
    }
  }

  private def possibleNeighbours(point: Point): List[Point] = {
    var positions: List[Point] = List[Point]()

    if (inBound(Point(point.x + 1, point.y)) && !mazeCells(point.y)(point.x + 1).isVisited) {
      positions = positions :+ Point(point.x + 1, point.y)
    }

    if (inBound(Point(point.x, point.y + 1)) && !mazeCells(point.y + 1)(point.x).isVisited) {
      positions = positions :+ Point(point.x, point.y + 1)
    }

    if (inBound(Point(point.x - 1, point.y)) && !mazeCells(point.y)(point.x - 1).isVisited) {
      positions = positions :+ Point(point.x - 1, point.y)
    }

    if (inBound(Point(point.x, point.y - 1)) && !mazeCells(point.y - 1)(point.x).isVisited) {
      positions = positions :+ Point(point.x, point.y - 1)
    }

    positions
  }

  private def neighbouringCells(point: Point): Array[Point] = {
    var positions: Array[Point] = Array[Point]()

    if (inBound(Point(point.x + 1, point.y))) positions = positions :+ Point(point.x + 1, point.y)

    if (inBound(Point(point.x, point.y + 1))) positions = positions :+ Point(point.x, point.y + 1)

    if (inBound(Point(point.x - 1, point.y))) positions = positions :+ Point(point.x - 1, point.y)

    if (inBound(Point(point.x, point.y - 1))) positions = positions :+ Point(point.x, point.y - 1)

    positions
  }

  private def addWall(currentCell: Point, otherCell: Point): Unit = {
    if (Point(currentCell.x + 1, currentCell.y) == otherCell && !mazeCells(otherCell.y)(otherCell.x).walls('w')) {
      mazeCells(currentCell.y)(currentCell.x).walls('e') = true
    }

    if (Point(currentCell.x, currentCell.y + 1) == otherCell && !mazeCells(otherCell.y)(otherCell.x).walls('n')) {
      mazeCells(currentCell.y)(currentCell.x).walls('s') = true
    }

    if (Point(currentCell.x - 1, currentCell.y) == otherCell && !mazeCells(otherCell.y)(otherCell.x).walls('e')) {
      mazeCells(currentCell.y)(currentCell.x).walls('w') = true
    }

    if (Point(currentCell.x, currentCell.y - 1) == otherCell && !mazeCells(otherCell.y)(otherCell.x).walls('s')) {
      mazeCells(currentCell.y)(currentCell.x).walls('n') = true
    }
  }

  private def uniqueLocation(): Point = {
    var foundPoint : Boolean = false
    val coords : List[Point] = List[Point]()

    while (!foundPoint) {
      val randomX = rand.nextInt(mazeCells.length - 1) + 1
      val randomY = rand.nextInt(mazeCells.length - 1) + 1

      if(!coords.contains(Point(randomX, randomY)) && !mazeCells(randomY)(randomX).isClock && !mazeCells(randomY)(randomX).isEnemyOn && !mazeCells(randomY)(randomX).isKey && !mazeCells(randomY)(randomX).isWeapon && !mazeCells(randomY)(randomX).isCoin) {
        foundPoint = true
        return Point(randomX, randomY)
      }
    }
    Point(0,0)
  }

  private def generateWeapons(): Unit = {
    for (i <- 0 until math.ceil(math.pow(2, width / 5)).toInt) {
      val swordLocation = uniqueLocation()
      mazeCells(swordLocation.y)(swordLocation.x).isWeapon = true
    }
  }

  private def generateCoins(): Unit = {
    for (i <- 0 until math.ceil(math.pow(2, width / 6)).toInt) {
      val coinLocation = uniqueLocation()
      mazeCells(coinLocation.y)(coinLocation.x).isCoin = true
    }
  }

  private def generateClock(): Unit = {
    for (i <- 0 until math.ceil(math.pow(2, width / 5)).toInt) {
      val coinLocation = uniqueLocation()
      mazeCells(coinLocation.y)(coinLocation.x).isClock = true
    }
  }

  private def generateHeart(): Unit = {
    if (player.hp < player.maxHP) {
      val rng : Int = rand.nextInt(3)
      if (rng == 1) {
        val heartLocation = uniqueLocation()
        mazeCells(heartLocation.y)(heartLocation.x).isHeart = true
      }
    }
  }

  def generateWalls(): Unit = {
    for (i <- 0 until height) {
      for (j <- 0 until width) {
        val neighbours = neighbouringCells(Point(j, i))

        neighbours.foreach(cell => {
          val isCellNotLinked = !mazeCells(cell.y)(cell.x).linkedCells.contains(Point(j, i))
          if (!mazeCells(i)(j).linkedCells.contains(cell) && isCellNotLinked) {
            addWall(mazeCells(i)(j).pos, cell)
          }})
      }}
  }

  def generateMaze(): ArrayBuffer[ArrayBuffer[Cell]] = {
    initMaze()
    var theStack = mutable.Stack[Cell](mazeCells(0)(0).setVisited(true).setPlayer(true))

    while (visitedCells < area) {
      val possibleNeigh = possibleNeighbours(theStack.top.pos)
      if (possibleNeigh.length <= 0) {
        theStack.pop()
      } else {
        val newCell = possibleNeigh(rand.nextInt(possibleNeigh.length))
        mazeCells(theStack.top.pos.y)(theStack.top.pos.x).addLinkedCell(Point(newCell.x, newCell.y))
        theStack = theStack.push(mazeCells(newCell.y)(newCell.x).setVisited(true))
        visitedCells += 1
      }
    }

    generateWalls()
    mazeCells
  }

  private def generateEnemy(): Unit = {
    for (i <- 0 until enemyCap) {
      var state : Boolean = true
      var randomPos: Point = Point(0,0)

      while (state) {
        randomPos = uniqueLocation()
        if (enemys.isEmpty) {
          state = false
        } else {
          enemys.foreach(enemy => if (enemy.point.distance(randomPos) > width/5) state = false) // makes sure no enemy spawns close to the player
        }
      }

      val enemyId = rand.nextInt(100000)
      enemys = enemys :+ Enemy(randomPos, Point(playerPosition.x, playerPosition.y), enemyId)

      mazeCells(randomPos.y)(randomPos.x).isEnemyOn = true
      mazeCells(randomPos.y)(randomPos.x).enemyIds = mazeCells(randomPos.y)(randomPos.x).enemyIds :+ enemyId
    }
  }

  case class Enemy(var point: Point, var destination: Point, id: Int) {
    var stack : Stack[Direction] = Stack[Direction](Direction(point, possibleMove()))

    def possibleMove(): Stack[Point] = {
      var positions : Stack[Point] = Stack[Point]()

      if (inBound(Point(point.x + 1, point.y))  && !mazeCells(point.y)(point.x).walls('e') &&  !mazeCells(point.y)(point.x + 1).walls('w') )  positions = positions.push(Point(point.x + 1, point.y))

      if (inBound(Point(point.x, point.y + 1)) && !mazeCells(point.y)(point.x).walls('s') &&  !mazeCells(point.y + 1)(point.x).walls('n')) positions = positions.push(Point(point.x, point.y + 1))

      if (inBound(Point(point.x - 1, point.y)) && !mazeCells(point.y)(point.x).walls('w') &&  !mazeCells(point.y)(point.x - 1).walls('e')) positions = positions.push(Point(point.x - 1, point.y))

      if (inBound(Point(point.x, point.y - 1)) && !mazeCells(point.y)(point.x).walls('n') &&  !mazeCells(point.y - 1)(point.x).walls('s')) positions = positions.push(Point(point.x, point.y - 1))

      positions
    }
  }
}