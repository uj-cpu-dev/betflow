package com.services.api_gateway.config;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions.route()
                .GET("/api/auth/**", HandlerFunctions.http())
                .POST("/api/auth/**", HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions.route()
                .GET("/api/users/**", HandlerFunctions.http())
                .POST("/api/users/**", HandlerFunctions.http())
                .PUT("/api/users/**", HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://localhost:8081"))
                .before(request -> {
                    String auth = request.headers().firstHeader("Authorization");
                    if (auth != null) {
                        return ServerRequest.from(request)
                                .header("Authorization", auth)
                                .build();
                    }
                    return request;
                })
                .build();
    }
}
