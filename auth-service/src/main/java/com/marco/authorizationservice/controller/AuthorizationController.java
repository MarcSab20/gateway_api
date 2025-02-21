package com.marco.authorizationservice.controller;

import com.marco.authorizationservice.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authorization")
@Tag(name = "Service d'Autorisation", description = "API pour gérer les autorisations des services")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Operation(
        summary = "Vérifier l'autorisation d'un service",
        description = "Vérifie si un service spécifique est autorisé à communiquer"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Retourne true si le service est autorisé, false sinon"
    )
    @GetMapping("/check/{serviceName}")
    public ResponseEntity<Boolean> isServiceAuthorized(
        @Parameter(description = "nom du service à vérifier") 
        @PathVariable String serviceName
    ) {
        return ResponseEntity.ok(authorizationService.isServiceAuthorized(serviceName));
    }

    @Operation(
        summary = "Activer un service",
        description = "Active un service spécifique pour permettre la communication"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service activé avec succès"
    )
    @PostMapping("/enable/{serviceName}")
    public ResponseEntity<String> enableService(
        @Parameter(description = "nom du service à activer") 
        @PathVariable String serviceName
    ) {
        authorizationService.enableService(serviceName);
        return ResponseEntity.ok("Service " + serviceName + " activé avec succès");
    }

    @Operation(
        summary = "Désactiver un service",
        description = "Désactive un service spécifique pour bloquer la communication"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service désactivé avec succès"
    )
    @PostMapping("/disable/{serviceName}")
    public ResponseEntity<String> disableService(
        @Parameter(description = "nom du service à désactiver") 
        @PathVariable String serviceName
    ) {
        authorizationService.disableService(serviceName);
        return ResponseEntity.ok("Service " + serviceName + " désactivé avec succès");
    }

    @Operation(
        summary = "Basculer l'état d'autorisation d'un service",
        description = "Inverse l'état d'autorisation actuel du service"
    )
    @ApiResponse(
        responseCode = "200",
        description = "État du service basculé avec succès"
    )
    @PostMapping("/toggle/{serviceName}")
    public ResponseEntity<String> toggleService(
        @Parameter(description = "nom du service à basculer") 
        @PathVariable String serviceName
    ) {
        boolean newState = authorizationService.toggleServiceAuthorization(serviceName);
        String status = newState ? "activé" : "désactivé";
        return ResponseEntity.ok("Service " + serviceName + " " + status + " avec succès");
    }
}