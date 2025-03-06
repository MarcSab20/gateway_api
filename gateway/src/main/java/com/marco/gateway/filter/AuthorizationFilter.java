package com.marco.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
    
    private final WebClient webClient;
    
    
    public AuthorizationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.baseUrl("http://authorization-service").build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Extraire le nom du service à partir de l'URI
            String path = request.getURI().getPath();
            String serviceName = extractServiceName(path);

            // Appeler le service d'autorisation
            return webClient
                .get()
                .uri("http://authorization-service/api/authorization/check/" + serviceName)
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(isAuthorized -> {
                    if (isAuthorized) {
                        return chain.filter(exchange);
                    } else {
                        // Retourner une erreur 403 si non autorisé
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        return response.setComplete();
                    }
                });
        };
    }

    private String extractServiceName(String path) {
        // si le mot qui vient après  /api/ est le nom du service
        String[] segments = path.split("/");
        for (int i = 0; i < segments.length; i++) {
            if ("api".equals(segments[i]) && i + 1 < segments.length) {
                return segments[i + 1];
            }
        }
        return "";
    }

    public static class Config {
        // pour plus tard
    }
}
