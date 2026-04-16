-- 1) Listar todos los clientes.
SELECT *
FROM cliente;

-- 2) Packs con duración y precio, de más barato a más caro.
SELECT id_pack, nombre, duracion, precio
FROM pack
ORDER BY precio ASC;

-- 3) Salas disponibles con su capacidad.
SELECT *
FROM sala
WHERE estado = 'disponible';

-- 4) Compras con su cliente y estado.
SELECT co.id_compra,
       co.id_cliente,
       cl.nombre,
       cl.apellidos,
       co.fecha,
       co.total,
       co.estado
FROM compra co
INNER JOIN cliente cl
        ON co.id_cliente = cl.id_cliente
ORDER BY co.fecha;

-- 5) Pagos completados con el método usado.
SELECT id_pago, id_compra, importe, metodo, fecha_pago
FROM pago
WHERE estado = 'completado';

-- 6) Reservas con nombre de cliente y nombre de pack.
SELECT r.id_reserva,
       r.id_cliente,
       c.nombre,
       c.apellidos,
       p.nombre AS nombre_pack,
       r.fecha,
       r.hora,
       r.estado
FROM reserva r
INNER JOIN cliente c ON r.id_cliente = c.id_cliente
INNER JOIN pack    p ON r.id_pack    = p.id_pack
ORDER BY r.fecha, r.hora;

-- 7) Reservas con sala y supervisor.
SELECT r.id_reserva,
       s.nombre AS nombre_sala,
       su.nombre AS nombre_supervisor,
       su.apellidos AS apellidos_supervisor,
       r.fecha,
       r.hora,
       r.estado
FROM reserva r
INNER JOIN sala       s  ON r.id_sala       = s.id_sala
INNER JOIN supervisor su ON r.id_supervisor = su.id_supervisor;

-- 8) Clientes sin ninguna reserva.
SELECT c.id_cliente,
       c.nombre,
       c.apellidos
FROM cliente c
LEFT JOIN reserva r
       ON c.id_cliente = r.id_cliente
WHERE r.id_reserva IS NULL;

-- 9) Reservas sin invitados.
SELECT r.id_reserva,
       r.fecha,
       r.hora,
       r.estado
FROM reserva r
LEFT JOIN reserva_invitado ri
       ON r.id_reserva = ri.id_reserva
WHERE ri.id_invitado IS NULL;

-- 10) Invitados que han confirmado asistencia en alguna reserva.
SELECT DISTINCT i.id_invitado,
       i.nombre,
       i.apellidos
FROM invitado i
INNER JOIN reserva_invitado ri
        ON i.id_invitado = ri.id_invitado
WHERE ri.es_confirmado = 1;

-- 11) Número de reservas por cliente.
SELECT c.id_cliente,
       c.nombre,
       c.apellidos,
       COUNT(r.id_reserva) AS numero_reservas
FROM cliente c
LEFT JOIN reserva r
       ON c.id_cliente = r.id_cliente
GROUP BY c.id_cliente, c.nombre, c.apellidos
ORDER BY numero_reservas DESC;

-- 12) Veces que se ha reservado cada pack.
SELECT p.id_pack,
       p.nombre,
       COUNT(r.id_reserva) AS veces_reservado
FROM pack p
LEFT JOIN reserva r
       ON p.id_pack = r.id_pack
GROUP BY p.id_pack, p.nombre
ORDER BY veces_reservado DESC;

-- 13) Importe total pagado por cada cliente (solo pagos completados).
SELECT c.id_cliente,
       c.nombre,
       c.apellidos,
       SUM(pa.importe) AS total_pagado
FROM cliente c
INNER JOIN compra co
        ON c.id_cliente = co.id_cliente
INNER JOIN pago pa
        ON co.id_compra = pa.id_compra
WHERE pa.estado = 'completado'
GROUP BY c.id_cliente, c.nombre, c.apellidos
ORDER BY total_pagado DESC;

-- 14) Reservas a partir de una fecha concreta.
SELECT id_reserva, id_cliente, fecha, hora, estado
FROM reserva
WHERE fecha >= '2026-04-01'
ORDER BY fecha, hora;

-- 15) Reservas del Pack Premium con sus clientes.
SELECT r.id_reserva,
       r.id_cliente,
       c.nombre,
       c.apellidos,
       r.fecha,
       r.hora,
       r.estado
FROM reserva r
INNER JOIN pack p
        ON r.id_pack = p.id_pack
INNER JOIN cliente c
        ON r.id_cliente = c.id_cliente
WHERE p.nombre = 'Pack Premium';

-- 16) Clientes que han comprado más de un pack distinto.
SELECT c.id_cliente,
       c.nombre,
       c.apellidos,
       COUNT(DISTINCT lc.id_pack) AS packs_distintos
FROM cliente c
INNER JOIN compra co
        ON c.id_cliente = co.id_cliente
INNER JOIN linea_compra lc
        ON co.id_compra = lc.id_compra
GROUP BY c.id_cliente, c.nombre, c.apellidos
HAVING COUNT(DISTINCT lc.id_pack) > 1;

-- 17) Detalle de la reserva 1 con sus invitados.
SELECT r.id_reserva,
       r.fecha,
       r.hora,
       c.nombre      AS nombre_cliente,
       c.apellidos   AS apellidos_cliente,
       p.nombre      AS nombre_pack,
       i.id_invitado,
       i.nombre      AS nombre_invitado,
       i.apellidos   AS apellidos_invitado,
       ri.es_confirmado
FROM reserva r
INNER JOIN cliente c          ON r.id_cliente    = c.id_cliente
INNER JOIN pack p             ON r.id_pack       = p.id_pack
LEFT JOIN reserva_invitado ri ON r.id_reserva    = ri.id_reserva
LEFT JOIN invitado i          ON ri.id_invitado  = i.id_invitado
WHERE r.id_reserva = 1;

-- 18) Personalizaciones con estado y datos del cliente.
SELECT pe.id_personalizacion,
       pe.id_reserva,
       pe.estado,
       pe.fecha_solicitud,
       pe.fecha_aprobacion,
       pe.video_ref,
       c.id_cliente,
       c.nombre,
       c.apellidos
FROM personalizacion pe
INNER JOIN reserva r ON pe.id_reserva = r.id_reserva
INNER JOIN cliente c ON r.id_cliente  = c.id_cliente;

-- 19) Reservas que tienen personalización.
SELECT r.id_reserva,
       r.fecha,
       r.hora,
       p.nombre AS nombre_pack,
       pe.estado AS estado_personalizacion
FROM reserva r
INNER JOIN pack p           ON r.id_pack      = p.id_pack
INNER JOIN personalizacion pe ON r.id_reserva = pe.id_reserva;

