package com.marco.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authorization-service")
public interface AuthorizationServiceClient {
    
    @GetMapping("/api/authorization/check/{serviceName}")
    boolean checkAuthorization(@PathVariable("serviceName") String serviceName);
}