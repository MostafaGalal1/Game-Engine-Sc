package Games

case class Piece(var player: Char, var name: String) {
  var image = s"assets/${player + name}.png"
}
