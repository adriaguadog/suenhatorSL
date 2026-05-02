# Sueñator S.L.

Sueñator S.L. es una empresa ficticia dedicada a crear experiencias de realidad virtual personalizadas para dormir mejor, superar fobias y vivir “sueños” a la carta. Este proyecto forma parte del Proyecto Intermodular de 1º de DAW e integra varios módulos: Lenguajes de Marcas, Bases de Datos, Programación, Modulo Optativo y Sistemas Informáticos.

## Objetivo del proyecto

El objetivo general es diseñar y desarrollar un sistema completo para Sueñator S.L. que incluya:

- Un portal web corporativo donde se presenta la marca y sus servicios.
- Una base de datos relacional que modele clientes, packs, reservas, compras y pagos.
- Una aplicación Java (JavaFX) que gestione la lógica de negocio y se conecte a la base de datos mediante JDBC.
- Documentación técnica del entorno de ejecución.

A nivel de negocio, la empresa ofrece distintos packs de experiencias (relajación, traumas/fobias, fantasía, etc.) y necesita gestionar la relación con sus clientes: altas, reservas, compras, pagos y personalizaciones de las sesiones de realidad virtual.

## Tecnologías utilizadas

- HTML5: estructura semántica de la página, secciones, navegación y formulario.
- CSS: estilos y media queries para adaptación básica a móvil.
- SQL: definición de tablas, relaciones, restricciones y datos de ejemplo.
- Java + JavaFX + JDBC: aplicación de escritorio para la gestión de reservas, clientes, compras y pagos.

## Estructura del proyecto (vista general)

Actualmente el repositorio tiene, entre otras, la siguiente estructura básica:

- `index.html` → Página principal del portal web.
- `suenhator.css` → Hoja de estilos.
- `logo.jpg` → Logotipo de Sueñator S.L.
- `pack1.jpg`, `pack2.jpg`, `pack3.jpg`, `pack4.jpg` → Imágenes de los distintos packs y experiencias.
- `ovejitas.jpg`, `ovejitas2.jpg` → Imágenes de fondo.

En la parte de backend se añaden:

- `/src` → Código Java de la aplicación (modelo, DAOs, controladores y vistas JavaFX).
- `/DOCUMENTACION_BD/` → Scripts SQL y documentación del diseño de la base de datos.
- `/docs/sistemas/` → Informe técnico de entorno de ejecución y capturas.

## Estructura del repositorio

La estructura del repositorio se ha organizado para separar la parte visual del proyecto, la lógica de negocio, la conexión con base de datos y la documentación de cada módulo.

- `/.idea/`  
  Carpeta de configuración del proyecto para el entorno de desarrollo.

- `/DOCUMENTACION_BD/`  
  Carpeta donde se encuentra toda la documentación relacionada con la base de datos:
  - `Documentacion_BD.pdf`
  - `ER_suenhator.drawio`
  - `modelo_relacional.drawio`
  - `creacion_suenhatorDB.sql`
  - `insercion_suenhator.sql`
  - `queries.sql`
  - Archivos `.bkp` de respaldo de los diagramas.

- `/DOCUMENTACION_PROGRAMACION/`  
  Carpeta destinada a la documentación de la parte de programación.

- `/docs/`  
  Carpeta que contiene documentación adicional del proyecto y, en concreto:
  - `/docs/sistemas/` → Informe técnico del entorno de ejecución y capturas del módulo de Sistemas Informáticos.

