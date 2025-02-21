package com.marco.authorizationservice.service;

import com.marco.authorizationservice.model.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    @Autowired
    private DiscoveryClient discoveryClient;

    private final Map<String, Boolean> authorizedServices = new ConcurrentHashMap<>();

    public boolean isServiceAuthorized(String serviceName) {
        String normalizedServiceName = serviceName.toUpperCase();
        List<String> registeredServices = discoveryClient.getServices();
        
        // Convertir tous les services enregistrés en majuscules pour la comparaison
        List<String> upperRegisteredServices = registeredServices.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    
        if (!upperRegisteredServices.contains(normalizedServiceName)) {
            System.out.println("🔴 Service NON trouvé dans Eureka : " + serviceName);
            return false;
        }

        // Vérifier si le service est explicitement désactivé
        Boolean isAuthorized = authorizedServices.get(normalizedServiceName);
        if (isAuthorized != null && !isAuthorized) {
            System.out.println("🔴 Service désactivé manuellement : " + serviceName);
            return false;
        }
    
        // Utiliser le nom de service original pour récupérer les instances
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        System.out.println("✅ Instances trouvées pour " + serviceName + " : " + instances);
    
        return !instances.isEmpty();
    }

    public boolean toggleServiceAuthorization(String serviceName) {
        return authorizedServices.compute(serviceName.toUpperCase(), (key, value) -> {
            if (value == null) {
                
                return false;
            }
            
            return !value;
        });
    }

    public boolean enableService(String serviceName) {
        return authorizedServices.put(serviceName.toUpperCase(), true);
    }
    
    public boolean disableService(String serviceName) {
        return authorizedServices.put(serviceName.toUpperCase(), false);
    }

    public List<ServiceInfo> getAllServices() {
        List<ServiceInfo> services = new ArrayList<>();
        List<String> serviceIds = discoveryClient.getServices();
        
        for (String serviceId : serviceIds) {
            if (!serviceId.equalsIgnoreCase("authorization-service")) {
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceId(serviceId);
                
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
                boolean isUp = !instances.isEmpty();
                
                serviceInfo.setStatus(isUp ? "UP" : "DOWN");
                serviceInfo.setAuthorized(isServiceAuthorized(serviceId));
                
                if (isUp) {
                    ServiceInstance instance = instances.get(0);
                    serviceInfo.setInstanceId(instance.getInstanceId());
                    serviceInfo.setUrl(instance.getUri().toString());
                }
                
                services.add(serviceInfo);
            }
        }
        return services;
    }
}