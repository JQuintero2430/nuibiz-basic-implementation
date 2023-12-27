package com.jorge.practice.niubizdemo.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorge.practice.niubizdemo.service.PagoWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class PagoWebServiceImpl implements PagoWebService {

    private final WebClient webClient;
    @Autowired
    public PagoWebServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<String> createAuthenticationToken(String username, String password) {
        return webClient.mutate()
.defaultHeaders(header -> header.setBasicAuth(username, password))
                .build()
                .get()
                .uri("api.security/v1/security")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.error(new Exception("Error: " + e.getMessage())));
    }

    @Override
public Mono<String> createSessionToken(String authenticationToken, Long merchantId, double amount) {

    String url = "api.ecommerce/v2/ecommerce/token/session/" + merchantId;

    Map<String, Object> body = getStringObjectMap(amount);

    return webClient.post()
            .uri(url)
            .header("Authorization", authenticationToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> Mono.error(new Exception("Error: " + e.getMessage())));
}
    @Override
    public String invokeForm(String sessionTokenJson, Long merchantId, double amount) {
        String sessionToken = extractSessionKey(sessionTokenJson);
            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Payment Gateway</title>\n" +
                    "    <script src=\"https://static-content-qas.vnforapps.com/env/qa/js/checkout.js\"></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "    function openForm() {\n" +
                    "      VisanetCheckout.configure({\n" +
                    "        sessiontoken:'" + sessionToken + "',\n" +
                    "        channel:'web',\n" +
                    "        merchantid: '" + merchantId + "',\n" +
                    "        purchasenumber:2020100901,\n" +
                    "        amount:" + amount + ",\n" +
                    "        expirationminutes:'20',\n" +
                    "        timeouturl:'about:blank',\n" +
                    "        merchantlogo:'img/comercio.png',\n" +
                    "        formbuttoncolor:'#000000',\n" +
                    "        action:'paginaRespuesta',\n" +
                    "        complete: function(params) {\n" +
                    "          alert(JSON.stringify(params));\n" +
                    "        }\n" +
                    "      });\n" +
                    "      VisanetCheckout.open();\n" +
                    "    }\n" +
                    "    openForm(); // Call openForm immediately after the page loads\n" +
                    "    </script>\n" +
                    "</body>\n" +
                    "</html>";
    }

    private static Map<String, Object> getStringObjectMap(double amount) {
        Map<String, Object> body = new HashMap<>();
        body.put("channel", "web");
        body.put("amount", amount);
        Map<String, Object> antiFraud = new HashMap<>();
        antiFraud.put("clientIp", "24.252.107.29");
        Map<String, String> merchantDefineData = new HashMap<>();
        merchantDefineData.put("MDD15", "Valor MDD 15");
        merchantDefineData.put("MDD20", "Valor MDD 20");
        merchantDefineData.put("MDD33", "Valor MDD 33");
        antiFraud.put("merchantDefineData", merchantDefineData);
        body.put("antifraud", antiFraud);
        return body;
    }

    public static String extractSessionKey(String sessionTokenJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> tokenMap = objectMapper.readValue(sessionTokenJson, new TypeReference<Map<String, String>>() {
            });
            return tokenMap.get("sessionKey");
        } catch (Exception e) {
            throw new RuntimeException("Error parsing session token JSON.", e);
        }
    }
}
