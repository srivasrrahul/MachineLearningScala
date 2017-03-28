package BasicStatisticalValue

/**
 * Created by Rahul on 11/10/16.
 */


class DescriptiveStatistics(val data : Array[List[Double]]) {
  val meanVector = new Array[Double](data.size)
  var index = 0



  //Data Types
  val covarianceMatrix = Array.ofDim[Double](data.size,data.size)
  val correlationMatrix = Array.ofDim[Double](data.size,data.size)


  //Calculation
  data.foreach(lst => {
    meanVector(index) = lst.sum/lst.size
    index = index + 1
  })

  covariance()
  correlation()

  def calculateCovariance(i : Int, k : Int) : Double = {

    def itr(lst1 : List[Double],lst2 : List[Double]) : Double = {
      var sum : Double = 0
      val mean1 = meanVector(i)
      val mean2 = meanVector(k)
      (lst1 zip lst2).map {case (x1,y1) => {
        sum = sum + (x1-mean1) *(y1-mean2)
      }}

      sum
    }


    itr(data(i),data(k))/data(0).size
  }

  def covariance(): Unit = {
    val observationsType = data.size
    //val eachObservationCount = data(0).size

    for (i <- 0 to observationsType - 1) {
      for (k <- 0 to observationsType - 1) {
        covarianceMatrix(i)(k) = calculateCovariance(i,k)
      }
    }

  }

  def correlation() : Unit = {

    val observationsType = data.size
    for (i <- 0 to observationsType-1) {
      for (k <- 0 to observationsType-1) {
        if (i == k) {
          correlationMatrix(i)(k) = 1.0
        }else {
          correlationMatrix(i)(k) = covarianceMatrix(i)(k)/(Math.sqrt(covarianceMatrix(i)(i)) * Math.sqrt(covarianceMatrix(k)(k)))
        }
      }
    }


  }

}

object DescriptiveStatisticsTest extends  App {
  val lst1 = List[Double](42,52,48,58)
  val lst2 = List[Double](4,5,4,3)

  val descriptiveStats = new DescriptiveStatistics(Array(lst1,lst2))

  println(descriptiveStats.covarianceMatrix.deep.mkString(","))
  println(descriptiveStats.correlationMatrix.deep.mkString(","))
}
