package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONObject;

import helpers.Database;

public class ControladorActividad {

	public ControladorActividad() {
		
	}
	
	//METODO ESTATICO PARA CREAR ACTIVIDADES
	public static String crearActividad(HttpServletRequest request) {
		try {
//			String path = request.getSession().getServletContext().getRealPath("WEB-INF/../");
//			File file = new File(path);
//			String fullPathToYourWebappRoot = file.getCanonicalPath();
//			System.out.println("a: " +fullPathToYourWebappRoot);
			
			
			String ruta_activity_images= request.getSession().getServletContext().getRealPath("public/activity-images/");
			//String ruta_a_activity_images_act_21 = request.getSession().getServletContext().getRealPath("public/activity-images/act-21/kitten.jpg");
			//File archprueba = new File(ruta_a_activity_images_act_21);
			//System.out.println(ruta_a_activity_images_act_21);
			//System.out.println("archprueba: " + archprueba.exists());
			
			//PROPERTIES
			Properties prop = new Properties();
			prop.load(ControladorActividad.class.getResourceAsStream("/properties/db.properties"));
			System.out.println(prop.getProperty("prueba"));
						
			//OBTENER LAS PARTES DEL ARCHIVO IMAGEN DEL CLIENTE
			Part part_imagen = request.getPart("imagen-crear-actividad");
			System.out.println("Nombre del archivo: " + part_imagen.getSubmittedFileName());
			
			String ruta_almacenar_db;
			if(part_imagen.getSubmittedFileName().equals("")) {
				//SI NO INGRESO UNA IMAGEN SE ALMACENA NULL EN EL CAMPO RUTA EN LA BASE DE DATOS
				System.out.println("almacenar null");
				ruta_almacenar_db = null;
			}else {
				//SI EL ARCHIVO NO ES NULO, ALMACENARLO EN "activity-images/test"
				String ruta_guardar_imagen = ruta_activity_images+"\\test\\"+part_imagen.getSubmittedFileName();
				File archivo = new File(ruta_guardar_imagen);
				System.out.println(archivo.getAbsolutePath());
				part_imagen.write(ruta_guardar_imagen);
				//SE COLOCA LA RUTA DEL ARCHIVO EN LA BASE DE DATOS
				ruta_almacenar_db = "public/activity-images/test/"+part_imagen.getSubmittedFileName();
			}
//			String ruta_guardar_imagen = ruta_activity_images+"\\test\\"+part_imagen.getSubmittedFileName();
//			File archivo = new File(ruta_guardar_imagen);
//			System.out.println(archivo.getAbsolutePath());
//			//File archivo_imagen = new File(part_imagen.getSubmittedFileName());
//			//part_imagen.write(part_imagen.getSubmittedFileName());
//			part_imagen.write(ruta_guardar_imagen);
			
			//CREAR UN ARREGLO DE OBJETOS CON LOS PARAMETROS DE LA PETICION (DATOS ACTIVIDAD)
			Object[] datos_nueva_actividad = {
				request.getParameter("select-calendario-crear-actividad"), 
				request.getParameter("detalle-actividad"),
				request.getParameter("fecha-crear-actividad"),
				request.getParameter("hora-inicio"),
				request.getParameter("hora-fin"),
				//ruta_guardar_imagen
				ruta_almacenar_db
			};
			//INGRESAR EN LA BASE DE DATOS LA INFORMACION DE LA ACTIVIDAD
			Database DB = Database.getInstances();
			if(DB.dbCrearActividad(datos_nueva_actividad)) {
				return("{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+"}");
			}else {
				return("{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
