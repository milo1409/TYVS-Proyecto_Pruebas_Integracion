# TYVS - Proyecto Pruebas de Integración y Sistema

## Descripción general

Este repositorio contiene el desarrollo del taller de **Pruebas de Integración y Sistema**, correspondiente a la unidad de Gestión de Defectos.

El proyecto implementa un sistema básico de registro de personas para una Registraduría. El sistema permite validar si una persona puede registrarse como votante, aplicando reglas de negocio relacionadas con mayoría de edad, estado de vida, identificación válida y duplicidad del registro.

El objetivo principal del taller es demostrar la implementación de pruebas automatizadas, pruebas de integración, pruebas de sistema HTTP, pruebas con mocks, cobertura de código con JaCoCo y automatización mediante un pipeline CI/CD.

---

## Integrantes

| Nombre               | Correo institucional             |
| -------------------- | -------------------------------- |
| Camilo Andres Porras | [camilo.porras@unisabana.edu.co] |

---

## Tecnologías utilizadas

* Java 17
* Maven
* Spring Boot
* JUnit 5
* Mockito
* H2 Database
* MockMvc
* JaCoCo
* GitHub Actions
* GitHub Wiki

---

## Estructura del proyecto

```text
src/main/java/edu/unisabana/tyvs/registry/
 ├─ domain/
 │   └─ model/
 │       ├─ Person.java
 │       ├─ Gender.java
 │       └─ RegisterResult.java
 │
 ├─ application/
 │   ├─ usecase/
 │   │   └─ Registry.java
 │   └─ port/out/
 │       └─ RegistryRepositoryPort.java
 │
 ├─ infrastructure/
 │   └─ persistence/
 │       ├─ RegistryRecord.java
 │       └─ RegistryRepository.java
 │
 └─ delivery/
     └─ rest/
         ├─ RegistryController.java
         └─ dto/
             └─ PersonRequest.java

src/test/java/edu/unisabana/tyvs/registry/
 ├─ application/
 │   └─ usecase/
 │       ├─ RegistryTest.java
 │       ├─ RegistryWithMockTest.java
 │       ├─ FakeRegistryRepository.java
 │       └─ RegistryWithFakeRepositoryTest.java
 │
 └─ delivery/
     └─ rest/
         └─ RegistryControllerTest.java
```

---

## Arquitectura aplicada

El proyecto se organiza siguiendo una arquitectura limpia por capas:

| Capa             | Responsabilidad                                                                    |
| ---------------- | ---------------------------------------------------------------------------------- |
| `domain`         | Contiene el modelo de dominio: `Person`, `Gender`, `RegisterResult`.               |
| `application`    | Contiene el caso de uso principal `Registry` y el puerto `RegistryRepositoryPort`. |
| `infrastructure` | Implementa la persistencia usando H2/JDBC mediante `RegistryRepository`.           |
| `delivery`       | Expone el sistema mediante un controlador REST: `RegistryController`.              |

Esta separación permite probar la lógica de negocio de forma aislada, integrar la lógica con una base de datos en memoria y validar el comportamiento del sistema desde la capa HTTP.

---

## Reglas de negocio

El caso de uso `Registry` valida los siguientes escenarios:

| Escenario             | Resultado    |
| --------------------- | ------------ |
| Persona válida        | `VALID`      |
| Persona duplicada     | `DUPLICATED` |
| Persona menor de edad | `UNDERAGE`   |
| Persona fallecida     | `DEAD`       |
| Persona inválida      | `INVALID`    |

---

## Ejecución del proyecto

Para compilar y ejecutar todas las pruebas del proyecto:

```bash
mvn clean verify
```

Este comando realiza las siguientes acciones:

1. Limpia los artefactos anteriores.
2. Compila el código fuente.
3. Ejecuta las pruebas automatizadas.
4. Genera el reporte de cobertura con JaCoCo.
5. Valida el umbral mínimo de cobertura configurado.

---

## Ejecución de pruebas

Para ejecutar únicamente las pruebas:

```bash
mvn clean test
```

Resultado esperado:

