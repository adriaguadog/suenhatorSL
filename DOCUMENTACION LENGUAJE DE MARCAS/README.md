Sueñator S.L.

Sueñator S.L. es una empresa ficticia dedicada a crear experiencias de realidad virtual personalizadas para dormir mejor, superar fobias y vivir “sueños” a la carta. Este proyecto forma parte del Proyecto Intermodular de 1º de DAW y, de momento, incluye la parte de Lenguajes de Marcas (HTML y CSS).

    Objetivo del proyecto

El objetivo es diseñar y maquetar un portal web corporativo que pudiera tener esta empresa en la vida real:

- Presentar la marca y el concepto de Sueñator S.L.
- Mostrar los distintos packs de experiencias (relajación, traumas/fobias, fantasía, etc.).
- Permitir que la persona usuaria pueda solicitar información o reservar una experiencia a través de un formulario.
- Dejar la base preparada para conectar en el futuro con una BBDD y una aplicación Java (para los módulos de Programación y Bases de Datos).

La idea es que sea un proyecto “realista pero divertido”, con un toque de humor, pero que se pueda usar como ejemplo de trabajo para el portfolio.

    Tecnologías utilizadas

HTML5: estructura semántica de la página, secciones, navegación y formulario.
CSS: estilos y media queries para adaptación básica a móvil.

    Estructura del proyecto

Actualmente el repositorio tiene la siguiente estructura básica:

- index.html → Página principal del portal (todo el contenido).
- suenhator.css → Hoja de estilos.
- logo.jpg → Logotipo de Sueñator S.L.
- pack1.jpg, pack2.jpg, pack3.jpg, pack4.jpg → Imágenes de los distintos packs y experiencias.
- ovejitas.jpg, ovejitas2.jpg → Imágenes de fondo.

Más adelante se añadirá una carpeta /src para el código Java y una carpeta /sql para los scripts de Base de Datos, siguiendo la idea del intermodular.

    Páginas y secciones

El portal está montado en una sola página principal (index.html) organizada en secciones:

- Header
  Logotipo de Sueñator S.L.
  Menú de navegación con enlaces internos a las secciones.

- Presentación / Hero
  Explicación breve de qué es Sueñator S.L. y qué tipo de experiencias ofrece.
  Imagen principal relacionada con la realidad virtual y los sueños.

- Packs y experiencias
  Bloque con cards donde se muestran los distintos packs.
  Cada card incluye título, descripción, imagen y botón de acción.
  Se utilizan display: grid y flex para organizar los elementos.

- Precios
  Apartado con los precios de cada pack y una breve descripción de lo que incluye.

- Reserva / Contacto
  Formulario para que el usuario pueda:
  Introducir sus datos.
  Elegir pack o experiencia.
  Añadir comentarios o personalizaciones.
  Se usan fieldset, label, input, select y textarea.

- Nota legal / Avisos
  Texto con condiciones y aclaraciones sobre el servicio ficticio.

- Footer
  Información básica de la empresa ficticia y recordatorio de que se trata de un proyecto académico.

      Cómo visualizar el proyecto

Clonar el repositorio o descargarlo como archivo zip.
Abrir el archivo index.html directamente en el navegador.
No es necesaria ninguna instalación extra, solo hace falta un navegador moderno.

La web está pensada para verse bien en escritorio y tiene una adaptación básica para móviles con una media query a 768px.

    Futuras ampliaciones (Intermodular)

La idea es ir ampliando el proyecto para el resto de módulos:

Bases de Datos
Diseñar el modelo entidad relación y la base de datos de Sueñator S.L. (clientes, reservas, experiencias, pagos, etc.).
Diseñar el modelo entidad relación y la base de datos de Sueñator S.L. (clientes, reservas, experiencias, pagos, etc.).

Programación y MPO
Crear una aplicación en Java que gestione las reservas y se conecte a la base de datos mediante JDBC.

Sistemas Informáticos
Documentar el entorno de ejecución, sistema operativo, requisitos, instalación y mantenimiento básico.

---

## Base de datos: SuenhatorDB

