# Sistema de Gestión - Asociación (Base de Datos 1)

Este proyecto consiste en el diseño, implementación y desarrollo de una base de datos relacional junto con una interfaz gráfica en Java para la gestión de miembros, membresías, pagos y contratos de una asociación.

##  Estructura del Repositorio

* **`database/`**: Scripts SQL divididos en DDL (tablas y vistas) y lógica ejecutable (funciones y triggers).
* **`App/`**: Código fuente de la aplicación Swing/Java con gestión de dependencias vía Maven.
* **`docs/`**: Documentación técnica, asignaciones e informes.
* **`Capturas de pantalla/`**: Evidencias del funcionamiento de la interfaz.

##  Requisitos Previos

- Java JDK 17 o superior
- Apache Maven
- PostgreSQL / MySQL

##  Instalación y Configuración

1. **Base de Datos:**
   - Ejecutar los scripts en el siguiente orden:
     1. `database/ddl/` (Creación de tablas y vistas)
     2. `database/programmability/functions/` (Funciones SQL)
     3. `database/programmability/triggers/` (Triggers y lógica de auditoría)

2. **Aplicación Java:**
   - Navegar a la carpeta `App/`
   - Compilar y ejecutar con Maven:
     ```bash
     mvn clean install
     mvn exec:java
     ```
