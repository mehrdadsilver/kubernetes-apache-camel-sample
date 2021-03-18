package coma.silver.camel.sample.pods.routes;

import coma.silver.camel.sample.pods.PodService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class ListPodsRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        restConfiguration().contextPath("/k8s")
                .port(8080)
                .host("localhost")
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest("/api/").description("Teste REST Service")
                .id("api-route")
                .get("/allpods")
                .produces(APPLICATION_JSON_VALUE)
                .bindingMode(RestBindingMode.auto)
                .enableCORS(true)
                .outType(List.class)
                .to("direct:allpods");

        from("direct:allpods").routeId("direct-route")
                .toF("kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=listPods")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

    }
}
