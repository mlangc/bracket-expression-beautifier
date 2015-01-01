organization := "at.lnet"

name := "bracket-expression-beautifier"

version := "1.0"

scalaVersion := "2.11.4"

scalacOptions := Seq("-encoding", "utf8")

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.6" % "test"

//testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")
