package dev.at.mikorn.com.services;

import io.vertx.ext.web.RoutingContext;

/**
 * Author Mikorn vietnam
 * Created on 04-Sep-18.
 */

public interface KafkaService {
    void handleKafkaClientApi(RoutingContext routingContext);
}
