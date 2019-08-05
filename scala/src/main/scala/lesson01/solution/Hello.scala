package lesson01.solution

import io.jaegertracing.internal.JaegerTracer
import lib.{TracerObject, Tracing}

import scala.collection.JavaConverters._
import scala.util.Try

object Hello {
  def sayHello(helloTo: String, tracer: JaegerTracer): Unit = {
    Try {
      val span = tracer.buildSpan("say-hello").start()
      span.setTag("hello-to", helloTo)
      val helloStr = String.format("Hello, %s!", helloTo)

      val logs = Map("event" -> "string-format", "value" -> helloStr).asJava

      span.log(logs)

      println(helloStr)
      span.log(Map("event" -> "println").asJava)

      span
    }.map(_.finish())
  }

  def main(args: Array[String]): Unit = {
    //    if (args.length != 1) {
    //      throw new IllegalArgumentException("Expecting one argument");
    //    }

    val helloTo: String = "Hello" //args[0].toString()
    if (TracerObject.tracer.isDefined)
      sayHello(helloTo, TracerObject.tracer.get)

    TracerObject.terminate
  }
}