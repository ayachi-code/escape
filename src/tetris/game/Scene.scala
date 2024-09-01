package tetris.game
import processing.core.{PApplet, PConstants, PFont, PImage}
import ddf.minim.{AudioPlayer, Minim}
import processing.event.KeyEvent
import sun.java2d.Surface


trait Scene {
  def run(surface: processing.core.PSurface, state: GameStateManager) : GameStateManager
  def keyEvent(event: KeyEvent) : Unit
}