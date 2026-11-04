# Proyecto didáctico: arquitectura hexagonal

Aplicación de gestión de usuarios construida con Java 21 y Spring Boot. Sirve como ejemplo para estudiar dominio, casos de uso, puertos y adaptadores con una interfaz web Thymeleaf y persistencia H2/JPA.

La interfaz permite crear, consultar, listar, editar y eliminar usuarios.

## Requisitos

- JDK 21
- Visual Studio Code con Extension Pack for Java y soporte para Spring Boot (opcional)

## Ejecutar

Desde la carpeta del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

Después abre `http://localhost:8080/`. En VS Code también puedes ejecutar `HexagonalApplication`; la configuración del proyecto abre el navegador cuando Spring Boot inicia.

## Pruebas

```powershell
.\mvnw.cmd clean test
```

La base H2 está configurada en memoria: sus datos se pierden al detener la aplicación.

## Documentación

Consulta [DocumentacionHexagonal.Md](DocumentacionHexagonal.Md) para los pasos de creación con VS Code y Spring Initializr, versiones y dependencias, configuración de base de datos, explicación de capas y recorrido de los casos de uso.
