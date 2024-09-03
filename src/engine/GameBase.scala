// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package engine

import engine.graphics.Color.Black
import engine.graphics.{Color, Point, Rectangle, Triangle}
import processing.core.{PApplet, PConstants, PImage, PFont}
import escape.game.driver
import escape.logic._

import java.sql.Driver

class GameBase(PApplet: PApplet) extends driver  {
  // inner class: can call current time of outer class
  class UpdateTimer(val framesPerSecond: Float) {

    val frameDuration: Float = 1000 / framesPerSecond // ms
    var nextFrame: Float = Float.MaxValue

    def init(): Unit = nextFrame = currentTime() + frameDuration
    def timeForNextFrame(): Boolean = currentTime() >= nextFrame
    def advanceFrame(): Unit = nextFrame = nextFrame + frameDuration
  }

  def drawImage(img: PImage, position: Point, width: Float, height: Float): Unit = {
    PApplet.image(img, position.x, position.y, width, height)
  }

  // ===Processing Wrappers & Abstractions===

  /** An alias for the obscurely named function millis()
    *
    * @return Current time in milliseconds since stating the program.
   */
  def currentTime(): Int = millis()

  def drawTextCentered(string: String, size: Float, center: Point): Unit = {
    val (x, y) = (center.x, center.y-(size/2))
    PApplet.textAlign(PConstants.CENTER, PConstants.CENTER)
    PApplet.textSize(size)
    //drawText(string, Point(x, y))
    PApplet.text(string, x, y)
  }

  def drawText(string: String, pos: Point, color: (Float, Float, Float), font: PFont, size: Float): Unit = {
    PApplet.textFont(font)
    PApplet.textSize(size)
    PApplet.fill(color._1, color._2, color._3)
    PApplet.text(string, pos.x,pos.y)
    //if (withShadow) drawTextShadow(string, pos)
    PApplet.text(string, pos.x, pos.y)
  }

  def drawLine(p1 : Point, p2 : Point) : Unit = {
    PApplet.line(p1.x,p1.y, p2.x,p2.y )
  }


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

  def drawPortal(r: Rectangle): Unit = {
    val img : PImage = PApplet.loadImage("src/escape/assets/sprite/door.png")
    PApplet.image(img, r.left, r.top, r.width, r.height)
  }

  def drawPlayer(r : Rectangle): Unit = {
    PApplet.fill(255, 165, 0)
    PApplet.strokeWeight(1)
    PApplet.ellipse(r.center.x, r.center.y, r.width / 2, r.height / 2)
  }

  def drawAttackSword(r: Rectangle, game: EscapeLogic): Unit = {
    game.gameState.player.playersWeapons.last.direction match {
      case 'e' => {
        val img : PImage = PApplet.loadImage("src/escape/assets/weapons/sword/attackEast.png")
        PApplet.image(img, (r.left + r.right - r.width/2 - 40) / 2, (r.top + r.bottom - r.width/2) / 2, r.width / 2, r.height / 2)
      }
      case 'w' => {
        val img : PImage = PApplet.loadImage("src/escape/assets/weapons/sword/attackWest.png")
        PApplet.image(img, (r.left + r.right - r.width/2 + 40) / 2, (r.top + r.bottom - r.width/2) / 2, r.width / 2, r.height / 2)
      }
      case 's' => {
        val img : PImage = PApplet.loadImage("src/escape/assets/weapons/sword/attackSouth.png")
        PApplet.image(img, (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2 - 40) / 2, r.width / 2, r.height / 2)
      }
      case 'n' => {
        val img : PImage = PApplet.loadImage("src/escape/assets/weapons/sword/attackNorth.png")
        PApplet.image(img, (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2 + 40) / 2, r.width / 2, r.height / 2)
      }

    }

  }


  def drawPlayerOnDoor(r: Rectangle): Unit = {
    drawPortal(r)
    drawPlayer(r)
  }

  def drawOpenDoor(r: Rectangle): Unit = {
    val img : PImage = PApplet.loadImage("src/escape/assets/sprite/openDoor.png")
    PApplet.image(img, r.left, r.top, r.width, r.height)

    PApplet.fill(255, 165, 0)
    PApplet.strokeWeight(1)
    PApplet.ellipse(r.center.x - 5, r.center.y, r.width / 2 - 10, r.height / 2 - 10)
  }

  def drawHeart(r: Rectangle): Unit = {
    val go : PImage = PApplet.loadImage("src/escape/assets/sprite/heart.png")
    PApplet.image(go, r.left, r.top, r.width, r.height)
  }

  def drawKey(r: Rectangle): Unit = {
    val go : PImage = PApplet.loadImage("src/escape/assets/sprite/key.png")
    PApplet.image(go, r.left, r.top, r.width, r.height)
  }

  def drawClock(r: Rectangle): Unit = {
    val go : PImage = PApplet.loadImage("src/escape/assets/sprite/clock.png")
    PApplet.image(go, (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2) / 2, r.width / 2, r.height / 2)
  }



  def drawWeapon(r: Rectangle): Unit = {
    val img : PImage = PApplet.loadImage("src/escape/assets/weapons/sword/sword_1.png")
    PApplet.image(img, (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2) / 2, r.width, r.height)
  }

  def drawEnemy(r: Rectangle, img: PImage): Unit = {
    PApplet.image(img, (r.left + r.right - r.width/2) / 2, (r.top + r.bottom - r.width/2) / 2, r.width / 2, r.height / 2 )
  }

  def drawEllipse(r: Rectangle): Unit =
    ellipse(r.center.x, r.center.y, r.width, r.height)

  def setFillColor(c: Color): Unit =
    PApplet.fill(c.red, c.green, c.blue, c.alpha)

  def setBackground(c: Color): Unit =
    background(c.red, c.green, c.blue, c.alpha)

}
