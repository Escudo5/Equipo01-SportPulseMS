<div align="center">

# ⚽SportPulse

<br/>

<img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
<img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
<img src="https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />

<br/><br/>

</div>

---

## 📋 Descripción del Proyecto

**SportPulse** es una plataforma de análisis de fútbol en tiempo real construida con arquitectura de **microservicios**. Consume datos de la API externa [API-Football (RapidAPI)](https://rapidapi.com/api-sports/api/api-football) para ofrecer información actualizada sobre ligas, equipos, partidos, clasificaciones y más.

El sistema está compuesto por **8 microservicios independientes** que se comunican entre sí, todos protegidos mediante autenticación **JWT** y desplegables con **Docker Compose**.

> **Equipo:** equipo-01 &nbsp;|&nbsp; **Sprint:** 2 semanas &nbsp;|&nbsp; **API externa:** API-Football v3

---

## 🧩 Microservicios

| # | Microservicio | Puerto | Responsabilidad |
|---|--------------|--------|-----------------|
| 1 | 🔵 `ms-gateway` | `8080` | Punto de entrada único, enrutamiento y rate limiting (60 req/min por IP) |
| 2 | 🟣 `ms-auth` | `8081` | Registro, login y emisión/validación de tokens JWT |
| 3 | 🟢 `ms-leagues` | `8082` | Ligas de fútbol, países y temporadas |
| 4 | 🔴 `ms-teams` | `8083` | Equipos, escudos e información de estadios |
| 5 | 🟡 `ms-fixtures` | `8085` | Partidos, calendarios, resultados y eventos en vivo |
| 6 | 🔵 `ms-standings` | `8086` | Clasificaciones por liga y temporada |
| 7 | 🔔 `ms-notifications` | `8088` | Suscripciones y alertas de eventos vía webhook |
| 8 | 🟤 `ms-dashboard` | `8089` | Resumen ejecutivo agregado del día |

### Comunicación entre servicios

```
ms-gateway        → enruta hacia todos los servicios
ms-dashboard      → consume: ms-fixtures, ms-standings, ms-leagues
ms-standings      → consume: ms-teams
ms-fixtures       → consume: ms-teams
ms-notifications  → consume: ms-fixtures
Todos             → validan JWT emitido por ms-auth
```

---

<div align="center">

**⚽ equipo-01 — SportPulse**

</div>
