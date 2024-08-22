// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package engine

import engine.graphics.Color.Black
import engine.graphics.{Color, Point, Rectangle, Triangle}
import processing.core.{PApplet, PConstants, PImage}

class GameBase   extends PApplet {

  // inner class: can call current time of outer class
  class UpdateTimer(val framesPerSecond: Float) {

    val frameDuration: Float = 1000 / framesPerSecond // ms
    var nextFrame: Float = Float.MaxValue

    def init(): Unit = nextFrame = currentTime() + frameDuration
    def timeForNextFrame(): Boolean = currentTime() >= nextFrame
    def advanceFrame(): Unit = nextFrame = nextFrame + frameDuration

  }

  // ===Processing Wrappers & Abstractions===

  /** An alias for the obscurely named function millis()
    *
    * @return Current time in milliseconds since stating the program.
    */
  def currentTime(): Int = millis()

  def drawTextCentered(string: String, size: Float, center: Point): Unit = {
    val (x, y) = (center.x, center.y-(size/2))
    textAlign(PConstants.CENTER, PConstants.CENTER)
    textSize(size)
    drawText(string, Point(x, y))
  }

  def drawText(string: String, pos: Point, withShadow: Boolean = true): Unit = {
    if (withShadow) drawTextShadow(string, pos)
    text(string, pos.x, pos.y)
  }

  /** Quick hack for legibility on different backgrounds */
  def drawTextShadow(string: String, pos: Point, color: Color = Black, thickness: Float = 1): Unit = {
    pushStyle()
    setFillColor(color)
    List((1,0),(-1,0),(0,1),(0,-1)).foreach(t => {
      text(string, pos.x+(t._1*thickness), pos.y+t._2*thickness)
    })
    popStyle()
  }


  def drawLine(p1 : Point, p2 : Point) : Unit = {
//    strokeWeight(5)
    line(p1.x,p1.y, p2.x,p2.y )
  }


  def drawMazeCell(r: Rectangle, walls: List[Char]): Unit = {

    stroke(112, 128, 144)
    strokeWeight(13)
    if (walls.contains('n')) {
      drawLine(Point(r.left, r.top), Point(r.left + r.width, r.top))
    }

    if (walls.contains('s')) {
      drawLine(Point(r.left + r.width, r.top + r.height), Point(r.left, r.top + r.height))
    }

    if (walls.contains('e')) {
      drawLine(Point(r.left + r.width, r.top), Point(r.left + r.width, r.top + r.height))
    }

    if (walls.contains('w')) {
      drawLine(Point(r.left, r.top + r.height), Point(r.left, r.top))
    }
  }

  def drawRectangle(r: Rectangle): Unit = {
    rect(r.left,r.top, r.width, r.height)
  }

  def drawPortal(r: Rectangle): Unit = {
    val img : PImage = loadImage("src/tetris/assets/door.png")
    image(img, r.left, r.top, r.width, r.height)
  }

  def drawPlayer(r : Rectangle): Unit = {
    fill(255, 165, 0)
    strokeWeight(1)
    ellipse(r.center.x, r.center.y, r.width / 2, r.height / 2)
  }


  def drawTriangle(t: Triangle): Unit =
    triangle(t.p1.x, t.p1.y, t.p2.x,t.p2.y, t.p3.x,t.p3.y)

  def drawCoin(r: Rectangle): Unit = { // TODO: Substarct by halve of cell width
    val img : PImage = loadImage("src/tetris/assets/x.png")
    image(img, (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2) / 2, r.width / 2, r.height / 2)
  }

  def drawPlayerOnDoor(r: Rectangle): Unit = {
    drawPortal(r)
    drawPlayer(r)
  }

  def drawOpenDoor(r: Rectangle): Unit = {
    val img : PImage = loadImage("src/tetris/assets/openDoor.png")
    image(img, r.left, r.top, r.width, r.height)

    fill(255, 165, 0)
    strokeWeight(1)
    ellipse(r.center.x - 5, r.center.y, r.width / 2 - 10, r.height / 2 - 10)
  }

  def drawHeart(r: Rectangle): Unit = {
    val go : PImage = loadImage("src/tetris/assets/heart.png")
    image(go, r.left, r.top, r.width, r.height)
  }

  def drawKey(r: Rectangle): Unit = {
    val go : PImage = loadImage("src/tetris/assets/dd.png")
    image(go, r.left, r.top, r.width, r.height)
  }

  def drawEllipse(r: Rectangle): Unit =
    ellipse(r.center.x, r.center.y, r.width, r.height)

  def setFillColor(c: Color): Unit =
    fill(c.red, c.green, c.blue, c.alpha)

  def setBackground(c: Color): Unit =
    background(c.red, c.green, c.blue, c.alpha)

}
