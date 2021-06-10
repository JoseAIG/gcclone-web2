package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import helpers.Database;

public class ControladorActividad {

	public ControladorActividad() {
		
	}
	
	//METODO ESTATICO PARA CREAR ACTIVIDADES
	public static String crearActividad(HttpServletRequest request) {
		String estado_directorio = null;
		String ruta_imagen_guardar = null;
		boolean resultado_carga = false;
		try {
//			String path = request.getSession().getServletContext().getRealPath("public/activity-images/");
//			File file = new File(path);
//			String fullPathToYourWebappRoot = file.getCanonicalPath();
//			System.out.println("a: " +fullPathToYourWebappRoot);
			
//			String ruta_activity_images= request.getSession().getServletContext().getRealPath("public/activity-images/");
//			System.out.println("Ruta activity images: " + ruta_activity_images);
//			System.out.println("getAbsolutePath: " + new File("public/activity-images/").getAbsolutePath());
			
			//String ruta_a_activity_images_act_21 = request.getSession().getServletContext().getRealPath("public/activity-images/act-21/kitten.jpg");
			//File archprueba = new File(ruta_a_activity_images_act_21);
			//System.out.println(ruta_a_activity_images_act_21);
			//System.out.println("archprueba: " + archprueba.exists());
			
			//PROPERTIES
//			Properties prop = new Properties();
//			prop.load(ControladorActividad.class.getResourceAsStream("/properties/db.propertiess"));
//			System.out.println(prop.getProperty("prueba"));
						
			//OBTENER LAS PARTES DEL ARCHIVO IMAGEN DEL CLIENTE
			Part part_imagen = request.getPart("imagen-crear-actividad");
			System.out.println(part_imagen.getSize());
			//System.out.println("Nombre del archivo: " + part_imagen.getSubmittedFileName());
			System.out.println("Nombre del archivo: " + getSubmittedFileName(part_imagen));
			
			//MKDIRS
			String carpeta_imagenes_raiz = "/imagenes/actividades/";
			File directorio = new File(carpeta_imagenes_raiz);
			if(directorio.mkdirs()) {
				estado_directorio = "ruta creada: " + directorio.getAbsolutePath();
				System.out.println("ruta creada: " + directorio.getAbsolutePath());
			}else {
				estado_directorio= "no se creo la ruta";
				System.out.println("No se pudo crear la ruta");
			}

			String ruta_almacenar_db;
			if(getSubmittedFileName(part_imagen).equals("")) {
				//SI NO INGRESO UNA IMAGEN SE ALMACENA NULL EN EL CAMPO RUTA EN LA BASE DE DATOS
				System.out.println("almacenar null");
				ruta_imagen_guardar = null;
				ruta_almacenar_db = null;
				resultado_carga=false;
			}else {
				//SI EL ARCHIVO NO ES NULO, ALMACENARLO EN "activity-images/test"
//				String ruta_guardar_imagen = ruta_activity_images+"\\test\\"+getSubmittedFileName(part_imagen);
//				File archivo = new File(ruta_guardar_imagen);
//				System.out.println(archivo.getAbsolutePath());
//				part_imagen.write(ruta_guardar_imagen);
//				//SE COLOCA LA RUTA DEL ARCHIVO EN LA BASE DE DATOS
//				ruta_almacenar_db = "public/activity-images/test/"+getSubmittedFileName(part_imagen);
				
				//MKDIRS
				//part_imagen.write(carpeta_imagenes_raiz+getSubmittedFileName(part_imagen));
				//System.out.println("La imagen se guerdo en: " + directorio.getAbsolutePath()+"\\"+getSubmittedFileName(part_imagen));
				InputStream inputStreamPart = part_imagen.getInputStream();
				FileOutputStream outputStream = new FileOutputStream(carpeta_imagenes_raiz+getSubmittedFileName(part_imagen));
				System.out.println("Ruta de la imagen que se cargara: " + carpeta_imagenes_raiz+getSubmittedFileName(part_imagen));
				ruta_imagen_guardar= carpeta_imagenes_raiz+getSubmittedFileName(part_imagen);
				//ruta_imagen_guardar= directorio.getAbsolutePath()+File.separator+getSubmittedFileName(part_imagen);
				System.out.println("Ruta absoluta de la imagen a cargar: " + directorio.getAbsolutePath()+File.separator+getSubmittedFileName(part_imagen));
				//System.out.println("resultado de la carga: " + cargar(inputStreamPart, outputStream));
				resultado_carga = cargar(inputStreamPart, outputStream);
				ruta_almacenar_db = carpeta_imagenes_raiz+getSubmittedFileName(part_imagen);
				//ruta_almacenar_db = directorio.getAbsolutePath()+File.separator+getSubmittedFileName(part_imagen);
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
				System.out.println(1);
				String respuesta = "{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+", \"estado-directorio\":\""+estado_directorio+"\", \"ruta-imagen-guardar\":\""+ruta_imagen_guardar+"\", \"resultado-carga\":\""+resultado_carga+"\" }";
				System.out.println(respuesta);
				return(respuesta);
			}else {
				System.out.println(2);
				return("{\"resultado\": \"No se pudo ingresar la actividad en la base de datos\", \"status\":"+500+", \"estado-directorio\":\""+estado_directorio+"\", \"ruta-imagen-guardar\":\""+ruta_imagen_guardar+"\", \"resultado-carga\":\""+resultado_carga+"\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(3);
			return("{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+", \"estado-directorio\":\""+estado_directorio+"\", \"ruta-imagen-guardar\":\""+ruta_imagen_guardar+"\", \"resultado-carga\":\""+resultado_carga+"\"}");
		}
		
	}
	
	//METODO PARA MODIFICAR UNA ACTIVIDAD
	public static String modificarActividad(HttpServletRequest request) {
		try {			
			Object [] datos_edicion_actividad = {
				request.getParameter("detalle-editar-actividad"),
				request.getParameter("fecha-editar-actividad"),
				request.getParameter("hora-inicio"),
				request.getParameter("hora-fin"),
				null
			};

			Database DB = Database.getInstances();
			if(DB.dbModificarActividad(Integer.parseInt(request.getParameter("id-actividad")), datos_edicion_actividad)) {
				return "{\"resultado\": \"Actividad modificada con exito\", \"status\":"+200+"}";
			}else {
				return "{\"resultado\": \"La actividad no se pudo modificar\", \"status\":"+500+"}";				
			}
			
		} catch (Exception e) {
			return "{\"resultado\": \"Error al editar actividad\", \"status\":"+500+"}";
		}
	}
	
	//METODO PARA ELIMINAR UNA ACTIVIDAD
	public static String eliminarActividad(HttpServletRequest request) {
		try {
			Database DB = Database.getInstances();
			if(DB.dbEliminarActividad(Integer.parseInt(request.getParameter("id-actividad")))) {
				return "{\"resultado\": \"Actividad eliminada con exito\", \"status\":"+200+"}";
			}else {
				return "{\"resultado\": \"No se pudo eliminar la actividad\", \"status\":"+500+"}";
			}
		} catch (Exception e) {
			return "{\"resultado\": \"Error al eliminar actividad\", \"status\":"+500+"}";
		}
	}
	
	private static String getSubmittedFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}
	
	//METODO PARA CARGAR LA IMAGEN
	private static boolean cargar(InputStream input, FileOutputStream output) {
		int lectura = 0;
		final byte[] bytes = new byte[1024];
		
		try {
			while((lectura = input.read(bytes)) != -1) {
				output.write(bytes, 0, lectura);
			}
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
