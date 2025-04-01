resolvers += Resolver.ApacheMavenSnapshotsRepo

val pekkoGrpcSbtPluginVersion = "1.1.1"
val sbtNativePackagerVersion = "1.10.0"

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % sbtNativePackagerVersion)
addSbtPlugin("org.apache.pekko" % "pekko-grpc-sbt-plugin" % pekkoGrpcSbtPluginVersion)
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.4")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.14.2")
addSbtPlugin("org.virtuslab.psh" % "sbt-pekko-serialization-helper" % "0.9.0")
