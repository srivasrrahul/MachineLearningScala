package Sparks

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable

/**
 * Created by rasrivastava on 3/26/17.
 */

class WikipediaData(title : String,text : String) {
  def mentionsLanguage(lang: String): Boolean = text.split(' ').contains(lang)
}


object Wikepedia {
  def main (args: Array[String]){
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val filePath = "/Users/rasrivastava/DATA_SETS/SPARK/WIKI/wikipedia/file/wikipedia.dat"


    val wikiRdd : RDD[WikipediaData] = sc.textFile(filePath,2).map(line => {
      val subs = "</title><text>"
      val i = line.indexOf(subs)
      val title = line.substring(14, i)
      val text  = line.substring(i + subs.length, line.length-16)
      new WikipediaData(title, text)

    }).persist()


    val langs = List(
      "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
      "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")

    def occurrencesOfLang(lang: String, rdd: RDD[WikipediaData]): Long = {
      rdd.filter(data => {
        data.mentionsLanguage(lang)
      }).count()

    }

    def rankLangs(langs: List[String], rdd: RDD[WikipediaData]): List[(String, Long)] = {
      val rankings = new mutable.HashMap[String,Long]()
      langs.map(lang => {
        val count = occurrencesOfLang(lang,rdd)


        rankings.+=((lang,count))
      })

      rankings.toList

    }

    def efficientRankLang(langs: List[String], rdd: RDD[WikipediaData]): List[(String, Long)] = {
      val rankings = new mutable.HashMap[String,Long]()

      rdd.map(data => {
        langs.map(lang => {
          if (data.mentionsLanguage(lang) == true) {
            if (rankings.contains(lang) == false) {
              rankings.+=((lang,0))
            }

            rankings.+=((lang,rankings.get(lang).get+1))
          }
        })
      })

      rankings.toList

    }



    val lst = rankLangs(langs,wikiRdd)
    println("Rahul" + lst.size)
    lst.foreach(println)






  }
}
