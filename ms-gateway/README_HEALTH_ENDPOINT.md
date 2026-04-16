# 🏥 Guía y Arquitectura: Endpoint de Salud (HU-05)

Este documento es una guía del equipo para entender cómo se ha implementado el endpoint `/health` del Gateway, cuya función es reportar en tiempo real el estado de nuestros microservicios conectados.

---

## 🏗️ Para perfiles Senior (Decisiones Arquitectónicas)

1. **Paralelización (WebFlux Completo):** En lugar de iterar secuencialmente el pingeo de servicios con enfoques bloqueantes, se ha implementado `WebClient` aprovechando la naturaleza del Gateway. Utilizamos `Flux.fromIterable().flatMap()` para emitir un ráfaga paralela y *no-bloqueante* hacia todas las URLs asíncronamente; reduciendo las latencias a lo equivalente al microservicio más lento, no a la suma de todos.
2. **Componentes Resilientes y Timeouts Estrictos:** Debido a la criticidad del hilo principal de Netty, en `WebClientConfig` se ha sobreescrito a nivel de conector Reactor-Netty (`ChannelOption.CONNECT_TIMEOUT_MILLIS` y `Read/WriteTimeoutHandler`) limitando los bloqueos por socket al máximo de 2000 ms. Caídas de Downstreams no colgarán el Gateway.
3. **Contract Estándar (Actuator Protocol):** Para las comprobaciones aguas abajo, nuestro ping consulta explícitamente vía GET la ruta `PUERTO/actuator/health`. Cualquier respuesta 2xx mapea a "UP"; un TimeOut o status 5xx es transformado en `onErrorResume()` asegurando un state "DOWN" de alta tolerancia a fallos.

---

## 🌱 Para perfiles Junior / Mid (Mapa de Archivos)

La implementación sigue una arquitectura estructurada estándar de capas usando Java 17. 

Esta es la explicación de cada carpeta (`package`) y fichero que vais a encontraros si tenéis que tocar el código en el futuro:

### 1. `📂 dto/` (Data Transfer Objects)
Modelos de Datos "tontos" e inmutables (Aprovechamos los `record` de Java) cuya única responsabilidad es dibujar y formatear la forma que tendrá nuestro JSON en pantalla:
- **📄 `ServiceHealthStatus.java`:** La caja estructural de un solo servicio independiente (Por ejemplo: *"Nombre: Autenticacion, Estado: DOWN"*).
- **📄 `SystemHealthResponse.java`:** La respuesta que envuelve todo. Te devuelve la fecha y hora (`timestamp`) que exige la HU05 y la lista completa con todas las cajitas individuales de arriba que componen el sistema. 

### 2. `📂 config/` (Configuración Técnica)
Los "Ajustes de fábrica".
- **📄 `WebClientConfig.java`:** Es la configuración global de nuestro "teléfono" para llamar por internet a otros servidores. Básicamente le decimos a Spring que al intentar comunicarse con los puertos 8081, 8082, etc... cuelgue el teléfono si no responden pasados 2 segundos.

### 3. `📂 service/` (La lógica / El Cerebro)
Acoge las transformaciones y comprobaciones técnicas principales.
- **📄 `HealthCheckService.java`:** Contiene nuestro mapa de servidores / puertos (`auth`, `leagues`...) para poder chillarles asíncronamente: `"¡Eh, ¿Estás Vivo?!"`.
Este servicio lanza la petición concurrente e intercepta todos los errores y respuestas mapeándolas en el estado correspondiente que formatea en los DTOs de arriba.

### 4. `📂 controller/` (Los Interfaces expuestos)
Recepción de las peticiones Web a internet.
- **📄 `HealthController.java`:** Lo único que hace es escuchar en internet y "recibir a la gente" cuando introduce `http://localhost:8080/health`. Como es un "camarero", no hace los cálculos de comprobar si los sistemas y puertos están vivos, simplemente hace la llamada al Servicio de arriba para recibir los datos de vuelta para el usuario final.
