-- =============================================
-- Script de Base de Datos - Biblioteca Digital
-- Desafío Práctico #02 - Programación Orientada a Objetos
-- =============================================

CREATE DATABASE IF NOT EXISTS biblioteca_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE biblioteca_db;

-- Tabla Autor
CREATE TABLE IF NOT EXISTS autor (
                                     id_autor INT AUTO_INCREMENT PRIMARY KEY,
                                     nombre VARCHAR(100) NOT NULL,
                                     nacionalidad VARCHAR(50)
);

-- Tabla Categoría
CREATE TABLE IF NOT EXISTS categoria (
                                         id_categoria INT AUTO_INCREMENT PRIMARY KEY,
                                         nombre_categoria VARCHAR(100) NOT NULL
);

-- Tabla Libro
CREATE TABLE IF NOT EXISTS libro (
                                     id_libro INT AUTO_INCREMENT PRIMARY KEY,
                                     titulo VARCHAR(200) NOT NULL,
                                     año_publicacion INT,
                                     id_autor INT,
                                     id_categoria INT,
                                     FOREIGN KEY (id_autor) REFERENCES autor(id_autor) ON DELETE SET NULL,
                                     FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE SET NULL
);

-- Datos de ejemplo
INSERT INTO autor (nombre, nacionalidad) VALUES
                                             ('Gabriel García Márquez', 'Colombia'),
                                             ('Mario Vargas Llosa', 'Perú'),
                                             ('Jorge Luis Borges', 'Argentina'),
                                             ('Isabel Allende', 'Chile');

INSERT INTO categoria (nombre_categoria) VALUES
                                             ('Novela'), ('Cuento'), ('Poesía'), ('Ensayo'), ('Ciencia Ficción');

INSERT INTO libro (titulo, año_publicacion, id_autor, id_categoria) VALUES
                                                                        ('Cien años de soledad', 1967, 1, 1),
                                                                        ('La ciudad y los perros', 1963, 2, 1),
                                                                        ('El Aleph', 1949, 3, 2);

-- Mensaje final
SELECT 'Base de datos "biblioteca_db" creada correctamente con datos de ejemplo.' AS Mensaje;