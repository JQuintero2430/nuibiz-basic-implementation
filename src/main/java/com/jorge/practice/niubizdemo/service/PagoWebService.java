package com.jorge.practice.niubizdemo.service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PagoWebService {
    public Mono<String> createAuthenticationToken(String username, String password);
    public Mono<String> createSessionToken(String authenticationToken, Long merchantId, double amount);
}
