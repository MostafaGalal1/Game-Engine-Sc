package Games

import Games.getPosition
import Games.{GameState, Piece}
import java.awt.{Color, Dimension, Window}
import javax.swing.ImageIcon
import scala.annotation.tailrec
import scala.swing.*

def chessDrawer(state: GameState): Unit = {
  val chessFrame = new Frame {
    title = "Chess"
    contents = new GridPanel(8, 8) {
      val buttons: Array[Array[Button]] = Array.ofDim[Button](8, 8)
      Array.tabulate(8, 8) { case (i, j) =>
        buttons(i)(j) = new Button()
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
  chessFrame.visible = true
}

def chessController (gameState: GameState, gameMove: String) : (GameState, Boolean) = {
  val parsedMove = gameMove.split(" ").map { elem => getPosition(elem) }
  val pieceSelected = parsedMove(0)
  val move = parsedMove(1)

  if (gameState.board(pieceSelected.width)(pieceSelected.height).player != gameState.currentPlayer)
    return (gameState, false)

  val validMove = gameState.board(pieceSelected.width)(pieceSelected.height).name match {
    case "rook" => rook(gameState, move)
    case "knight" => knight(gameState, move)
    case "bishop" => bishop(gameState, move)
    case "queen" => queen(gameState, move)
    case "king" => king(gameState, move)
    case "pawn" => pawn(gameState, move)
    case _ => false
  }

  if (validMove) {
    gameState.board(move.width)(move.height) = gameState.board(pieceSelected.width)(pieceSelected.height)
    gameState.board(pieceSelected.width)(pieceSelected.height) = Piece('n', "none")
    (gameState, true)
  } else
    (gameState, false)
}

def king (state: GameState, move: Dimension): Boolean = {
  val src = state.pieceSelected
  val dst = move

  if (state.board(dst.width)(dst.height).player == state.currentPlayer)
    return false

  val dx = Array(-1, -1, 0, 1, 1, 1, 0, -1)
  val dy = Array(0, 1, 1, 1, 0, -1, -1, -1)

  dx.zip(dy).foreach { case (x, y) => if (src.width + x == dst.width && src.height + y == dst.height) return true}
  false
}

def knight (state: GameState, move: Dimension): Boolean = {
  val src = state.pieceSelected
  val dst = move

  if (state.board(dst.width)(dst.height).player == state.currentPlayer)
    return false

  val dx = Array(-2, -2, -1, -1, 1, 1, 2, 2)
  val dy = Array(-1, 1, -2, 2, -2, 2, -1, 1)

  dx.zip(dy).foreach { case (x, y) => if (src.width + x == dst.width && src.height + y == dst.height) return true}
  false
}

  def pawn (state: GameState, move: Dimension): Boolean = {
  val src = state.pieceSelected
  val dst = move

  val direction = if (state.board(src.width)(src.height).player == 'w') -1 else 1
  val startingRow = if (state.board(src.width)(src.height).player == 'w') 6 else 1

  if (state.board(dst.width)(dst.height).player == state.currentPlayer)
    return false

  if (src.height == dst.height) {
    if (!(state.board(dst.width)(dst.height).name == "none"))
      return false

    if (dst.width - src.width == direction) {
      return true
    } else if (dst.width - src.width == 2 * direction && src.width == startingRow) {
      val middleSquare = new Dimension(src.width + direction, src.height)
      if (state.board(middleSquare.width)(middleSquare.height).name == "none") {
        return true
      }
    }
  } else if (Math.abs(src.height - dst.height) == 1) {
    if (!(state.board(dst.width)(dst.height).name == "none") && dst.width - src.width == direction) {
      return true
    }
  }

  false
}

def bishop (state: GameState, move: Dimension): Boolean = {
  val src = state.pieceSelected
  val dst = move

  if (state.board(dst.width)(dst.height).player == state.currentPlayer)
    return false

  if (Math.abs(src.width - dst.width) != Math.abs(src.height - dst.height))
    return false

  val dx = if (src.width < dst.width) 1 else -1
  val dy = if (src.height < dst.height) 1 else -1

  @tailrec
  def loop(x: Int, y: Int): Boolean = {
    if (x == dst.width || y == dst.height) true
    else if (state.board(x)(y).name != "none") false
    else loop(x + dx, y + dy)
  }

  loop(src.width + dx, src.height + dy)
}

def queen (state: GameState, move: Dimension): Boolean = {
  val src = state.pieceSelected
  val dst = move

  if (state.board(dst.width)(dst.height).player == state.currentPlayer)
    return false

  if (src.width == dst.width || src.height == dst.height) {
    val dx = if (src.width == dst.width) 0 else if (src.width < dst.width) 1 else -1
    val dy = if (src.height == dst.height) 0 else if (src.height < dst.height) 1 else -1

    @tailrec
    def loop(x: Int, y: Int): Boolean = {
      if (x == dst.width && y == dst.height) true
      else if (state.board(x)(y).name != "none") false
      else loop(x + dx, y + dy)
    }

    loop(src.width + dx, src.height + dy)
  }

  if (Math.abs(src.width - dst.width) == Math.abs(src.height - dst.height)) {
    val dx = if (src.width < dst.width) 1 else -1
    val dy = if (src.height < dst.height) 1 else -1

    @tailrec
    def loop(x: Int, y: Int): Boolean = {
      if (x == dst.width && y == dst.height) true
      else if (state.board(x)(y).name != "none") false
      else loop(x + dx, y + dy)
    }

    loop(src.width + dx, src.height + dy)
  }

  false
}

def rook (state: GameState, move: Dimension): Boolean = {
  val src = state.pieceSelected
  val dst = move

  if (state.board(dst.width)(dst.height).player == state.currentPlayer)
      return false

  if (src.width == dst.width || src.height == dst.height) {
    val dx = if (src.width < dst.width) 1 else if (src.width > dst.width) -1 else 0
    val dy = if (src.height < dst.height) 1 else if (src.height > dst.height) -1 else 0

    @tailrec
    def loop(x: Int, y: Int): Boolean = {
      if (x == dst.width && y == dst.height) true
      else if (state.board(x)(y).name != "none") false
      else loop(x + dx, y + dy)
    }

    loop(src.width + dx, src.height + dy)
  }

  false
}
