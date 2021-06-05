package controllers;

import javax.servlet.http.HttpServletRequest;

import helpers.Database;

public class ControladorActividad {

	public ControladorActividad() {
		
	}
	
	//METODO ESTATICO PARA CREAR ACTIVIDADES
	public static String crearActividad(HttpServletRequest request) {
		try {
			//CREAR UN ARREGLO DE OBJETOS CON LOS PARAMETROS DE LA PETICION (DATOS ACTIVIDAD)
			Object[] datos_nueva_actividad = {
				request.getParameter("select-calendario-crear-actividad"), 
				request.getParameter("detalle-actividad"),
				request.getParameter("fecha-crear-actividad"),
				request.getParameter("hora-inicio"),
				request.getParameter("hora-fin"),
				"wip.png"
			};
			//INGRESAR EN LA BASE DE DATOS LA INFORMACION DE LA ACTIVIDAD
			Database DB = Database.getInstances();
			if(DB.dbCrearActividad(datos_nueva_actividad)) {
				return("{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+"}");
			}else {
				return("{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+"}");
			}
		} catch (Exception e) {
			return("{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+"}");
		}
		
	}

}
