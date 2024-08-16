package tetris.logic

import javax.swing.text.Position

import collection.mutable.ArrayBuffer

class Maze(width: Int, height: Int) {
  val area: Int = width * height
  var mazeCells : ArrayBuffer[ArrayBuffer[Cell]] = ArrayBuffer[ArrayBuffer[Cell]]()

  def initMaze(): Unit = {
    for (i <- 0 until height) {
      var row: ArrayBuffer[Cell] = ArrayBuffer[Cell]()
      for (j <- 0 until width) {
        row.append(new Cell(Point(i,j)))
      }
      mazeCells.append(row)
    }
  }

  initMaze()

  //Array[Array[Cell]]

  class Cell(pos: Point) {
    val coordinate : Point = pos
    val walls : Map[Char, Boolean] = Map[Char, Boolean]('u' -> true, 'd' -> true, 'l' -> true, 'r' -> true)
  }

}

object testing {
  def main(args: Array[String]): Unit = {
   val foo = new Maze(10,10)
   println("test")
  }
}
