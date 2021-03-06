package coma.silver.camel.sample.pods.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class ListPodsRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        rest("/api/").description("List all pods rest api")
                .id("api-allpods")
                .get("/allpods")
                .produces(APPLICATION_JSON_VALUE)
                .bindingMode(RestBindingMode.auto)
                .enableCORS(true)
                .outType(List.class)
                .to("direct:allpods");

        from("direct:allpods").routeId("direct-allpods")
                .toF("kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=listPods")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
    }
}
