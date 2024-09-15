package escape.logic.buttons

import processing.core.PApplet

abstract class Button extends PApplet{
  def display() : Unit
  def onButton() : Boolean
  def pressed() : Boolean
}