```text
BUILD SUCCESS
```

Evidencia:

![Ejecucion de pruebas](/evidencias/Imagen1.png)

![Ejecucion de pruebas](/evidencias/Imagen2.png)

---

## Reporte de cobertura JaCoCo

El reporte de cobertura se genera con:

```bash
mvn clean verify
```

Ruta del reporte generado:

```text
target/site/jacoco/index.html
```

Resultado obtenido durante la ejecución del taller:

```text
Cobertura global: 83%
```

Evidencia:

![Cobertura global](/evidencias/Imagen3.png)


La cobertura global supera el mínimo requerido del 80%. Además, las capas `application` y `delivery` cumplen el umbral mínimo esperado para el taller.

---

## Pruebas de integración con H2

Las pruebas de integración con base de datos H2 se implementaron en:

```text
src/test/java/edu/unisabana/tyvs/registry/application/usecase/RegistryTest.java
```

Estas pruebas validan la integración real entre:

```text
Registry
```

y

```text
RegistryRepository
```

usando una base de datos H2 en memoria.

Casos cubiertos:

| Caso                  | Resultado esperado | Verificación                                              |
| --------------------- | ------------------ | --------------------------------------------------------- |
| Persona válida        | `VALID`            | Se verifica persistencia real con `repo.existsById(...)`. |
| Persona duplicada     | `DUPLICATED`       | Se valida que un ID existente no se registre dos veces.   |
| Persona menor de edad | `UNDERAGE`         | Se verifica que no se persista en H2.                     |
| Persona fallecida     | `DEAD`             | Se verifica que no se persista en H2.                     |
| Persona inválida      | `INVALID`          | Se valida que datos inválidos no sean almacenados.        |

Cada prueba aplica el formato AAA:

* **Arrange:** preparación de datos, repositorio H2 y caso de uso.
* **Act:** ejecución de `registry.registerVoter(...)`.
* **Assert:** validación del resultado y verificación de persistencia.

---

## Pruebas con Mockito

Las pruebas con mocks se implementaron en:

```text
src/test/java/edu/unisabana/tyvs/registry/application/usecase/RegistryWithMockTest.java
```

Estas pruebas validan el comportamiento del caso de uso `Registry` usando un mock del puerto `RegistryRepositoryPort`.

Casos cubiertos:

| Caso                        | Técnica utilizada                              |
| --------------------------- | ---------------------------------------------- |
| Persona existente           | `when(repo.existsById(...)).thenReturn(true)`  |
| Persona válida no existente | `when(repo.existsById(...)).thenReturn(false)` |
| Validación de guardado      | `verify(repo).save(...)`                       |
| Validación de no guardado   | `verify(repo, never()).save(...)`              |
| Excepción simulada          | `thenThrow(...)`                               |

Estas pruebas permiten aislar la lógica de negocio sin depender de una base de datos real.

---

## Pruebas con Fake Repository

Adicionalmente, se implementó un repositorio falso usando `HashMap` en memoria, sin Mockito.

Archivos:

```text
src/test/java/edu/unisabana/tyvs/registry/application/usecase/FakeRegistryRepository.java
src/test/java/edu/unisabana/tyvs/registry/application/usecase/RegistryWithFakeRepositoryTest.java
```

Este enfoque permite probar el caso de uso usando una implementación simple del puerto de salida, sin depender de infraestructura real ni de un framework de mocking.

---

## Pruebas de sistema HTTP

Las pruebas de sistema HTTP se implementaron en:

```text
src/test/java/edu/unisabana/tyvs/registry/delivery/rest/RegistryControllerTest.java
```

Estas pruebas validan el comportamiento del endpoint REST usando MockMvc.

Endpoint probado:

```text
POST /registry/register
```

Casos cubiertos:

