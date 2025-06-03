# Proyecto Final Java – Gestión Veterinaria

**Alumno:** Ortiz Ceballos Francisco  
**Profesora:** Claudia Naveda

---

## ¿Qué hace este sistema?

Esto es una app de consola para manejar dueños y mascotas de una veterinaria.  
Podés agregar, borrar, modificar y ver dueños.  
A cada dueño se le pueden cargar varias mascotas, y también podés modificar o borrar las mascotas.  
Cuando borrás un dueño, automáticamente se borran sus mascotas (si existen).

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

## ¿Con qué base de datos anda?

Funciona igual con MariaDB y con MySQL.  
Está listo para MariaDB, pero en el caso de que use MySQL,  
solo tiene que cambiar el driver en el `build.gradle` así:


// Para MySQL (descomentar esta y comentar la de MariaDB si hace falta)
implementation 'mysql:mysql-connector-java:8.3.0'

