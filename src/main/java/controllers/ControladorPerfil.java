package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Database;
import helpers.Hashing;

public class ControladorPerfil {

	public ControladorPerfil() {
		// TODO Auto-generated constructor stub
	}
	
	//METODO PARA OBTENER LOS DATOS DE UN PERFIL
	public static String obtenerDatosPerfil(HttpServletRequest request) {
		try {
			//OBTENER LA SESION PARA CON ELLA OBTENER EL ATRIBUTO USUARIO DE LA MISMA Y OBTENER LA INFORMACION DE ESTE EN LA BASE DE DATOS
			HttpSession sesion = request.getSession();
			Database DB = Database.getInstances();
			String [] datos_usuario = DB.dbObtenerDatosUsuario(sesion.getAttribute("usuario").toString());
			return("{\"usuario\": \""+datos_usuario[0]+"\", \"correo\":\""+datos_usuario[1]+"\", \"status\":"+200+"}");
		} catch (Exception e) {
			return("{\"resultado\": \"Error al obtener datos\", \"status\":"+200+"}");
		}
	}
	
	//METODO PARA ACTUALIZAR LOS DATOS DE UN PERFIL DEL SISTEMA
	public static String actualizarDatosPerfil(HttpServletRequest request) {
		try {			
			HttpSession sesion = request.getSession();
			
			//OBTENER PARAMETROS DEL FORM EDITAR PERFIL
			String usuario = request.getParameter("usuario");
			String correo = request.getParameter("correo");
			String clave = request.getParameter("clave");
			
			Database DB = Database.getInstances();
			Boolean resultado;
			//ACTUALIZAR DATOS CUANDO NO CAMBIA LA CLAVE
			if(clave.equals("")) {
				resultado = DB.dbActualizarDatosUsuario(sesion.getAttribute("usuario").toString(), usuario, correo);
			}else {
				//ACTUALIZAR DATOS CUANDO LA CLAVE ES MODIFICADA
				resultado = DB.dbActualizarDatosUsuario(sesion.getAttribute("usuario").toString(), usuario, correo, Hashing.obtenerHash(clave));
			}
			//DE SER SATISFACTORIO COLOCAR EL NOMBRE DE USUARIO ACTUALIZADO AL ATRIBUTO "usuario" EN LA SESION
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
			//OBTENER LA SESION PARA CON ELLA OBTENER EL ATRIBUTO USUARIO Y CON EL ELIMINAR EL REGISTRO EN LA BASE DE DATOS
			HttpSession sesion = request.getSession();
			Database DB = Database.getInstances();
			Boolean resultado = DB.dbEliminarPerfil(sesion.getAttribute("usuario").toString());
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
