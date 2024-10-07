// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package engine

import engine.graphics.{Color, Point, Rectangle, Triangle}
import processing.core.{PApplet, PConstants, PFont, PImage}
import escape.game.{Driver}
import escape.logic._

class GameBase(PApplet: PApplet) extends Driver  {
  class UpdateTimer(val framesPerSecond: Float) {

    val frameDuration: Float = 1000 / framesPerSecond // ms
    var nextFrame: Float = Float.MaxValue

    def init(): Unit = nextFrame = currentTime() + frameDuration
    def timeForNextFrame(): Boolean = currentTime() >= nextFrame
    def advanceFrame(): Unit = nextFrame = nextFrame + frameDuration
  }

  // ===Processing Wrappers & Abstractions===

  def getWidth: Int = PApplet.width
  def getHeight: Int = PApplet.height


  /** An alias for the obscurely named function millis()
    *
    * @return Current time in milliseconds since stating the program.
   */
  def currentTime(): Int = millis()

  def drawTextCentered(string: String, size: Float, center: Point): Unit = {
    val (x, y) = (center.x, center.y-(size/2))
    PApplet.textAlign(PConstants.CENTER, PConstants.CENTER)
    PApplet.textSize(size)
    PApplet.text(string, x, y)
  }

  def drawText(string: String, pos: Point, color: (Float, Float, Float), font: PFont, size: Float): Unit = {
    PApplet.textFont(font)
    PApplet.textSize(size)
    PApplet.fill(color._1, color._2, color._3)
    PApplet.text(string, pos.x,pos.y)
    PApplet.text(string, pos.x, pos.y)
  }

  def drawLine(p1 : Point, p2 : Point) : Unit = PApplet.line(p1.x,p1.y, p2.x,p2.y )


  def drawMazeCell(r: Rectangle, walls: List[Char]): Unit = {

    PApplet.stroke(112, 128, 144)
    PApplet.strokeWeight(13)
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

  def drawPlayer(r : Rectangle): Unit = {
    PApplet.fill(255, 165, 0)
    PApplet.strokeWeight(1)
    PApplet.ellipse(r.center.x, r.center.y, r.width / 2, r.height / 2)
  }

  def drawAttackSword(r: Rectangle, game: EscapeLogic, assets : Map[String, PImage]): Unit = {
//    println(game.gameState.player.playersWeapons.last.direction)
    println(game.gameState.player.playersWeapons.last.direction)
    game.gameState.player.playersWeapons.last.direction match {
      case 'e' =>
        println("draw east")
        PApplet.image(assets("swordEast"), (r.left + r.right - r.width/2 - 40) / 2, (r.top + r.bottom - r.width/2) / 2, r.width / 2, r.height / 2)
      case 'w' =>
        PApplet.image(assets("swordWest"), (r.left + r.right - r.width/2 + 40) / 2, (r.top + r.bottom - r.width/2) / 2, r.width / 2, r.height / 2)
      case 's' =>
        PApplet.image(assets("swordSouth"), (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2 - 40) / 2, r.width / 2, r.height / 2)
      case 'n' =>
        PApplet.image(assets("swordNorth"), (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2 + 40) / 2, r.width / 2, r.height / 2)
    }
  }

  def drawOpenDoor(r: Rectangle): Unit = {
    val img : PImage = PApplet.loadImage("src/escape/assets/sprite/openDoor.png")
    PApplet.image(img, r.left, r.top, r.width, r.height)

    PApplet.fill(255, 165, 0)
    PApplet.strokeWeight(1)
    PApplet.ellipse(r.center.x - 5, r.center.y, r.width / 2 - 10, r.height / 2 - 10)
  }

  def resetTextAlign() : Unit = PApplet.textAlign(PConstants.LEFT, PConstants.BASELINE)
  def sleep(milli: Int): Unit = PApplet.delay(milli)


  def drawSprite(r: Rectangle, img: PImage): Unit = PApplet.image(img, r.leftUp.x, r.leftUp.y, r.width, r.height)
  def setFillColor(rgb: (Float, Float, Float)): Unit = PApplet.fill(rgb._1, rgb._2, rgb._3)
  def setBackground(rgb: (Float, Float, Float)): Unit = PApplet.background(rgb._1, rgb._2, rgb._3)
}
