package Sparks

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.util.MLUtils

/**
 * Created by rasrivastava on 3/22/17.
 */
class Data(text : String) {
  val strArr = text.split(" ")
  
  def isSpam() : Boolean = {
    if (strArr(0) == "ham") {
      false
    }else {
      true
    }
  }
}
object BayesianApp {
  def main(args: Array[String]) {
    val logFile = "/Users/rasrivastava/DATA_SETS/HOUSE_VOTES/a.txt" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    val sc = new SparkContext(conf)
//    val logData = sc.textFile(logFile, 2).cache()
//    val numAs = logData.filter(line => line.contains("shloak")).count()
//    val numBs = logData.filter(line => line.contains("am")).count()
//    println(s"Lines with a: $numAs, Lines with b: $numBs")
    val filePath = "/Users/rasrivastava/DATA_SETS/SMS_SPAM/SMSSpamCollection"


    sc.stop()
  }
}
