package lesson01.solution

import io.jaegertracing.internal.JaegerTracer
import lib.Tracing
import scala.collection.JavaConverters._
import scala.util.Try

object Hello {

  object TracerObject {
    val tracer: Option[JaegerTracer] = Try {
      Tracing.init("hello-world")
    }.toOption
  }

  def sayHello(helloTo: String, tracer: JaegerTracer): Unit = {
    val span = tracer.buildSpan("say-hello").start()
    span.setTag("hello-to", helloTo)
    val helloStr = String.format("Hello, %s!", helloTo)

    val logs = Map("event" -> "string-format", "value" -> helloStr).asJava

    span.log(logs)

    println(helloStr)
    span.log(Map("event" -> "println").asJava)

    span.finish()
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      throw new IllegalArgumentException("Expecting one argument");
    }

    val helloTo: String = args[0].toString()
    if (TracerObject.tracer.isDefined)
      sayHello(helloTo, TracerObject.tracer.get)
  }
}