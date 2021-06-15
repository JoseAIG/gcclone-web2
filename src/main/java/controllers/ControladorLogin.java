package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Autenticacion;

public class ControladorLogin {

	public ControladorLogin() {
		// TODO Auto-generated constructor stub
	}

	//METODO PARA INICIAR SESION EL CUAL RECIBE LOS PARAMETROS DE LA PETICION PARA REALIZAR LA AUTENTICACION DE CREDENCIALES
	public static String iniciarSesion(HttpServletRequest request) {
		try {
			String usuario = request.getParameter("usuario");
			String clave = request.getParameter("clave");
			if(Autenticacion.verificarCredenciales(usuario, clave)) {
				//DE SER VALIDAS LAS CREDENCIALES, OBTENER LA SESION Y OTORGAR EL NOMBRE DE USUARIO COMO ATRIBUTO A ESTA
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
