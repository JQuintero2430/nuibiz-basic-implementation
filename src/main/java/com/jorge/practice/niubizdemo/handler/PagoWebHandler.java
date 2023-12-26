package com.jorge.practice.niubizdemo.handler;

import com.jorge.practice.niubizdemo.service.PagoWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class PagoWebHandler {
    private final PagoWebService pagoWebService;

    @Autowired
    public PagoWebHandler(PagoWebService pagoWebService) {
        this.pagoWebService = pagoWebService;
    }

    public Mono<ServerResponse> home(ServerRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Home Screen");

        return ServerResponse.ok().contentType(MediaType.TEXT_HTML)
                .render("index", model);
    }

    public Mono<ServerResponse> handleSubmit(ServerRequest request) {
        return request.formData()
                .flatMap(formData -> {
                    String username = formData.getFirst("username");
                    String password = formData.getFirst("password");
                    Long merchantId = Long.parseLong(Objects.requireNonNull(formData.getFirst("merchantId")));
                    double amount = Double.parseDouble(Objects.requireNonNull(formData.getFirst("amount")));

                    return pagoWebService.createAuthenticationToken(username, password)
                            .flatMap(authenticationToken -> pagoWebService.createSessionToken(authenticationToken, merchantId, amount)
                                    .flatMap(sessionToken -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(sessionToken)
                                            .onErrorResume(e -> Mono.error(new Exception("Error: " + e.getMessage())))));
                });
    }
}
