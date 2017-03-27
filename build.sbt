organization := "com.github.mlangc"
name := "bracket-expression-beautifier"
version := "2.0.0"

scalaVersion := "2.11.8"
crossScalaVersions := Seq("2.11.8", "2.12.1")

scalacOptions := Seq("-encoding", "utf8", "-feature", "-deprecation", "-Ywarn-unused", "-Ywarn-dead-code", "-Ywarn-unused-import")

scalacOptions ++= {
  if (scalaVersion.value.startsWith("2.12.")) Seq("-opt:l:project")
  else Nil
}

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8.2" % "test"
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "test"
libraryDependencies += "org.scalameta" %% "scalameta" % "1.6.0"

libraryDependencies += "com.lihaoyi" % "ammonite" % "0.8.2" % "test" cross CrossVersion.full

val ammInitialCommands =
  "import com.github.mlangc.demo._; " +
  "import com.github.mlangc.brackets.api._;" +
  "import scala.meta._"

initialCommands in(Test, console) := s"""ammonite.Main("$ammInitialCommands").run()"""

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true
publishArtifact in Test := false

excludeDependencies += "org.mongodb"

pomExtra := (
  <url>https://github.com/mlangc/bracket-expression-beautifier</url>
    <licenses>
      <license>
        <name>BSD-3-Clause</name>
        <url>http://opensource.org/licenses/BSD-3-Clause</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>scm:git:https://github.com/mlangc/bracket-expression-beautifier</url>
      <connection>scm:git:https://github.com/mlangc/bracket-expression-beautifier.git</connection>
    </scm>
    <developers>
      <developer>
        <id>mlangc</id>
        <name>Matthias Langer</name>
        <url>http://mlangc.wordpress.com/</url>
      </developer>
    </developers>
  )
