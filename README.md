# Mini PC Simulator (🛠️ Proyecto en construcción)

![Badge en Desarollo](https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green)

> [!WARNING]
> La presente documentación no se encuentra actualizada con respecto al estado del proyecto, será actualizada a medida que avance el proyecto o cuando se finalicen sus etapas claves de desarrollo.

<p align="left">
  <img src="https://res.cloudinary.com/dpuuo4mfh/image/upload/v1789428731/simulatorimgt1so2_r6cqry.png" width="1000" alt="Vista previa del simulador">
</p>

## Información del proyecto

**Estudiante:** 2024178835 - Johnsy Steven López Aguilar  
**Curso:** Principios de Sistemas Operativos  
**Estado del proyecto:** Escala de 1 a 5
**Enlace del video:** [Ver en YouTube](https://youtu.be/xyo6f_jqHMA)

---

## Descripción

Mini PC Simulator es un simulador básico de un sistema operativo, desarrollado con Java, cuyo objetivo es representar de manera visual y práctica algunos de los conceptos fundamentales relacionados con el curso de Principios de Sistemas Operativos.

Esta tarea permite observar el proceso que ocurre desde que un programa es cargado hasta que sus instrucciones son ejecutadas por el procesador.

El simulador trabaja con programas escritos en un tipo de ensamblador mediante archivos `.asm`. Estos programas son analizados y validados antes de ser cargados en memoria. Posteriormente, las instrucciones válidas son traducidas a su representación interna y almacenadas en la memoria RAM simulada. De momento, solo se puede cargar un programa a la vez.

Una vez cargado el programa, el usuario puede ejecutar las instrucciones utilizando dos modalidades:

- **Paso a paso:** permite observar individualmente cada ciclo de ejecución de cada instrucción.
- **Ejecutar todo:** procesa automáticamente todas las instrucciones del programa.

Durante la ejecución es posible observar el estado de los principales componentes del sistema, incluyendo la memoria, los registros del CPU y el estado del proceso.

---

## Objetivo

El objetivo principal de la tarea es construir una representación funcional y visual de una Mini PC que permita comprender cómo interactúan diferentes componentes de un sistema computacional.

En particular, el simulador busca representar:

- El almacenamiento de instrucciones en memoria principal.
- La lectura de instrucciones desde la memoria.
- El ciclo de búsqueda y ejecución por el CPU.
- El funcionamiento de registros del procesador.
- La administración básica de un proceso mediante un PCB (Process Control Block).
- La función del Kernel como controlador del sistema.
- La carga de programas desde archivos externos.arios.
- El manejo de memoria disponible para los programas.

Esta tarea no pretende ser una implementación de un sistema operativo real, sino una herramienta que simula el comportamiento de un sistema operativo y permite comprender conceptos fundamentales relacionados con el curso de Principios de Sistemas Operativos.

---

# Manual de Ejecución y Usuario

## Requisitos previos

Para ejecutar el proyecto se requiere:

- **JDK 11 o superior.**
- **Apache NetBeans**, únicamente si se desea ejecutar el proyecto desde el IDE.
- Sistema operativo compatible con Java.
- La biblioteca **FlatLaf 3.7.2**, incluida dentro del proyecto (se encuentra en `lib/flatlaf-3.7.2.jar`).

No es necesario instalar herramientas adicionales para utilizar la aplicación una vez que el proyecto ha sido descargado con todas sus dependencias.

---

## Descargar el proyecto

El repositorio puede clonarse mediante Git:

```bash
git clone https://github.com/johnsydev/t1-principios-sistemas-operativos-mini-pc.git
````

Después de clonar el repositorio, ingresar a la carpeta del proyecto:

```bash
cd t1-principios-sistemas-operativos-mini-pc
```

---

## Opción 1: Ejecutar desde NetBeans

1. Abrir **Apache NetBeans**.
2. Seleccionar **File → Open Project**.
3. Buscar la carpeta del proyecto clonado.
4. Abrir el proyecto.
5. Localizar la clase principal:

```text
MiniPCSimulator.java
```

6. Ejecutar el proyecto utilizando **F6** o la opción **Run Project**.

Al iniciar la aplicación se crea la ventana principal del simulador y se puede interactuar con ella.

---

## Opción 2: Ejecutar desde la línea de comandos

Ingresar a la carpeta donde se encuentra el proyecto:

```bash
cd Programa/MiniPCSimulator
```

### Compilar

En Windows PowerShell se puede utilizar:

```bash
./compilar
```

El comando compila las clases Java ubicadas dentro de `src` y coloca los archivos `.class` generados en ```build/classes```.

Además, se incluye la biblioteca FlatLaf necesaria para la interfaz gráfica.

### Ejecutar

Una vez compilado el proyecto:

```bash
./ejecutar
```

La aplicación debería abrir la ventana principal del simulador.

No cierre la consola hasta que termine de utilizar el simulador.

Los scripts de compilación y ejecución se encuentran en el archivo `compilar.bat` y `ejecutar.bat` dentro de la carpeta del proyecto.

---

# Guía de uso

## 1. Configurar la memoria

Al iniciar la aplicación, el usuario observa una ventana con todos los componentes visuales y sin ningún programa cargado.

El usuario puede establecer la cantidad de memoria RAM y el espacio reservado para el Kernel en la sección ubicada en la parte inferior derecha.

Después de seleccionar los valores deseados, se debe presionar: **Aplicar configuración**

**Presionar el botón es necesario** para aplicar los cambios realizados, ya que **simula** la acción de cambiar la memoria principal física por otra de otro tamaño.

El simulador utiliza estos valores para determinar qué parte de la memoria estará disponible para el sistema y qué parte podrá utilizar el programa.

La configuración puede editarse **únicamente cuando no haya un programa cargado**.

La configuración predeterminada es:

* **Memoria principal:** 256 celdas.
* **Reservado para Kernel:** 64 celdas.

### Límites de memoria

- El tamaño máximo para la memoria principal es de 65536 celdas.
- El tamaño mínimo para la memoria principal es de 128 celdas.
- El tamano maximo establecido para el Kernel es de (memoria principal - 16).
- El tamaño mínimo establecido para el Kernel es de 16 celdas.

Los límites se establecen de esta forma debido a que para esta tarea solo se requiere un proceso a la vez y el PCB solo necesita 8 celdas.

El usuario puede establecer el tamaño de la memoria principal y el espacio reservado para el Kernel según sus necesidades y criterios técnicos.

---

## 2. Seleccionar un programa

El simulador trabaja con archivos de código fuente que utilizan la extensión:

```text
.asm
```

Para seleccionar un programa se utiliza el explorador de archivos disponible en la interfaz.

El archivo seleccionado contiene las instrucciones que posteriormente serán procesadas por el simulador.

En la carpeta `Ejemplo` se encuentra un ejemplo de programa válido, que contiene la totalidad de instrucciones posibles para el simulador, debe seguir esa sintaxis.

Al seleccionar el archivo ocurre una serie de procesos:
1. Se lee el contenido del archivo (FileManager).
2. Se analizan sus líneas y se descartan las líneas vacías (AsmParser).
4. Se valida la sintaxis de las instrucciones (AsmParser).
5. Se verifican los rangos permitidos de números y se descartan las instrucciones no validas (AsmParser).
6. Se crea el proceso en estado `NEW` (MiniPCController).
7. Las instrucciones válidas se convierten a su representación interna y se almacena en disco en lista de instrucciones (Loader).
8. Se actualiza la tabla de instrucciones en la GUI (VentanaPrincipal).

---

## 3. Cargar el programa

Después de seleccionar el archivo `.asm`, se debe presionar:

**Cargar programa**

En esta etapa ocurre una serie de procesos:

1. Se valida si existe un proceso cargado.
2. El programa es colocado en las posiciones disponibles de la memoria RAM (Loader).
3. Se cambia el estado del proceso a `READY` (MiniPCController).

Si el programa requiere más memoria de la disponible para el usuario, el simulador informa que no existe suficiente espacio para cargarlo.

---

## 4. Ejecutar el programa

Una vez que el programa ha sido cargado correctamente, existen dos formas principales de ejecución.

### Paso a paso

La opción **Paso a paso** permite ejecutar una instrucción a la vez.

Esta modalidad resulta especialmente útil para observar cómo cambian:

* El `PC`.
* El `AC`.
* Los registros `AX`, `BX`, `CX` y `DX`.
* La memoria.
* El estado del proceso.
* La instrucción que está siendo ejecutada.

De esta manera se puede observar visualmente el ciclo de búsqueda y ejecución de las instrucciones.

### Ejecutar todo

La opción **Ejecutar todo** ejecuta automáticamente las instrucciones restantes del programa hasta finalizar su ejecución.

Esta opción permite comprobar rápidamente el resultado final del programa sin tener que avanzar manualmente por cada instrucción.

---

# Arquitectura del sistema

El proyecto utiliza una separación por responsabilidades entre los diferentes componentes. La lógica principal se encuentra distribuida entre el modelo de hardware simulado, el controlador que representa al Kernel, los servicios encargados de procesar programas y la interfaz gráfica.

## Estructura de archivos

``` text
t1-principios-sistemas-operativos-mini-pc/
├── compilar.bat
├── ejecutar.bat
├── README.md
├── Ejemplo/
│   └── file.asm
└── Programa/
    ├── readme.md
    └── MiniPCSimulator/
        ├── build.xml
        ├── manifest.mf
        ├── build/          (Clases compiladas)
        ├── lib/
        │   └── flatlaf-3.7.2.jar
        ├── nbproject/      (Configuración de NetBeans)
        ├── src/
        │   └── minipcsimulator/
        │       ├── MiniPCSimulator.java
        │       ├── controller/
        │       │   └── MiniPCController.java
        │       ├── gui/
        │       │   └── VentanaPrincipal.java
        │       ├── model/
        │       │   ├── CPU.java
        │       │   ├── Dispatcher.java
        │       │   ├── Instruction.java
        │       │   ├── Loader.java
        │       │   ├── MainMemory.java
        │       │   ├── MemoryRegister.java
        │       │   ├── PCB.java
        │       │   └── Process.java
        │       ├── services/
        │       │   ├── AsmParser.java
        │       │   └── FileManager.java
        │       └── utils/
        │           └── SystemConfig.java
        └── test/
```

## Funcionalidades de cada componente

| Componente    | Capa        | Responsabilidad                                                                                                                                           |
| ----------------------- | ----------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `MiniPCSimulator.java`  | Principal   | Punto de entrada de la aplicación. Inicializa la interfaz y configura el tema visual utilizando FlatLaf.                                                  |
| `MiniPCController.java` | Controlador | Actúa como el controlador principal del sistema y representa las funciones básicas del Kernel. Coordina la carga, ejecución y administración del proceso, así como los handlers de eventos de la interfaz gráfica. |
| `VentanaPrincipal.java` | Vista       | Implementa la interfaz gráfica utilizando Swing y FlatLaf. Permite configurar el sistema, cargar programas y visualizar el estado de la Mini PC.          |
| `CPU.java`              | Modelo      | Representa el procesador. Implementa la lógica relacionada con los ciclos de búsqueda (*fetch*) y ejecución (*execute*) de instrucciones.                 |
| `MainMemory.java`       | Modelo      | Representa la memoria RAM utilizada por el sistema y proporciona acceso a sus posiciones de memoria.                                                      |
| `MemoryRegister.java`   | Modelo      | Representa una posición individual de memoria y permite almacenar instrucciones o valores numéricos.                                                      |
| `Process.java`          | Modelo      | Representa un proceso y agrupa sus instrucciones junto con la información necesaria para su ejecución.                                                    |
| `PCB.java`              | Modelo      | Representa el Bloque de Control de Proceso (BCP en español), almacenando información como el identificador, estado y registros asociados al proceso.             |
| `Loader.java`           | Modelo    | Se encarga de cargar el programa y escribir sus instrucciones en las posiciones correspondientes de la memoria.                                           |
| `Dispatcher.java`       | Modelo    | Modela las transiciones y cambios de estado relacionados con la ejecución del proceso.                                                                    |
| `AsmParser.java`        | Servicio    | Analiza los archivos `.asm`, valida la sintaxis de las instrucciones, verifica rangos y descarta líneas vacías.                                           |
| `FileManager.java`      | Servicio    | Gestiona la selección y lectura de archivos `.asm` mediante el explorador de archivos.                                                                    |
| `SystemConfig.java`     | Utilidad    | Contiene las configuraciones generales del sistema, como el tamaño de la RAM y el espacio reservado para el Kernel, entre otras.                                     |


---

# Funcionamiento interno

## Carga de un programa

El proceso de carga comienza cuando el usuario selecciona un archivo `.asm`.

El `FileManager` se encarga de obtener el archivo seleccionado y proporcionar su contenido al sistema. Posteriormente, `AsmParser` analiza cada línea y determina si cumple con las reglas de sintaxis establecidas.

Las instrucciones que superan la validación son procesadas para obtener su representación interna. Esta representación es utilizada por el `Loader`, que cuando se presiona en `Cargar programa` se encarga de escribir las instrucciones en las posiciones disponibles de la memoria RAM.

De esta manera, el programa deja de existir únicamente como un archivo externo y pasa a estar representado dentro de la memoria de la Mini PC.

---

## Ciclo de ejecución

La ejecución del programa se basa en el ciclo fundamental de:

```text
FETCH -> EXECUTE
```

Durante el **FETCH**, el procesador utiliza el `PC` (Program Counter) para identificar la posición de memoria que contiene la siguiente instrucción.

La instrucción es obtenida desde la memoria y posteriormente procesada por el CPU.

Durante el **EXECUTE**, el procesador interpreta la instrucción y realiza la operación correspondiente. Dependiendo de la instrucción, pueden modificarse los registros internos del CPU. 

Por motivos de diseño escalable, se utilizaron `Enums` para facilitar la representación de las instrucciones y su utilización en `switch` para cada caso. Por cada tipo de operación se realizó un método distinto dentro de la clase `CPU` para separar la lógica de ejecución de cada instrucción.

Este proceso se repite hasta que el programa finaliza.

La opción **Paso a paso** permite observar este comportamiento de manera individual, mientras que **Ejecutar todo** realiza el proceso de forma continua.

---

# Memoria

La memoria simulada está organizada en celdas individuales representadas mediante objetos `MemoryRegister`. Este objeto se implementó debido a que al necesitar guardar información de BCP u otras cosas tener un array de `Instruction` no era suficiente.

Cada posición puede contener información asociada con una instrucción o un valor numérico.

La interfaz muestra información de la memoria para facilitar la observación de lo que está ocurriendo internamente.

La memoria se divide conceptualmente entre:

* **Espacio reservado para el Kernel.**
* **Espacio disponible para los programas de usuario.**

Si un programa requiere más posiciones de las disponibles en el área de usuario, el sistema impide su carga y muestra un aviso indicando que no existe suficiente memoria.

---

# Registros y CPU

El procesador simulado cuenta con los siguientes registros principales:

| Registro | Función                                                                                                   |
| -------- | --------------------------------------------------------------------------------------------------------- |
| `AX`     | Registro general utilizado durante las operaciones del procesador.                                        |
| `BX`     | Registro general utilizado para almacenar valores durante la ejecución.                                   |
| `CX`     | Registro general utilizado durante las operaciones del programa.                                          |
| `DX`     | Registro general utilizado durante las operaciones del programa.                                          |
| `AC`     | Acumulador utilizado para almacenar resultados y valores temporales.                                      |
| `PC`     | Program Counter. Indica la posición de memoria asociada con la siguiente instrucción que debe procesarse. |
| `IR`     | Instruction Register. Contiene la instrucción en ejecución.                   |
| `PCB`    | Contiene la información administrativa correspondiente al proceso en ejecución.                           |

La interfaz gráfica muestra estos elementos para que el usuario pueda observar cómo cambian durante la ejecución.

---

# Proceso y PCB

Cada programa cargado es tratado como un proceso dentro del simulador.

El proceso mantiene sus instrucciones y se relaciona con un **PCB (Process Control Block / Bloque de Control de Proceso)**.

El PCB permite representar la información necesaria para administrar el proceso, incluyendo su identificador, estado y registros.

La información del PCB se mantiene asociada al proceso y también se representa dentro de la memoria simulada. Esta decisión permite visualizar de manera directa las posiciones utilizadas por el sistema para almacenar la información administrativa del proceso.

Por cada espacio disponible en la memoria se crea un objeto `MemoryRegister` para representar cada atributo del PCB (8 en total). Se actualiza en cada paso debido a lo solicitado para esta tarea, sin embargo, en futuras actualizaciones este comportamiento puede cambiar.

La interfaz del CPU utiliza una estructura visual similar a la información representada en el PCB, facilitando la comparación entre el estado administrativo del proceso y el estado actual del procesador.

---

# Validación de programas

Antes de cargar un programa en memoria, el archivo `.asm` pasa por un proceso de validación.

El `AsmParser` se encarga de comprobar que las instrucciones cumplan con las reglas establecidas por el simulador.

Entre las validaciones realizadas se encuentran:

* Sintaxis de las instrucciones.
* Formato de los operandos.
* Rangos permitidos.
* Estructura esperada de cada tipo de instrucción.
* Identificación y descarte de líneas vacías.

Una línea no se considera una instrucción válida simplemente por existir dentro del archivo. Debe superar los filtros sintácticos establecidos y contener una instrucción procesable.

Esto evita que contenido vacío o instrucciones incorrectas sean trasladados a la memoria como parte del programa.

Al descartar las líneas vacías se evita el consumo de memoria innecesario, optimizando lo dicho de que "cada línea del programa es un espacio en la memoria", haciendo que únicamente las líneas con instrucciones sean almacenadas en memoria. Si un archivo tiene 200 líneas pero 50 de ellas están vacías, el simulador solo ocupará 150 posiciones de memoria.

---

# Manejo de Overflow

El sistema busca representar el comportamiento de un procesador que trabaja con una cantidad limitada de bits. Además, no hay especificación en el enunciado sobre este comportamiento.

Por esta razón, no se implementa una validación de software que impida automáticamente una operación cuando el resultado supera el rango representable.

Cuando una operación produce un valor fuera del rango disponible para 8 bits, el resultado se comporta de acuerdo con las limitaciones de la representación utilizada por el hardware simulado.

Esto permite observar de forma más realista el comportamiento asociado al desbordamiento.

---

# Decisiones de diseño

## Interfaz gráfica

Se utilizó **Swing** como base para la interfaz gráfica y tras una investigación de librerías para interfaz moderna y agradable para el usuario, se decidió utilizar **FlatLaf** para proporcionar una apariencia visual más moderna.

La interfaz fue diseñada para que los componentes internos del simulador puedan observarse directamente, evitando que el usuario tenga que interactuar únicamente mediante una consola.

El tema oscuro también permite separar visualmente las diferentes secciones de la aplicación y facilita la lectura de las tablas y registros.

## Separación de responsabilidades

La aplicación se divide en diferentes capas y componentes con responsabilidades específicas, utilizando la arquitectura MVC (Modelo-Vista-Controlador).

El modelo representa los elementos de la Mini PC, los servicios procesan los archivos y operaciones auxiliares, el controlador coordina el funcionamiento del sistema y la vista se encarga de la interacción con el usuario.

Esta separación facilita el mantenimiento del proyecto y permite modificar un componente sin tener que concentrar toda la lógica en una única clase.

## Uso de Loader

Se implementó un `Loader` para representar de forma más cercana el proceso mediante el cual un programa es trasladado desde un archivo externo hacia la memoria.

En lugar de realizar toda la carga directamente desde el controlador, el Loader concentra esta responsabilidad y se encarga de escribir las instrucciones en las posiciones correspondientes de la RAM.

## Uso de un Dispatcher

Se implementó un `Dispatcher` para representar la administración del proceso y sus transiciones de estado.

Aunque el simulador trabaja con un modelo simplificado y no busca reproducir todas las funciones de un sistema operativo real, este componente permite mantener una estructura más cercana a los conceptos estudiados en sistemas operativos.

## Almacenamiento del PCB en memoria

Los atributos principales del PCB se representan directamente en la memoria simulada.

Esta decisión se tomó para que la información pueda observarse celda por celda desde la interfaz, permitiendo relacionar los conceptos teóricos del PCB con una representación concreta dentro de la memoria, según lo solicitado para esta tarea.

## Visualización del CPU

La interfaz incluye una representación visual de los registros y elementos principales del CPU.

La estructura utilizada se mantiene relacionada con la información del PCB para facilitar la visualización del estado actual del proceso y del procesador durante la ejecución.

## Configuración de memoria

El tamaño de la RAM y el espacio reservado para el Kernel pueden configurarse desde la interfaz, siempre y cuando no haya ningún proceso cargado.

En cualquier momento se puede limpiar el proceso actual y de esta manera se puede volver a configurar la memoria para luego cargar otro programa.

El tamaño mínimo del Kernel se estableció en **16 celdas**, aunque un proceso pueda requerir menos espacio, con el objetivo de mantener un margen suficiente para las estructuras posteriores.

Como configuración predeterminada se utilizan:

```text
Kernel: 64 celdas
RAM:    256 celdas
```

## Manejo de memoria insuficiente

El simulador verifica si existe suficiente espacio disponible antes de cargar un programa.

Si el programa excede la cantidad de celdas disponibles para el usuario, la carga no se realiza y se informa al usuario que la memoria disponible es insuficiente.

Esto permite representar una situación básica de administración de memoria y evita que las instrucciones sean colocadas fuera del espacio asignado.

## Configuración global

Se implementó `SystemConfig` para centralizar los parámetros generales de la Mini PC.

Esto evita distribuir valores de configuración por diferentes clases y permite que componentes como el controlador, la memoria y la interfaz trabajen con los mismos parámetros.

---

# Consideraciones y limitaciones

El simulador representa una computadora simplificada con fines académicos. Por lo tanto, algunos comportamientos de un sistema operativo o procesador real son abstraídos para mantener el modelo manejable.

Entre las principales consideraciones se encuentran:

* La arquitectura utiliza valores numéricos de 8 bits.
* El sistema administra un proceso mediante un modelo simplificado.
* El Kernel no representa un sistema operativo completo.
* La memoria RAM es una estructura simulada y no corresponde directamente a la RAM física del equipo.
* Las instrucciones disponibles dependen del conjunto de instrucciones definido por el proyecto.
* El Dispatcher representa de manera simplificada la administración del proceso.
* El comportamiento de overflow se encuentra limitado por la representación de 8 bits.
* La interfaz gráfica está orientada principalmente a la observación y demostración del funcionamiento interno del simulador.

---

# Tecnologías utilizadas

* **Java**
* **Java Swing**
* **FlatLaf 3.7.2**
* **Apache NetBeans**
* **Git / GitHub**

La aplicación no requiere conexión a Internet para ejecutar el simulador una vez que el proyecto y sus dependencias se encuentran disponibles localmente.

---

# Conclusión

Mini PC Simulator integra diferentes conceptos de arquitectura de sistemas operativos dentro de una aplicación interactiva.

El proyecto permite seguir el recorrido de un programa desde su archivo fuente `.asm`, pasando por la validación y carga en memoria, hasta su ejecución mediante el ciclo de búsqueda y ejecución del CPU.

La visualización de la memoria, los registros, el PCB y el estado del proceso permite utilizar el simulador como una herramienta de apoyo para comprender conceptos que normalmente se estudian de forma teórica.

Además, la separación entre modelo, servicios, controlador y vista permite mantener una estructura organizada y facilita la incorporación de nuevas funcionalidades en futuras versiones.

---

*JohnsyDev, 2026*
