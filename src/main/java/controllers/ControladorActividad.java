package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

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
	
	public static String modificarActividad(HttpServletRequest request) {
		try {
			//OBTENER DATOS DE LA PETICION
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String data = br.readLine();
			JSONObject json_editar_actividad = new JSONObject(data);
			
			//System.out.println("Controlador actividad - modificar actividad: " + json_editar_actividad.get("id-actividad"));
			
			Object [] datos_edicion_actividad = {
				json_editar_actividad.get("detalle-editar-actividad"),
				json_editar_actividad.get("fecha-editar-actividad"),
				json_editar_actividad.get("hora-inicio"),
				json_editar_actividad.get("hora-fin"),
				"wip-edicion.png"
			};

			Database DB = Database.getInstances();
			if(DB.dbModificarActividad(json_editar_actividad.getInt("id-actividad"), datos_edicion_actividad)) {
				return "{\"resultado\": \"Actividad modificada con exito\", \"status\":"+200+"}";
			}else {
				return "{\"resultado\": \"La actividad no se pudo modificar\", \"status\":"+500+"}";				
			}
			
		} catch (Exception e) {
			return "{\"resultado\": \"Error al editar actividad\", \"status\":"+500+"}";
		}
	}
	
	public static String eliminarActividad(HttpServletRequest request) {
		try {
			//OBTENER DATOS DE ENTRADA Y CONVERTIRLO A JSONOBJECT
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String data = br.readLine();
			System.out.println("Controlador actividad - eliminarActividad: " + data);
			JSONObject json_peticion = new JSONObject(data);
			Database DB = Database.getInstances();
			if(DB.dbEliminarActividad(json_peticion.getInt("id-actividad"))) {
				return "{\"resultado\": \"Actividad eliminada con exito\", \"status\":"+200+"}";
			}else {
				return "{\"resultado\": \"No se pudo eliminar la actividad\", \"status\":"+500+"}";
			}
		} catch (Exception e) {
			return "{\"resultado\": \"Error al eliminar actividad\", \"status\":"+500+"}";
		}
	}

}
