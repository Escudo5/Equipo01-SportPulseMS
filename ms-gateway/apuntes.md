# 📚 Apuntes: Java, Spring Boot y Web Backend

Bienvenido al mundo del backend. Aquí tienes explicados los conceptos clave de forma sencilla.

## 1. Java: El Lenguaje
A diferencia de lenguajes como JavaScript o Python, Java es **Estricto (Tipado Estático)**.
- **Tipado estático**: Significa que si creas una variable para guardar un texto (`String`), nunca podrás guardar ahí un número (`int`). Tienes que decirle a Java qué es cada cosa desde el principio.
- **Orientado a Objetos**: Todo en Java es una "Clase" (el plano de un edificio) y un "Objeto" (el edificio ya construido). Piensa en las Clases como moldes.
- **Compilado**: Antes de ejecutar el código, Java lo traduce (compila) a un idioma que la computadora entiende. Si hay un error de escritura, falla antes de arrancar, ¡lo cual evita muchos errores en vivo!

## 2. Spring Boot: La Magia de Configuración
Hacer una web "a mano" en Java requeriría miles de líneas de configuración. Spring Boot es un "Framework" (un entorno de trabajo) que te da la casa ya construida para que solo la decores.
- **Anotaciones (`@`)**: Verás muchos símbolos como `@RestController` o `@Service`. Son como "etiquetas mágicas". Al ponerlas encima de tu código, le estás dando órdenes a Spring (ej: *"Oye Spring, esta parte del código va a encargarse de recibir peticiones web"*).
- **Arquitectura de Capas**: En Spring, no mezclamos todo el código en un solo archivo. Lo separamos como en un restaurante:
    1. **Controllers (Controladores / Los Camareros)**: Reciben tu petición en internet (como pedir el menú) y te devuelven la respuesta.
    2. **Services (Servicios / Los Cocineros)**: Es donde hacemos los cálculos, la lógica compleja y tomamos las decisiones.
    3. **Repositorios (La Despensa)**: Se conectan a la base de datos (por ahora no usaremos esto).

## 3. Básicos de la Web
- **Petición HTTP**: Cuando entras a una web, tu navegador hace una petición a un ordenador lejano (servidor). El tipo más común de petición es el **`GET`** (que usamos para "pedir" o "leer" información).
- **JSON**: Es el idioma universal en el que se pasan los datos por internet hoy en día. Es solo texto estructurado con llaves. 
  Ejemplo: `{"servicio": "auth", "estado": "UP"}`.

## 4. ¿Qué es la Capa de Datos (DTO)?
DTO significa **"Data Transfer Object"** (Objeto de Transferencia de Datos).
Imagina que es como un **tupper**. Tu código (`Gateway`) hace cálculos complejos para saber la salud de tu sistema, pero cuando necesita enviar esa información al usuario por internet, tú no le mandas tus cálculos, archivos y cables.
**Creas un tupper (DTO)**, metes únicamente los datos finales que le importan al administrador (`nombre_servicio` y `estado_salud`), le pones la tapa y se lo envías. ¡Es solo una caja tonta para transportar información de forma limpia!

Al final, Spring Boot coge ese "tupper" (DTO) en Java y lo convierte automáticamente en texto `JSON` para enviarlo por internet.

## 5. ¿Qué es un "Healthcheck" (Endpoint de Salud)?
En un sistema grande, la aplicación se divide en pequeños programas independientes (Microservicios). A veces uno de estos programitas se cuelga.
- **El concepto**: Un "Healthcheck" es crear una ruta web (ej. `http://miserver.com/health`) a la que un administrador o un programa automático pueda llamar periódicamente. Este debe responder rápidamente *"¡Estoy vivo!"* (`UP`) o *"Fallo"* (`DOWN`).
- **El Estándar (Actuator)**: En el mundo de Spring Boot, el estándar de la industria es usar una herramienta nativa llamada **Actuator**. Esta herramienta añade automáticamente la ruta `/actuator/health` a todos los microservicios sin que tú programes nada. 
- **¿Qué haremos?**: Nuestro `ms-gateway` le mandará peticiones "ping" a las rutas `/actuator/health` de los demás microservicios en segundo plano. Si contestan con un HTTP 200, sabemos que están `UP`. Si no contestan a tiempo, lo marcaremos como `DOWN`.