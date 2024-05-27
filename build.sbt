ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"
val sparkVersion = "3.5.0"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaPractise"
  )

val monixVersion = "3.4.1"
libraryDependencies ++= Seq(
  "io.monix" %% "monix" % monixVersion,
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion
)






