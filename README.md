# GC Clone (gcclone-web2)
Clon de Google Calendar, gcclone por sus siglas en ingles.

**Desarrollo Web II - Universidad Rafael Urdaneta - 2021B**

Desarrollado por José Inciarte C.I. 27.696.083

---

## Funcionalidades de la app web:

- Registrarse en el sistema con usuario, correo y clave. Realizando las validaciones pertinentes.
- Iniciar sesion en la aplicacion web con las credenciales validas de un usuario.
- Capacidad de modificar los distintos datos del perfil de un usuario, como tambien poder eliminarlo.
- Creacion de calendarios con un nombre y color que el usuario desee, capacidad de agregar invitados al calendario.
- Modificacion de los calendarios (nombre, color e invitados) segun los privilegios del usuario.
- Eliminacion de los calendarios, de ser propietario se elimina completamente, de ser un invitado, se eliminan solo los datos de edicion.
- Creacion de actividades en los calendarios en los que un usuario es propietario, ajustando el detalle, el dia, rango de horas e imagen si el mismo lo desea.
- Modificacion de las distintas propiedades de una actividad incluyendo imagemes, siempre que el usuario sea propietario del calendario, por ende de la actividad.
- Eliminacion de actividades si el usuario es propietario del calendario al que pertenece la actividad.

- Visualizar en bloques fijos las actividades de uno o varios calendarios en una vista semanal.
- Desplazarse entre semanas con selector de fecha o por medio de botones para ir a la semana anterior o siguiente.
- Mostrar notificaciones de las actividades del dia actual (Actividad en curso, a punto de acabar o finalizada).

---

Aplicacion web desarrollada en Eclipse for Java EE 2021.03 el cual brinda una nueva estructura de carpetas en los Dynamic Web Projects.
- Backend hecho en Java.
- Apache Tomcat 9.
- PostgreSQL como SGBD.

En el front-end no se ha empleado ningun framework, unicamente se hace uso de Materialize.
