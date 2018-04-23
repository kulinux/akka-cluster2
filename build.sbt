name := "akka-cluster-2"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

retrieveManaged := true


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)



