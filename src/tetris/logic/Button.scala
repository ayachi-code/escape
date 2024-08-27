package tetris.logic
import engine.GameBase
import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PFont}

class Button(PApplet: PApplet, point: Point, width: Int, height: Int, textOnButton: String, font: PFont) extends GameBase {
  def display(): Unit = {

    if (onButton()) {
      PApplet.stroke(255)
    } else {
      PApplet.stroke(0)
    }

    PApplet.strokeWeight(3)
    PApplet.fill(139, 0, 0)
    PApplet.rect(point.x, point.y, width, height)

    PApplet.textFont(font)
    PApplet.textSize(50)
    PApplet.fill(255, 255, 255)
    PApplet.text(textOnButton, point.x + width / 2 - 60, point.y + 50 )
  }


  def onButton(): Boolean = {
    if (PApplet.mouseX > point.x && PApplet.mouseX < point.x + width && PApplet.mouseY > point.y && PApplet.mouseY < point.y + height) {
      return true
    }
    false
  }

  def pressed(): Boolean = {
    if (PApplet.mouseX > point.x && PApplet.mouseX < point.x + width && PApplet.mouseY > point.y && PApplet.mouseY < point.y + height && PApplet.mousePressed) {
      return  true
    }
    false
  }
}
