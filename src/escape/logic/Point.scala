package escape.logic

import scala.math.{pow, sqrt}

case class Point(x : Int, y : Int) {
  def distance(otherPoint: Point): Double = sqrt(pow(x - otherPoint.x, 2) + pow(y - otherPoint.y, 2))
}
