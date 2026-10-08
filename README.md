# 🐾 Gestor de Mascotas — *Pancho*

> Proyecto académico desarrollado en la asignatura de **Fundamentos de Ingeniería del Software (FIS)** de la **Universidad Politécnica de Madrid (UPM)**, que cubre el ciclo de vida completo del software: desde el modelado de requisitos hasta la construcción orientada a objetos y las pruebas unitarias.

---

## 📋 Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Ciclo de Vida del Software](#ciclo-de-vida-del-software)
  - [Fase 1 — Modelado de Requisitos](#fase-1--modelado-de-requisitos)
  - [Fase 2 — Diseño Arquitectónico](#fase-2--diseño-arquitectónico)
  - [Fase 3 — Construcción Orientada a Objetos](#fase-3--construcción-orientada-a-objetos)
  - [Fase 4 — Pruebas Unitarias](#fase-4--pruebas-unitarias)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Modelo de Dominio](#modelo-de-dominio)
- [Patrones de Diseño Aplicados](#patrones-de-diseño-aplicados)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías y Herramientas](#tecnologías-y-herramientas)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Comandos de la CLI](#comandos-de-la-cli)
- [Persistencia de Datos](#persistencia-de-datos)
- [Trabajo en Equipo](#trabajo-en-equipo)

---

## Descripción del Proyecto

**Gestor de Mascotas (*Pancho*)** es un sistema de gestión de mascotas y servicios de cuidado desarrollado íntegramente en Java como aplicación de consola (CLI). El sistema permite registrar mascotas convencionales y exóticas, gestionar propietarios y cuidadores, y coordinar servicios de cuidado entre ellos.

La autenticación de usuarios se delega en un **servidor externo de la ETSISI (UPM)**, y la validación del número de identificación animal (RIAC) se realiza a través de una **API externa de registro animal**. Los datos se persisten localmente en archivos JSON.

El proyecto fue desarrollado en grupo de trabajo en la asignatura **FIS (Fundamentos de Ingeniería del Software)** del Grado en Ingeniería del Software de la UPM, siguiendo todas las fases del proceso de desarrollo software: análisis de requisitos, modelado, diseño, implementación y pruebas.

---

## Ciclo de Vida del Software

### Fase 1 — Modelado de Requisitos

La primera entrega del proyecto consistió en la **elicitación y modelado formal de requisitos** del sistema. Se identificaron los actores principales y los casos de uso que el sistema debería soportar, documentados mediante **diagramas UML de casos de uso**.

#### Actores del sistema

| Actor | Descripción |
|---|---|
| **Propietario (Owner)** | Usuario registrado que posee mascotas y puede solicitar servicios de cuidado |
| **Cuidador (PetSitter)** | Usuario registrado que ofrece servicios de cuidado de mascotas |
| **Sistema RIAC** | Sistema externo de validación del registro de identificación animal |
| **Sistema RRSS** | Servidor externo de autenticación de usuarios (ETSISI/UPM) |

#### Casos de uso identificados

Se modelaron **5 áreas funcionales** mediante diagramas de casos de uso:

**1. Gestión de Propietarios** (`Diagrama_casos_de_uso_Gestión_Dueño.uml`)
- Registro de propietario en el sistema
- Inicio de sesión con autenticación externa
- Consulta de perfil y mascotas propias

**2. Gestión de Mascotas** (`Diagrama_casos_de_uso_Gestión_Mascota.uml`)
- Alta de mascota convencional con validación RIAC
- Alta de mascota exótica con documentación legal adicional
- Listado de mascotas por propietario
- Búsqueda de mascotas en el sistema

**3. Búsqueda** (`Diagrama_Casos_De_Usos_Busqueda.png`)
- Búsqueda de mascotas disponibles para cuidado
- Filtrado por criterios

**4. Cuidador** (`Diagrama_Casos_De_Usos_Cuidador.png`)
- Registro de disponibilidad del cuidador
- Aceptación de servicios de cuidado
- Gestión de mascotas bajo su cuidado

**5. Pagos** (`pagos.uml`)
- Gestión de precios por hora de servicio
- Registro de rangos horarios de cuidado

> 📁 Todos los diagramas se encuentran en `Modelado/diagramas/` y el documento completo de la primera entrega en `Modelado/diagramas/Conjunto_de_diagramas_Entrega_1.pdf`

---

### Fase 2 — Diseño Arquitectónico

A partir de los requisitos modelados, se diseñó la arquitectura del sistema y el **diagrama de clases de dominio** antes de comenzar la implementación.

#### Diagrama de clases de diseño

El diagrama principal de clases (`Modelado/Diagrama_clases_Pancho__1_.uml`) recoge:

- La **jerarquía de herencia** entre `User → Owner` y `User → PetSitter`
- La **jerarquía de herencia** entre `Pet → ExoticPet`
- Las **relaciones de asociación** entre `Pet` y `Owner` (una mascota pertenece a un propietario)
- Las **relaciones de asociación** entre `Care`, `Pet` y `PetSitter` (un servicio de cuidado relaciona mascota y cuidador)
- Las interfaces con los sistemas externos: `ExternalRIAC` y `ExternalRRSS`
- Los controladores como puntos de coordinación: `PetController`, `UserController`, `CareController`

#### Decisiones de diseño tomadas

- **Arquitectura MVC** para separar presentación, lógica de negocio y datos.
- **Patrón Singleton** en los tres controladores, garantizando una única instancia que gestiona el estado en memoria durante la ejecución.
- **Patrón Factory** para la creación de mascotas, desacoplando la lógica de instanciación del tipo concreto (Pet vs ExoticPet).
- **Patrón Dispatcher** en la capa de vista, donde cada comando CLI tiene su propio objeto encargado de parsear, validar y delegar la ejecución.
- **Result<T> genérico** para el manejo funcional de errores sin excepciones no controladas.

---

### Fase 3 — Construcción Orientada a Objetos

La implementación siguió fielmente el diseño, aplicando los principios de la POO a lo largo de toda la base de código.

#### Herencia y Polimorfismo

```java
// Jerarquía de mascotas
public class Pet { ... }
public class ExoticPet extends Pet {
    String legalProcedure;    // Documentación legal adicional
    String healthCertificate; // Certificado sanitario
    
    @Override
    public boolean isExotic() { return true; }
}

// Jerarquía de usuarios
public class User { ... }
public class Owner extends User { List<Pet> pets; ... }
public class PetSitter extends User { ... }
```

El método `isExotic()` permite polimorfismo en tiempo de ejecución: el `PetController` lo usa para decidir en qué colección (`pets` o `exoticPets`) almacenar la mascota, sin necesidad de `instanceof`.

#### Integración con APIs Externas

El sistema integra dos servicios externos proporcionados por la ETSISI:

```java
// Validación RIAC en la construcción del modelo
public String checkRiac(String RIAC) {
    if (ExternalRIAC.RIAC(RIAC)) {
        return RIAC;
    } else return null;
}

// Autenticación de usuarios contra servidor externo
// → ExternalRRSS (vía UserController → UserFactory)
```

#### Manejo Estructurado de Errores

Se implementó un tipo genérico `Result<T>` que encapsula o un dato de éxito o un error, evitando propagación de excepciones a lo largo de la pila de llamadas:

```java
public class Result<T> {
    private final T data;
    private final Error error;

    public boolean hasError() { return error != null; }
    public T getData()        { return data; }
    public Error getError()   { return error; }
}
```

Errores tipados: `Error`, `UnauthorizedError`, `InvalidNumberOfArgumentsError`.

#### Capa de Vista — Patrón Dispatcher

Cada comando de la CLI es manejado por una clase `Dispatcher` independiente que implementa un contrato abstracto:

```java
abstract public class Dispatcher {
    protected abstract Error checkSintax(String[] input); // Validación de argumentos
    public abstract void execute(String[] arguments);      // Lógica del comando
}
```

La CLI registra un array de dispatchers y resuelve el comando recibido buscando por nombre:

```java
private final Dispatcher[] dispatchers = {
    new LoginDispatcher(),
    new AddPetDispatcher(),
    new ListPetsDispatcher(),
    new CareDispatcher(),
    new ExitDispatcher(this),
};
```

---

### Fase 4 — Pruebas Unitarias

Se implementaron **9 pruebas unitarias** con JUnit 4 sobre la clase `PetFactory`, cubriendo tanto casos válidos como inválidos (partición de equivalencia y análisis de valores límite).

#### Casos de prueba implementados

| Test | Tipo | Descripción |
|---|---|---|
| `createPet_RIAC_long10_Numero` | ✅ Válido | Pet convencional con RIAC de 10 dígitos numéricos |
| `createExoticPet_RIAC_long15_Numero` | ✅ Válido | ExoticPet con RIAC de 15 dígitos y healthCertificate |
| `createExoticPet2_RIAC_long10_Numero` | ✅ Válido | ExoticPet con RIAC de 10 dígitos y legalProcedure |
| `createExoticPet3_RIAC_long15_Numero` | ✅ Válido | ExoticPet con RIAC de 15 dígitos y ambos documentos |
| `createBadPet_NameEmpty` | ❌ Inválido | Nombre vacío → `InvalidParameterException` |
| `createBadPet_AddressEmpty` | ❌ Inválido | Dirección vacía → `InvalidParameterException` |
| `createBadPet_RIAC_longNo10no15` | ❌ Inválido | RIAC con longitud distinta de 10 o 15 → excepción |
| `createBadPet_RIAC_long10_NoNumero` | ❌ Inválido | RIAC de 10 chars no numéricos → excepción |
| `createBadPet_RIAC_long15_NoNumero` | ❌ Inválido | RIAC de 15 chars no numéricos → excepción |

#### Técnicas aplicadas

- **Partición de equivalencia**: clases válidas (RIAC numérico de 10 o 15 dígitos) vs. inválidas (otro tamaño, no numérico)
- **Valores límite**: exactamente 10 y 15 dígitos (válidos), 11 dígitos (inválido)
- **Pruebas de excepción esperada**: `@Test(expected = InvalidParameterException.class)` para validar que el sistema falla correctamente ante entradas inválidas

```java
@Test(expected = InvalidParameterException.class)
public void createBadPet_RIAC_longNo10no15_Numero() {
    PetFactory.createPet("Frodo", "Bueno", "Calle Palacios",
        "01234567890", "seguridad", "", ""); // 11 dígitos → inválido
}
```

---

## Arquitectura del Sistema

```
┌──────────────────────────────────────────────────────────────┐
│                    CAPA DE VISTA (CLI)                      │
│                                                              │
│   CLI.java                                                   │
│   ├── LoginDispatcher      (comando: login)                 │
│   ├── AddPetDispatcher     (comando: add-pet)               │
│   ├── ListPetsDispatcher   (comando: list-pets)             │
│   ├── CareDispatcher       (comando: care)                  │
│   ├── ExitDispatcher       (comando: exit)                  │
│   └── HelpDispatcher       (comando: help)                  │
└───────────────────────────┬──────────────────────────────────┘
                            │ delega en
┌───────────────────────────▼──────────────────────────────────┐
│                CAPA DE CONTROLADORES                         │
│                                                              │
│   PetController  (Singleton)   — lógica de mascotas         │
│   UserController (Singleton)   — lógica de usuarios         │
│   CareController (Singleton)   — lógica de cuidados         │
└───────────────────────────┬──────────────────────────────────┘
                            │ opera sobre
┌───────────────────────────▼──────────────────────────────────┐
│                   CAPA DE MODELOS                            │
│                                                              │
│   User ──► Owner                                            │
│        └─► PetSitter                                        │
│   Pet  ──► ExoticPet                                        │
│   Care                                                       │
│   PetFactory / UserFactory                                   │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌──────────────┬────────────▼────────────┬──────────────────────┐
│  PERSISTENCIA│                         │  APIs EXTERNAS       │
│              │   Utils                 │                      │
│  pets.json   │   ├── File (Singleton)  │  ExternalRIAC        │
│  exotics.json│   ├── Console(Singleton)│  ExternalRRSS        │
│  owners.json │   └── Result<T>         │  (externals-2.0.jar) │
│  sitters.json│       Error             │                      │
│  users.json  │                         │                      │
└──────────────┴─────────────────────────┴──────────────────────┘
```

---

## Modelo de Dominio

```
User
├── username      : String
├── usernameCode  : String
└── id            : int

    Owner extends User
    └── pets : List<Pet>

    PetSitter extends User

Pet
├── name          : String
├── description   : String
├── address       : String
├── RIAC          : String    ← validado contra ExternalRIAC
├── securePolicy  : String
├── owner         : Owner
└── id            : int

    ExoticPet extends Pet
    ├── legalProcedure    : String
    └── healthCertificate : String

Care
├── pet           : Pet
├── description   : String
├── priceHour     : float
├── startHour     : String
└── finishHour    : String
```

---

## Patrones de Diseño Aplicados

| Patrón | Clase(s) | Propósito |
|---|---|---|
| **Singleton** | `PetController`, `UserController`, `CareController`, `File`, `Console` | Una única instancia por ejecución que mantiene el estado en memoria |
| **Factory Method** | `PetFactory`, `UserFactory` | Decide en tiempo de ejecución si crear `Pet`/`ExoticPet` u `Owner`/`PetSitter` sin acoplar al cliente |
| **Dispatcher (Command)** | `Dispatcher` + subclases | Encapsula cada comando CLI como un objeto con nombre, descripción, validación y ejecución |
| **Result / Either** | `Result<T>`, `Error` | Manejo funcional de errores: cada operación retorna éxito o error sin lanzar excepciones |
| **Template Method** | `Dispatcher` abstracto | Define el esqueleto del flujo (checkSintax → execute), delegando los detalles a subclases |

---

## Estructura del Proyecto

```
upmfis-iwsim21-02/
├── Modelado/
│   ├── Diagrama_clases_Pancho__1_.uml          # Diagrama de clases principal
│   └── diagramas/
│       ├── Conjunto_de_diagramas_Entrega_1.pdf # Documento de entrega de requisitos
│       ├── Diagrama_casos_de_uso_Gestión_Dueño.uml
│       ├── Diagrama_casos_de_uso_Gestión_Mascota.uml
│       ├── Diagrama_Casos_De_Usos_Busqueda.png
│       ├── Diagrama_Casos_De_Usos_Cuidador.png
│       ├── pagos.uml
│       ├── cuenta.uml
│       └── panchopuntos.uml
├── app/
│   ├── pom.xml
│   └── src/
│       ├── main/java/
│       │   ├── App.java                            # Punto de entrada
│       │   ├── controllers/
│       │   │   ├── PetController.java              # Singleton — lógica de mascotas
│       │   │   ├── UserController.java             # Singleton — lógica de usuarios
│       │   │   └── CareController.java             # Singleton — lógica de cuidados
│       │   ├── models/
│       │   │   ├── Pet.java                        # Entidad mascota base
│       │   │   ├── ExoticPet.java                  # Mascota exótica (hereda Pet)
│       │   │   ├── User.java                       # Entidad usuario base
│       │   │   ├── Owner.java                      # Propietario (hereda User)
│       │   │   ├── PetSitter.java                  # Cuidador (hereda User)
│       │   │   ├── Care.java                       # Entidad de servicio de cuidado
│       │   │   ├── PetFactory.java                 # Factory de mascotas
│       │   │   └── UserFactory.java                # Factory de usuarios
│       │   ├── views/
│       │   │   ├── CLI.java                        # Interfaz de línea de comandos
│       │   │   └── dispatchers/
│       │   │       ├── Dispatcher.java             # Clase abstracta base
│       │   │       ├── LoginDispatcher.java
│       │   │       ├── AddPetDispatcher.java
│       │   │       ├── ListPetsDispatcher.java
│       │   │       ├── CareDispatcher.java
│       │   │       ├── ExitDispatcher.java
│       │   │       └── HelpDispatcher.java
│       │   └── utils/
│       │       ├── Console.java                    # Singleton — I/O de consola
│       │       ├── File.java                       # Singleton — lectura/escritura JSON
│       │       └── results/
│       │           ├── Result.java                 # Tipo genérico Result<T>
│       │           ├── Error.java
│       │           ├── UnauthorizedError.java
│       │           └── InvalidNumberOfArgumentsError.java
│       └── test/java/models/
│           └── PetFactoryTest.java                 # 9 pruebas unitarias JUnit 4
├── pets.json       # Mascotas convencionales persistidas
├── exotics.json    # Mascotas exóticas persistidas
├── owners.json     # Propietarios persistidos
├── sitters.json    # Cuidadores persistidos
└── users.json      # Servicios de cuidado persistidos
```

---

## Tecnologías y Herramientas

| Tecnología | Versión | Uso |
|---|---|---|
| **Java** | 21 | Lenguaje de implementación |
| **Maven** | 3.x | Gestión de dependencias, build y Javadoc |
| **Gson** | 2.3 | Serialización y deserialización JSON |
| **JUnit** | 4.11 | Framework de pruebas unitarias |
| **ExternalRIAC** | UPM lib | API de validación de número de identificación animal |
| **ExternalRRSS** | UPM lib | Servidor de autenticación ETSISI |
| **Git** | — | Control de versiones |
| **Redmine** | UPM | Gestión ágil del proyecto (issues, milestones, wiki) |
| **StarUML / PlantUML** | — | Modelado de diagramas UML |

---

## Instalación y Ejecución

### Prerrequisitos

- Java 21 o superior
- Maven 3.x

### Compilar

```bash
cd app
mvn clean compile
```

### Ejecutar

```bash
mvn exec:java -Dexec.mainClass="App"
```

### Ejecutar pruebas

```bash
mvn test
```

### Generar Javadoc

```bash
mvn javadoc:javadoc
# La documentación se genera en app/target/site/apidocs/
```

---

## Comandos de la CLI

Al arrancar la aplicación se muestra la lista de comandos disponibles (`help`). El formato de entrada es `comando [argumentos]`:

| Comando | Descripción | Requiere login |
|---|---|---|
| `help` | Muestra todos los comandos disponibles | No |
| `login` | Autenticación vía servidor externo RRSS | No |
| `add-pet` | Alta de mascota (convencional o exótica) con validación RIAC | Sí (Owner) |
| `list-pets` | Listado de mascotas del usuario actual | Sí |
| `care` | Gestión de servicios de cuidado | Sí |
| `exit` | Salir de la aplicación | No |

### Ejemplo de sesión

```
Welcome to PetSitter!
Available commands: help, login, add-pet, list-pets, care, exit

>_ login
[autenticación externa RRSS]

>_ add-pet
If you want to use default examples write 1, if not just press enter: 1
[mascotas de ejemplo cargadas: toby, rayo, alfa (exótica)]

>_ list-pets
Pet{name='toby', description='golden retriever', ...}
Pet{name='rayo', description='yorkshire', ...}
ExoticPet{name='alfa', description='chow-chow', legalProcedure='9520853A', ...}

>_ exit
```

---

## Persistencia de Datos

Los datos se persisten en archivos JSON en el directorio raíz mediante la librería **Gson**. La clase `File` (Singleton) abstrae las operaciones de lectura y escritura. Cada controlador carga los datos al arrancar y los guarda tras cada operación de escritura:

```json
// pets.json (ejemplo)
[
  {"name":"toby","description":"golden retriever","address":"Monte Alicia...","RIAC":"3936142643","securePolicy":"98314","id":1},
  {"name":"rayo","description":"yorkshire","address":"Muelle Victoria...","RIAC":"4607761661","securePolicy":"71299","id":2}
]

// exotics.json (ejemplo)
[
  {"name":"alfa","legalProcedure":"9520853A","healthCertificate":"83120FAR","RIAC":"5174828926","id":3}
]
```

---

## Trabajo en Equipo

El proyecto fue desarrollado en grupo en la asignatura **FIS** de la **ETSISI — UPM**, aplicando herramientas profesionales de gestión de proyectos:

- **Redmine** como plataforma de gestión: issues, asignación de tareas, milestones y seguimiento del progreso
- **Git** para control de versiones con ramas por funcionalidad
- **Entregas formales** por fases: primera entrega de modelado y requisitos, segunda entrega de implementación y pruebas
- Gestión de proyecto en: [fis.etsisi.upm.es/projects/upmfis-iwsim21-02](https://fis.etsisi.upm.es/projects/upmfis-iwsim21-02)