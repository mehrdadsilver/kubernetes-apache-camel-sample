package coma.silver.camel.sample.pods.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CRDRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        rest("/api/").description("List CRDs rest api")
                .id("api-allcrds")
                .get("/allcrds")
                .produces(APPLICATION_JSON_VALUE)
                .bindingMode(RestBindingMode.auto)
                .enableCORS(true)
                .outType(List.class)
                .to("direct:allcrds");

        from("direct:allcrds").routeId("direct-allcrds")
                .toF("kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=listCustomResources&crdName=CronTab")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
    }
}
