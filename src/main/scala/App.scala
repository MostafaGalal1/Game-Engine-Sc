import Games.{Piece, chessController, chessDrawer, gameEngine, queensController, queensDrawer
  , sudokuController, sudokuDrawer, ticController, ticDrawer, connect4Drawer, connect4Controller
  ,checkersController, checkersDrawer, fillRandom}

import java.lang.reflect.Constructor
import scala.swing.*

object App extends SimpleSwingApplication {
  def top: MainFrame = {
    val mainMenu: MainFrame = new MainFrame {
      title = "Game Engine"
      contents = new BoxPanel(Orientation.Vertical) {
        contents += new Label("Main menu")
        contents += gameButton("Chess")
        contents += gameButton("8 Queens")
        contents += gameButton("Checkers")
        contents += gameButton("Connect 4")
        contents += gameButton("TicTacToe")
        contents += gameButton("Sudoku")
        for (e <- contents)
          e.xLayoutAlignment = 0.0
        border = Swing.EmptyBorder(10, 20, 10, 20)
      }
      val dialog: Dialog = new Dialog(owner) {
        modal = false
        title = title
      }
      centerOnScreen()
    }
    mainMenu
  }

  def switchFrames(grid: String): Unit = {
    grid match {
      case "Chess" => gameEngine(chessController, chessDrawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), players = 2, board =
        Array(Array(Piece('b', "rook"), Piece('b', "knight"), Piece('b', "bishop"), Piece('b', "queen"), Piece('b', "king"), Piece('b', "bishop"), Piece('b', "knight"), Piece('b', "rook")),
          Array(Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn")),
          Array(Piece('w', "rook"), Piece('w', "knight"), Piece('w', "bishop"), Piece('w', "queen"), Piece('w', "king"), Piece('w', "bishop"), Piece('w', "knight"), Piece('w', "rook")))))
      case "8 Queens" => gameEngine(queensController, queensDrawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), players = 1, board =
        Array.fill(8, 8)(Piece('n', "none"))))
      case "Checkers" => gameEngine(checkersController, checkersDrawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), players = 2, board =
        Array(Array(Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n")),
          Array(Piece('b', "n"), Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n"), Piece('n', "none"), Piece('b', "n")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none"), Piece('w', "n")),
          Array(Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none"), Piece('w', "n"), Piece('n', "none")))))
      case "TicTacToe" => gameEngine(ticController, ticDrawer, Games.GameState(currentPlayer = 'x', pieceSelected = new Dimension(-1, -1), players = 2, board =
        Array(
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"))
        )))
      case "Sudoku" => gameEngine(sudokuController, sudokuDrawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), players = 1, board = {
        fillRandom().map((row) => row.map((elem) => {
          if (elem == 0)
            Piece('n', (elem).toString)
          else
            Piece('n', (elem + 20).toString)
        }))
      }
      ))
      case "Connect 4" => gameEngine(connect4Controller, connect4Drawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), players = 2, board =
        Array.fill(6, 7)(Piece('n', "empty"))))

      case _ =>
    }
  }

  def gameButton(game: String): Button = {
    new Button(game) {
      preferredSize = new Dimension(250, 50)
      maximumSize = new Dimension(250, 50)
      minimumSize = new Dimension(250, 50)
      reactions += {
        case event.ButtonClicked(_) => switchFrames(game)
      }
    }
  }
}
