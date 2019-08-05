package lib

import java.util
import java.util.{HashMap, Iterator}

import io.jaegertracing.Configuration
import io.jaegertracing.Configuration.{ReporterConfiguration, SamplerConfiguration, SenderConfiguration}
import io.jaegertracing.internal.JaegerTracer
import io.jaegertracing.internal.samplers.ConstSampler
import io.opentracing.propagation.{Format, TextMap, TextMapExtractAdapter}
import io.opentracing.tag.Tags
import io.opentracing.{Scope, SpanContext, Tracer}
import javax.ws.rs.core.MultivaluedMap
import okhttp3.Request

import scala.util.{Success, Try}

object Tracing {

  def init(service: String): JaegerTracer = {
    val samplerConfig: SamplerConfiguration = SamplerConfiguration.fromEnv()
      .withType(ConstSampler.TYPE)
      .withParam(1);

    val reporterConfig: ReporterConfiguration = ReporterConfiguration.fromEnv()
      .withSender(SenderConfiguration.fromEnv().withAgentHost("localhost")
        .withAgentPort(6832))
      .withLogSpans(true);

    getTracer(service, samplerConfig, reporterConfig)
  }

  def init(service: String, managerHostPort: String): JaegerTracer = {
    val samplerConfig: SamplerConfiguration = SamplerConfiguration.fromEnv().withManagerHostPort(managerHostPort)
      .withType(ConstSampler.TYPE)
      .withParam(1);

    val reporterConfig: ReporterConfiguration = ReporterConfiguration.fromEnv()
      .withLogSpans(true);
    getTracer(service, samplerConfig, reporterConfig)
  }

  private def getTracer(service: String, samplerConfig: SamplerConfiguration, reporterConfig: ReporterConfiguration) = {
    val config: Configuration = new Configuration(service)
      .withSampler(samplerConfig)
      .withReporter(reporterConfig);

    config.getTracer
  }

  //  def startServerSpan(tracer: Tracer, httpHeaders: javax.ws.rs.core.HttpHeaders, operationName: String): Scope = {
  //    // format the headers for extraction
  //    val rawHeaders: MultivaluedMap[String, String] = httpHeaders.getRequestHeaders
  //    val headers = new util.HashMap[String, String]()
  //
  //    rawHeaders.keySet().forEach(key => headers.put(key, rawHeaders.get(key).get(0)))
  //
  //    val spanBuilder = Try {
  //      val parentSpanCtx: SpanContext = tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headers))
  //      if (parentSpanCtx == null)
  //        tracer.buildSpan(operationName);
  //      else
  //        tracer.buildSpan(operationName).asChildOf(parentSpanCtx);
  //
  //    } match {
  //      case Success(value) => value
  //      case _ => tracer.buildSpan(operationName)
  //    }
  //    // TODO could add more tags like http.url
  //
  //    spanBuilder.withTag(Tags.SPAN_KIND.getKey, Tags.SPAN_KIND_SERVER).startActive(true)
  //  }

}
