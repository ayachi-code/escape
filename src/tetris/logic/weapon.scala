package tetris.logic

abstract class Weapon {
  var damage: Int
  def attack(maze: Maze): Unit
}


case class Sword(player: Player) extends Weapon {

  override var damage: Int = 1

  override def attack(maze: Maze): Unit = {
    var x = player.possibleAttack(maze)
    x.foreach(cell => {
      if (maze.mazeCells(cell.y)(cell.x).isEnemyOn) {
        println("Enemy killed")
        maze.mazeCells(cell.y)(cell.x).isEnemyOn = false

        maze.mazeCells(cell.y)(cell.x).enemyIds.foreach(enemyId => {
         maze.enemys = maze.enemys.filterNot(id => id.id == enemyId)
        })
      }
    })
  }

}