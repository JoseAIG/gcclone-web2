package helpers;

public class Autenticacion {

	public Autenticacion() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean verificarCredenciales(String usuario, String clave) {
		try {
			//OBTENER EL HASH DE LA CLAVE INGRESADA POR EL USUARIO
			String hash_clave = Hashing.obtenerHash(clave);
			
			//OBTENER LOS DATOS DEL USUARIO
			Database DB = Database.getInstances();
			String[] datos_usuario = DB.dbObtenerDatosUsuario(usuario);
			
			//COMPROBAR SI EL NOMBRE DE USUARIO O CORREO COINCIDEN Y SI EL HASH DE LA CLAVE ES EL MISMO QUE EL ALMACENADO EN LA BBDD
			if((usuario.equals(datos_usuario[0]) || usuario.equals(datos_usuario[1])) && hash_clave.equals(datos_usuario[2])) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
