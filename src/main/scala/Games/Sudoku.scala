package Games

import org.jpl7._
import Games.getPosition

import java.awt.{Color, Dimension}
import javax.swing.ImageIcon
import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dimension, GridPanel, MainFrame}
import scala.swing.*

def sudokuDrawer(state: GameState): Unit = {
  val sudokuFrame: MainFrame = new MainFrame {
    Array.tabulate(9, 9) { case (i, j) =>
      title = "Sudoku"
      background = new Color(0, 0, 0)
      contents = new GridPanel(9, 9) {
        Array.tabulate(9, 9) { case (i, j) =>
          val buttons: Array[Array[Button]] = Array.ofDim[Button](9, 9)
          buttons(i)(j) = new Button()
          val button = buttons(i)(j)
          button.background = new Color(255, 255, 255)
          button.borderPainted = true
          button.preferredSize = new Dimension(64, 64)
          if (state.board(i)(j).name.toInt > 10) {
            button.background = new Color(150, 150, 150)
          }else{
            button.background = new Color(150, 150, 200)
          }
          button.text = (state.board(i)(j).name.toInt % 10).toString
          contents += button
        }
      }
    }


    centerOnScreen()
    pack()
    open()

  }
    java.awt.Window.getWindows.foreach(w => w.dispose())
    sudokuFrame.visible = true
}

def sudokuController(state: GameState, move: String): (GameState, Boolean) = {
  def parseMove(str: String): Array[Int] = {
    try {
      val column = str.charAt(0).toInt - 97
      val row = 8 - (str.charAt(1).toInt - 49)
      val value = str.split(" ")(1).toInt
      Array(row, column, value)
    } catch {
      case _: Exception => Array(-1)
    }
  }
  if(move.toLowerCase() == "solve"){
    solveSudoku(state)
    return (state, true)
  }

  val parsedMove: Array[Int] = parseMove(move)
  // add checking

  if(!parsedMove.forall(elem => elem>=0 || elem <= 9)) {
    return (state, false)
  }

  try {
    if (state.board(parsedMove(0))(parsedMove(1)).name.toInt > 10) {
      println("Can't change a preset value")
      return (state, false)
    }
  } catch {
    case _: Exception =>
      println("Invalid move")
      return (state, false)
  }

 

  val originI = (parsedMove(0)/3)*3
  val originJ = (parsedMove(1)/3)*3
  val colArr = state.board.map(_(parsedMove(1)))
  println(parsedMove(2))
  if(parsedMove(2) > 9 || parsedMove(2) < 0){
    return (state, false)
  }
  if (parsedMove(2).toString == "0") {
    state.board(parsedMove(0))(parsedMove(1)) = Piece('n', parsedMove(2).toString)
    return (state, true)
  }
  if(!state.board(parsedMove(0)).forall((x: Piece) => x.name.toInt%10 != parsedMove(2))
  || !colArr.forall((x: Piece) => x.name.toInt%10 != parsedMove(2))
  || !state.board.zipWithIndex.filter { case (_, index) => index >= originI && index <= originI + 2 }
    .map { case (row, _) => row }.transpose.zipWithIndex.filter { case (_, index) => index >= originJ && index <= originJ + 2 }
    .flatMap { case (row, _) => row }.forall((x: Piece) => x.name.toInt%10 != parsedMove(2))
  ){
    println("wrong number")
    state.board(parsedMove(0))(parsedMove(1)) = Piece('n', parsedMove(2).toString)
    return (state, false)
  }

  state.board(parsedMove(0))(parsedMove(1)) = Piece('n', parsedMove(2).toString)

  (state, true)
}

def solveSudoku(state: GameState): Unit = {
  val parsedSudoku = state.board.map{row => {row.map{elem => {if(elem.name.toInt%10 == 0) '_' else {elem.name.toInt%10}}}}}
  val parsedSudokuString = "[" + parsedSudoku.map(_.mkString(",")).mkString("[", "],[", "]") + "]"
  val q1 = new Query("consult('C:/CSED/Paradiams/project/Game-Engine-Sc/src/main/scala/Games/SudokuSolver.pl')")
  System.out.println("consult " + (if (q1.hasSolution) "succeeded" else "failed"))
  val q = Query("Puzzle=" + parsedSudokuString + ",sudoku(Puzzle).")
  if (q.hasSolution) {
    val qsValue = q.oneSolution().get("Puzzle").toString
    val parsedSolution = qsValue.stripPrefix("[[").stripSuffix("]]").split("\\], \\[").map{row =>row.split(", ")}
    state.board = state.board.zipWithIndex.map{case(row, i) => row.zipWithIndex.map{case(elem, j) => if(elem.name.toInt==0) Piece('n', parsedSolution(i)(j)) else elem}}
  } else {
    Dialog.showMessage(null, "Current state has no solution!", "Alert", Dialog.Message.Error)
  }
}