package BayesianLearning

import java.io.{FileWriter, BufferedWriter, File}

import scala.io.Source

/**
 * Created by Rahul on 11/3/16.
 */


class PriorValue(val total : Int,val success : Int) {
  def updatePosterior(result : Boolean) : PriorValue = {
    new PriorValue(total+1,if (result) success+1 else success)
  }


  def getUpdatedAlphaBeta(alpha : Int,beta : Int) : Tuple2[Double,Double] = {
    val updatedAlpha = success + alpha
    val updatedBeta = total - success + beta
    Tuple2[Double,Double](updatedAlpha,updatedBeta)
  }

}

object TestCoinBias extends App  {
  val fileName = "/Users/rasrivastava/CODE_OPEN/MACHINE_LEARNING_SCALA/src/test/resources/experiment.txt"
  def generateRandomWithBias(bias : Double,count : Int) : Unit = {
    val random = scala.util.Random
    val file = new File(fileName)
    if (!file.exists()) {
      file.createNewFile();
    }
    val bufferedWriter = new BufferedWriter(new FileWriter(file))
    for (i <- 0 to count-1) {
      val newValue = random.nextDouble()
      var value = 0
      if (newValue <= bias) {
        value = 1
      }else {
        value = 0
      }

      bufferedWriter.write(value.toString)
      bufferedWriter.write("\n")
    }

    bufferedWriter.close()
  }
  //val fileName = "/Users/rasrivastava/CODE_OPEN/MACHINE_LEARNING_SCALA/src/test/resources/experiment.txt"

  val biasValue = 0.75
  generateRandomWithBias(biasValue,1000)
  val lines = Source.fromFile(fileName).getLines()
  var priorValue = new PriorValue(0,0)
  for (line <- lines) {
    val result = line.toInt
    if (result == 0) {
      priorValue = new PriorValue(priorValue.total,priorValue.success).updatePosterior(false)
    }else {
      priorValue = new PriorValue(priorValue.total,priorValue.success).updatePosterior(true)
    }


  }
  val alpha = 1
  val beta  = 1
  val alphaBeta = priorValue.getUpdatedAlphaBeta(alpha,beta)

  println("Bias for 1 is " + alphaBeta._1/(alphaBeta._1 + alphaBeta._2))


}
