package tetris.logic

import engine.graphics.Color

import collection.mutable.Stack
import collection.mutable.ArrayBuffer
import scala.util.Random

class Maze(width: Int, height: Int) {
  val area: Int = width * height
  var mazeCells : ArrayBuffer[ArrayBuffer[Cell]] = ArrayBuffer[ArrayBuffer[Cell]]()
  var visitedCells : Int = 1

  val rand = new Random()

  var playerPosition : Point = Point(0,0)

  val portalLocation : Point = Point(width - 1, height - 1)

  var enemyCap : Int = 2

  var enemys: Array[Enemy] = Array[Enemy]()

  private def initMaze(): Unit = {

    var roof = ArrayBuffer[Cell]().append(Cell(Point(0,0)).addWall('n').addWall('w'))
    for (i <- 1 until width-1) roof.append(Cell(Point(i,0)).addWall('n'))
    mazeCells.append(roof.append(Cell(Point(width-1,0)).addWall('n').addWall('e')))

    for (i <- 1 until height-1) {
      var row: ArrayBuffer[Cell] = ArrayBuffer[Cell]()
      for (j <- 0 until width) {
        if (j == 0) {
          row.append(Cell(Point(j,i)).addWall('w'))
        } else if (j == width-1) {
          row.append(Cell(Point(j,i)).addWall('e'))
        } else {
          row.append(Cell(Point(j,i)))
        }
      }
      mazeCells.append(row)
    }
    var floor = ArrayBuffer[Cell]().append(Cell(Point(0,height-1)).addWall('s').addWall('w'))
    for (l <- 1 until width-1) floor.append(Cell(Point(l,height-1)).addWall('s'))
    mazeCells.append(floor.append(Cell(Point(width-1,height-1)).addWall('s').addWall('e')))

  }

  case class Direction(position: Point, neigb: Stack[Point])

  case class EnemyPair(isEnemyOn: Boolean, enemyId: Int)

  def enemyPath(): Unit = {

    enemys.foreach(enemy => {

      mazeCells(enemy.point.y)(enemy.point.x).currentEnemyOn = mazeCells(enemy.point.y)(enemy.point.x).currentEnemyOn :+ enemy.id

      enemy.destination = playerPosition

      mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy = mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy :+ enemy.id
      var neigbours = enemy.stack.top.neigb

      var state: Boolean = true

      while (state) {

        if (neigbours.length <= 0) {
          var x = mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy.filterNot(elm => elm == enemy.id)
          mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy = x

          if (enemy.stack.nonEmpty) enemy.stack.pop()
          if (enemy.stack.isEmpty) {
            enemy.stack.push(Direction(enemy.point, enemy.possibleMove()))
          } else {
            mazeCells(enemy.point.y)(enemy.point.x).isEnemyOn = false

            mazeCells(enemy.point.y)(enemy.point.x).enemyIds = mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy.filterNot(elm => elm == enemy.id)

//            mazeCells(enemy.point.y)(enemy.point.x).updateIsEnemyOn = EnemyPair(false, -1)

            var mov = enemy.stack.top
            enemy.point = mov.position
            mazeCells(mov.position.y)(mov.position.x).isEnemyOn = true
            mazeCells(enemy.point.y)(enemy.point.x).enemyIds = mazeCells(enemy.point.y)(enemy.point.x).enemyIds :+ enemy.id

//            mazeCells(enemy.point.y)(enemy.point.x).updateIsEnemyOn = EnemyPair(false, -1)
          }
          state = false
        } else {
          if (!mazeCells(neigbours.top.y)(neigbours.top.x).visitByEnemy.contains(enemy.id)) {
            mazeCells(enemy.point.y)(enemy.point.x).isEnemyOn = false

            mazeCells(enemy.point.y)(enemy.point.x).enemyIds = mazeCells(enemy.point.y)(enemy.point.x).visitByEnemy.filterNot(elm => elm == enemy.id)

            var move = neigbours.top
            enemy.point = move
            mazeCells(move.y)(move.x).isEnemyOn = true

            mazeCells(enemy.point.y)(enemy.point.x).enemyIds = mazeCells(enemy.point.y)(enemy.point.x).enemyIds :+ enemy.id

            enemy.stack.top.neigb.pop()
            enemy.stack.push(Direction(move, enemy.possibleMove()))
            state = false
          } else {
            neigbours.pop()
          }
        }
      }

      var xz = mazeCells(enemy.point.y)(enemy.point.x).currentEnemyOn.filterNot(elm => elm == enemy.id)
      mazeCells(enemy.point.y)(enemy.point.x).currentEnemyOn = xz

    })
  }

