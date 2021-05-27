package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Database;
import helpers.Hashing;

public class ControladorLogin {

	public ControladorLogin() {
		// TODO Auto-generated constructor stub
	}
	
	public static String iniciarSesion(HttpServletRequest request) {
		try {
			String usuario = request.getParameter("usuario");
			String clave = request.getParameter("clave");
			Object[] datos_usuario = {usuario,Hashing.obtenerHash(clave)};
			Database DB = Database.getInstances();
			Boolean resultadoLogin = DB.dbLogin(datos_usuario);
			if(resultadoLogin) {
				HttpSession sesion = request.getSession();
				sesion.setAttribute("usuario", usuario);
				return "{\"resultado\": \"Login exitoso\", \"status\":"+200+", \"redirect\": \"/Dashboard\"}";
			}else {
				return "{\"resultado\": \"Credenciales invalidas\", \"status\":"+401+"}";
			}

		} catch (Exception e) {
			return "{\"resultado\": \"Error de login\", \"status\":"+500+"}";
		}
	}

}
