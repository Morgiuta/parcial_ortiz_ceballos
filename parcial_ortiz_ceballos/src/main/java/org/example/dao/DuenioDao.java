package org.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Duenio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DuenioDao implements Interfaz<Duenio> { // Acá implementás la interfaz directamente
  private static final Logger logger = LogManager.getLogger(DuenioDao.class);

  // Inserta un dueño nuevo en la base
  @Override
  public void insertar(Connection conn, Duenio duenio) {
    String sql = "INSERT INTO duenio (nombre, apellido, telefono, direccion) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, duenio.getNombre());
      stmt.setString(2, duenio.getApellido());
      stmt.setString(3, duenio.getTelefono());
      stmt.setString(4, duenio.getDireccion());
      stmt.executeUpdate();
      logger.info("Dueño insertado: {}", duenio);
    } catch (SQLException e) {
      logger.error("Error al insertar dueño", e);
    }
  }

  // Busca y devuelve un dueño por su ID
  @Override
  public Duenio buscarPorId(Connection conn, int id) {
    String sql = "SELECT * FROM duenio WHERE duenio_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Duenio duenio = new Duenio(
                rs.getInt("duenio_id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("telefono"),
                rs.getString("direccion")
        );
        logger.info("Dueño encontrado: {}", duenio);
        return duenio;
      }
    } catch (SQLException e) {
      logger.error("Error al buscar dueño", e);
    }
    return null;
  }

  // Devuelve todos los dueños cargados
  @Override
  public List<Duenio> listarTodos(Connection conn) {
    List<Duenio> lista = new ArrayList<>();
    String sql = "SELECT * FROM duenio";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Duenio duenio = new Duenio(
                rs.getInt("duenio_id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("telefono"),
                rs.getString("direccion")
        );
        lista.add(duenio);
      }
      logger.info("Dueños listados: {}", lista.size());
    } catch (SQLException e) {
      logger.error("Error al listar dueños", e);
    }
    return lista;
  }

  // Actualiza los datos del dueño recibido (por ID)
  @Override
  public void actualizar(Connection conn, Duenio duenio) {
    String sql = "UPDATE duenio SET nombre=?, apellido=?, telefono=?, direccion=? WHERE duenio_id=?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, duenio.getNombre());
      stmt.setString(2, duenio.getApellido());
      stmt.setString(3, duenio.getTelefono());
      stmt.setString(4, duenio.getDireccion());
      stmt.setInt(5, duenio.getDuenioId());
      stmt.executeUpdate();
      logger.info("Dueño actualizado: {}", duenio);
    } catch (SQLException e) {
      logger.error("Error al actualizar dueño", e);
    }
  }

  // Borra el dueño con el ID dado
  @Override
  public void eliminar(Connection conn, int id) {
    String sql = "DELETE FROM duenio WHERE duenio_id=?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
      logger.info("Dueño eliminado, id={}", id);
    } catch (SQLException e) {
      logger.error("Error al eliminar dueño", e);
    }
  }
}
