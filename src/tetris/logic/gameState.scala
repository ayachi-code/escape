package tetris.logic

case class GameState(gotKey: Boolean, playerPosition: Point, gameDone: Boolean, score: Int, leaveRoomButtonPressed: Boolean, level: Int, transits: Boolean) {}
