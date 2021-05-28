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
			String usuario = request.getParameter("usuario");
			String correo = request.getParameter("correo");
			String clave = request.getParameter("clave");
			
			String hash_clave = Hashing.obtenerHash(clave);
			Object[] datos_usuario = {usuario, correo, hash_clave};
			
			Database DB = Database.getInstances();
			resultado_registro = DB.dbRegistroUsuario(datos_usuario);
			System.out.println(resultado_registro);
			
			if(resultado_registro.equals("Operacion exitosa")) {
				System.out.println("redireccionamiento aqui...");
				return "{\"resultado\": \""+resultado_registro+"\", \"status\":"+200+"}";
			}else {
				System.out.println("No se redireccionara.");
				return "{\"resultado\": \""+resultado_registro+"\", \"status\":"+422+"}";
			}
		} catch (Exception e) {
			return "{\"resultado\": \"No se pudo realizar el registro\", \"status\":"+500+"}";
		} 
	}

}
