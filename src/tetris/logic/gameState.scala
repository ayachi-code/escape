package tetris.logic

case class GameState(timeLeft: Int,lives: Int, gotKey: Boolean, playerPosition: Point, gameDone: Boolean, score: Int, leaveRoomButtonPressed: Boolean, level: Int, transits: Boolean) {}
