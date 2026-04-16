package com.equipo01.sportpulse.ms_gateway.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SystemHealthResponse(
    LocalDateTime timestamp,
    List<ServiceHealthStatus> services
) {
}