La base de datos **suenhator** modela un negocio de experiencias inmersivas y sesiones personalizadas de fantasías/sueños dirigidas a clientes individuales o pequeños grupos. El diseño está normalizado y organizado en tres bloques principales: gestión de clientes e invitados, catálogo de packs/salas/supervisores y ciclo de venta–reserva–personalización.

#### Cliente e invitados

- **cliente**: representa a la persona que contrata y paga la experiencia.  
  Justificación: se necesita identificar al pagador de forma fiable (DNI/email únicos) y poder contactar con él.

- **invitado**: personas que participan en la sesión pero no necesariamente pagan.  
  Justificación: permite gestionar grupos donde el cliente reserva y abona, pero otras personas asisten y pueden confirmar asistencia individualmente.

#### Catálogo de packs y salas

- **pack**: define el producto que se vende (tipo de experiencia).  
  Justificación: se cubren las cuatro ofertas reales del negocio con sus duraciones y precios oficiales.

- **sala**: espacios físicos donde se realizan las sesiones.  
  Justificación: cada reserva se asigna a una sala concreta y el estado permite bloquear salas en mantenimiento.

- **supervisor**: personal encargado de controlar la experiencia.  
  Justificación: cada reserva está supervisada por un trabajador concreto y se evita duplicar personas con DNI/email.

### 3. Ciclo de venta: compra, líneas y pago

- **compra**: cabecera del pedido de un cliente.  
  Justificación: modelo clásico de cabecera de pedido independiente de la reserva concreta; permite registrar compras futuras y cancelaciones.

- **linea_compra**: detalle de cada pack comprado.  
  Justificación: permite que una compra incluya varios packs, mantiene trazabilidad de precios y cantidades sin recalcular sobre el catálogo.

#### Pagos

- **pago**: registros de cobros asociados a una compra.  
  Justificación: permite gestionar pagos parciales, fallidos o reembolsos sin alterar la compra original.

### 4. Reservas, asistentes y personalización

- **reserva**: vincula al cliente con la ejecución real de la experiencia.  
  Diseño de datos: las reservas de ejemplo cubren los distintos packs y salas, con estados variados (confirmadas, canceladas, pendientes y completadas).

#### Invitados por reserva

- **reserva_invitado**: tabla de relación N:M entre `reserva` e `invitado`.  
  Justificación: un cliente puede llevar varios invitados a la misma reserva y cada uno puede confirmar o no su asistencia de forma independiente.

#### Personalización de la experiencia

- **personalizacion**: información extra asociada a una reserva concreta.  
  Justificación: soporta los casos de Pack Premium y otros packs con material previo (vídeos, instrucciones) y seguimiento de estado distinto al de la reserva.

### 5. Decisiones de diseño e integridad

- Integridad referencial: todas las tablas hijas usan claves foráneas con `ON DELETE CASCADE`, de forma que borrar un cliente o una reserva limpia automáticamente sus datos relacionados.
- Claves únicas: `dni` y `email` se han marcado como únicos donde tiene sentido (cliente, supervisor, invitado para DNI) para evitar duplicidades.
- Datos de ejemplo coherentes:
  - Los importes de `pago` coinciden con los totales de `compra` y los precios de `linea_compra`.
  - Las duraciones y precios de `pack` son los mismos que se muestran en la web.
  - Los `id_reserva` usados en `reserva_invitado` y `personalizacion` existen realmente en `reserva`, y solo se usan invitados 1–5, que son los creados.

### 6. Uso del esquema de base de datos

1. Ejecutar el script de creación de tablas (`creacion_suenhatorDB.sql`) sobre el esquema `suenhator`.
2. Ejecutar después el script de inserción de datos insercion_suenhator.sql en este orden lógico:  
   `cliente`, `invitado`, `pack`, `sala`, `supervisor`, `compra`, `linea_compra`, `pago`, `reserva`, `reserva_invitado`, `personalizacion`.

A partir de ahí, la aplicación Java podrá conectarse mediante JDBC para gestionar clientes, compras, pagos, reservas, asistentes y personalizaciones.

Programación y MPO
Crear una aplicación en Java que gestione las reservas y se conecte a la base de datos mediante JDBC.

Sistemas Informáticos
Documentar el entorno de ejecución, sistema operativo, requisitos, instalación y mantenimiento básico.
