# Proyecto Final Java – Gestión Veterinaria

**Alumno:** Ortiz Ceballos Francisco  
**Profesora:** Claudia Naveda

---

## ¿Qué hace este sistema?

Esto es un programa de consola para manejar dueños y mascotas de una veterinaria.  
Podés agregar, borrar, modificar y ver dueños.  
A cada dueño se le pueden cargar varias mascotas, y también podés modificar o borrar las mascotas.  
Cuando borrás un dueño, automáticamente se borran sus mascotas.

El menú es simple, vas eligiendo lo que querés hacer y seguís los pasos.  
Para borrar un dueño, sí o sí tenés que poner la contraseña (es `admin123`).

---

## Organización del código

Todo el código está en el paquete `org.example`.  
Las clases principales son:

- `Main.java` — Menú y lógica del sistema.
- `Duenio.java`, `Mascota.java` — Las clases de datos.
- `DuenioDao.java`, `MascotaDao.java`, `Interfaz.java` — Acceso a base de datos y operaciones.
- `DataBaseManager.java` — La que arma las tablas y la conexión a la base.

---

## Base de Datos

Funciona igual con MariaDB y con MySQL.  
Está listo para MariaDB, ya que en debian tiene mejor soporte, pero en el caso de que use MySQL,  
solo tiene que cambiar el driver en el `build.gradle` y la URL en `DatabaseManager`


// Para MySQL (descomentar esta y comentar la de MariaDB si hace falta)
implementation 'mysql:mysql-connector-java:8.3.0'

private static final String URL = "jdbc:mysql://localhost:3306/veterinaria";

Ademas hay que cambiar el usuario y la contraseña