- `/src/main/java/org/example/suenhator/`  
  Contiene el código fuente principal de la aplicación JavaFX, organizado por capas:
  - `/controller/` → Controladores de las vistas (`ClientesViewController`, `ComprasViewController`, `FormularioReservaViewController`, `MainViewController`, `PacksViewController`, `PagosViewController`, `RecursosViewController`, `RegistroViewController`, `ReservasViewController`).
  - `/dao/` → Clases DAO para el acceso a datos (`ClienteDAO`, `CompraDAO`, `InvitadoDAO`, `PackDAO`, `PagoDAO`, `PersonalizacionDAO`, `ReservaDAO`, `SalaDAO`, `SupervisorDAO`).
  - `/database/` → Clases de conexión y esquema (`DBConnection.java`, `SchemDB.java`).
  - `/model/` → Clases del modelo de datos (`Cliente`, `Compra`, `Invitado`, `LineaCompra`, `Pack`, `Pago`, `Personalizacion`, `Reserva`, `ReservaInvitado`, `Sala`, `Supervisor`).
  - `/model/enums/` → Enumerados del sistema (`EstadoCompra`, `EstadoPersonalizacion`, `EstadoReserva`, `MetodoPago`).
  - `/utils/` → Clases auxiliares (`AlertCreation.java`, `ViewLoader.java`).
  - `HelloApplication.java` → Punto de entrada principal de la aplicación.
  - `module-info.java` → Configuración modular del proyecto Java.

- `/src/main/resources/org/example/suenhator/`  
  Contiene los recursos visuales de la aplicación JavaFX:
  - `clientes-view.fxml`
  - `compras-view.fxml`
  - `formReserva-view.fxml`
  - `main-view.fxml`
  - `packs-view.fxml`
  - `pagos-view.fxml`
  - `recursos-view.fxml`
  - `registro-view.fxml`
  - `reservas-view.fxml`
  - `styles.css`

- `/target/`  
  Carpeta generada automáticamente por Maven con los archivos compilados del proyecto.

- `.gitignore`  
  Archivo que indica qué ficheros o carpetas no deben subirse al repositorio.

Esta organización permite mantener el proyecto ordenado y separar claramente la interfaz, la lógica de negocio, el acceso a base de datos y la documentación generada para cada módulo del intermodular.

## Páginas y secciones (parte web)

El portal está montado en una sola página principal (index.html) organizada en secciones:

- **Header**  
Logotipo de Sueñator S.L. y menú de navegación con enlaces internos a las secciones.

- **Presentación / Hero**  
Explicación breve de qué es Sueñator S.L. y qué tipo de experiencias ofrece, junto con una imagen relacionada con la realidad virtual y los sueños.

- **Packs y experiencias**  
Bloque con “cards” donde se muestran los distintos packs. Cada card incluye título, descripción, imagen y botón de acción. Se utilizan display: grid y flex para organizar los elementos.

- **Precios**  
Apartado con los precios de cada pack y una breve descripción de lo que incluye.

- **Reserva / Contacto**  
Formulario para que la persona usuaria pueda introducir sus datos, elegir pack o experiencia y añadir comentarios o personalizaciones. Se usan fieldset, label, input, select y textarea.

- **Nota legal / Avisos**  
Texto con condiciones y aclaraciones sobre el servicio ficticio.

- **Footer**  
Información básica de la empresa.

## Cómo visualizar la parte web

1. Clonar el repositorio o descargarlo como archivo .zip.
2. Abrir el archivo index.html directamente en el navegador.

La web está pensada para verse bien en escritorio y tiene una adaptación para móviles con una media query a 768px.

## Base de datos: diseño de SuenhatorDB

La base de datos suenhator está diseñada para cubrir las necesidades reales de Sueñator S.L. como empresa de experiencias de realidad virtual. El objetivo principal es permitir que la aplicación gestione todo el ciclo de la relación con los clientes: altas y bajas, reservas de experiencias, compras de packs, pagos, asistentes invitados y personalizaciones de las sesiones.

La aplicación JavaFX utiliza esta base de datos mediante JDBC para ofrecer funcionalidades como: alta, modificación y baja de clientes; consulta de packs disponibles; gestión de recursos (salas y supervisores); creación, modificación y cancelación de reservas; búsqueda de reservas por DNI o por supervisor; registro de compras y pagos; y consulta de compras pendientes de pago.

El esquema se ha organizado en varios bloques lógicos.

### 1. Gestión de clientes e invitados

- **cliente**  
Almacena los datos principales de la persona que contrata y paga las experiencias: nombre, apellidos, DNI (único), teléfono, email (único), fecha de alta y fecha de nacimiento. Esto permite identificar de forma fiable a cada cliente, contactar con él y gestionar el alta, modificación y baja desde la aplicación.

