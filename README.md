# TuCasaca - E-commerce de Camisetas de Fútbol

## Descripción del Proyecto y Alcance
TuCasaca es una plataforma e-commerce orientada a la comercialización de camisetas de fútbol de diversos clubes y ligas. El sistema provee una API REST robusta que centraliza la administración del catálogo de productos (clasificados por equipo, liga, año, jugador y talle) y la gestión de usuarios registrados. Su alcance abarca la persistencia relacional de datos, validaciones de negocio, filtrado y búsqueda de indumentaria deportiva, y la exposición de servicios web para su consumo e integración con interfaces cliente.

---

## Aspectos Funcionales

- **Gestión de Catálogo de Camisetas:**
  - Consulta general de camisetas disponibles con información detallada (precio, talle, año, dorsal, jugador, club y liga).
  - Búsqueda y filtrado específico por identificador (`id`), por nombre de equipo y por liga (tanto por ID como por nombre).
  - Alta, modificación y baja lógica/física de camisetas para la gestión de inventario.
- **Gestión de Usuarios:**
  - Registro de nuevos usuarios con información de perfil (nombre, apellido, correo electrónico, fecha de nacimiento, sexo y rol asignado).
  - Consulta y listado de usuarios del sistema.
  - Validación de unicidad de correo electrónico y restricciones de acceso.
- **Gestión del Carrito de Compras:**
  - Creación automática de carrito activo por usuario al realizar la primera consulta.
  - Agregado de ítems con validación de stock disponible y cálculo de subtotales.
  - Eliminación de ítems individuales y vaciado completo del carrito.
  - Proceso de checkout: cierre del carrito y descuento automático de stock por ítem comprado.
- **Poblado Inicial de Datos (Data Seeding):**
  - Carga automática de datos de prueba (`DataInitializer`) con ligas, equipos y camisetas de muestra al iniciar la aplicación en caso de encontrarse vacía.

---

## Aspectos Técnicos y Arquitectura

- **Lenguaje y Entorno de Ejecución:** Java 17.
- **Framework Principal:** Spring Boot 4.x / Spring Framework (Spring Web MVC, Spring Data JPA).
- **Herramienta de Construcción y Dependencias:** Apache Maven.
- **Persistencia y Base de Datos:**
  - Hibernate / JPA como ORM para el mapeo objeto-relacional.
  - Soporte para **MySQL** (base de datos relacional para desarrollo/producción) y **H2 Database** (para pruebas en memoria).
- **Herramientas de Productividad:** Project Lombok para simplificación de código repetitivo (getters, setters, constructores).
- **Manejo Centralizado de Excepciones:** Controlador global (`GlobalExceptionHandler`) para capturar y estandarizar respuestas de error HTTP (`ResourceNotFoundException`, `PrecioNegativoException`, etc.).
- **Patrón de Diseño y Arquitectura por Capas:**
  - **Controller:** Exposición de endpoints RESTful y manejo de solicitudes/respuestas HTTP.
  - **Service:** Implementación de la lógica de negocio y reglas de validación.
  - **Repository:** Interfaces basadas en Spring Data JPA para el acceso a datos.
  - **DTO (Data Transfer Object) y Mappers:** Desacoplamiento entre entidades de dominio y modelos expuestos al cliente.

---

## Endpoints de la API

### Camisetas (`/api/casacas`)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/casacas` | Obtiene el listado completo de camisetas activas. |
| `GET` | `/api/casacas/{id}` | Obtiene el detalle de una camiseta por su ID. |
| `GET` | `/api/casacas/equipo/{equipo}` | Filtra camisetas por el nombre del equipo. |
| `GET` | `/api/casacas/liga/{ligaId}` | Filtra camisetas por el ID de la liga. |
| `GET` | `/api/casacas/liga/nombre/{nombre}` | Filtra camisetas por el nombre de la liga. |
| `POST` | `/api/casacas` | Registra una nueva camiseta en el catálogo. |
| `PUT` | `/api/casacas/{id}` | Actualiza la información de una camiseta existente. |
| `DELETE` | `/api/casacas/{id}` | Elimina una camiseta del catálogo por su ID. |

### Usuarios (`/api/usuarios`)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/usuarios/registro` | Registra un nuevo usuario en la plataforma. |
| `GET` | `/api/usuarios` | Lista todos los usuarios registrados. |

### Carrito de Compras (`/api/carritos`)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/carritos/{usuarioId}` | Obtiene el carrito activo del usuario (lo crea si no existe). |
| `POST` | `/api/carritos/{usuarioId}/items` | Agrega un ítem al carrito con validación de stock. |
| `DELETE` | `/api/carritos/{usuarioId}/items/{itemId}` | Elimina un ítem específico del carrito. |
| `DELETE` | `/api/carritos/{usuarioId}/vaciar` | Elimina todos los ítems del carrito. |
| `POST` | `/api/carritos/{usuarioId}/checkout` | Confirma la compra, cierra el carrito y descuenta el stock. |

---

## Requisitos y Puesta en Marcha

### Prerrequisitos
- JDK 17 o superior instalado.
- Maven 3.8+ (o utilizar el wrapper incluido `./mvnw`).
- Servidor MySQL activo con una base de datos disponible (por defecto configurado para crear `ecommerce_db3`).

### Configuración
Ajustar los parámetros de conexión en `src/main/resources/application.properties` si las credenciales de base de datos difieren de las predeterminadas:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db3?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### Ejecución
Para iniciar el servidor de desarrollo, ejecutar en la raíz del proyecto:
```bash
# Con Maven Wrapper (Windows PowerShell / CMD)
.\mvnw.cmd spring-boot:run

# Con Maven Wrapper (Linux / macOS)
./mvnw spring-boot:run

# O utilizando Maven directamente
mvn spring-boot:run
```
La aplicación iniciará en el puerto `8080` de manera predeterminada (`http://localhost:8080`).

