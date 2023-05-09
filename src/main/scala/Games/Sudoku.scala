package Games

import Games.getPosition
import java.awt.{Color, Dimension}
import javax.swing.ImageIcon
import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dimension, GridPanel, MainFrame}
import scala.swing._

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
          button.text = (state.board(i)(j).name.toInt % 10).toString;
          contents += button
        }
      }
    }
    centerOnScreen()
    pack()
    open()

  }
    java.awt.Window.getWindows.foreach((w) => w.dispose())
    sudokuFrame.visible = true
}

def sudokuController(state: GameState, move: String): Boolean = {
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

  val parsedMove: Array[Int] = parseMove(move)
  // add checking

  if(!parsedMove.forall((elem)=> elem>=0 || elem <= 9)) {
    return false
  }

  try {
    if (state.board(parsedMove(0))(parsedMove(1)).name.toInt > 10) {
      println("Can't change a preset value")
      Dialog.showMessage(null, "Can't change a preset value", "Alert", Dialog.Message.Error)
      return false
    }
  } catch {
    case _: Exception =>
      Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
      println("Invalid move")
      return false
  }

  val originI = (parsedMove(0)/3)*3;
  val originJ = (parsedMove(1)/3)*3;
  val colArr = state.board.map(_(parsedMove(1)))

  if(!state.board(parsedMove(0)).forall((x: Piece) => x.name.toInt%10 != parsedMove(2))
  || !colArr.forall((x: Piece) => x.name.toInt%10 != parsedMove(2))
  || !state.board.zipWithIndex.filter { case (_, index) => index >= originI && index <= originI + 2 }
    .map { case (row, _) => row }.transpose.zipWithIndex.filter { case (_, index) => index >= originJ && index <= originJ + 2 }
    .flatMap { case (row, _) => row }.forall((x: Piece) => x.name.toInt%10 != parsedMove(2))
  ){
    Dialog.showMessage(null, "Invalid move", "Alert", Dialog.Message.Error)
    println("wrong number")
  }

  state.board(parsedMove(0))(parsedMove(1)) = Piece('n', parsedMove(2).toString)

  false
}

