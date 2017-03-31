package Sparks


import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.util.MLUtils

/**
 * Created by Rahul on 3/22/17.
 */
class Data(val gender : Int,val reasonCode : Int,val month : Int,val weekType : Int,val timeBand : Int,val breath : Int,val ageBand : Int) {

  override def toString = s"Data(gender=$gender, reasonCode=$reasonCode, month=$month, weekType=$weekType, timeBand=$timeBand, breath=$breath, ageBand=$ageBand)"
}

class DataParser {
  def parseData(text : String) : Data = {
    val arr =text.split(",")

    val d = new Data(parseGender(arr(7)),
             parseReasonCode(arr(0)),
             parseMonth(arr(1)),
             parseWeekType(arr(3)),
             parseTimeBand(arr(4)),
             arr(5).toInt,
             parseAgeBand(arr(6))
             )
    //val d = new Data(0,0,0,0,0,0,0)
    //println(d)
    d




  }

  def parseGender(gender : String) : Int = {
    //println("Inside gender")
    val g = gender match {
      case "Male"                          => 0
      case "Female"                        => 1
      case "Unknown"                       => 2
      case _                               => 99
    }

    //println("Inside gender " + g)
    g
  }

  def parseReasonCode(reasonStr : String) : Int = {

    val r = reasonStr match {
      case "Moving Traffic Violation" => 1
      case "Road Traffic Collision" => 2
      case "Suspicion of Alcohol" => 3
      case _ => 4
    }
    //println("Inside reasonCode " + r)
    r
  }

  def parseMonth(month : String) : Int = {
    month match {
      case "Jan" => 1
      case "Feb" => 2
      case "Mar" => 3
      case "April" => 4
      case "May" => 5
      case "June" => 6
      case "July" => 7
      case "Aug" => 8
      case "Sep" => 9
      case "Oct" => 10
      case "Nov" => 11
      case "Dec" => 12
      case _ => 99
    }
  }

  def parseWeekType(weekType : String) : Int = {
    weekType match {
      case "Weekday" => 1
      case _ => 2
    }
  }

  def parseTimeBand(timeBand : String) : Int = {
    timeBand match {
      case "12am-4am"                      => 0
      case "4am-8am"                       => 1
      case "8am-12pm"                      => 2
      case "12pm-4pm"                      => 3
      case "4pm-8pm"                       => 4
      case "8pm-12pm"                      => 5
      case _                               => 99
    }
  }

  def parseAgeBand(ageBand : String) : Int = {
    ageBand match {
      case "16-19"                         => 0
      case "20-24"                         => 1
      case "25-29"                         => 2
      case "30-39"                         => 3
      case "40-49"                         => 4
      case "50-59"                         => 5
      case "60-69"                         => 6
      case "70-98"                         => 7
      case "Other"                         => 8
      case _                               => 99
    }
  }




}
object BayesianApp {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    val sc = new SparkContext(conf)
//    val logData = sc.textFile(logFile, 2).cache()
//    val numAs = logData.filter(line => line.contains("shloak")).count()
//    val numBs = logData.filter(line => line.contains("am")).count()
//    println(s"Lines with a: $numAs, Lines with b: $numBs")
    val filePath = "/Users/rasrivastava/DATA_SETS/DigitalBreathTestData2013.txt"
    println("Rahul Start ")
    //val dataRDD : RDD[Data] =
    val basicRDD : RDD[String] = sc.textFile(filePath,2).map(line => line).persist()
    val basicRDD1 : RDD[String] = basicRDD.mapPartitionsWithIndex((i,itr) => {
      if (i == 0 && itr.hasNext) {
        itr.next()
        itr
      }else {
        itr
      }
    })
    //basicRDD1.collect().map(println)

    val dataRDD : RDD[LabeledPoint] =   basicRDD1.map(line => {
      val data = new DataParser().parseData(line)
      val labeledPoint = new LabeledPoint(data.gender,Vectors.dense(data.reasonCode,data.month,data.weekType,data.timeBand,data.breath,data.ageBand))
      //println(line)
      //println(data)
      //data
      labeledPoint
    }).persist()

    //dataRDD.collect().map(println)
    val Array(training, test) = dataRDD.randomSplit(Array(0.6, 0.4))

    val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")
    val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
    println("Accuracy is " + accuracy)


    //    dataRDD.repartition(1).saveAsTextFile("/Users/rasrivastava/DATA_SETS/a.txt")
//    dataRDD.collect().map(println)




    sc.stop()
  }
}
