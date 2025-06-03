package org.example;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) {
    logger.info("Iniciando sistema de gestión veterinaria...");
    System.out.println("===============================================");
    System.out.println("   BIENVENIDO AL SISTEMA DE GESTIÓN VETERINARIA");
    System.out.println("===============================================");

    DataBaseManager.inicializarBaseDeDatos();

    try (Connection conn = DataBaseManager.getConnection();
         Scanner scanner = new Scanner(System.in)) {
      DuenioDao duenioDao = new DuenioDao();
      MascotaDao mascotaDao = new MascotaDao();

      boolean salir = false;
      while (!salir) {
        System.out.println("\nElija una opción:");
        System.out.println("1. Listar dueños");
        System.out.println("2. Crear nuevo dueño");
        System.out.println("3. Modificar información de un dueño");
        System.out.println("4. Borrar un dueño");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
          case "1":
            listarDuenios(conn, duenioDao, mascotaDao, scanner);
            break;
          case "2":
            crearDuenio(conn, duenioDao, mascotaDao, scanner);
            break;
          case "3":
            modificarDuenio(conn, duenioDao, mascotaDao, scanner);
            break;
          case "4":
            borrarDuenio(conn, duenioDao, scanner);
            break;
          case "0":
            salir = true;
            System.out.println("¡Gracias por usar el sistema de gestión veterinaria!");
            break;
          default:
            System.out.println("Opción no válida. Intente de nuevo.");
        }
      }
    } catch (Exception e) {
      logger.error("Error general en el sistema: ", e);
    }
  }

  private static void listarDuenios(Connection conn, DuenioDao duenioDao, MascotaDao mascotaDao, Scanner scanner) {
    List<Duenio> duenios = duenioDao.listarTodos(conn);
    if (duenios.isEmpty()) {
      System.out.println("No hay dueños registrados.");
    } else {
      System.out.println("\nLISTADO DE DUEÑOS:");
      duenios.forEach(System.out::println);

      boolean seguirPreguntando = true;
      while (seguirPreguntando) {
        System.out.print("\n¿Desea ver las mascotas de algún dueño? (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        if (respuesta.equals("S")) {
          System.out.print("Ingrese el ID del dueño: ");
          int duenioId;
          try {
            duenioId = Integer.parseInt(scanner.nextLine());
          } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            continue;
          }
          List<Mascota> mascotas = mascotaDao.buscarPorDuenio(conn, duenioId);
          if (mascotas.isEmpty()) {
            System.out.println("Este dueño no tiene mascotas registradas.");
          } else {
            System.out.println("Mascotas del dueño " + duenioId + ":");
            for (Mascota m : mascotas) {
              System.out.println(m);
            }
          }
          // Después de mostrar las mascotas, preguntamos si quiere ver de otro dueño
          System.out.print("\n¿Desea ver las mascotas de otro dueño? (S/N): ");
          String otra = scanner.nextLine().trim().toUpperCase();
          if (!otra.equals("S")) {
            seguirPreguntando = false;
          }
        } else {
          seguirPreguntando = false;
        }
      }
    }
  }

  private static void crearDuenio(Connection conn, DuenioDao duenioDao, MascotaDao mascotaDao, Scanner scanner) {
    System.out.println("Ingrese el nombre:");
    String nombre = scanner.nextLine();
    System.out.println("Ingrese el apellido:");
    String apellido = scanner.nextLine();
    System.out.println("Ingrese el teléfono:");
    String telefono = scanner.nextLine();
    System.out.println("Ingrese la dirección:");
    String direccion = scanner.nextLine();

    Duenio nuevo = new Duenio(0, nombre, apellido, telefono, direccion);
    duenioDao.insertar(conn, nuevo);

    List<Duenio> duenios = duenioDao.listarTodos(conn);
    int duenioId = duenios.get(duenios.size() - 1).getDuenioId();

    System.out.print("¿Cuántas mascotas tiene este dueño? ");
    int cantidadMascotas = 0;
    try {
      cantidadMascotas = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Cantidad inválida. No se agregarán mascotas.");
      return;
    }

    for (int i = 1; i <= cantidadMascotas; i++) {
      System.out.println("Mascota #" + i + ":");
      System.out.print("  Nombre: ");
      String nombreMascota = scanner.nextLine();
      System.out.print("  Especie: ");
      String especie = scanner.nextLine();
      System.out.print("  Fecha de nacimiento (YYYY-MM-DD, opcional): ");
      String fecha = scanner.nextLine();
      Mascota mascota;
      if (fecha.isEmpty()) {
        mascota = new Mascota(0, nombreMascota, especie, null, duenioId);
      } else {
        mascota = new Mascota(0, nombreMascota, especie, java.time.LocalDate.parse(fecha), duenioId);
      }
      mascotaDao.insertar(conn, mascota);
    }
    System.out.println("Dueño y mascotas agregados correctamente.");
  }


  private static void modificarDuenio(Connection conn, DuenioDao duenioDao, MascotaDao mascotaDao, Scanner scanner) {
    // Mostrar lista de dueños (ID y Nombre)
    List<Duenio> duenios = duenioDao.listarTodos(conn);
    if (duenios.isEmpty()) {
      System.out.println("No hay dueños registrados para modificar.");
      return;
    }
    System.out.println("Lista de dueños:");
    for (Duenio d : duenios) {
      System.out.println("ID: " + d.getDuenioId() + " - Nombre: " + d.getApellido() + " " + d.getNombre());
    }

    System.out.print("Ingrese el ID del dueño a modificar: ");
    int idModificar;
    try {
      idModificar = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("ID inválido.");
      return;
    }
    Duenio duenioExistente = duenioDao.buscarPorId(conn, idModificar);
    if (duenioExistente == null) {
      System.out.println("No existe un dueño con ese ID.");
      return;
    }

    boolean salir = false;
    while (!salir) {
      System.out.println("\n¿Qué desea modificar?");
      System.out.println("1. Datos personales del dueño");
      System.out.println("2. Gestionar mascotas de este dueño");
      System.out.println("0. Volver al menú principal");
      System.out.print("Opción: ");
      String opcion = scanner.nextLine();
      switch (opcion) {
        case "1":
          modificarDatosPersonalesDuenio(conn, duenioDao, scanner, duenioExistente);
          break;
        case "2":
          gestionarMascotas(conn, mascotaDao, scanner, duenioExistente.getDuenioId());
          break;
        case "0":
          salir = true;
          break;
        default:
          System.out.println("Opción no válida. Intente de nuevo.");
      }
    }
  }

  // Modifica los datos del dueño. Si dejás un campo vacío, lo deja como está.
  private static void modificarDatosPersonalesDuenio(Connection conn, DuenioDao duenioDao, Scanner scanner, Duenio duenioExistente) {
    System.out.println("Deje vacío el campo para mantener el valor actual.");
    System.out.print("Nombre actual (" + duenioExistente.getNombre() + "): ");
    String nuevoNombre = scanner.nextLine();
    if (!nuevoNombre.isEmpty()) duenioExistente.setNombre(nuevoNombre);

    System.out.print("Apellido actual (" + duenioExistente.getApellido() + "): ");
    String nuevoApellido = scanner.nextLine();
    if (!nuevoApellido.isEmpty()) duenioExistente.setApellido(nuevoApellido);

    System.out.print("Teléfono actual (" + duenioExistente.getTelefono() + "): ");
    String nuevoTelefono = scanner.nextLine();
    if (!nuevoTelefono.isEmpty()) duenioExistente.setTelefono(nuevoTelefono);

    System.out.print("Dirección actual (" + duenioExistente.getDireccion() + "): ");
    String nuevaDireccion = scanner.nextLine();
    if (!nuevaDireccion.isEmpty()) duenioExistente.setDireccion(nuevaDireccion);

    duenioDao.actualizar(conn, duenioExistente);
    System.out.println("Dueño actualizado correctamente.");
  }

  // Muestra los dueños con sus IDs y nombres, pide un ID y borra el dueño (y sus mascotas) si ponés bien la contraseña.
  private static void borrarDuenio(Connection conn, DuenioDao duenioDao, Scanner scanner) {
    List<Duenio> duenios = duenioDao.listarTodos(conn);
    if (duenios.isEmpty()) {
      System.out.println("No hay dueños registrados para borrar.");
      return;
    }
    System.out.println("Lista de dueños:");
    for (Duenio d : duenios) {
      System.out.println("ID: " + d.getDuenioId() + " - Nombre: " + d.getApellido() + " " + d.getNombre());
    }

    System.out.print("Ingrese el ID del dueño a borrar: ");
    int idBorrar;
    try {
      idBorrar = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("ID inválido.");
      return;
    }

    System.out.print("Ingrese la contraseña para confirmar el borrado: ");
    String password = scanner.nextLine();
    final String CLAVE = "admin123"; // Cambiala si querés

    if (!password.equals(CLAVE)) {
      System.out.println("Contraseña incorrecta. Operación cancelada.");
      return;
    }

    duenioDao.eliminar(conn, idBorrar);
    System.out.println("Dueño y mascotas asociadas eliminados correctamente (si existían).");
  }

  // Permite gestionar las mascotas del dueño. Se puede agregar, editar o eliminar mascotas.
  private static void gestionarMascotas(Connection conn, MascotaDao mascotaDao, Scanner scanner, int duenioId) {
    boolean volver = false;
    while (!volver) {
      System.out.println("\nGestión de mascotas:");
      System.out.println("1. Agregar nueva mascota");
      System.out.println("2. Modificar una mascota existente");
      System.out.println("3. Eliminar una mascota");
      System.out.println("0. Volver al menú anterior");
      System.out.print("Opción: ");
      String opcion = scanner.nextLine();
      switch (opcion) {
        case "1":
          agregarMascota(conn, mascotaDao, scanner, duenioId);
          break;
        case "2":
          modificarMascota(conn, mascotaDao, scanner, duenioId);
          break;
        case "3":
          eliminarMascota(conn, mascotaDao, scanner, duenioId);
          break;
        case "0":
          volver = true;
          break;
        default:
          System.out.println("Opción no válida.");
      }
    }
  }

  // Agrega una mascota nueva para el dueño indicado.
  private static void agregarMascota(Connection conn, MascotaDao mascotaDao, Scanner scanner, int duenioId) {
    System.out.print("Nombre de la mascota: ");
    String nombre = scanner.nextLine();
    System.out.print("Especie: ");
    String especie = scanner.nextLine();
    System.out.print("Fecha de nacimiento (YYYY-MM-DD, opcional): ");
    String fecha = scanner.nextLine();
    Mascota mascota;
    if (fecha.isEmpty()) {
      mascota = new Mascota(0, nombre, especie, null, duenioId);
    } else {
      mascota = new Mascota(0, nombre, especie, java.time.LocalDate.parse(fecha), duenioId);
    }
    mascotaDao.insertar(conn, mascota);
    System.out.println("Mascota agregada correctamente.");
  }

  // Muestra las mascotas del dueño, deja elegir una y modificar sus datos.
  private static void modificarMascota(Connection conn, MascotaDao mascotaDao, Scanner scanner, int duenioId) {
    List<Mascota> mascotas = mascotaDao.buscarPorDuenio(conn, duenioId);
    if (mascotas.isEmpty()) {
      System.out.println("Este dueño no tiene mascotas para modificar.");
      return;
    }
    System.out.println("Mascotas:");
    for (Mascota m : mascotas) {
      System.out.println("ID: " + m.getMascotaId() + " | Nombre: " + m.getNombre() + " | Especie: " + m.getEspecie());
    }
    System.out.print("Ingrese el ID de la mascota a modificar: ");
    int idMascota;
    try {
      idMascota = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("ID inválido.");
      return;
    }
    Mascota mascota = mascotaDao.buscarPorId(conn, idMascota);
    if (mascota == null) {
      System.out.println("No existe una mascota con ese ID.");
      return;
    }
    System.out.print("Nuevo nombre (actual: " + mascota.getNombre() + "): ");
    String nombre = scanner.nextLine();
    if (!nombre.isEmpty()) mascota.setNombre(nombre);

    System.out.print("Nueva especie (actual: " + mascota.getEspecie() + "): ");
    String especie = scanner.nextLine();
    if (!especie.isEmpty()) mascota.setEspecie(especie);

    System.out.print("Nueva fecha de nacimiento (YYYY-MM-DD, actual: " + mascota.getFechaNacimiento() + "): ");
    String fecha = scanner.nextLine();
    if (!fecha.isEmpty()) mascota.setFechaNacimiento(java.time.LocalDate.parse(fecha));

    mascotaDao.actualizar(conn, mascota);
    System.out.println("Mascota actualizada correctamente.");
  }

  // Muestra las mascotas del dueño, deja elegir una y la borra.
  private static void eliminarMascota(Connection conn, MascotaDao mascotaDao, Scanner scanner, int duenioId) {
    List<Mascota> mascotas = mascotaDao.buscarPorDuenio(conn, duenioId);
    if (mascotas.isEmpty()) {
      System.out.println("Este dueño no tiene mascotas para eliminar.");
      return;
    }
    System.out.println("Mascotas:");
    for (Mascota m : mascotas) {
      System.out.println("ID: " + m.getMascotaId() + " | Nombre: " + m.getNombre() + " | Especie: " + m.getEspecie());
    }
    System.out.print("Ingrese el ID de la mascota a eliminar: ");
    int idMascota;
    try {
      idMascota = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("ID inválido.");
      return;
    }
    mascotaDao.eliminar(conn, idMascota);
    System.out.println("Mascota eliminada correctamente (si existía).");
  }

}
