package com.equipo01.sportpulse.ms_gateway.service;

import com.equipo01.sportpulse.ms_gateway.dto.ServiceHealthStatus;
import com.equipo01.sportpulse.ms_gateway.dto.SystemHealthResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class HealthCheckService {

    // Nuestro teléfono hacia el exterior
    private final WebClient webClient;
    
    // Nuestro glosario de servicios basados en el application.yml
    private final Map<String, String> services = Map.of(
            "auth", "http://localhost:8081",
            "leagues", "http://localhost:8082",
            "teams", "http://localhost:8083",
            "fixtures", "http://localhost:8085",
            "standings", "http://localhost:8086",
            "notifications", "http://localhost:8088",
            "dashboard", "http://localhost:8089"
    );

    // Spring Boot nos inyecta el WebClient.Builder que habíamos configurado antes.
    public HealthCheckService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<SystemHealthResponse> checkAllServicesHealth() {
        // Flux.fromIterable: Agarra la lista de servicios y empieza a llamar de manera simultánea
        return Flux.fromIterable(services.entrySet())
                // flatMap: Permite ejecutar tareas asíncronas simultáneamente (Llama a todos a la vez)
                .flatMap(entrySet -> checkService(entrySet.getKey(), entrySet.getValue()))
                // collectList: Cuando todos terminen de responder, agrúpalos en una lista
                .collectList()
                // map: Transforma esa lista al DTO principal "SystemHealthResponse", añadiéndole la fecha actual
                .map(statusList -> new SystemHealthResponse(LocalDateTime.now(), statusList));
    }

    private Mono<ServiceHealthStatus> checkService(String serviceName, String baseUrl) {
        // Petición GET a "http://localhost:PUERTO/actuator/health"
        return webClient.get()
                .uri(baseUrl + "/actuator/health")
                .retrieve()
                .toBodilessEntity() // Solo queremos ver que devuelva 2xx OK. Ignoramos qué texto responde.
                .map(response -> new ServiceHealthStatus(serviceName, "UP")) // Si el ping fue bien, creamos un tupper con estado UP
                .onErrorResume(error -> Mono.just(new ServiceHealthStatus(serviceName, "DOWN"))); // Si dio time-out o un 404/5xx, devolvemos un tupper con estado DOWN sin romper la aplicación completa
    }
}
