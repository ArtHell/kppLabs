package sample

class Algorithm {
  val MOVED_RIGHT: Char = 'r'
  val MOVED_LEFT: Char = 'l'

  def sort(xs: Array[Int]): Array[Int] =
    if (xs.length <= 1) xs
    else {
      val pivot = xs(xs.length / 2)
      Array.concat(
        sort(xs filter (pivot >)),
        xs filter (pivot ==),
        sort(xs filter (pivot <)))
    }

  def countLeftMove(xs: Array[Char]): Int = {
    xs.filter(_ == MOVED_LEFT).length
  }

  def countRightMove(xs: Array[Char]): Int = {
    xs.filter(_ == MOVED_RIGHT).length
  }
}
