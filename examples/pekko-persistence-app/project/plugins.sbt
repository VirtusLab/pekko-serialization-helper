resolvers += Resolver.ApacheMavenSnapshotsRepo

val pekkoGrpcSbtPluginVersion = "1.0.0-15-c95da038-SNAPSHOT"

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9")
addSbtPlugin("org.apache.pekko" % "sbt-pekko-grpc" % pekkoGrpcSbtPluginVersion)
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.11.1")
addSbtPlugin("org.virtuslab.psh" % "sbt-pekko-serialization-helper" % "0.7.3")
