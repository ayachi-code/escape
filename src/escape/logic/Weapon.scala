package escape.logic

abstract class Weapon {
  var damage: Int
  var attackCell: Point
  var direction : Char

  def attack(maze: Maze): Unit
  def animation(maze: Maze) : Unit
}

case class Sword(player: Player) extends Weapon {

  override var damage: Int = 1

  override var attackCell: Point = _
  override var direction: Char = _

  override def attack(maze: Maze): Unit = {
    player.possibleAttack(maze).foreach(cell => {
      if (maze.mazeCells(cell.point.y)(cell.point.x).isEnemyOn) {
        direction = cell.direction
        attackCell = cell.point
        maze.mazeCells(cell.point.y)(cell.point.x).isEnemyOn = false

        maze.mazeCells(cell.point.y)(cell.point.x).enemyIds.foreach(enemyId => {
         maze.enemys = maze.enemys.filterNot(id => id.id == enemyId)
        })
      }

    })
  }


  override def animation(maze: Maze): Unit = {
    if (attackCell == null) {
      val at = player.possibleAttack(maze)
      attackCell = at.head.point
      direction = at.head.direction
    }
    maze.mazeCells(attackCell.y)(attackCell.x).isAttacked = true
  }

}