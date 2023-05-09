package Games

case class Piece(var player: Char, var name: String) {
  var image = s"src/main/scala/assets/${player + name}.png"
}
