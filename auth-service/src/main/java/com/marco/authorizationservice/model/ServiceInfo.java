package com.marco.authorizationservice.model;

import lombok.Data;

@Data
public class ServiceInfo {
    private String serviceId;
    private String status;
    private boolean authorized;
    private String instanceId;
    private String url;
}