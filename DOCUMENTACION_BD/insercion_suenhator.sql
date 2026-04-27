INSERT INTO cliente (nombre, apellidos, dni, telefono, email, fecha_alta, fecha_nac) VALUES
('Lucia', 'Martinez Gomez', '11111111A', '600111222', 'lucia.martinez@gmail.com', '2026-01-10', '1998-04-12'),
('Carlos', 'Ruiz Fernandez', '22222222B', '600222333', 'carlos.ruiz@gmail.com', '2026-01-15', '1995-08-21'),
('Marta', 'Lopez Sanchez', '33333333C', '600333444', 'marta.lopez@gmail.com', '2026-02-01', '2000-02-18'),
('Javier', 'Moreno Diaz', '44444444D', '600444555', 'javier.moreno@gmail.com', '2026-02-10', '1992-11-30'),
('Ana', 'Gil Romero', '55555555E', '600555666', 'ana.gil@gmail.com', '2026-03-05', '1999-06-25');

INSERT INTO invitado (nombre, apellidos, dni, telefono, email, fecha_nac) VALUES
('Sergio', 'Navarro Perez', '66666666F', '611111111', 'sergio.navarro@gmail.com', '1997-03-14'),
('Elena', 'Torres Vega', '77777777G', '622222222', 'elena.torres@gmail.com', '2001-07-09'),
('Pablo', 'Castro Molina', '88888888H', '633333333', 'pablo.castro@gmail.com', '1996-12-01'),
('Raquel', 'Ortega Ruiz', '99999999I', '644444444', 'raquel.ortega@gmail.com', '1998-10-20'),
('Diego', 'Herrera Leon', '10101010J', '655555555', 'diego.herrera@gmail.com', '1994-05-17');

INSERT INTO pack (nombre, descripcion, tipo_pack, duracion, precio, es_premium, aforo, es_18) VALUES
('Pack Básico', 'Para empezar sin liarla demasiado. Perfecto para una primera experiencia.', 'basico', 40, 39.99, 0, 1, 0),
('Pack Aventura', 'Para quienes quieren algo más intenso y surrealista: playas paradisíacas, aventuras espaciales o terrorífica casa embrujada.', 'aventura', 60, 49.99, 0, 2, 0),
('Pack Trauma', 'Ideal para enfrentarte a tus miedos: aracnofobia, dejar de fumar, hablar en público o miedo a las alturas. A partir de 15 años.', 'trauma', 90, 55.00, 0, 2, 0),
('Pack Premium', 'Vive tus fantasías más raras con acabado deluxe y personajes personalizables. Incluye la opción de adaptar hasta 2 personajes.', 'premium', 90, 99.99, 1, 3, 1);

INSERT INTO sala (nombre, capacidad) VALUES
('Sala Playa Onírica', 3),
('Sala Mansión Zombi', 3),
('Sala Bosque de Nubes', 2),
('Sala Oficina Dramática', 2);

INSERT INTO supervisor (nombre, apellidos, dni, telefono, email) VALUES
('Alberto', 'Jimenez Soto', '13131313L', '677111222', 'alberto.jimenez@suenhator.com'),
('Patricia', 'Mendez Cano', '14141414M', '677222333', 'patricia.mendez@suenhator.com'),
('Ruben', 'Flores Vidal', '15151515N', '677333444', 'ruben.flores@suenhator.com');

INSERT INTO compra (id_cliente, fecha, total, estado) VALUES
(1, '2026-03-10', 39.99, 'pagada'),
(2, '2026-03-12', 49.99, 'pagada'),
(3, '2026-03-15', 55.00, 'pendiente'),
(4, '2026-03-18', 99.99, 'cancelada'),
(5, '2026-03-20', 99.99, 'pagada');

INSERT INTO linea_compra (id_compra, id_pack, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 39.99, 39.99),
(2, 2, 1, 49.99, 49.99),
(3, 3, 1, 55.00, 55.00),
(4, 4, 1, 99.99, 99.99),
(5, 4, 1, 99.99, 99.99);

INSERT INTO pago (id_compra, fecha_pago, importe, metodo) VALUES
(1, '2026-03-10', 39.99, 'tarjeta'),
(2, '2026-03-12', 49.99, 'bizum'),
(3, '2026-03-16', 55.00, 'transferencia'),
(4, '2026-03-18', 99.99, 'efectivo'),
(5, '2026-03-20', 99.99, 'tarjeta');

INSERT INTO reserva (id_cliente, id_sala, id_pack, id_supervisor, fecha, hora, estado, es_confirmado) VALUES
(1, 1, 1, 1, '2026-04-01', '18:00:00', 'confirmada', 1),
(2, 2, 2, 2, '2026-04-02', '20:00:00', 'confirmada', 1),
(3, 3, 3, 1, '2026-04-05', '17:30:00', 'pendiente', 0),
(4, 4, 4, 3, '2026-04-08', '21:00:00', 'cancelada', 0),
(5, 3, 4, 2, '2026-04-10', '19:00:00', 'completada', 1);

INSERT INTO reserva_invitado (id_reserva, id_invitado, es_confirmado) VALUES
(1, 1, 1),
(1, 2, 1),
(2, 3, 1),
(2, 4, 0),
(3, 5, 0);

INSERT INTO personalizacion (id_reserva, video_ref, descripcion, fecha_solicitud, fecha_aprobacion, estado) VALUES
(1, 'video_bienvenida_lucia.mp4', 'Video de bienvenida con mensaje personalizado para el grupo.', '2026-03-25', '2026-03-28', 'completada'),
(2, 'intro_pack_aventura.mp4', 'Intro especial para Pack Aventura con nombres de los participantes.', '2026-03-27', '2026-03-30', 'enproceso'),
(3, NULL, 'Decoracion tematica para trabajar traumas y fobias de forma suave.', '2026-03-29', NULL, 'pendiente'),
(5, 'premium_personalizado_ana.mp4', 'Experiencia premium con historia totalmente adaptada al cliente.', '2026-04-01', '2026-04-05', 'completada');
