package tetris.logic

case class GameState(var player: Player, timeLeft: Int, gameDone: Boolean, leaveRoomButtonPressed: Boolean, level: Int, transits: Boolean) {}
