# Guía completa: arquitectura hexagonal con Spring Boot

## 1. Objetivo de esta guía

Esta guía explica cómo crear un proyecto equivalente a este ejemplo, cómo ejecutarlo y cómo leerlo desde la perspectiva de la arquitectura hexagonal. Está escrita para poder recorrerla con un grupo de estudiantes, desde las decisiones iniciales en Spring Initializr hasta el flujo de registrar, buscar, listar y eliminar usuarios.

El proyecto administra usuarios con nombre y correo. Utiliza Java, Spring Boot, Spring MVC, Thymeleaf, Spring Data JPA y una base H2. La aplicación tiene una interfaz web y organiza el código en dominio, aplicación e infraestructura.

> **Importante:** en las secciones de estructura y flujo se describe el código de este repositorio. Cuando se recomienda una mejora o una convención diferente, se identifica expresamente para no confundirla con algo que ya esté implementado.

## 2. Herramientas, conceptos y versiones

### Java, Kotlin y el JDK

- **Java** es el lenguaje de programación utilizado por este proyecto. Las clases de dominio, servicios, controladores y adaptadores están escritas en archivos `.java`.
- **Kotlin** es otro lenguaje que puede ejecutarse sobre la máquina virtual de Java (JVM). Spring Boot permite crear aplicaciones con Kotlin, pero este repositorio no lo usa. Al crear un proyecto equivalente, selecciona **Java**, no Kotlin.
- **JDK (Java Development Kit)** es el conjunto que permite compilar y ejecutar programas Java. Incluye la JVM, el compilador y otras herramientas. No basta con tener solamente un editor instalado.
- Este proyecto declara **Java 21** en `pom.xml`. Para reproducirlo, instala un JDK 21 y configura VS Code para usarlo. Una versión posterior podría funcionar, pero para la clase conviene mantener todos los equipos en Java 21.
- La versión mostrada por `java -version` es la del JDK activo en la terminal. La propiedad `<java.version>21</java.version>` de Maven le indica al proyecto la versión del lenguaje y del bytecode que debe usar.

### Spring Boot y Maven

- **Spring** es un conjunto de herramientas para construir aplicaciones Java. Incluye, entre otras cosas, inyección de dependencias y aplicaciones web.
- **Spring Boot** facilita configurar y ejecutar Spring, configura valores razonables automáticamente y administra versiones compatibles de muchas bibliotecas.
- **Maven** administra dependencias y automatiza tareas como compilar, probar y empaquetar el proyecto. El archivo `pom.xml` es su configuración.
- **Maven Wrapper** (`mvnw` y `mvnw.cmd`) permite ejecutar Maven usando la versión esperada por el proyecto sin depender de una instalación global de Maven. En Windows se usa `mvnw.cmd`.

### Versiones de este proyecto

La configuración actual declara:

| Elemento | Valor del proyecto |
|---|---|
| Lenguaje | Java |
| Versión de Java | 21 |
| Spring Boot | 4.0.9 |
| Construcción | Maven |
| Empaquetado | JAR |
| Base de datos | H2 |
| Motor de plantillas | Thymeleaf |

Al crear el proyecto, selecciona Spring Boot **4.0.9 si Spring Initializr todavía la ofrece** y Java 21. Si Initializr ya ofrece una versión posterior y no muestra 4.0.9, elige una versión estable de Spring Boot que admita Java 21. Las versiones de Spring Boot disponibles cambian con el tiempo; no elijas una versión marcada como *SNAPSHOT* o *M* (milestone) para esta introducción.

Después de generar el proyecto, verifica la etiqueta `<version>` del padre Spring Boot en `pom.xml` y la propiedad `java.version`. Las versiones compatibles de los *starters* se administran mediante Spring Boot; normalmente no hay que asignar una versión manual a cada dependencia.

## 3. Crear el proyecto desde Visual Studio Code

> **Aclaración:** los pasos son para **Visual Studio Code (VS Code)**, no para el IDE Visual Studio.

### 3.1 Instalar lo necesario

