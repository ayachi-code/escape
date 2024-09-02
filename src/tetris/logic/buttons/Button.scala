package tetris.logic.buttons

import engine.graphics.Point
import processing.core.{PApplet, PFont}

abstract class Button extends PApplet{
  def display() : Unit
  def onButton() : Boolean
  def pressed() : Boolean
}