  def inBound(point: Point): Boolean = {
    if (point.x >= 0 && point.y <= height-1 && point.x <= width-1 && point.y >= 0) true
    else false
  }


  private def possibleNeigbours(point: Point): Array[Point] = {
    var positions: Array[Point] = Array[Point]()

    if (inBound(Point(point.x + 1, point.y)) && !mazeCells(point.y)(point.x + 1).isVisited())  positions = positions :+ Point(point.x + 1, point.y)

    if (inBound(Point(point.x, point.y + 1)) && !mazeCells(point.y + 1)(point.x).isVisited()) positions = positions :+ Point(point.x, point.y + 1)

    if (inBound(Point(point.x - 1, point.y)) && !mazeCells(point.y)(point.x - 1).isVisited()) positions = positions :+ Point(point.x - 1, point.y)

    if (inBound(Point(point.x, point.y - 1)) && !mazeCells(point.y - 1)(point.x).isVisited()) positions = positions :+ Point(point.x, point.y - 1)

    positions
  }


  private def neighb(point: Point): Array[Point] = {
    var positions: Array[Point] = Array[Point]()

    if (inBound(Point(point.x + 1, point.y)))  positions = positions :+ Point(point.x + 1, point.y)

    if (inBound(Point(point.x, point.y + 1))) positions = positions :+ Point(point.x, point.y + 1)

    if (inBound(Point(point.x - 1, point.y))) positions = positions :+ Point(point.x - 1, point.y)

    if (inBound(Point(point.x, point.y - 1))) positions = positions :+ Point(point.x, point.y - 1)

    positions
  }

