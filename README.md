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

Programación y MPO
Crear una aplicación en Java que gestione las reservas y se conecte a la base de datos mediante JDBC.

Sistemas Informáticos
Documentar el entorno de ejecución, sistema operativo, requisitos, instalación y mantenimiento básico.
