package tetris.logic.buttons

import engine.graphics.Point
import processing.core.{PApplet, PFont, PImage}


class ImageButton(pApplet: PApplet, point: Point, width: Int, height: Int, img: PImage) extends Button {

  var enabled = true

  override def display(): Unit = if (enabled) pApplet.image(img, point.x, point.y, width, height)

  override def onButton(): Boolean =
    if (pApplet.mouseX > point.x && pApplet.mouseX < point.x + width && pApplet.mouseY > point.y && pApplet.mouseY < point.y + height) true else false

  override def pressed(): Boolean =
    if (onButton() && pApplet.mousePressed && enabled) true else false
}

