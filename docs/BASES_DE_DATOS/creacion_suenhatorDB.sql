CREATE DATABASE suenhator;

-- Tabla CLIENTE
CREATE TABLE cliente (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellidos VARCHAR(150) NOT NULL,
  dni VARCHAR(20) NOT NULL UNIQUE,
  telefono VARCHAR(20),
  email VARCHAR(150) NOT NULL UNIQUE,
  fecha_alta DATE NOT NULL,
  fecha_nac DATE
);


-- Tabla INVITADO
CREATE TABLE invitado (
  id_invitado INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellidos VARCHAR(150) NOT NULL,
  dni VARCHAR(20) UNIQUE,
  telefono VARCHAR(20),
  email VARCHAR(150),
  fecha_nac DATE
);


-- Tabla PACK
CREATE TABLE pack (
  id_pack INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  tipo_pack VARCHAR(50) NOT NULL,
  duracion INT NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  es_premium BOOLEAN NOT NULL,
  aforo INT NOT NULL,
  es_18 BOOLEAN NOT NULL,
  
);


-- Tabla SALA
CREATE TABLE sala (
  id_sala INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  capacidad INT NOT NULL,
);


-- Tabla SUPERVISOR
CREATE TABLE supervisor (
  id_supervisor INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellidos VARCHAR(150) NOT NULL,
  dni VARCHAR(20) NOT NULL UNIQUE,
  telefono VARCHAR(20),
  email VARCHAR(150) UNIQUE
);


-- Tabla COMPRA
CREATE TABLE compra (
  id_compra INT AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  estado ENUM('pendiente', 'pagada', 'cancelada') NOT NULL,

  id_cliente INT NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)
  ON DELETE CASCADE ON UPDATE CASCADE,

);


-- Tabla PAGO
CREATE TABLE pago (
  id_pago INT AUTO_INCREMENT PRIMARY KEY,
  fecha_pago DATE NOT NULL,
  importe DECIMAL(10,2) NOT NULL,
  metodo ENUM('tarjeta', 'efectivo', 'transferencia', 'bizum') NOT NULL,

  id_compra INT NOT NULL,
  FOREIGN KEY (id_compra) REFERENCES compra (id_compra)
  ON DELETE CASCADE ON UPDATE CASCADE,

);


-- Tabla RESERVA
CREATE TABLE reserva (
  id_reserva INT AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  hora TIME NOT NULL,
  estado ENUM('pendiente', 'confirmada', 'cancelada', 'completada') NOT NULL,
  es_confirmado BOOLEAN NOT NULL,

  id_cliente INT NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)
  ON DELETE CASCADE ON UPDATE CASCADE,

  id_sala INT NOT NULL,
  FOREIGN KEY (id_sala) REFERENCES sala (id_sala)
  ON DELETE CASCADE ON UPDATE CASCADE,

  id_pack INT NOT NULL,
  FOREIGN KEY (id_pack) REFERENCES pack (id_pack)
  ON DELETE CASCADE ON UPDATE CASCADE,

  id_supervisor INT NOT NULL,
  FOREIGN KEY (id_supervisor) REFERENCES supervisor (id_supervisor)
  ON DELETE CASCADE ON UPDATE CASCADE
);


-- Tabla LINEA_COMPRA
CREATE TABLE linea_compra (
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,

  id_compra INT NOT NULL,
  id_pack INT NOT NULL,

  PRIMARY KEY (id_compra, id_pack),
  FOREIGN KEY (id_compra) REFERENCES compra (id_compra)
  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_pack) REFERENCES pack (id_pack)
  ON DELETE CASCADE ON UPDATE CASCADE,

  CHECK (cantidad > 0),
  CHECK (precio_unitario >= 0),
  CHECK (subtotal >= 0)
);


-- Tabla RESERVA_INVITADO
CREATE TABLE reserva_invitado (
  es_confirmado BOOLEAN NOT NULL,

  id_reserva INT NOT NULL,
  id_invitado INT NOT NULL,

  PRIMARY KEY (id_reserva, id_invitado),
  FOREIGN KEY (id_reserva) REFERENCES reserva (id_reserva)
  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_invitado) REFERENCES invitado (id_invitado)
  ON DELETE CASCADE ON UPDATE CASCADE
);


-- Tabla PERSONALIZACION
CREATE TABLE personalizacion (
  id_personalizacion INT AUTO_INCREMENT PRIMARY KEY,
  video_ref VARCHAR(255),
  descripcion TEXT,
  fecha_solicitud DATE NOT NULL,
  fecha_aprobacion DATE,
  estado ENUM('pendiente', 'enproceso', 'completada', 'rechazada') NOT NULL,

  id_reserva INT NOT NULL,
  FOREIGN KEY (id_reserva) REFERENCES reserva (id_reserva)
  ON DELETE CASCADE ON UPDATE CASCADE
);