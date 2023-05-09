package Games

import Games.Piece
import scala.swing.Dimension

case class GameState(var currentPlayer: Char, var pieceSelected: Dimension, var board: Array[Array[Piece]])
