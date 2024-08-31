package tetris.game

class gameStateManager {
  var currentGameState : String = ""

  def getGameState() : String = currentGameState
  def setGameState(newState: String) = currentGameState = newState
}