1. Instala un JDK 21.
2. Instala Visual Studio Code.
3. En VS Code instala el paquete de extensiones **Extension Pack for Java**. Incluye soporte para Java, Maven y pruebas.
4. Instala la extensión **Spring Boot Extension Pack** o, como mínimo, Spring Initializr y Spring Boot Dashboard.
5. Abre una terminal nueva y verifica:

   ```powershell
   java -version
   ```

   Debe aparecer Java 21 como versión activa.

### 3.2 Generar desde Spring Initializr en VS Code

La extensión Spring Initializr permite generar el proyecto sin salir del editor:

1. Abre la paleta con `Ctrl+Shift+P`.
2. Ejecuta **Spring Initializr: Create a Maven Project** (el nombre puede variar ligeramente según la versión de la extensión).
3. Selecciona la versión estable de Spring Boot compatible con Java 21.
4. Selecciona **Java** como lenguaje.
5. Escribe el grupo, por ejemplo `com.example`.
6. Escribe el identificador, por ejemplo `Hexagonal`.
7. Elige el empaquetado **Jar**.
8. Selecciona **Java 21**.
9. Agrega las dependencias descritas en la sección 5.
10. Selecciona una carpeta de destino y espera a que se genere y descargue el proyecto.
11. Abre la carpeta que contiene `pom.xml` en VS Code. Si VS Code pregunta si confías en los autores, acepta únicamente si reconoces el origen de los archivos.

También se puede crear un proyecto Java básico con **Java: Create Java Project**, pero para este ejemplo es preferible Spring Initializr: agrega desde el comienzo la estructura y dependencias de Spring Boot.

## 4. Crear el proyecto desde Spring Initializr en el navegador

Spring Initializr es un generador de proyectos. No es el framework ni la base de datos: produce un proyecto inicial con la configuración seleccionada.

