import com.typesafe.sbt.SbtMultiJvm.multiJvmSettings
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm
import org.virtuslab.psh.PekkoSerializationHelperPlugin

name := "pekko-cluster-app"
version := "0.1"
scalaVersion := "2.13.14"

val circeVersion = "0.14.9"
val pekkoVersion = "1.1.0"

lazy val `pekko-cluster-app` = project
  .in(file("."))
  .enablePlugins(PekkoSerializationHelperPlugin)
  .settings(multiJvmSettings: _*)
  .settings(
    libraryDependencies ++= pekkoDependencies ++ ashDependencies ++ Seq(logbackDependency, circeDependency),
    fork := true, // must be true due to https://discuss.lightbend.com/t/pekko-projection-getting-started-guide-example-could-not-run-eventgeneratorapp/9434/2
    Global / cancelable := false,
    scalacOptions += "-Ywarn-unused")
  .configs(MultiJvm)

lazy val pekkoDependencies =
  Seq("org.apache.pekko" %% "pekko-actor-typed", "org.apache.pekko" %% "pekko-cluster-typed").map(_ % pekkoVersion)

lazy val circeDependency = "io.circe" %% "circe-core" % circeVersion

lazy val ashDependencies =
  Seq(PekkoSerializationHelperPlugin.annotation, PekkoSerializationHelperPlugin.circePekkoSerializer)

lazy val logbackDependency = "ch.qos.logback" % "logback-classic" % "1.5.7"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := "4.9.3"
