

packageArchetype.java_application

name := "pemcrack"

mainClass in Compile := Some("com.pemcrack.PemCrack")

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.9",
  "bouncycastle" % "bcprov-jdk16" % "140",
  "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)