- **invitado**  
Representa a las personas que participarán en una experiencia pero que no tienen por qué ser quienes pagan. Incluye nombre, apellidos, DNI opcional, teléfono, email y fecha de nacimiento.

Una reserva puede incluir tanto al cliente principal como a varios invitados, lo que encaja con la idea de asistir en grupo o regalar experiencias de realidad virtual a otras personas.

### 2. Catálogo de packs, salas y supervisores

- **pack**  
Define los distintos packs de experiencias que ofrece Sueñator: nombre, descripción, tipo de pack, duración, precio, si es premium, aforo máximo y si es solo para mayores de 18 años.

La aplicación consulta esta tabla para mostrar los packs disponibles y sus detalles, y para validar aspectos como el aforo o si el pack es solo para adultos.

- **sala**  
Representa las salas físicas de la empresa donde se realizan las experiencias de realidad virtual. Se almacena un identificador, el nombre y la capacidad.

Esta tabla se utiliza para la gestión de recursos de la empresa, permitiendo dar de alta y baja salas y asignar cada reserva a una sala concreta respetando el aforo.

- **supervisor**  
Almacena el personal que supervisa las sesiones: nombre, apellidos, DNI (único), teléfono y email (único).

Esto permite gestionar la plantilla de supervisores (alta, baja y consulta) y asociar cada reserva a un supervisor específico, lo que facilita búsquedas de reservas por supervisor desde la aplicación.

### 3. Ciclo de venta: compras, líneas y pagos

La parte de compras se ha separado claramente de las reservas para soportar escenarios como comprar un pack por adelantado y reservar más tarde.

- **compra**  
Tabla cabecera de la compra que realiza un cliente. Incluye fecha, total, estado (pendiente, pagada, cancelada) y una relación con el cliente que la realiza.

Esto permite:

- Registrar compras realizadas antes de reservar (por ejemplo, compra de un pack regalo).
- Tener compras pendientes de pago y marcarlas como pagadas cuando se abonan presencialmente.
- Consultar el listado de compras de cada cliente.

- **linea_compra**  
Detalle de cada compra. Almacena la cantidad, el precio unitario, el subtotal, el pack asociado y la relación con la compra. La clave primaria compuesta (id_compra, id_pack) evita duplicar un mismo pack dentro de la misma compra y las restricciones CHECK garantizan que cantidades y precios sean positivos.

Esto permite que una compra incluya varios packs y deja registrado el importe exacto en el momento de la compra, incluso si el precio del pack cambia más adelante en el catálogo.

- **pago**  
Registra los pagos que se realizan sobre cada compra: fecha, importe y método (tarjeta, efectivo, transferencia, bizum), más la relación con la compra.

Con esta tabla, la aplicación puede:

- Registrar pagos completos o parciales en el momento presencial.
- Consultar pagos de un cliente.
- Mostrar compras que todavía están pendientes de pago en la vista de pagos.

### 4. Reservas, asistentes y personalizaciones

La parte central de la aplicación es la gestión de reservas, ya que representa las experiencias reales que se van a vivir.

- **reserva**  
Tabla que vincula al cliente con la experiencia concreta que se va a realizar. Incluye fecha, hora, estado (pendiente, confirmada, cancelada, completada), un indicador de confirmación, y claves foráneas a cliente, sala, pack y supervisor.

Esto permite:

- Crear nuevas reservas desde la aplicación (por teléfono, en recepción, etc.).
- Modificar o cancelar reservas existentes.
- Buscar reservas por DNI del cliente o por supervisor.
- Consultar todos los datos relevantes de la reserva (pack, sala, supervisor, estado y confirmación).

En el flujo de negocio, la reserva puede crearse:

- Antes de realizar el pago (quedando la compra asociada como pendiente).
- A partir de una compra ya existente, por ejemplo cuando alguien compra un pack para regalar y la otra persona reserva más tarde.

- **reserva_invitado**  
Tabla intermedia que relaciona reserva e invitado (relación N:M) con una clave primaria compuesta (id_reserva, id_invitado) y un campo es_confirmado.

