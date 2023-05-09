import Games.{Piece, SudokuGenerator, chessController, chessDrawer, gameEngine, queensController, queensDrawer
  , sudokuController, sudokuDrawer, ticController, ticDrawer, connect4Drawer, connect4Controller}

import java.lang.reflect.Constructor
import scala.swing.*

object App extends SimpleSwingApplication {
  def top: MainFrame = {
    val mainMenu = new MainFrame {
      title = "Game Engine"
      contents = new BoxPanel(Orientation.Vertical) {
        contents += new Label("Main menu")
        contents += gameButton("Chess")
        contents += gameButton("8 Queens")
        contents += gameButton("Checkers")
        contents += gameButton("Connect 4")
        contents += gameButton("TicTacToe")
        contents += gameButton("Sudoku")
      }
      centerOnScreen()
    }
    mainMenu
  }

  def switchFrames(grid: String): Unit = {
    grid match {
      case "Chess" => gameEngine(chessController, chessDrawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), board =
        Array(Array(Piece('b', "rook"), Piece('b', "knight"), Piece('b', "bishop"), Piece('b', "queen"), Piece('b', "king"), Piece('b', "bishop"), Piece('b', "knight"), Piece('b', "rook")),
          Array(Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn"), Piece('b', "pawn")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn"), Piece('w', "pawn")),
          Array(Piece('w', "rook"), Piece('w', "knight"), Piece('w', "bishop"), Piece('w', "queen"), Piece('w', "king"), Piece('w', "bishop"), Piece('w', "knight"), Piece('w', "rook")))))
      case "8 Queens" => gameEngine(queensController, queensDrawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), board =
        Array(Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none"), Piece('n', "none")))))
      case "TicTacToe" => gameEngine(ticController, ticDrawer, Games.GameState(currentPlayer = 'x', pieceSelected = new Dimension(-1, -1), board =
        Array(
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none")),
          Array(Piece('n', "none"), Piece('n', "none"), Piece('n', "none"))
        )))
      case "Sudoku" => gameEngine(sudokuController, sudokuDrawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), board = {
        val constructor: Constructor[SudokuGenerator] = classOf[SudokuGenerator].getDeclaredConstructor(classOf[Int], classOf[Int])
        constructor.setAccessible(true) // allow access to package-private constructor
        val sudokuGenerator: SudokuGenerator = constructor.newInstance(9, 20)

        sudokuGenerator.fillValues().map((row) => row.map((elem) => {
          if (elem == 0)
            Piece('n', (elem).toString)
          else
            Piece('n', (elem + 20).toString)
        }))
      }
      ))
      case "Connect 4" => gameEngine(connect4Controller, connect4Drawer, Games.GameState(currentPlayer = 'w', pieceSelected = new Dimension(-1, -1), board =
        Array.fill(6, 7)(Piece('n', "empty"))))

      case _ =>
    }
  }

  def gameButton(game: String): Button = {
    new Button(game) {
      reactions += {
        case event.ButtonClicked(_) => switchFrames(game)
      }
    }
  }
}
