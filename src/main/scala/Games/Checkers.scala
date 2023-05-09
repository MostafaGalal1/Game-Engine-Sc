package Games

import java.awt.Dimension
import javax.swing.ImageIcon
import scala.swing.{Button, Color, Dialog, Dimension, Frame, GridPanel}

def checkersDrawer(state: GameState): Unit = {
  val CheckersFrame = new Frame {
    title = "Checkers"
    contents = new GridPanel(8, 8) {
      val buttons: Array[Array[Button]] = Array.ofDim[Button](8, 8)
      Array.tabulate(8, 8) { case (i, j) => buttons(i)(j) = new Button() }
      Array.tabulate(8, 8) { case (i, j) =>
        val button = buttons(i)(j)
        if ((i + j) % 2 == 0) {
          button.background = new Color(255, 206, 158)
        } else {
          button.background = new Color(209, 139, 71)
        }
        button.borderPainted = false
        button.preferredSize = new Dimension(64, 64)
        button.icon = new ImageIcon(state.board(i)(j).image)
        contents += button
      }
    }
    pack()
    open()
  }
  for (w <- java.awt.Window.getWindows)
    w.dispose()
  CheckersFrame.visible = true
}

def checkersController(state: GameState, gameMove: String): Boolean = {
  val move = getPosition(gameMove)
  if(move.height>7 || move.height<0){
    Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
    return false
  }
  if (state.pieceSelected.width == -1 && state.pieceSelected.height == -1) {
    if (state.board(move.width)(move.height).player != state.currentPlayer) {
      Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
      return false
    }
    state.pieceSelected = new Dimension(move.width, move.height)
    false
  } else {
    val validMove = state.board(state.pieceSelected.width)(state.pieceSelected.height).name match {
      case "n" => validate(state, move, "n")
      case "k" => validate(state, move, "k")
      case _ => {Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error); false}
    }
    if (validMove) {
      state.board(move.width)(move.height) = state.board(state.pieceSelected.width)(state.pieceSelected.height)
      state.board(state.pieceSelected.width)(state.pieceSelected.height) = Piece('n', "none")
      if (state.currentPlayer == 'w' && move.width == 0) {
        state.board(move.width)(move.height) = Piece('w', "k")
      }
      if (state.currentPlayer == 'b' && move.width == 7) {
        state.board(move.width)(move.height) = Piece('b', "k")
      }
      if ((move.height - state.pieceSelected.height) * (move.height - state.pieceSelected.height) + (move.width - state.pieceSelected.width) * (move.width - state.pieceSelected.width) == 8) {
        state.board((state.pieceSelected.width + move.width) / 2)((state.pieceSelected.height + move.height) / 2) = Piece('n', "none")
        if (state.currentPlayer == 'w') state.currentPlayer = 'b'
        else state.currentPlayer = 'w'
      }
      state.pieceSelected = new Dimension(-1, -1)
      true
    }
    else {
      state.pieceSelected = new Dimension(-1, -1)
      Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
      false
    }
  }
}

private def validate(state: GameState, move: Dimension, name: String): Boolean = {
  val dx = move.height - state.pieceSelected.height
  val dy = move.width - state.pieceSelected.width

  if (state.board(move.width)(move.height).player == 'w' || state.board(move.width)(move.height).player == 'b')
    return false

  if ((name == "n") && (state.currentPlayer == 'w' && dy > 0 || state.currentPlayer == 'b' && dy < 0)) return false
  else if (dx * dx + dy * dy == 2) return true
  else {
    if (dx * dx + dy * dy != 8) return false
    val midX = (state.pieceSelected.height + move.height) / 2
    val midY = (state.pieceSelected.width + move.width) / 2
    val midPiece = state.board(midY)(midX)
    if (midPiece.name == "none") return false
    else if (midPiece.name == "n" || midPiece.name == "k") {
      if (midPiece.player != state.currentPlayer) return true
      else return false
    }
  }
  true
}