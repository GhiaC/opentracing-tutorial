package lib

import io.jaegertracing.internal.JaegerTracer

import scala.util.Try

object TracerObject {
  val tracer: Option[JaegerTracer] = Try {
    Tracing.init("hello-world", "localhost:6831")
  }.toOption

  def terminate = tracer.map(_.close())
}