| Caso                  | Entrada                               | Resultado esperado      |
| --------------------- | ------------------------------------- | ----------------------- |
| Registro exitoso      | JSON válido con persona mayor de edad | HTTP 200 + `VALID`      |
| Persona duplicada     | JSON con ID previamente registrado    | HTTP 200 + `DUPLICATED` |
| Persona menor de edad | JSON con edad menor a 18              | HTTP 200 + `UNDERAGE`   |
| Persona fallecida     | JSON con `alive=false`                | HTTP 200 + `DEAD`       |
| JSON inválido         | Tipo incorrecto en campo numérico     | HTTP 400                |

---

## Pruebas manuales con curl

Para ejecutar la aplicación:

```bash
mvn spring-boot:run
```

Luego, desde otra terminal, se pueden validar los endpoints.

### Registro válido

```bash
curl.exe -X POST http://localhost:8080/registry/register -H "Content-Type: application/json" -d "{\"name\":\"Ana\",\"id\":100,\"age\":30,\"gender\":\"FEMALE\",\"alive\":true}"
```

Respuesta esperada:

```text
VALID
```
Evidencia:

![Valid](/evidencias/Imagen4.png)


### Persona duplicada

Ejecutar dos veces:

```bash
curl.exe -X POST http://localhost:8080/registry/register -H "Content-Type: application/json" -d "{\"name\":\"Ana\",\"id\":100,\"age\":30,\"gender\":\"FEMALE\",\"alive\":true}"
```

Segunda respuesta esperada:

```text
DUPLICATED
```
Evidencia:

![Duplicated](/evidencias/Imagen5.png)

### Persona menor de edad

```bash
curl.exe -X POST http://localhost:8080/registry/register -H "Content-Type: application/json" -d "{\"name\":\"Luis\",\"id\":301,\"age\":15,\"gender\":\"MALE\",\"alive\":true}"
```

Respuesta esperada:

```text
UNDERAGE
```

Evidencia:

![Underage](/evidencias/Imagen5.png)

### Persona fallecida

```bash
curl.exe -X POST http://localhost:8080/registry/register -H "Content-Type: application/json" -d "{\"name\":\"Pedro\",\"id\":303,\"age\":50,\"gender\":\"MALE\",\"alive\":false}"
```

Respuesta esperada:

```text
DEAD
```

Evidencia:

![Dead](/evidencias/Imagen6.png)

### JSON inválido

```bash
curl.exe -X POST http://localhost:8080/registry/register -H "Content-Type: application/json" -d "{\"name\":\"Error\",\"id\":\"ABC\",\"age\":30,\"gender\":\"MALE\",\"alive\":true}"
```

Respuesta esperada:

```text
400 Bad Request
```

Evidencia:

![Bad](/evidencias/Imagen6.png)

---

## Matriz de pruebas de integración

| Caso                       | Entrada                    | Resultado esperado          | Tipo de prueba | Test que lo valida                                  |
| -------------------------- | -------------------------- | --------------------------- | -------------- | --------------------------------------------------- |
| Persona válida             | ID=100, edad=30, viva      | `VALID`                     | H2             | `shouldRegisterValidPerson()`                       |
| Persona duplicada          | ID existente               | `DUPLICATED`                | H2             | `shouldRejectDuplicatedPerson()`                    |
| Persona menor de edad      | Edad=15                    | `UNDERAGE`                  | H2             | `shouldRejectUnderagePerson()`                      |
| Persona fallecida          | `alive=false`              | `DEAD`                      | H2             | `shouldRejectDeadPerson()`                          |
| Persona inválida           | ID inválido o nombre vacío | `INVALID`                   | H2             | `shouldRejectInvalidPerson()`                       |
| Persona existente con mock | `existsById=true`          | `DUPLICATED`                | Mock           | `shouldReturnDuplicatedWhenPersonAlreadyExists()`   |
| Persona válida con mock    | `existsById=false`         | `VALID` y `save()` invocado | Mock           | `shouldSaveWhenPersonIsValidAndDoesNotExist()`      |
| Registro HTTP válido       | JSON válido                | HTTP 200 + `VALID`          | HTTP           | `shouldRegisterPersonThroughRestEndpoint()`         |
| Registro HTTP duplicado    | JSON repetido              | HTTP 200 + `DUPLICATED`     | HTTP           | `shouldRejectDuplicatedPersonThroughRestEndpoint()` |
| JSON inválido              | `id="ABC"`                 | HTTP 400                    | HTTP           | `shouldRejectInvalidJsonThroughRestEndpoint()`      |

