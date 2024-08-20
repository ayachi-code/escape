package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.logic.TetrisLogic._

/** To implement Tetris, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``tetris`` package.
 */
class TetrisLogic(val randomGen: RandomGenerator,
                  val gridDims : Dimensions,
                  val initialBoard: Seq[Seq[CellType]]) {

  def this(random: RandomGenerator, gridDims : Dimensions) =
    this(random, gridDims, makeEmptyBoard(gridDims))

  def this() =
    this(new ScalaRandomGen(), DefaultDims, makeEmptyBoard(DefaultDims))

  // TODO implement me
  def rotateLeft(): Unit = ()

  // TODO implement me
  def rotateRight(): Unit = ()

  // TODO implement me
  def moveLeft(): Unit = ()

  // TODO implement me
  def moveRight(): Unit = ()

  // TODO implement me
  def moveDown(): Unit = ()

  // TODO implement me
  def doHardDrop(): Unit = ()

  // TODO implement me
  def isGameOver: Boolean = false

  // TODO implement me
  def getCellType(p : Point): CellType = {
    if (p.x == 0) {
      ICell
    } else {
      MazeCell
    }
  }
}

object TetrisLogic {

  val FramesPerSecond: Int = 5 // change this to speed up or slow down the game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller


  var maze = new Maze(10,10).generateMaze()


  def makeEmptyBoard(gridDims : Dimensions): Seq[Seq[CellType]] = {
    val emptyLine = Seq.fill(gridDims.width)(MazeCell)
    Seq.fill(gridDims.height)(emptyLine)
  }


  val DefaultWidth: Int = 30
  val NrTopInvisibleLines: Int = 4
  val DefaultVisibleHeight: Int = 30
  val DefaultHeight: Int = DefaultVisibleHeight //+ NrTopInvisibleLines
  val DefaultDims : Dimensions = Dimensions(width = DefaultWidth, height = DefaultHeight)


  def apply() = new TetrisLogic(new ScalaRandomGen(),
    DefaultDims,
    makeEmptyBoard(DefaultDims))

}