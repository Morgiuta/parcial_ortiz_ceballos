package org.example.dao;

import java.sql.Connection;
import java.util.List;

// Interfaz genérica para CRUD. Define lo básico que se tiene que poder hacer con cualquier entidad.
public interface Interfaz<T> {
  void insertar(Connection conn, T entidad);
  T buscarPorId(Connection conn, int id);
  List<T> listarTodos(Connection conn);
  void actualizar(Connection conn, T entidad);
  void eliminar(Connection conn, int id);
}