---

## Gestión de defectos

El registro de defectos se encuentra en:

```text
defectos.md
```

Este archivo contiene defectos reales o simulados detectados durante la ejecución de pruebas, incluyendo:

* Caso probado.
* Resultado esperado.
* Resultado obtenido.
* Causa probable.
* Estado.
* Evidencia.

---

## Pipeline CI/CD

El proyecto cuenta con un pipeline de integración continua configurado en:

```text
.github/workflows/ci.yml
```

El pipeline ejecuta automáticamente:

```bash
mvn clean verify
```

Esto permite validar que el código compile, que las pruebas pasen correctamente y que la cobertura mínima se mantenga.

El pipeline se ejecuta en eventos de:

* `push`
* `pull_request`

hacia las ramas principales del repositorio.

Evidencia:

![Git](/evidencias/Imagen7.png)

---

## Restricción de integración

La integración de código se restringe mediante el pipeline CI/CD. Si alguna prueba falla o si la cobertura baja del umbral mínimo configurado, el comando:

```bash
mvn clean verify
```

termina con error y el pipeline queda en estado fallido.

Esto evita integrar código defectuoso en la rama principal.

---

## Evidencias

Las evidencias del taller se almacenan en la carpeta:

```text
evidencias/
```

Evidencias sugeridas:

```text
evidencias/
 ├── Imagen1.png
 ├── Imagen2.png
 ├── Imagen3.png
 ├── Imagen4.png
 ├── Imagen5.png
 ├── Imagen6.png
 ├── Imagen7.png
 └── Imagen8.png
```

---

## Enlaces a código relevante

| Elemento               | Ruta                                                                                           |
| ---------------------- | ---------------------------------------------------------------------------------------------- |
| Caso de uso principal  | `src/main/java/edu/unisabana/tyvs/registry/application/usecase/Registry.java`                  |
| Puerto de persistencia | `src/main/java/edu/unisabana/tyvs/registry/application/port/out/RegistryRepositoryPort.java`   |
| Adaptador H2/JDBC      | `src/main/java/edu/unisabana/tyvs/registry/infrastructure/persistence/RegistryRepository.java` |
| Controlador REST       | `src/main/java/edu/unisabana/tyvs/registry/delivery/rest/RegistryController.java`              |
| DTO de entrada         | `src/main/java/edu/unisabana/tyvs/registry/delivery/rest/dto/PersonRequest.java`               |
| Pruebas H2             | `src/test/java/edu/unisabana/tyvs/registry/application/usecase/RegistryTest.java`              |
| Pruebas Mockito        | `src/test/java/edu/unisabana/tyvs/registry/application/usecase/RegistryWithMockTest.java`      |
| Pruebas HTTP           | `src/test/java/edu/unisabana/tyvs/registry/delivery/rest/RegistryControllerTest.java`          |
| Pipeline CI/CD         | `.github/workflows/ci.yml`                                                                     |

---

## Wiki del proyecto

La documentación oficial del taller se encuentra en el Wiki del repositorio.

Estructura sugerida del Wiki:

1. Inicio
2. Tipos de pruebas
3. Arquitectura limpia
4. Pruebas de integración
5. Pruebas con Mockito
6. Pruebas de sistema HTTP
7. Resultados y cobertura
8. Matriz de pruebas
9. Gestión de defectos
10. Conclusiones técnicas

---

## Conclusión

El proyecto demuestra la aplicación de pruebas automatizadas en diferentes niveles:

* Pruebas de integración con H2.
* Pruebas con mocks usando Mockito.
* Pruebas de sistema HTTP con MockMvc.
* Cobertura de código con JaCoCo.
* Automatización con GitHub Actions.

La separación por capas facilita la mantenibilidad, mejora la capacidad de prueba y permite validar la lógica de negocio sin acoplarla directamente a detalles de infraestructura.
