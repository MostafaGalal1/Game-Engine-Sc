package Games
import scala.util.Random

def fillRandom(): Array[Array[Int]] = {

  val a: Array[Array[Int]] = Array.fill(9, 9)(0)
  val r = Array.fill(9)(Set[Int]())
  val c = Array.fill(9)(Set[Int]())
  val z = Array.fill(3, 3)(Set[Int]())

  for (x <- 0 to 8; y <- 0 to 8)
    if (a(x)(y) != 0)
      setExist(a(x)(y), x, y)

  def setExist(v: Int, x: Int, y: Int) = {
    r(x) += v
    c(y) += v
    z(x / 3)(y / 3) += v
  }

  def remove(a: Array[Array[Int]], count: Int): Array[Array[Int]] = {
    val rs = Random.shuffle(List.range(0, 81))
    for (i <- 0 until count)
      a(rs(i) / 9)(rs(i) % 9) = 0
    a
  }

  def fill(x: Int, y: Int): Boolean = {
    if (a(x)(y) == 0) {
      var candidates = Set() ++ (1 to 9) -- r(x) -- c(y) -- z(x / 3)(y / 3)

      def current(): Boolean = {
        if (candidates.isEmpty)
          false
        else {
          val v = Random.shuffle(candidates.toList).iterator.next
          candidates = candidates - v
          a(x)(y) = v
          setExist(v, x, y)
          val good = if (y < 8) fill(x, y + 1) else if (x < 8) fill(x + 1, 0) else true
          if (good)
            true
          else {
            a(x)(y) = 0
            r(x) -= v
            c(y) -= v
            z(x / 3)(y / 3) -= v
            current()
          }
        }
      }

      current()
    }
    else if (y < 8) fill(x, y + 1) else if (x < 8) fill(x + 1, 0) else true
  }

  fill(0, 0)
  remove(a, 30)

}