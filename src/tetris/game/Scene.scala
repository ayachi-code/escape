package tetris.game
import processing.core.{PApplet, PConstants, PFont, PImage}
import ddf.minim.{AudioPlayer, Minim}


abstract class Scene {
  def run() : Unit
}