1. Abre [https://start.spring.io](https://start.spring.io).
2. Completa las opciones:

   | Campo de Initializr | Selección recomendada para este ejemplo |
   |---|---|
   | Project | Maven |
   | Language | Java |
   | Spring Boot | 4.0.9 si está disponible; si no, una versión estable compatible con Java 21 |
   | Group | `com.example` |
   | Artifact | `Hexagonal` |
   | Name | `Hexagonal` |
   | Description | `Ejemplo didáctico de arquitectura hexagonal` |
   | Package name | `com.example.Hexagonal` para coincidir exactamente con este proyecto |
   | Packaging | Jar |
   | Java | 21 |

3. En la sección de dependencias, agrega las necesarias (consulta la sección 5).
4. Presiona **Generate** y descarga el ZIP.
5. Descomprime el ZIP en una carpeta de trabajo.
6. En VS Code abre **File > Open Folder...** y selecciona la carpeta que contiene `pom.xml`.
7. Espera a que Java y Maven importen las dependencias. La primera importación requiere conexión a Internet y puede tardar.

### ¿Por qué el nombre del paquete aparece en mayúsculas?

El proyecto existente usa paquetes como `com.example.Hexagonal`. La convención habitual de Java es que todos los componentes del paquete estén en minúsculas, por ejemplo `com.example.hexagonal`. Para recrear este proyecto sin renombrar sus clases, utiliza exactamente `com.example.Hexagonal`. En un proyecto nuevo para producción, considera el nombre en minúsculas desde el inicio.

## 5. Qué dependencias elegir y para qué sirven

En Initializr, agrega las dependencias por su nombre descriptivo. El nombre del *starter* Maven puede variar de acuerdo con la versión de Spring Boot; revisa el `pom.xml` generado.

| Dependencia | Uso en este proyecto |
|---|---|
| Spring Web / Spring Web MVC | Recibe las solicitudes HTTP y permite declarar rutas como `/usuarios/nuevo`. En el `pom.xml` de este proyecto aparece como `spring-boot-starter-webmvc`. |
| Thymeleaf | Renderiza HTML en el servidor usando plantillas y datos del modelo. La plantilla principal es `src/main/resources/templates/usuario.html`. |
| Spring Data JPA | Facilita persistir entidades en una base de datos mediante repositorios. El adaptador de salida lo usa para guardar, consultar, listar y eliminar. |
| H2 Database | Base relacional pequeña, útil para aprender y ejecutar sin instalar un servidor de base de datos separado. Este proyecto la configura en memoria. |
| Spring Boot DevTools | Ayuda durante el desarrollo, por ejemplo con reinicios al cambiar archivos. No es necesaria para el funcionamiento de la aplicación en producción. |
| Spring Boot Starter Test (o dependencias de prueba equivalentes) | Proporciona herramientas para pruebas. En este proyecto las dependencias de prueba de JPA, MVC y Thymeleaf están declaradas por separado. |

No agregues Kotlin: se eligió Java. No hace falta agregar un servidor Tomcat por separado: el starter web trae lo necesario para ejecutar la aplicación.

### Dependencias que aparecen en el `pom.xml` de este repositorio

- `spring-boot-starter-data-jpa`
- `spring-boot-starter-thymeleaf`
- `spring-boot-starter-webmvc`
- `spring-boot-devtools` (opcional, de ejecución y desarrollo)
- `h2` (de ejecución)
- `spring-boot-h2console`
- `spring-boot-starter-data-jpa-test` (pruebas)
- `spring-boot-starter-thymeleaf-test` (pruebas)
- `spring-boot-starter-webmvc-test` (pruebas)

Spring Boot 4 utiliza starters separados para algunas áreas, incluidas las pruebas. Si Initializr genera un conjunto de dependencias de prueba ligeramente diferente, conserva las dependencias que haya generado para la versión elegida y verifica compilando con Maven.

## 6. Configurar la base de datos

La configuración está en `src/main/resources/application.properties`:

```properties
spring.application.name=Hexagonal
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### Qué significa cada propiedad

- `spring.application.name`: nombre identificador de la aplicación; no crea la base de datos.
- `spring.datasource.url`: dirección de conexión. `jdbc:h2:mem:testdb` crea una base H2 en memoria llamada `testdb`.
- `spring.datasource.driverClassName`: driver JDBC que permite que Java hable con H2.
- `spring.datasource.username` y `spring.datasource.password`: credenciales de conexión. La configuración de ejemplo usa el usuario `sa` y contraseña vacía.
- `spring.h2.console.enabled=true`: habilita la consola web de H2 mientras la aplicación está en ejecución. En esta configuración puede abrirse en `/h2-console`.

### ¿Qué implica usar H2 en memoria?

Es cómodo para clases y pruebas: no se instala ni configura un servidor aparte. Como la URL usa `mem`, los datos se guardan en memoria y normalmente desaparecen cuando se detiene la aplicación. No se debe presentar esta configuración como persistencia permanente.

Para conectar otra base, por ejemplo PostgreSQL o MySQL, no basta con cambiar la URL: también hay que agregar el driver correspondiente, configurar usuario y contraseña de forma segura, y revisar el esquema y el comportamiento de JPA. No guardes contraseñas reales en el repositorio.

### Crear las tablas

`UsuarioEntity` está marcada como una entidad JPA y representa la tabla `usuarios`. La estrategia de creación o actualización del esquema depende de la configuración de Hibernate/Spring Boot. Este proyecto no declara explícitamente `spring.jpa.hibernate.ddl-auto`; por eso, si se enseña control exacto del esquema, conviene agregar esa configuración deliberadamente o usar migraciones (por ejemplo, Flyway). No se deben asumir ajustes que no estén en `application.properties`.

## 7. La estructura de carpetas del proyecto

La parte relevante de la estructura es:

```text
Hexagonal/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/example/Hexagonal/
    │   │   ├── HexagonalApplication.java
    │   │   ├── Dominio/
    │   │   │   └── Modelo/
    │   │   │       └── Usuario.java
    │   │   ├── Aplicacion/
    │   │   │   ├── Puertos/
    │   │   │   │   ├── in/
    │   │   │   │   │   └── UsuarioUseCase.java
    │   │   │   │   └── out/
    │   │   │   │       └── UsuarioRepositoryPort.java
    │   │   │   └── Servicio/
    │   │   │       └── UsuarioService.java
    │   │   └── Infrastructura/
    │   │       ├── Configuracion/
    │   │       │   └── BeanConfiguration.java
    │   │       └── Adaptador/
    │   │           ├── in/
    │   │           │   ├── UsuarioController.java
    │   │           │   └── UsuarioRequest.java
    │   │           └── out/
    │   │               ├── UsuarioAdapter.java
    │   │               ├── UsuarioEntity.java
    │   │               └── UsuarioRepository.java
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       │   └── estilos.css
    │       └── templates/
    │           └── usuario.html
    └── test/
        └── java/com/example/Hexagonal/
            └── HexagonalApplicationTests.java
```

La carpeta `target/` se genera al compilar. Contiene clases compiladas y otros resultados temporales; no es donde se editan los archivos fuente.

> **Convención de nombres:** en este repositorio la capa se llama `Infrastructura`. La forma ortográfica recomendada es `Infraestructura`. Se conserva aquí el nombre que ya usa el código, porque cambiarlo requeriría mover paquetes e imports en todas las clases.

## 8. Qué va en cada capa

La arquitectura hexagonal busca que la lógica importante de la aplicación no quede atada a una interfaz web, una base de datos o un framework. El dominio y la aplicación expresan el negocio; los adaptadores traducen entre el mundo externo y esos componentes.

### 8.1 Dominio

En este ejemplo, el dominio está en `Dominio/Modelo/Usuario.java`.

- Contiene conceptos y reglas del negocio.
- No debe necesitar Spring MVC, JPA, Thymeleaf ni conocer solicitudes HTTP.
- `Usuario` contiene `id`, `nombre` y `correo`.
- El constructor valida que nombre y correo no sean nulos o vacíos.
- El constructor de dos argumentos crea un usuario nuevo sin ID; la persistencia asigna el ID. El constructor con ID se utiliza al reconstruir el modelo desde lo persistido.

En una aplicación más grande, aquí también podrían vivir objetos de valor, políticas y reglas más elaboradas del negocio.

### 8.2 Aplicación

La aplicación coordina casos de uso. En este proyecto:

- `Aplicacion/Puertos/in/UsuarioUseCase.java` define las operaciones que la aplicación ofrece: registrar, obtener, listar y eliminar usuarios.
- `Aplicacion/Servicio/UsuarioService.java` implementa esas operaciones.
- `Aplicacion/Puertos/out/UsuarioRepositoryPort.java` declara las operaciones de persistencia que el caso de uso necesita.

El servicio crea el modelo `Usuario` al registrar y delega guardar/buscar/listar/eliminar al puerto de salida. La aplicación depende de abstracciones (interfaces), no de `UsuarioRepository` de Spring Data.

### 8.3 Infraestructura y adaptadores

Esta capa conecta el sistema con tecnologías externas:

- **Adaptador de entrada web:** `UsuarioController.java` recibe rutas HTTP, prepara el modelo para la vista y llama al puerto de entrada `UsuarioUseCase`.
- **DTO/request web:** `UsuarioRequest.java` recibe nombre y correo del formulario. Es un objeto de transporte para la entrada web; no es el modelo de dominio.
- **Adaptador de salida de persistencia:** `UsuarioAdapter.java` implementa `UsuarioRepositoryPort`. Convierte entre `Usuario` y `UsuarioEntity`, y delega el acceso a Spring Data JPA.
- **Entidad JPA:** `UsuarioEntity.java` representa la tabla `usuarios` y contiene anotaciones de persistencia. Se mantiene separada del modelo de dominio para que JPA no se filtre automáticamente a la lógica de negocio.
- **Repositorio técnico:** `UsuarioRepository.java` extiende `JpaRepository<UsuarioEntity, Long>` y es el mecanismo específico de Spring Data para operar con la base.
- **Configuración:** `BeanConfiguration.java` ensambla el servicio y sus dependencias.
- **Vista y recursos web:** `templates/usuario.html` es el HTML Thymeleaf; `static/estilos.css` contiene los estilos; `application.properties` configura Spring y H2.

## 9. Por qué hay un `@Bean` en `BeanConfiguration`

El servicio de aplicación es una clase Java normal:

```java
public class UsuarioService implements UsuarioUseCase {
    private final UsuarioRepositoryPort usuarioRepository;

    public UsuarioService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
}
```

No tiene `@Service` ni `@Component`. En cambio, la infraestructura lo registra explícitamente:

```java
@Configuration
public class BeanConfiguration {

    @Bean
    public UsuarioService usuarioService(UsuarioRepositoryPort usuarioRepository) {
        return new UsuarioService(usuarioRepository);
    }
}
```

Qué ocurre:

1. `@Configuration` indica a Spring que esta clase declara configuración de beans.
2. `@Bean` indica que el objeto devuelto por el método debe ser administrado por Spring.
3. Spring busca una implementación de `UsuarioRepositoryPort`. En este proyecto encuentra `UsuarioAdapter`, registrado con `@Component`.
4. Spring pasa esa implementación al método de configuración.
5. El método crea `UsuarioService` y Spring lo puede inyectar donde se solicite `UsuarioUseCase`.

### ¿Por qué no poner `@Service` en la capa de aplicación?

Una alternativa válida en muchas aplicaciones Spring sería anotar `UsuarioService` con `@Service` y dejar que el escaneo de componentes lo registre. Este proyecto utiliza `@Bean` para mostrar que:

- la aplicación puede permanecer como Java sin anotaciones de Spring;
- la creación de objetos y el acoplamiento al contenedor se mantienen en el punto de configuración de infraestructura;
- se ve explícitamente cómo se construye el grafo de dependencias;
- es posible cambiar el ensamblado sin introducir anotaciones del framework en la capa de aplicación.

`@Bean` **no es obligatorio** para que una arquitectura sea hexagonal. Es una elección de composición e inversión de dependencias. La meta no es evitar toda anotación en todo el proyecto: los adaptadores sí usan anotaciones Spring (`@Controller`, `@Component`, `@Entity`, etc.), pues son infraestructura.

También es importante no exagerar el beneficio: el servicio todavía importa interfaces de puertos y el modelo de dominio, como corresponde; evitar `@Service` elimina una dependencia de anotación del framework, pero no convierte por sí solo el proyecto en hexagonal. La separación de responsabilidades y la dirección de las dependencias son lo esencial.

## 10. Cómo recorrer el flujo de este código

### 10.1 Registrar un usuario (entrada web a persistencia)

1. La persona envía el formulario de `templates/usuario.html` a `POST /usuarios/registrar`.
2. `UsuarioController.registrarUsuario` recibe los campos en un `UsuarioRequest`.
3. El controlador llama a `usuarioUseCase.registrar(nombre, correo)`. Su tipo es la interfaz de entrada `UsuarioUseCase`, no el servicio concreto.
4. Spring ya inyectó en ese puerto la instancia `UsuarioService` construida por `BeanConfiguration`.
5. `UsuarioService.registrar` crea un objeto de dominio `Usuario`.
6. El servicio llama a `UsuarioRepositoryPort.guardar`.
7. Spring inyectó allí `UsuarioAdapter`, que implementa ese puerto.
8. `UsuarioAdapter` crea un `UsuarioEntity`, llama a `UsuarioRepository.save` y convierte el resultado a un `Usuario` de dominio.
9. El controlador coloca un mensaje de éxito en `RedirectAttributes` y redirige a `/usuarios/nuevo`.
10. La vista vuelve a pedir la lista actualizada y muestra los usuarios.

Diagrama resumido:

```text
Formulario Thymeleaf
        │ HTTP POST
        ▼
UsuarioController (adaptador de entrada)
        │ UsuarioUseCase (puerto de entrada)
        ▼
UsuarioService (caso de uso)
        │ UsuarioRepositoryPort (puerto de salida)
        ▼
UsuarioAdapter (adaptador de salida)
        │ Spring Data JPA
        ▼
UsuarioRepository → H2
```

El movimiento de vuelta transporta el usuario guardado y termina en una redirección HTTP:

```text
H2 → UsuarioAdapter → UsuarioService → UsuarioController
   → redirect:/usuarios/nuevo → Thymeleaf
```

### 10.2 Listar usuarios

1. `GET /` o `GET /usuarios` redirige a `/usuarios/nuevo`.
2. `UsuarioController.mostrarFormularioRegistro` llama a `mostrarFormulario`.
3. `mostrarFormulario` llama a `usuarioUseCase.listarUsuarios()`.
4. El servicio delega a `UsuarioRepositoryPort.listarTodos()`.
5. `UsuarioAdapter` ejecuta `findAll()` a través de `UsuarioRepository` y convierte las entidades JPA a modelos de dominio.
6. El controlador agrega `usuarios` y `totalUsuarios` al `Model`.
7. Thymeleaf itera sobre `usuarios` para construir la tabla.

### 10.3 Buscar por ID

1. El formulario envía `GET /usuarios/buscar?id=...`.
2. El controlador inicializa también los datos necesarios para pintar el resto de la página.
3. Llama a `usuarioUseCase.obtenerUsuario(...)`.
4. El servicio delega en el puerto de salida y el adaptador consulta el repositorio.
5. Si existe, la vista muestra el usuario encontrado; si no, presenta un mensaje de error.

### 10.4 Eliminar

1. El formulario envía `POST /usuarios/eliminar` con un ID.
2. El controlador valida que el ID sea positivo y que el usuario exista.
3. El controlador llama a `usuarioUseCase.eliminarUsuario(...)`.
4. El servicio convierte el ID recibido a `Long` al invocar el puerto de salida, cuyo método de eliminación recibe `Long`.
5. `UsuarioAdapter` ejecuta `deleteById`.
6. El controlador agrega el mensaje de confirmación y redirige a `/usuarios/nuevo`.
7. La página recarga el directorio desde la base y el usuario eliminado ya no aparece.

Este flujo es un buen ejemplo para explicar que el controlador no debe borrar directamente con JPA: solicita un caso de uso a la aplicación.

## 11. Cómo ejecutar y probar

### Desde VS Code

1. Abre la carpeta que contiene `pom.xml`.
2. Espera la importación de Maven.
3. Abre `HexagonalApplication.java`.
4. Pulsa **Run** sobre el método `main`, o ejecuta la configuración de Spring Boot desde Spring Boot Dashboard.
5. La configuración de lanzamiento del workspace puede abrir el navegador al detectar que Tomcat arrancó. Si no se abre automáticamente, visita `http://localhost:8080/`.
6. La ruta raíz `/` redirige a `/usuarios/nuevo`.

### Desde la terminal integrada de VS Code en Windows

Desde la carpeta del proyecto (la que contiene `pom.xml`):

```powershell
.\mvnw.cmd spring-boot:run
```

Luego abre `http://localhost:8080/`.

### Compilar y ejecutar pruebas

```powershell
.\mvnw.cmd clean test
```

- `clean` elimina resultados de compilaciones anteriores dentro de `target`.
- `test` compila el código y ejecuta las pruebas.
- El test de integración usa `@SpringBootTest` y verifica el contexto, el registro/consulta/eliminación y el listado de usuarios con H2.

La carpeta o configuración `.vscode` puede estar situada en la carpeta de workspace que se abrió, no necesariamente junto a `pom.xml`. VS Code usa la configuración del workspace abierto.

## 12. Cómo explicar la idea hexagonal al grupo

Una explicación corta y visual:

- El **centro** contiene reglas y casos de uso: dominio y aplicación.
- Los **puertos** son contratos que indican cómo se solicita una operación o qué necesita la aplicación.
- Los **adaptadores** conectan esos contratos con tecnologías concretas, por ejemplo HTTP/Thymeleaf o JPA/H2.
- Los detalles externos pueden cambiar con menos impacto si la lógica central no depende de ellos.

Para comprobar que el grupo entendió la separación, propón estas preguntas:

1. ¿Qué interfaz permite al controlador solicitar operaciones sin conocer el servicio concreto? `UsuarioUseCase`.
2. ¿Qué interfaz necesita el caso de uso para persistir sin conocer Spring Data? `UsuarioRepositoryPort`.
3. ¿Qué clase traduce `Usuario` a una entidad JPA? `UsuarioAdapter`.
4. ¿Qué clase conoce las rutas HTTP? `UsuarioController`.
5. Si se reemplaza H2 por otra base, ¿qué partes deberían cambiar? Principalmente la configuración del driver/conexión y el adaptador técnico; los casos de uso no deberían necesitar conocer el nuevo motor.
6. Si se crea un adaptador en memoria que implemente `UsuarioRepositoryPort`, ¿se debería reescribir `UsuarioService`? No; ese es precisamente el beneficio de depender del puerto.

### Ejercicio recomendado

Crear una implementación en memoria de `UsuarioRepositoryPort` para pruebas o demostración. Después, explicar cómo la configuración puede elegir qué implementación conectar. Así se observa el desacoplamiento en ejecución, no solo en un diagrama.

## 13. Alcance y mejoras didácticas posibles

Este proyecto es apropiado como ejemplo pequeño, pero no pretende ser una plantilla lista para producción. Puntos para discutir o mejorar en una siguiente práctica:

- Unificar el tipo del ID en todos los puertos y casos de uso; actualmente algunos métodos de búsqueda y del caso de uso utilizan `String`, mientras que el puerto de eliminación utiliza `Long`.
- Agregar pruebas unitarias de `UsuarioService` con un repositorio falso, para mostrar que la lógica se puede probar sin iniciar Spring ni H2.
- Agregar pruebas web para las rutas, formularios, redirecciones y mensajes.
- Definir manejo de errores y validaciones de entrada más completo. Las validaciones del navegador (`required`, `type=email`) no sustituyen validaciones del servidor.
- Configurar explícitamente la estrategia de esquema o usar migraciones cuando el proyecto deje de ser únicamente didáctico.
- Sustituir la base en memoria por una base persistente si el requisito es conservar datos al reiniciar.
- Evaluar nombres de paquetes en minúsculas en un proyecto nuevo.

Estas mejoras no son requisitos para comprender la primera versión de la arquitectura hexagonal. Es mejor agregar una a la vez y explicar qué problema resuelve cada una.

## 14. Glosario breve

| Término | Significado |
|---|---|
| API | Contrato o interfaz que permite que partes de un sistema se comuniquen. |
| Adaptador | Componente que convierte una interacción externa al formato que entiende un puerto, o viceversa. |
| Bean | Objeto cuya creación y ciclo de vida administra Spring. |
| Caso de uso | Operación de la aplicación que expresa una acción que el sistema permite realizar. |
| Controller | Adaptador web que recibe solicitudes y produce respuestas. |
| DTO | Objeto de transporte de datos entre límites, como formulario y controlador. |
| Entidad JPA | Objeto técnico que JPA relaciona con una tabla de la base de datos. |
| Inyección de dependencias | Técnica para entregar a un objeto las dependencias que necesita, en vez de construirlas dentro de él. |
| JPA | Especificación de Java para persistencia relacional; Hibernate es una implementación común. |
| Puerto | Interfaz que define una operación disponible o una necesidad de la aplicación. |
| Spring Initializr | Servicio/herramienta que genera la estructura inicial y configuración de un proyecto Spring. |
| Thymeleaf | Motor de plantillas que genera HTML en el servidor. |

## 15. Archivos de referencia del proyecto

- Configuración Maven y dependencias: `pom.xml`.
- Configuración de Spring y H2: `src/main/resources/application.properties`.
- Punto de entrada: `src/main/java/com/example/Hexagonal/HexagonalApplication.java`.
- Modelo de dominio: `src/main/java/com/example/Hexagonal/Dominio/Modelo/Usuario.java`.
- Puertos y servicio: `src/main/java/com/example/Hexagonal/Aplicacion/`.
- Controlador y adaptadores JPA: `src/main/java/com/example/Hexagonal/Infrastructura/`.
- Plantilla y estilos: `src/main/resources/templates/usuario.html` y `src/main/resources/static/estilos.css`.
- Pruebas: `src/test/java/com/example/Hexagonal/HexagonalApplicationTests.java`.
