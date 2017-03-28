package Sparks

/**
 * Created by rasrivastava on 3/22/17.
 */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf


object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "/Users/rasrivastava/DATA_SETS/HOUSE_VOTES/a.txt" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("shloak")).count()
    val numBs = logData.filter(line => line.contains("am")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    sc.stop()
  }
}
