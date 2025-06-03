CREATE DATABASE IF NOT EXISTS veterinaria
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE veterinaria;


CREATE TABLE IF NOT EXISTS duenio (
    duenio_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(150)
);


CREATE TABLE IF NOT EXISTS mascota (
    mascota_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE,
    duenio_id INT NOT NULL,
    FOREIGN KEY (duenio_id) REFERENCES duenio(duenio_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
