package Games

import Games.getPosition
import java.awt.{Color, Dimension}
import javax.swing.ImageIcon
import scala.swing.event.ButtonClicked
import scala.swing._
import org.jpl7._
import org.jpl7.Term

def queensDrawer(state: GameState): Unit = {
  val queensFrame = new MainFrame {
    title = "8 Queens"
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
  queensFrame.visible = true
}

def queensController(gameState: GameState, gameMove: String): (GameState, Boolean) = {
  if(gameMove.toLowerCase() == "solve"){
    solveQueens(gameState)
    return (gameState, true)
  }

  val move = getPosition(gameMove)
  if (gameState.board(move.width)(move.height).name == "none") {
    if (queenValid(gameState, move)) {
      gameState.board(move.width)(move.height) = Piece('w', "qqueen")
      return (gameState, true)
    }
  } else {
    gameState.board(move.width)(move.height) = Piece('w', "none")
    return (gameState, true)
  }

  (gameState, false)
}

def queenValid(state: GameState, move: Dimension): Boolean = {
  val range = 0 until 8
  val sameRowColCheck = range.exists(k => state.board(move.width)(k).name == "qqueen" || state.board(k)(move.height).name == "qqueen")
  val diagonalCheck = range.forall(k =>
    !isOnBoard(move.width - k, move.height - k) || state.board(move.width - k)(move.height - k).name != "qqueen") &&
    range.forall(k =>
      !isOnBoard(move.width + k, move.height + k) || state.board(move.width + k)(move.height + k).name != "qqueen") &&
    range.forall(k =>
      !isOnBoard(move.width - k, move.height + k) || state.board(move.width - k)(move.height + k).name != "qqueen") &&
    range.forall(k =>
      !isOnBoard(move.width + k, move.height - k) || state.board(move.width + k)(move.height - k).name != "qqueen")

  !sameRowColCheck && diagonalCheck
}

private def isOnBoard(i: Int, j: Int): Boolean = {
  i >= 0 && i < 8 && j >= 0 && j < 8
}

def solveQueens(state: GameState): Unit = {
  val query = new Query("consult('C:/CSED/Paradiams/project/Game-Engine-Sc/src/main/scala/Games/8queens.pl')")
  query.hasSolution

  var initialPositions: List[Int] = List.empty[Int]

  for (i <- 0 until 8) {
    var done = false
    for (j <- 0 until 8) {
      if (state.board(j)(i) == Piece('w', "qqueen")) {
        initialPositions = initialPositions :+ (8-j)
        done = true
      }
    }
    if (!done)
      initialPositions = initialPositions :+ 0
  }

  val solutionQuery = new Query(s"n_queens(8,${initialPositions.mkString("[", ",", "]")},X), label(X).")
  if (solutionQuery.hasSolution) {
    val solution: Array[Int] = solutionQuery.oneSolution().get("X").toString.stripPrefix("[").stripSuffix("]").split(",").map(_.trim.toInt)
    //  println(solutionQuery.oneSolution().get("X").toString)
    for (i <- 0 until 8)
      state.board(8 - solution(i))(i) = Piece('w', "qqueen")
    queensDrawer(state)
  } else {
    Dialog.showMessage(null, "There exist no solution for current queens positioning", "Cannot Solve", Dialog.Message.Error)
  }
}
