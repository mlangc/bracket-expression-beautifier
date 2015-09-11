organization := "com.github.mlangc"

name := "bracket-expression-beautifier"

version := "1.1-SNAPSHOT"

scalaVersion := "2.11.7"

scalacOptions := Seq("-encoding", "utf8", "-feature", "-deprecation", "-optimise", "-Ywarn-unused", "-Ywarn-dead-code", "-Ywarn-unused-import")

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.6" % "test"

//testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

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
