package tetris.game

class GameStateManager {
  var currentGameState : String = ""

  def getGameState() : String = currentGameState

  def setGameState(newState: String): Unit = {
    currentGameState = newState
  }
}
