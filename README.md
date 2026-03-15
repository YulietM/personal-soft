# Prueba Técnica Backend - BTG Pactual

Este proyecto es una API REST construida en **Spring Boot 3+ (Java 21)** y **MongoDB**, cuyo objetivo es gestionar las suscripciones de clientes a fondos de inversión.

## 🚀 Tecnologías y Herramientas

- **Lenguaje:** Java 21
- **Framework:** Spring Boot (Web, Data MongoDB, Validation)
- **Base de Datos:** MongoDB
- **Testing:** JUnit 5, Mockito (Enfoque BDD: Given, When, Then)
- **Contenedores:** Docker & Docker Compose
- **Documentación API:** Swagger UI (OpenAPI 3)
- **Lombok:** Para reducir el código boilerplate.

## 🏗️ Arquitectura y Patrones Aplicados

El proyecto sigue una estructura inspirada en **Clean Architecture**:
- `controllers`: Exposición de endpoints REST.
- `services`: Lógica de negocio (Casos de uso). Se implementaron validaciones de saldo y montos mínimos.
- `services/notificaciones`: Implementación del patrón de diseño **Strategy**. Permite decidir en tiempo de ejecución si se envía una notificación por `EMAIL` o `SMS` dependiendo de las preferencias de cada cliente.
- `models`: Mapeo directo de colecciones de MongoDB (`clientes`, `fondos`, `transacciones`). Usamos subdocumentos (`suscripcionesActivas` embebidas en `Cliente`) para optimizar el rendimiento de la lectura del saldo y fondos de un cliente sin requerir *joins*.
- `repositories`: Interfaces con Spring Data (MongoRepository).
- `dtos`: Objetos inmutables de transferencia de datos con validaciones `Jakarta Validation`.
- `exceptions`: Manejador centralizado (`@RestControllerAdvice`) para mapear errores de negocio personalizados a las convenciones estándar (HTTP 400 y 404).

## ⚙️ Instrucciones para Configurar y Ejecutar Localmente

### 1. Requisitos Previos
- Docker y Docker Compose instalados.
- Java 21 (si deseas ejecutarlo fuera de Docker).

### 2. Levantar la Aplicación
El proyecto incluye un `docker-compose.yml` que levanta tanto MongoDB como la aplicación.

Abre una terminal en la raíz y ejecuta:
```bash
docker-compose up --build
```
> **Nota:** La aplicación insertará automáticamente un usuario de pruebas con $500.000 COP y los 5 fondos base requeridos mediante la clase **`DataSeeder`**.
>
> ⚠️ **IMPORTANTE:** El componente `DataSeeder` de Spring Boot se ejecuta automáticamente al arrancar la aplicación (`CommandLineRunner`), pero **está restringido exclusivamente al entorno de desarrollo**. Usa la anotación `@Profile("dev")` para garantizar que nunca inicialice o sobreescriba datos en un entorno productivo.

Si deseas visualizar en vivo los cambios desde tu IDE, puedes levantar **sólo** la BD y luego tu aplicación en tu IDE usando el perfil `dev`:
```bash
docker-compose up -d mongodb
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## 📖 Documentación de Endpoints (Swagger)

Una vez la aplicación esté corriendo (puerto `8080`), puedes acceder a la interfaz de Swagger para probar los endpoints:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Resumen de Endpoints Principales
- `GET /api/v1/fondos` - Lista el catálogo de fondos.
- `POST /api/v1/fondos/suscripciones` - Suscribe a un cliente (`clienteId = c1` por defecto).
- `DELETE /api/v1/fondos/{fondoId}/suscripciones?clienteId={clienteId}` - Cancela la suscripción.
- `GET /api/v1/transacciones/cliente/{clienteId}` - Historial de aperturas y cancelaciones.

---

## 💻 Pruebas con cURL (Terminal)

Para facilitar las pruebas manuales sin Swagger, usa estos comandos asumiendo que el `DataSeeder` creó el cliente con ID **`c1`** (Usuario de Prueba) y los fondos del ID **`1`** al **`5`**.

### 1. Listar el catálogo de fondos
```bash
curl -X GET "http://localhost:8080/api/v1/fondos" \
     -H "Accept: application/json"
```

### 2. Crear un nuevo cliente (Regla: Inicia con saldo >= $500.000)
Si deseas crear un cliente propio en lugar de usar el de prueba (`c1`), ejecuta el siguiente POST.
Asegúrate de mandar el `saldo` con un valor mayor o igual a 500000.
```bash
curl -X POST "http://localhost:8080/api/v1/clientes" \
     -H "Content-Type: application/json" \
     -d '{
       "nombre": "Sofia G.",
       "email": "sofia@example.com",
       "celular": "3015555555",
       "saldo": 500000,
       "preferenciaNotificacion": "SMS"
     }'
```
*(Copia el `id` que te devuelve la respuesta para usarlo como `clienteId` en los siguientes comandos)*

### 3. Verificar el saldo inicial del cliente
```bash
# Cambia "c1" por el id que copiaste en el paso interior si creaste uno nuevo
curl -X GET "http://localhost:8080/api/v1/clientes/c1" \
     -H "Accept: application/json"
```

### 4. Suscribirse a un Fondo
Ejemplo: El cliente invierte **$50.000 COP** en el fondo DEUDAPRIVADA (ID: `3`).
```bash
curl -X POST "http://localhost:8080/api/v1/fondos/suscripciones" \
     -H "Content-Type: application/json" \
     -d '{
       "clienteId": "c1",
       "fondoId": "3",
       "monto": 50000
     }'
```

### 5. Consultar el nuevo saldo del cliente
Puedes volver a correr el paso 3 y notar que el saldo debe haber bajado de $500.000 a $450.000, además de ver que ese fondo activo entró a su arreglo interno de `suscripcionesActivas`.

### 5. Ver el Historial de Transacciones (del cliente)
Verificamos que haya quedado el registro de APERTURA:
```bash
curl -X GET "http://localhost:8080/api/v1/transacciones/cliente/c1" \
     -H "Accept: application/json"
```

### 6. Cancelar Suscripción a un Fondo (Devolución)
Ejemplo: El cliente `c1` cancela su fondo DEUDAPRIVADA (ID: `3`):
```bash
curl -X DELETE "http://localhost:8080/api/v1/fondos/3/suscripciones?clienteId=c1"
```
*Si verificas el saldo después de este paso, los $50.000 habrán regresado a la cuenta, y si verificas las transacciones, existirá un registro de CANCELACION.*

---

## 🧪 Pruebas Unitarias

Se incluyeron pruebas unitarias para la validación crítica de negocio en `FondoServiceTest`, validando los rechazos por falta de saldo, cancelación, y débito correcto. Para ejecutarlas:

```bash
./mvnw test
```

## ☁️ Despliegue en AWS (CloudFormation)

El archivo `aws-cloudformation.yaml` incluye la plantilla básica (IAC) con los siguientes recursos sugeridos para AWS:
- un Cluster en ECS (Fargate).
- un Repositorio ECR.
- Configuración de red para desplegar los contenedores sin gestionar servidores.
