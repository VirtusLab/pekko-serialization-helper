resolvers += Resolver.ApacheMavenSnapshotsRepo

val pekkoGrpcSbtPluginVersion = "1.0.2"
val sbtNativePackagerVersion = "1.10.0"

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % sbtNativePackagerVersion)
addSbtPlugin("org.apache.pekko" % "pekko-grpc-sbt-plugin" % pekkoGrpcSbtPluginVersion)
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.12.1")
addSbtPlugin("org.virtuslab.psh" % "sbt-pekko-serialization-helper" % "0.8.0")
