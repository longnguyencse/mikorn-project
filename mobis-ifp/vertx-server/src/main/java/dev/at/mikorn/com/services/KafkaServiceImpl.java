package dev.at.mikorn.com.services;

import dev.at.mikorn.kafka.client.KafkaClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author Mikorn vietnam
 * Created on 04-Sep-18.
 */

public class KafkaServiceImpl extends AbstractVerticle implements KafkaService {
    private Logger logger = LogManager.getLogger(KafkaServiceImpl.class);

    @Override
    public void handleKafkaClientApi(RoutingContext routingContext) {
        // call repository
        // rxjava
        logger.info("Call repository!");
        logger.info(routingContext.getBodyAsJson());
        String result = routingContext.getBodyAsString();
        KafkaClient.runProducer(result);
        routingContext.response().end("Success");
    }
}
