package Games

import Games.getPosition

import java.awt.{Color, Dimension}
import javax.swing.ImageIcon
import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dialog, Dimension, GridPanel, MainFrame}


def connect4Drawer(state: GameState): Unit = {
  val connect4Frame: MainFrame = new MainFrame {
    Array.tabulate(6, 7) { case (i, j) =>
      title = "Connect-4"
      background = new Color(16, 52, 166)
      contents = new GridPanel(6, 7) {
        Array.tabulate(6, 7) { case (i, j) =>
          val buttons: Array[Array[Button]] = Array.ofDim[Button](6, 7)
          buttons(i)(j) = new Button()
          val button = buttons(i)(j)
          button.background = new Color(16, 52, 166)
          button.borderPainted = false
          button.preferredSize = new Dimension(64, 64)
          button.icon = new ImageIcon(state.board(i)(j).image)
          contents += button
        }
      }
    }
    centerOnScreen()
    pack()
    open()
  }
  java.awt.Window.getWindows.foreach((w) => w.dispose())
  connect4Frame.visible = true
}

def connect4Controller(state: GameState, move: String): Boolean = {
  if(move.length != 1){
    Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
    return false
  }

  val parsedMove = move(0).toInt - '1'.toInt

  println(parsedMove)
  if(parsedMove > 7 || parsedMove < 0){
    Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
    return false
  }
  if (state.board(0)(parsedMove).name != "empty") {
    Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
    return false
  }

  val row = state.board.lastIndexWhere(_.apply(parsedMove).name == "empty")

  if (row >= 0) {
    if (state.currentPlayer == 'w')
      state.board(row)(parsedMove) = Piece('n', "red")

    if (state.currentPlayer == 'b')
      state.board(row)(parsedMove) = Piece('n', "yellow")


      println(state.board.foreach(_.mkString("Array(", ", ", ")")))

    return true
  }
  Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
  false
}
