package controllers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Database;
import helpers.Hashing;

public class ControladorPerfil {

	public ControladorPerfil() {
		// TODO Auto-generated constructor stub
	}
	
	//METODO PARA ACTUALIZAR LOS DATOS DE UN PERFIL DEL SISTEMA
	public static String actualizarDatosPerfil(HttpServletRequest request) {
		try {
			System.out.println("ControladorPerfil - actualizar datos");
			System.out.println(request.getParameter("usuario"));
			System.out.println(request.getParameter("clave"));
			System.out.println(request.getParameter("correo"));
			
			HttpSession sesion = request.getSession();
			
			String usuario = request.getParameter("usuario");
			String correo = request.getParameter("correo");
			String clave = request.getParameter("clave");
			
			Database DB = Database.getInstances();
			Boolean resultado;
			//ACTUALIZAR DATOS CUANDO NO CAMBIA LA CLAVE
			if(clave.equals("")) {
				System.out.println("Actualizar datos sin clave");
				resultado = DB.dbActualizarDatosUsuario(sesion.getAttribute("usuario").toString(), usuario, correo);
			}else {
				//ACTUALIZAR DATOS CUANDO LA CLAVE ES MODIFICADA
				System.out.println("Actualizar datos y clave");
				resultado = DB.dbActualizarDatosUsuario(sesion.getAttribute("usuario").toString(), usuario, correo, Hashing.obtenerHash(clave));
			}
			System.out.println(resultado);
			if(resultado) {
				sesion.setAttribute("usuario", usuario);
				return("{\"resultado\": \"Datos actualizados\", \"status\":"+200+", \"nuevo_usuario\":\""+usuario+"\"}");

			}else {
				return("{\"resultado\": \"Error: datos ya existentes\", \"status\":"+422+"}");

			}
		} catch (Exception e) {
			return("{\"resultado\": \"Error al actualizar datos\", \"status\":"+500+"}");
		}
	}
	
	//METODO PARA ELIMINAR LOS DATOS DE UN PERFIL DEL SISTEMA
	public static String eliminarDatosPerfil(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
		
			Database DB = Database.getInstances();
			Boolean resultado = DB.dbEliminarPerfil(sesion.getAttribute("usuario").toString());
			System.out.println("Resultado eliminar perfil: " + resultado);
			if(resultado) {
				sesion.setAttribute("usuario", null);
				return("{\"resultado\": \"Perfil eliminado\", \"status\":"+200+"}");
			}else {
				return("{\"resultado\": \"No se pudo eliminar el perfil\", \"status\":"+500+"}");
			}
		} catch (Exception e) {
			return("{\"resultado\": \"No se pudo eliminar el perfil\", \"status\":"+500+"}");
		}
	}

}
