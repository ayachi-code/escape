package escape.logic

import scala.math.{pow, sqrt}

// you can alter this file!

case class Point(x : Int, y : Int) {

  def distance(otherPoint: Point): Double = {
    sqrt(pow(x - otherPoint.x, 2) + pow(y - otherPoint.y, 2))
  }

}
