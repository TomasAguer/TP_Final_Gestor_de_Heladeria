-- Script de base de datos del Gestor de Heladería (MySQL)
-- Ejecutar completo antes de correr el programa.
CREATE DATABASE IF NOT EXISTS heladeria_db;
USE heladeria_db;

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    tipo_producto VARCHAR(50) NULL,
    peso VARCHAR(30) NULL
);

CREATE TABLE sabores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(255),
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(30),
    direccion VARCHAR(150)
);

CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    total DOUBLE NOT NULL,
    cliente_id INT NULL,
    es_domicilio BOOLEAN NOT NULL DEFAULT FALSE,
    direccion_entrega VARCHAR(150) NULL,
    costo_envio DOUBLE DEFAULT 0,
    estado_envio VARCHAR(30) NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
);

CREATE TABLE detalle_ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad DOUBLE NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    sabores_elegidos VARCHAR(255) NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- Datos de ejemplo
INSERT INTO sabores (nombre, descripcion, disponible) VALUES
('Dulce de Leche Granizado', 'Dulce de leche artesanal con trozos de chocolate semiamargo', TRUE),
('Chocolate Suizo', 'Chocolate con leche y almendras tostadas', TRUE),
('Frutilla a la Crema', 'Crema americana con pulpa natural de frutillas', TRUE),
('Tramontana', 'Crema americana con dulce de leche y galletitas bañadas en chocolate', TRUE),
('Limón al Agua', 'Sorbete refrescante de limón natural', TRUE),
('Sambayón', 'Crema tradicional a base de yemas y vino Marsala', FALSE);

INSERT INTO productos (nombre, precio, stock, tipo, tipo_producto, peso) VALUES
('Pote 1 Kilo', 16000, 20, 'POR_PESO', NULL, 'KILO'),
('Pote 1/2 Kilo', 9500, 25, 'POR_PESO', NULL, 'MEDIO'),
('Pote 1/4 Kilo', 5500, 30, 'POR_PESO', NULL, 'CUARTO'),
('Bombón Escocés', 2000, 40, 'ENVASADO', 'ESCOCES', NULL),
('Palito Bombón', 1500, 50, 'ENVASADO', 'PALITO_BOMBON', NULL);

INSERT INTO clientes (nombre, email, telefono, direccion) VALUES
('Lucas Guaras', 'lucas@mail.com', '223-9999999', 'Av. Colón 2450'),
('Santiago Iglesias', 'santi@mail.com', '223-9999999', 'Güemes 3120');
