package escape.logic

case class Cell(pos: Point) {
  var walls : collection.mutable.Map[Char, Boolean] = collection.mutable.Map[Char, Boolean]('n' -> false, 's' -> false, 'w' -> false, 'e' -> false)
  private var visited : Boolean = false
  var isPlayerOn : Boolean = false
  var isPortal : Boolean = false
  var isCoin : Boolean = false
  var isKey : Boolean = false
  var isClock : Boolean = false
  var isWeapon : Boolean = false
  var isAttacked : Boolean = false
  var isHeart : Boolean = false

  var visitByEnemy : List[Int] = List[Int]() // Int = ID of enemy

  var isEnemyOn : Boolean = false
  var enemyIds : List[Int] = List[Int]()
  var linkedCells : List[Point] = List[Point]()

  def addLinkedCell(cell: Point): Cell = {
    linkedCells = linkedCells :+ cell
    this
  }

  def getWalls: List[Char] = {
    var theWalls : List[Char] = List[Char]()
    if (walls('n')) theWalls = theWalls :+ 'n'
    if (walls('s')) theWalls = theWalls :+ 's'
    if (walls('e')) theWalls = theWalls :+ 'e'
    if (walls('w')) theWalls = theWalls :+ 'w'
    theWalls
  }

  def addWall(wall: Char): Cell = {
    walls(wall) = true
    this
  }

  def setVisited(state: Boolean): Cell = {
    visited = state
    this
  }

  def setPlayer(state: Boolean): Cell = {
    isPlayerOn = state
    this
  }

  def isVisited: Boolean = visited
}