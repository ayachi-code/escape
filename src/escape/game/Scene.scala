package escape.game
import processing.event.KeyEvent

trait Scene {
  def run(surface: processing.core.PSurface, state: GameStateManager) : GameStateManager
  def keyEvent(event: KeyEvent) : Unit
}