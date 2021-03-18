package coma.silver.camel.sample.pods;

import io.fabric8.kubernetes.api.model.Pod;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import java.util.List;

public class PodService {

    public static List<Pod> podList(Exchange exchange) {
        return  (List<Pod>) exchange.getIn().getBody();
    }
}
