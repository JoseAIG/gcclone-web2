package controllers;

import javax.servlet.http.HttpServletRequest;

import helpers.Database;
import helpers.Hashing;

public class ControladorRegistro {

	public ControladorRegistro() {
		
	}
	
	//METODO PARA REGISTRAR UN NUEVO USUARIO AL SISTEMA
	public static String registrarUsuario(HttpServletRequest request) {
		String resultado_registro;
		try {			
			//OBTENER LOS DATOS DEL NUEVO USUARIO
			String usuario = request.getParameter("usuario");
			String correo = request.getParameter("correo");
			String clave = request.getParameter("clave");
			
			//OBTENER EL HASH DE LA CLAVE Y GUARDAR EN UN OBJETO LOS DATOS
			String hash_clave = Hashing.obtenerHash(clave);
			Object[] datos_usuario = {usuario, correo, hash_clave};
			
			//SE EJECUTA EL REGISTRO DEL USUARIO EN LA BASE DE DATOS (EN ESE METODO SE COMPRUEBA SI EL USUARIO EXISTE O NO)
			Database DB = Database.getInstances();
			resultado_registro = DB.dbRegistroUsuario(datos_usuario);
			
			//SE GENERA RESPUESTA EN FUNCION DEL RESULTADO DEL REGISTRO
			if(resultado_registro.equals("Operacion exitosa")) {
				return "{\"resultado\": \""+resultado_registro+"\", \"status\":"+200+"}";
			}else {
				return "{\"resultado\": \""+resultado_registro+"\", \"status\":"+422+"}";
			}
		} catch (Exception e) {
			return "{\"resultado\": \"No se pudo realizar el registro\", \"status\":"+500+"}";
		} 
	}

}
