package escape.logic

case class GameState(attackAnimation: Boolean,  var player: Player, timeLeft: Int, gameDone: Boolean, leaveRoomButtonPressed: Boolean, level: Int, transits: Boolean)