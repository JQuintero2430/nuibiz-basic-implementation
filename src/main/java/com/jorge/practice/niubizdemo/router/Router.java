package com.jorge.practice.niubizdemo.router;

import com.jorge.practice.niubizdemo.handler.PagoWebHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {
    @Bean
    public RouterFunction<ServerResponse> route(PagoWebHandler handler) {
        return RouterFunctions
                .route(GET("/"), handler::home)
                .andRoute(GET("/home"), handler::home)
                .andRoute(POST("/submit"), handler::handleSubmit);

    }
}
