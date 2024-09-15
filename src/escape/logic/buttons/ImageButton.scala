package escape.logic.buttons

import engine.graphics.Point
import processing.core.{PApplet, PImage}


class ImageButton(pApplet: PApplet, point: Point, width: Float, height: Float, img: PImage) extends Button {

  var enabled = true

  override def display(): Unit = if (enabled) pApplet.image(img, point.x, point.y, width, height)

  override def onButton(): Boolean =
    if (pApplet.mouseX > point.x && pApplet.mouseX < point.x + width && pApplet.mouseY > point.y && pApplet.mouseY < point.y + height) true else false

  override def pressed(): Boolean =
    if (onButton() && pApplet.mousePressed && enabled) true else false
}

