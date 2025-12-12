package com.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class ApiGatewayRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {

        RouterFunction<ServerResponse> orderRoute = route("order-service-route")
                        .GET("/api/order/**", http())
                        .POST("/api/order/**", http())
                        .filter(rewritePath("/api/order/(?<segment>.*)", "/order/${segment}"))
                        .filter(lb("SPRING-ORDER-SERVICE"))
                        .build();

        RouterFunction<ServerResponse> paymentRoute = route("payment-service-route")
                        .GET("/api/payment/**", http())
                        .POST("/api/payment/**", http())
                        .filter(rewritePath("/api/payment/(?<segment>.*)", "/payment/${segment}"))
                        .filter(lb("SPRING-PAYMENT-SERVICE"))
                        .build();

         return orderRoute.and(paymentRoute);
    }
}
