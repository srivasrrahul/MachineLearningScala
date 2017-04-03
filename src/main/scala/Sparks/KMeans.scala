package Sparks

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Rahul on 4/1/17.
 */
class Posting(val postType : Int,val id : Int,val acceptedAnswerId : Int,val parentId : Int,val score : Int,val tag : String) extends  Serializable{

  override def toString = s"Posting(postType=$postType, id=$id, acceptedAnswerId=$acceptedAnswerId, parentId=$parentId, score=$score, tag=$tag)"
}
class PostingParser {
  def parsePosting(string: String) : Posting = {
    val arr = string.split(",")
    val postType = arr(0).toInt
    val id = arr(1).toLong
    var acceptedAnswerId : Long = -1
    if (arr(2).isEmpty == false) {
      acceptedAnswerId = arr(2).toLong
    }

    var parentId : Long = -1
    if (arr(3).isEmpty == false) {
      parentId = arr(3).toLong
    }

    val score = arr(4).toLong
    if (arr.size == 6) {
      val tag = arr(5)
      new Posting(postType, id.toInt, acceptedAnswerId.toInt, parentId.toInt, score.toInt, tag)
    }else {
      new Posting(postType, id.toInt, acceptedAnswerId.toInt, parentId.toInt, score.toInt, "")
    }
  }
}
object KMeans {





  def scored(questionAnswers : RDD[(Posting,RDD[Posting])]) : RDD[(Posting,Int)] = {
    questionAnswers.map(questionAnswer => {
      val answers = questionAnswer._2
      val ordering = new Ordering[Posting]() {
        override def compare(x: Posting, y: Posting): Int = {
          Ordering.Long.reverse.compare(x.score,y.score)
        }
      }
      val maxAnswer = answers.max()(ordering)
      (questionAnswer._1,maxAnswer.score)
    })
  }



  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val filePath = "/Users/rasrivastava/DATA_SETS/STACK_OVERFLOW/stackoverflow.csv"
    val lines = sc.textFile(filePath,2).persist()
    def rawPostings(strRDD : RDD[String]) : RDD[(Int,Posting)] = {

      strRDD.map(string => {
        val arr = string.split(",")
        val postType = arr(0).toInt
        val id = arr(1).toLong
        var acceptedAnswerId : Long = -1
        if (arr(2).isEmpty == false) {
          acceptedAnswerId = arr(2).toLong
        }

        var parentId : Long = -1
        if (arr(3).isEmpty == false) {
          parentId = arr(3).toLong
        }

        val score = arr(4).toLong
        if (arr.size == 6) {
          val tag = arr(5)
          (id.toInt,new Posting(postType, id.toInt, acceptedAnswerId.toInt, parentId.toInt, score.toInt, tag))
        }else {
          (id.toInt,new Posting(postType, id.toInt, acceptedAnswerId.toInt, parentId.toInt, score.toInt, ""))
        }
      }).persist()
    }

    def groupedPostings(postingRDD : RDD[(Int,Posting)]) : RDD[(Int,(Posting,Int))] = {
      val questionsRDD = postingRDD.filter(posting => {
        posting._2.postType == 1
      })
      val answersRDD = postingRDD.filter(posting => {
        posting._2.postType == 2
      })

      val questionWithAnswer = answersRDD.map(answersRDD => {
        (answersRDD._2.parentId,answersRDD._2)
      })

      val quesAnswers = questionsRDD.join(questionWithAnswer)
      val quesAnswersGroupedByKey = quesAnswers.groupByKey()
      quesAnswersGroupedByKey.map(itr => {
        val answers = itr._2
        //val answerSum = answers.toList.map(_._2.score).sum
        val maxAnswer = answers.toList.map(_._2.score).max
        val question = answers.toList.head._1
        (itr._1,(question,maxAnswer))
      })
    }

    val raw = rawPostings(lines)
    val grouped = groupedPostings(raw)
    grouped.collect().map(println)
    //raw.collect().map(println)
    sc.stop()
//    val grouped = groupedPostings(raw)
//    val scores = scored(grouped)
//    scores.collect().map(println)

  }
}
