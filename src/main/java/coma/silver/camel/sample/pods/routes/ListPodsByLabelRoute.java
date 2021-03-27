package coma.silver.camel.sample.pods.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kubernetes.KubernetesConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class ListPodsByLabelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        rest("/api/").description("List my pods rest api")
                .id("api-mypods")
                .get("/mypods")
                .produces(APPLICATION_JSON_VALUE)
                .bindingMode(RestBindingMode.auto)
                .enableCORS(true)
                .outType(List.class)
                .to("direct:mypods");

        from("direct:mypods").routeId("direct-mypods")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Map<String, String> labels = new HashMap<>();
                        labels.put("run", "nginx");
                        exchange.getIn().setHeader(KubernetesConstants.KUBERNETES_PODS_LABELS, labels);
                    }
                })
                .toF("kubernetes-pods:///?kubernetesClient=#kubernetesClient&operation=listPodsByLabels")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
    }
}
