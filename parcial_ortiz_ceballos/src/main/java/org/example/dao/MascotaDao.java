package org.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Mascota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MascotaDao implements Interfaz<Mascota> {
  private static final Logger logger = LogManager.getLogger(MascotaDao.class);

  // Inserta una mascota nueva en la base
  @Override
  public void insertar(Connection conn, Mascota mascota) {
    String sql = "INSERT INTO mascota (nombre, especie, fecha_nacimiento, duenio_id) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, mascota.getNombre());
      stmt.setString(2, mascota.getEspecie());
      if (mascota.getFechaNacimiento() != null) {
        stmt.setDate(3, Date.valueOf(mascota.getFechaNacimiento()));
      } else {
        stmt.setNull(3, Types.DATE);
      }
      stmt.setInt(4, mascota.getDuenioId());
      stmt.executeUpdate();
      logger.info("Mascota insertada: {}", mascota);
    } catch (SQLException e) {
      logger.error("Error al insertar mascota", e);
    }
  }

  // Busca y devuelve una mascota por su ID
  @Override
  public Mascota buscarPorId(Connection conn, int id) {
    String sql = "SELECT * FROM mascota WHERE mascota_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Mascota mascota = new Mascota(
                rs.getInt("mascota_id"),
                rs.getString("nombre"),
                rs.getString("especie"),
                rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getInt("duenio_id")
        );
        logger.info("Mascota encontrada: {}", mascota);
        return mascota;
      }
    } catch (SQLException e) {
      logger.error("Error al buscar mascota", e);
    }
    return null;
  }

  // Devuelve todas las mascotas cargadas
  @Override
  public List<Mascota> listarTodos(Connection conn) {
    List<Mascota> lista = new ArrayList<>();
    String sql = "SELECT * FROM mascota";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Mascota mascota = new Mascota(
                rs.getInt("mascota_id"),
                rs.getString("nombre"),
                rs.getString("especie"),
                rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getInt("duenio_id")
        );
        lista.add(mascota);
      }
      logger.info("Mascotas listadas: {}", lista.size());
    } catch (SQLException e) {
      logger.error("Error al listar mascotas", e);
    }
    return lista;
  }

  // Actualiza los datos de la mascota recibida (por ID)
  @Override
  public void actualizar(Connection conn, Mascota mascota) {
    String sql = "UPDATE mascota SET nombre=?, especie=?, fecha_nacimiento=?, duenio_id=? WHERE mascota_id=?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, mascota.getNombre());
      stmt.setString(2, mascota.getEspecie());
      if (mascota.getFechaNacimiento() != null) {
        stmt.setDate(3, Date.valueOf(mascota.getFechaNacimiento()));
      } else {
        stmt.setNull(3, Types.DATE);
      }
      stmt.setInt(4, mascota.getDuenioId());
      stmt.setInt(5, mascota.getMascotaId());
      stmt.executeUpdate();
      logger.info("Mascota actualizada: {}", mascota);
    } catch (SQLException e) {
      logger.error("Error al actualizar mascota", e);
    }
  }

  // Borra la mascota con el ID dado
  @Override
  public void eliminar(Connection conn, int id) {
    String sql = "DELETE FROM mascota WHERE mascota_id=?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
      logger.info("Mascota eliminada, id={}", id);
    } catch (SQLException e) {
      logger.error("Error al eliminar mascota", e);
    }
  }

  // Devuelve todas las mascotas de un dueño en particular
  public List<Mascota> buscarPorDuenio(Connection conn, int duenioId) {
    List<Mascota> lista = new ArrayList<>();
    String sql = "SELECT * FROM mascota WHERE duenio_id=?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, duenioId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Mascota mascota = new Mascota(
                rs.getInt("mascota_id"),
                rs.getString("nombre"),
                rs.getString("especie"),
                rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toLocalDate() : null,
                rs.getInt("duenio_id")
        );
        lista.add(mascota);
      }
      logger.info("Mascotas encontradas para duenio {}: {}", duenioId, lista.size());
    } catch (SQLException e) {
      logger.error("Error al buscar mascotas por duenio", e);
    }
    return lista;
  }
}