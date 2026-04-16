package com.equipo01.sportpulse.ms_gateway.controller;

import com.equipo01.sportpulse.ms_gateway.dto.SystemHealthResponse;
import com.equipo01.sportpulse.ms_gateway.service.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {

    private final HealthCheckService healthCheckService;

    // Aquí "contratamos" a nuestro cocinero en el camarero
    public HealthController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    // Le decimos a Spring: Cuando alguien entre a "http://localhost:8080/health" entraremos a esta función
    @GetMapping("/health")
    public Mono<SystemHealthResponse> getSystemHealth() {
        return healthCheckService.checkAllServicesHealth();
    }
}
