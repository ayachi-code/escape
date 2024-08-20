package tetris.logic

import collection.mutable.Stack
import collection.mutable.ArrayBuffer
import scala.util.Random

class Maze(width: Int, height: Int) {
  val area: Int = width * height
  var mazeCells : ArrayBuffer[ArrayBuffer[Cell]] = ArrayBuffer[ArrayBuffer[Cell]]()
  var visitedCells : Int = 1

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

  private def inBound(point: Point): Boolean = {
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

  def generateMaze(): ArrayBuffer[ArrayBuffer[Cell]] = { //TODO: Implement
    initMaze()

    val rand = new Random(10)

    var theStack = Stack[Cell](mazeCells(0)(0).setVisited(true))

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

    mazeCells
  }

  case class Cell(pos: Point) {
    val coordinate : Point = pos
    var walls : collection.mutable.Map[Char, Boolean] = collection.mutable.Map[Char, Boolean]('n' -> false, 's' -> false, 'w' -> false, 'e' -> false)
    private var visited : Boolean = false

    var linedCell : List[Point] = List[Point]()

    def addLinkedCell(cell: Point): Cell = {
      linedCell = linedCell :+ cell
      return this
    }

    def addWall(wall: Char): Cell = {
      walls(wall) = true
      return this
    }

    def setVisited(state: Boolean): Cell = {
      visited = state
      return this
    }

    def isVisited(): Boolean =  visited

  }

}