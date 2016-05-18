package sample

class Algorithm {
  val MOVED_RIGHT: Char = 'r'
  val MOVED_LEFT: Char = 'l'

  def testSort(xs: Array[Int]): Long = {
    val start = System.nanoTime()
    sort(xs.toList)
    val end = System.nanoTime()
    end - start
  }

  def sort(lst: List[Int]): List[Int] =
    lst match {
      case Nil => Nil
      case pivot :: tail => sort(tail filter {
        pivot >
      }) :::
        pivot :: sort(tail filter {
        pivot <=
      })
    }

  /**
    * make from replay Array that contains only symbols toFind
    *
    * @param xs
    * @param toFind
    * @return
    */
  def getSeqOfOneChar(xs: Array[Char], toFind: Char): IndexedSeq[Char] =
    for (i <- 0 until xs.length if xs(i) == toFind) yield xs(i)

  def countLeftMove(xs: Array[Char]): Int = {
    getSeqOfOneChar(xs, MOVED_LEFT).length
  }

  def countRightMove(xs: Array[Char]): Int = {
    getSeqOfOneChar(xs, MOVED_RIGHT).length
  }

  def checkIn(xs: String, ys: String): Boolean = {
    xs.contains(ys)
  }

  def findMaxRepeatedSeq(xs: Array[String]): String = {
    var min = 0
    for (elem <- xs) {
      if (elem.length < xs(min).length) min = xs.indexOf(elem)
    }
    var maxString = ""
    for (i <- 0 until xs(min).length)
      for (j <- i + 1 until xs(min).length) {
        var fl = true
        for (elem <- xs) {
          if (!checkIn(elem, xs(min).substring(i, j)))
            fl = false
        }
        if (fl && (maxString.length < xs(min).substring(i, j).length))
          maxString = xs(min).substring(i, j)
      }
    maxString
  }
}