  def addwall(currentCell: Point, otherCell: Point): Unit = {
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

  def uniqueCoin(): Point = {
    var state : Boolean = true
    var coords = List[Point]()

    while (state) {
      val randomX = rand.nextInt(mazeCells.length - 1) + 1
      val randomY = rand.nextInt(mazeCells.length - 1) + 1

      if(!coords.contains(Point(randomX, randomY)) && !mazeCells(randomY)(randomX).isClock && !mazeCells(randomY)(randomX).isEnemyOn && !mazeCells(randomY)(randomX).isKey) {
        state = false
        return Point(randomX, randomY)
      }
    }
    Point(0,0)
  }

  def generateWeapons(): Unit = {
    for (i <- 0 until 2) {
      val swordLocation = uniqueCoin()
      mazeCells(swordLocation.y)(swordLocation.x).isWeapon = true
    }
  }

  def generateCoins(): Unit = {
    for (i <- 0 until 2) {
      val coinLocation = uniqueCoin()
      mazeCells(coinLocation.y)(coinLocation.x).isCoin = true
    }

  }

  def generateClock(): Unit = {
    for (i <- 0 until 2) {
      val coinLocation = uniqueCoin()
      mazeCells(coinLocation.y)(coinLocation.x).isClock = true
    }
  }

  def generateMaze(): ArrayBuffer[ArrayBuffer[Cell]] = { //TODO: Implement
    initMaze()

    var theStack = Stack[Cell](mazeCells(0)(0).setVisited(true).setPlayer(true))

    while (visitedCells < area) {
      var possibleNeigh = possibleNeigbours(theStack.top.pos)

      if (possibleNeigh.length <= 0) {
        theStack.pop()
      } else {
        val x = rand.nextInt(possibleNeigh.length)
        val newCell = possibleNeigh(x)

        mazeCells(theStack.top.pos.y)(theStack.top.pos.x).addLinkedCell(Point(newCell.x, newCell.y)) //.linedCell = mazeCells(newCell.y)(newCell.x)

        theStack = theStack.push(mazeCells(newCell.y)(newCell.x).setVisited(true))
        visitedCells += 1

      }

    }

    for (i <- 0 until height) {

      for (j <- 0 until width) {
        var neigb = neighb(Point(j, i))

        for (cell <- neigb) {
          if (!mazeCells(i)(j).linedCell.contains(cell) && !mazeCells(cell.y)(cell.x).linedCell.contains(Point(j, i))) {
            addwall(mazeCells(i)(j).pos, cell)
          }
        }

      }

    }

    mazeCells(height - 1)(width - 1).isPortal = true

    generateCoins()
    generateClock()
    generateEnemy()
    generateWeapons()

    mazeCells(rand.nextInt(height - 1) + 1)(rand.nextInt(width - 1)).isKey = true // Spawns key

    mazeCells
  }

  def generateEnemy(): Unit = {
    for (i <- 0 until enemyCap) {
      var randomPos = uniqueCoin()
//      randomPos = Point(0, height - 1)
      val id = rand.nextInt(100000)
      enemys = enemys :+ Enemy(randomPos, Point(playerPosition.x, playerPosition.y), id)
      mazeCells(randomPos.y)(randomPos.x).isEnemyOn = true

      mazeCells(randomPos.y)(randomPos.x).enemyIds = mazeCells(randomPos.y)(randomPos.x).enemyIds :+ id

    }
  }

  case class Enemy(var point: Point, var destination: Point, id: Int) {
    var health: Int = 1
    var color: Color = Color.Purple
    var lastMove : Point = point
    var stack : Stack[Direction] = Stack[Direction](Direction(point, possibleMove()))
    //var st = Stack[Point]()

    def possibleMove(): Stack[Point] = {
      var positions: Stack[Point] = Stack[Point]()

      if (inBound(Point(point.x + 1, point.y))  && !mazeCells(point.y)(point.x).walls('e') &&  !mazeCells(point.y)(point.x + 1).walls('w') )  positions = positions.push(Point(point.x + 1, point.y))

      if (inBound(Point(point.x, point.y + 1)) && !mazeCells(point.y)(point.x).walls('s') &&  !mazeCells(point.y + 1)(point.x).walls('n')) positions = positions.push(Point(point.x, point.y + 1))

      if (inBound(Point(point.x - 1, point.y)) && !mazeCells(point.y)(point.x).walls('w') &&  !mazeCells(point.y)(point.x - 1).walls('e')) positions = positions.push(Point(point.x - 1, point.y))

      if (inBound(Point(point.x, point.y - 1)) && !mazeCells(point.y)(point.x).walls('n') &&  !mazeCells(point.y - 1)(point.x).walls('s')) positions = positions.push(Point(point.x, point.y - 1))

      positions
    }
  }

  case class Cell(pos: Point) {
    val coordinate : Point = pos
    var walls : collection.mutable.Map[Char, Boolean] = collection.mutable.Map[Char, Boolean]('n' -> false, 's' -> false, 'w' -> false, 'e' -> false)
    private var visited : Boolean = false
    var isPlayerOn : Boolean = false
    var isPortal : Boolean = false
    var isCoin : Boolean = false
    var isKey : Boolean = false
    var isClock : Boolean = false

    var isWeapon : Boolean = false

    var visitByEnemy : List[Int] = List[Int]() // Int = ID of enemy

    var currentEnemyOn : List[Int] = List[Int]()

    var isEnemyOn : Boolean = false


    var enemyIds : List[Int] = List[Int]()

    //var updateIsEnemyOn : EnemyPair = EnemyPair(false, -1)

    var linedCell : List[Point] = List[Point]()

    def addLinkedCell(cell: Point): Cell = {
      linedCell = linedCell :+ cell
      return this
    }

    def getWalls(): List[Char] = {
      var theWalls : List[Char] = List[Char]()
      if (walls('n')) theWalls = theWalls :+ 'n'
      if (walls('s')) theWalls = theWalls :+ 's'
      if (walls('e')) theWalls = theWalls :+ 'e'
      if (walls('w')) theWalls = theWalls :+ 'w'
      theWalls
    }

    def addWall(wall: Char): Cell = {
      walls(wall) = true
      return this
    }

    def setVisited(state: Boolean): Cell = {
      visited = state
      return this
    }

    def setPlayer(state: Boolean): Cell = {
      isPlayerOn = state
      return  this
    }

    def isVisited(): Boolean =  visited

  }

}