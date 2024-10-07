package escape.game

case class GameStateManager(
                             var scene: String,
                             var score: Int,
                             var highScore: Int,
                             var audioEnabled: Boolean,
                             var audioSupport: Boolean)
