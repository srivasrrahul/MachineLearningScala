package examples

import breeze.linalg.DenseMatrix

/**
 * Created by rasrivastava on 8/20/16.
 */
object BreezeExample extends App {
  val dm = DenseMatrix((1.0,2.0),(23.0,3.0))
  val dm1 = dm.t
  println({dm :+ dm1})

}
