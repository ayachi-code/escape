package escape.logic.buttons

import engine.graphics.Point
import processing.core.{PApplet, PFont}

class RectangleButton(pApplet: PApplet, point: Point, width: Float, height: Float, textOnButton: String, font: PFont) extends Button {

  override def display(): Unit = {
    if (onButton()) {
      pApplet.stroke(255)
    } else {
      pApplet.stroke(0)
    }

    pApplet.strokeWeight(3)
    pApplet.fill(139, 0, 0)
    pApplet.rect(point.x, point.y, width, height)

    pApplet.textFont(font)
    pApplet.textSize(50)
    pApplet.fill(255, 36, 0)
    pApplet.text(textOnButton, point.x + width / 2 - 80, point.y + 50 )
  }

  override def onButton(): Boolean = {
    val mouseOnX = pApplet.mouseX > point.x && pApplet.mouseX < point.x + width
    val mouseOnY = pApplet.mouseY > point.y && pApplet.mouseY < point.y + height

    if (mouseOnX && mouseOnY) {
      true
    } else{
      false
    }
  }

  override def pressed(): Boolean =
    if (onButton() && pApplet.mousePressed) true else false
}
