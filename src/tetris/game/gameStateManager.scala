package tetris.game

class GameStateManager {
  var currentGameState : String = ""
  var score : Int = 0
  var highScore : Int = 0

  def getGameState: String = currentGameState

  def setGameState(newState: String): Unit = {
    currentGameState = newState
  }
}
