package Games

import Games.GameState

import java.awt.Dimension
import scala.annotation.tailrec
import scala.swing.Dialog.showMessage
import scala.swing._

def gameEngine(gameController: (GameState, String) => Boolean, gameDrawer: GameState => Unit, gameState: GameState): Unit = {
  @tailrec
  def loop(gameState: GameState): Unit = {
    gameDrawer(gameState)
    val input: Option[String] = Dialog.showInput(null, "Enter your move", "Game Engine", Dialog.Message.Question, null, Nil, "")
    input match {
      case Some(gameMove) =>
        if (gameController(gameState, gameMove)) {
          gameState.currentPlayer = if (gameState.currentPlayer == 'w') 'b' else 'w'
        } else {
          Dialog.showMessage(null, "Your move doesn't follow the game rules", "Invalid Move", Dialog.Message.Error)
        }
        loop(gameState)
      case None =>
    }
  }
  /*
  iterative code just in case----->>>>
    gameDrawer(gameState)
    while (true) {
      val input: Option[String] = Dialog.showInput(null, "Enter your move", "Game Engine", Dialog.Message.Question, null, Nil, "")
      input match {
        case Some(gameMove) =>
          if (gameController(gameState, gameMove)) {
            gameState.currentPlayer = if (gameState.currentPlayer == 'w') 'b' else 'w'
          }
          gameDrawer(gameState)
        case None =>
      }
    }
  */

  loop(gameState)
}

def getPosition(position : String) : Dimension = {
  val column : Int = position(0) - 'a'
  val row : Int = 8 - (position(1) - '0')
  new Dimension(row, column)
}
