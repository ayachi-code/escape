package escape.logic

case class Dimensions(width : Int, height : Int) {
  // scanned from left to right, top to bottom
  def allPointsInside : Seq[Point] =
    for(y <- 0 until (height / 3); x <- 0 until (width / 3))
      yield Point(x,y)
}
