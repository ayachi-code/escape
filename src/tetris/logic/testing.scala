package tetris.logic

import javax.swing.text.Position

import collection.mutable.ArrayBuffer

class Maze(width: Int, height: Int) {
  val area: Int = width * height
  var mazeCells : ArrayBuffer[ArrayBuffer[Cell]] = ArrayBuffer[ArrayBuffer[Cell]]()

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

  initMaze()


  case class Cell(pos: Point) {
    val coordinate : Point = pos
    var walls : collection.mutable.Map[Char, Boolean] = collection.mutable.Map[Char, Boolean]('n' -> false, 's' -> false, 'w' -> false, 'e' -> false)

    def addWall(wall: Char): Cell = {
      walls(wall) = true
      return this
    }

  }

}

object testing {
  def main(args: Array[String]): Unit = {
   val foo = new Maze(10,10)
   println("test")
  }
}
