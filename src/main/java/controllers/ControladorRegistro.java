package controllers;

import helpers.Database;
import helpers.Hashing;

public class ControladorRegistro {

	public ControladorRegistro() {
		
	}
	
	//METODO PARA REGISTRAR UN NUEVO USUARIO AL SISTEMA
	public static String registrarUsuario(String usuario, String correo, String clave) {
		String resultado_registro;
		try {
			Database DB = Database.getInstances();
			
			String hash_clave = Hashing.obtenerHash(usuario, "gcclone-web2");
			Object[] datos_usuario = {usuario, correo, hash_clave};	
			
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
			// TODO: handle exception
		} 
		return "{\"resultado\": \"No se pudo realizar el registro\", \"status\":"+500+"}";
	}

}
