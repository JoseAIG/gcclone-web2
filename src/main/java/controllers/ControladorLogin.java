package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Autenticacion;

public class ControladorLogin {

	public ControladorLogin() {
		// TODO Auto-generated constructor stub
	}

	public static String iniciarSesion(HttpServletRequest request) {
		try {
			String usuario = request.getParameter("usuario");
			String clave = request.getParameter("clave");
			if(Autenticacion.verificarCredenciales(usuario, clave)) {
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
