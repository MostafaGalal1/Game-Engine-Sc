package Games

import Games.getPosition
import java.awt.{Color, Dimension}
import javax.swing.ImageIcon
import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dimension, GridPanel, MainFrame}

def ticDrawer(state: GameState): Unit = {
  val ticFrame = new MainFrame {
    title = "Tic Tac Toe"
    contents = new GridPanel(3, 3) {
      Array.tabulate(3, 3) { case (i, j) =>
        val buttons: Array[Array[Button]] = Array.ofDim[Button](3, 3)
        buttons(i)(j) = new Button()
        val button = buttons(i)(j)
        if ((i + j) % 2 == 0) {
          button.background = new Color(255, 206, 158)
        } else {
          button.background = new Color(209, 139, 71)
        }
        button.borderPainted = false
        button.preferredSize = new Dimension(200, 200)
        button.icon = new ImageIcon(state.board(i)(j).image)
        contents += button
      }
    }
    pack()
    open()
  }
  for (w <- java.awt.Window.getWindows)
    w.dispose()
  ticFrame.visible = true
}

def ticController(gameState: GameState, gameMove: String): Boolean = {
  def getPosition(position: String): Dimension = {
    val column: Int = position(0) - 'a'
    val row: Int = 3 - (position(1) - '0')
    new Dimension(row, column)
  }
  val move = getPosition(gameMove)
  println(move)
  if (gameState.board(move.width)(move.height).name == "none") {
    gameState.board(move.width)(move.height) = Piece(if gameState.currentPlayer == 'w' then 'o' else 'x', "")
    return true
  }
  false
}