Gracias a esta tabla, la aplicación puede:

- Añadir varios invitados a la misma reserva.
- Saber qué invitados han confirmado su asistencia.
- Controlar el número de asistentes y compararlo con el aforo de la sala y del pack.

- **personalizacion**  
Tabla que almacena las personalizaciones de una reserva (por ejemplo, vídeos de referencia, instrucciones especiales o contenido adaptado a la persona). Incluye video_ref, descripción, fecha de solicitud, fecha de aprobación, estado (pendiente, enproceso, completada, rechazada) y una relación directa con una reserva concreta.

Esto cubre la parte de “experiencias de realidad virtual personalizadas”: la empresa puede registrar qué personalizaciones ha pedido el cliente, en qué estado están y cuándo se han aprobado, y la aplicación puede mostrar y actualizar esa información.

### 5. Relación entre reservas y compras

Aunque en el script de creación las tablas reserva y compra no están unidas con una clave foránea directa, el modelo está pensado para que la lógica de la aplicación conecte ambos conceptos según el flujo de negocio:

- Se puede crear una compra antes de reservar, para contemplar el caso de regalos o compras anticipadas de packs.
- Al crear una reserva, la aplicación puede preguntar si se desea generar la compra en ese momento o dejarla pendiente.
- La aplicación permite consultar reservas por cliente, compras por cliente, pagos de cada compra y compras pendientes de pago, lo que cubre las funcionalidades descritas para la vista de pagos y la gestión económica.

En resumen:

- La reserva refleja la ejecución real de la experiencia (día, hora, sala, supervisor, estado).
- La compra y el pago reflejan la parte económica (qué se ha comprado y cómo se ha pagado).

### 6. Integridad y reglas de negocio

Para mantener la integridad de los datos, el diseño incluye:

- Claves foráneas con ON DELETE CASCADE y ON UPDATE CASCADE en todas las relaciones importantes (compra–cliente, linea_compra–compra y pack, reserva–cliente/sala/pack/supervisor, pago–compra, reserva_invitado–reserva/invitado, personalizacion–reserva). Esto evita datos huérfanos y mantiene la coherencia al borrar o actualizar registros.
- Restricciones CHECK en linea_compra para que las cantidades y los importes no sean negativos.
- Tipos ENUM para estados de compra, reserva, pago y personalización, de forma que solo se usen estados válidos y controlados por la lógica del negocio.

Este diseño permite que la aplicación JavaFX implemente de forma natural las funcionalidades de búsqueda de reservas por DNI o supervisor, creación y modificación de reservas, gestión de compras y pagos, consultas de packs disponibles y gestión de recursos internos (salas y supervisores).

### 7. Uso del esquema en el proyecto

1. Crear la base de datos y las tablas ejecutando el script `creacion_suenhatorDB.sql` sobre el servidor MySQL.
2. Cargar los datos de ejemplo ejecutando el script de inserción correspondiente en un orden coherente con las relaciones: primero tablas maestras (cliente, invitado, pack, sala, supervisor) y después tablas dependientes (compra, linea_compra, pago, reserva, reserva_invitado, personalizacion).
3. Configurar la conexión JDBC en la aplicación Java para apuntar a la base de datos suenhator y utilizar los DAOs que trabajan contra este esquema.

## Futuras ampliaciones

### Bases de Datos

Seguir refinando el modelo, añadir más validaciones y procedimientos si fuese necesario, y ampliar la documentación con más ejemplos de consultas típicas (por cliente, por rango de fechas, por estado de reserva, etc.).

### Programación y MPO

Ampliar la aplicación Java para explotar todo el potencial del esquema: filtros avanzados de reservas, generación de informes, estadísticas de uso de packs, etc. Se podria valorar el anhadir restricciones al crear una reserva en el caso de que el numero de invitados supere el aforo de la sala o del pack. Se podria escalar implementando un precio por invitado.

### Sistemas Informáticos

Mantener actualizado el informe técnico, revisar requisitos y entorno según evolucionen la aplicación y la base de datos, e incorporar nuevas evidencias de funcionamiento si el proyecto crece.
