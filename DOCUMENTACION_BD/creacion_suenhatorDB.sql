-- suenhator.cliente definition

CREATE TABLE `cliente` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(150) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(150) NOT NULL,
  `fecha_alta` date NOT NULL,
  `fecha_nac` date DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.invitado definition

CREATE TABLE `invitado` (
  `id_invitado` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(150) NOT NULL,
  `dni` varchar(20) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `fecha_nac` date DEFAULT NULL,
  PRIMARY KEY (`id_invitado`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.pack definition

CREATE TABLE `pack` (
  `id_pack` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `tipo_pack` varchar(50) NOT NULL,
  `duracion` int(11) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `es_premium` tinyint(1) NOT NULL DEFAULT 0,
  `aforo` int(11) NOT NULL,
  `es_18` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_pack`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.sala definition

CREATE TABLE `sala` (
  `id_sala` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `capacidad` int(11) NOT NULL,
  `estado` enum('disponible','ocupada','mantenimiento') NOT NULL,
  PRIMARY KEY (`id_sala`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.supervisor definition

CREATE TABLE `supervisor` (
  `id_supervisor` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(150) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id_supervisor`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.compra definition

CREATE TABLE `compra` (
  `id_compra` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `estado` enum('pendiente','pagada','cancelada') NOT NULL,
  PRIMARY KEY (`id_compra`),
  KEY `fk_compra_cliente` (`id_cliente`),
  CONSTRAINT `fk_compra_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.linea_compra definition

CREATE TABLE `linea_compra` (
  `id_compra` int(11) NOT NULL,
  `id_pack` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_compra`,`id_pack`),
  KEY `fk_linea_compra_pack` (`id_pack`),
  CONSTRAINT `fk_linea_compra_compra` FOREIGN KEY (`id_compra`) REFERENCES `compra` (`id_compra`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_linea_compra_pack` FOREIGN KEY (`id_pack`) REFERENCES `pack` (`id_pack`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.pago definition

CREATE TABLE `pago` (
  `id_pago` int(11) NOT NULL AUTO_INCREMENT,
  `id_compra` int(11) NOT NULL,
  `fecha_pago` date NOT NULL,
  `importe` decimal(10,2) NOT NULL,
  `metodo` enum('tarjeta','efectivo','transferencia','bizum') NOT NULL,
  `estado` enum('pendiente','completado','fallido','reembolsado') NOT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `fk_pago_compra` (`id_compra`),
  CONSTRAINT `fk_pago_compra` FOREIGN KEY (`id_compra`) REFERENCES `compra` (`id_compra`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.reserva definition

CREATE TABLE `reserva` (
  `id_reserva` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `id_sala` int(11) NOT NULL,
  `id_pack` int(11) NOT NULL,
  `id_supervisor` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `duracion` int(11) NOT NULL,
  `estado` enum('pendiente','confirmada','cancelada','completada') NOT NULL,
  `es_confirmado` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_reserva`),
  KEY `fk_reserva_cliente` (`id_cliente`),
  KEY `fk_reserva_pack` (`id_pack`),
  KEY `fk_reserva_sala` (`id_sala`),
  KEY `fk_reserva_supervisor` (`id_supervisor`),
  CONSTRAINT `fk_reserva_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reserva_pack` FOREIGN KEY (`id_pack`) REFERENCES `pack` (`id_pack`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reserva_sala` FOREIGN KEY (`id_sala`) REFERENCES `sala` (`id_sala`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reserva_supervisor` FOREIGN KEY (`id_supervisor`) REFERENCES `supervisor` (`id_supervisor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.reserva_invitado definition

CREATE TABLE `reserva_invitado` (
  `id_reserva` int(11) NOT NULL,
  `id_invitado` int(11) NOT NULL,
  `es_confirmado` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id_reserva`,`id_invitado`),
  KEY `fk_reserva_invitado_invitado` (`id_invitado`),
  CONSTRAINT `fk_reserva_invitado_invitado` FOREIGN KEY (`id_invitado`) REFERENCES `invitado` (`id_invitado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reserva_invitado_reserva` FOREIGN KEY (`id_reserva`) REFERENCES `reserva` (`id_reserva`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- suenhator.personalizacion definition

CREATE TABLE `personalizacion` (
  `id_personalizacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_reserva` int(11) NOT NULL,
  `video_ref` varchar(255) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha_solicitud` date NOT NULL,
  `fecha_aprobacion` date DEFAULT NULL,
  `estado` enum('pendiente','en_proceso','completada','rechazada') NOT NULL,
  PRIMARY KEY (`id_personalizacion`),
  KEY `fk_personalizacion_reserva` (`id_reserva`),
  CONSTRAINT `fk_personalizacion_reserva` FOREIGN KEY (`id_reserva`) REFERENCES `reserva` (`id_reserva`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;