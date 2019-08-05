name := "jaeger-tutotrial"

version := "0.1"

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  "io.jaegertracing" % "jaeger-client" % "0.35.5",
  "javax.ws.rs" % "javax.ws.rs-api" % "2.0")

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"