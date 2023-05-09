package Games

import Games.GameState
import java.awt.Dimension
import scala.swing.Dialog.showMessage
import scala.swing.{Dialog, Dimension, MainFrame}

def gameEngine(gameController: (GameState, String) => Boolean, gameDrawer: GameState => Unit, gameState: GameState): Unit = {
  gameDrawer(gameState)
  while (true) {
    val input: Option[String] = Dialog.showInput(null, "Enter your move", "Game Engine", Dialog.Message.Question, null, Nil, "")
    input match {
      case Some(gameMove) =>
        if (gameController(gameState, gameMove)) {
          gameState.currentPlayer = if (gameState.currentPlayer == 'w') 'b' else 'w'
        }
//      else {
//          showMessage(
//            parent = null,
//            message = "Invalid move",
//            title = "Game Engine",
//            messageType = Dialog.Message.Warning
//          )
//        }
        gameDrawer(gameState)
      case None =>
    }
  }
}

def getPosition(position : String) : Dimension = {
  val column : Int = position(0) - 'a'
  val row : Int = 8 - (position(1) - '0')
  new Dimension(row, column)
}
