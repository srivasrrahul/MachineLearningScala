package Recommendation

import scala.collection.mutable.ListBuffer

/**
 * Created by Rahul on 2/7/17.
 */
class Cosine {
  def cosine(v1: List[Double],v2 : List[Double]) : Double = {
    val mod1 = Math.sqrt(v1.foldRight(0.0)((x,y) => x*x + y))
    val mod2 = Math.sqrt(v2.foldRight(0.0)((x,y) => x*x + y))
    //println(mod1)
    //println(mod2)
    var s = 0.0
    (v1,v2).zipped.map{
      (x,y) => s = s + x*y
    }

    s/(Math.abs(mod1)* Math.abs(mod2))
  }

  def normalizeVector(v : List[Double]) : List[Double] = {
    val avg = v.sum/v.size
    val lstBuffer = new ListBuffer[Double]
    v.foreach(x => {
      lstBuffer.append(x-avg)
    })

    lstBuffer.toList
  }


}

object Main extends  App {
  val l1 = List[Double](3.06,2.08,2.92)
  val l2 = List[Double](500.0,320.0,640.0)
  val l3 = List[Double](6.0,4.0,6.0)
  val cosine = new Cosine
  println(cosine.cosine(cosine.normalizeVector(l1),cosine.normalizeVector(l2)))
}
