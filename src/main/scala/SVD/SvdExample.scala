package SVD

import breeze.linalg.{svd, DenseMatrix}

/**
 * Created by rasrivastava on 12/5/16.
 */
class SvdExample {
  def test() : Unit = {
    val dm = DenseMatrix((1.0,2.0,3.0),(2.0,3.0,4.0))
    val s = svd(dm)
    println(s.U)
    println("===")
    println(s.∑)
    println("---")
    println(s.Vt)

  }
}

object Test extends App {
  val svdExample = new SvdExample
  svdExample.test()
}
