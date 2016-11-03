name := "MACHINE_LEARNING_SCALA"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++=Seq(
 "org.scalanlp" %% "breeze" % "0.12",
  "org.scalanlp" %% "breeze-natives" % "0.12",
  "org.scalanlp" %% "breeze-viz" % "0.12",
  "com.quantifind" %% "wisp" % "0.0.4",
  "org.scala-saddle" %% "saddle-core" % "1.3.+"


)