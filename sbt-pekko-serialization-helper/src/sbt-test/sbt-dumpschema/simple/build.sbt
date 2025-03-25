lazy val scala213 = "2.13.16"
lazy val scala212 = "2.12.18"

name := "simple-dump"
version := "0.1"
scalaVersion := scala213
val pekkoVersion = "1.1.2"
libraryDependencies += "org.apache.pekko" %% "pekko-persistence-typed" % pekkoVersion
crossScalaVersions := Seq(scala213, scala212)

enablePlugins(PekkoSerializationHelperPlugin)
ashCompilerPluginCacheDirectory := crossTarget.value
ashDumpPersistenceSchema / ashDumpPersistenceSchemaOutputDirectoryPath := crossTarget.value.getPath
ashDumpPersistenceSchema / ashDumpPersistenceSchemaOutputFilename := "dump.yaml"

ashCodecRegistrationCheckerCompilerPlugin / ashCompilerPluginEnable := false
ashSerializabilityCheckerCompilerPlugin / ashCompilerPluginEnable := false
