package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Clase para manejar la conexión y la creación de las tablas de la base de datos
public class DataBaseManager {
  /*
  *  Yo estoy usando mariadb para linux. Si usted usa mysql comente el primer url y descomente el segundo
  * */
  private static final String URL = "jdbc:mariadb://localhost:3306/veterinaria";
  //private static final String URL = "jdbc:mysql://localhost:3306/veterinaria";
  private static final String USER = "root";
  private static final String PASSWORD = "generica";
  private static final Logger logger = LogManager.getLogger(DataBaseManager.class);

  public static Connection getConnection() {
    try {
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      logger.error("Error al conectar con la base de datos", e);
      throw new RuntimeException("Error al conectar con la base de datos", e);
    }
  }

  public static void inicializarBaseDeDatos() {
    try (Connection conn = getConnection()) {
      createTableDuenio(conn);
      createTableMascota(conn);
      logger.info("Base de datos inicializada correctamente");
    } catch (SQLException e) {
      logger.error("Error al inicializar la base de datos", e);
      throw new RuntimeException("Error al inicializar la base de datos", e);
    }
  }

  public static void createTableDuenio(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS duenio (" +
            "duenio_id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombre VARCHAR(100) NOT NULL," +
            "apellido VARCHAR(100) NOT NULL," +
            "telefono VARCHAR(20)," +
            "direccion VARCHAR(150)" +
            ")";
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
      logger.info("Tabla duenio creada correctamente");
    } catch (SQLException e) {
      logger.error("Error al crear tabla duenio", e);
      throw new RuntimeException("ERROR AL CREAR TABLA duenio", e);
    }
  }

  public static void createTableMascota(Connection conn) {
    String sql = "CREATE TABLE IF NOT EXISTS mascota (" +
            "mascota_id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombre VARCHAR(100) NOT NULL," +
            "especie VARCHAR(50) NOT NULL," +
            "fecha_nacimiento DATE," +
            "duenio_id INT NOT NULL," +
            "FOREIGN KEY (duenio_id) REFERENCES duenio(duenio_id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE" +
            ")";
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate(sql);
      logger.info("Tabla mascota creada correctamente");
    } catch (SQLException e) {
      logger.error("Error al crear tabla mascota", e);
      throw new RuntimeException("ERROR AL CREAR TABLA mascota", e);
    }
  }
}
