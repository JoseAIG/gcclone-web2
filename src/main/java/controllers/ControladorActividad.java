package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import helpers.Archivos;
import helpers.Database;
import helpers.PropertiesReader;

public class ControladorActividad {

	public ControladorActividad() {
		
	}
	
	//METODO ESTATICO PARA CREAR ACTIVIDADES
	public static String crearActividad(HttpServletRequest request) {
		String ruta_aplicacion = null;
		String ruta_imagenes = null;
		boolean existe_directorio = false;		
		try {
			//OBTENER PART DE LA IMAGEN Y COMPROBAR SI EN EFECTO SE HA CARGADO UNA FOTO	
			Part part_imagen = request.getPart("imagen-crear-actividad");
			String nombre_archivo = part_imagen.getSubmittedFileName();
			String ruta_almacenar=null;
			//DE HABERSE ENVIADO UNA IMAGEN...
			if(!nombre_archivo.equals("")) {
				//OBTENER LA RUTA DE LA APLICACION
				ruta_aplicacion = request.getServletContext().getRealPath("");
				//ESTABLECER LA RUTA PARA GUARDAR EL ARCHIVO
				ruta_imagenes = ruta_aplicacion + File.separator + "imagenes";
				
				//VERIFICAR SI EL DIRECTORIO EXISTE, DE NO EXISTIR, SE CREA
				File directorioImagenes = new File (ruta_imagenes);
				existe_directorio = directorioImagenes.exists();
				if(!existe_directorio) {
					directorioImagenes.mkdirs();
				}
				
				//GENERAR NOMBRE UNICO DEL ARCHIVO
				String nombre_unico = Archivos.generarNombreArchivo(nombre_archivo);

				//GUARDAR ARCHIVO
				//part_imagen.write(ruta_imagenes +  File.separator + nombre_unico);
				FileOutputStream output = new FileOutputStream(new File(ruta_imagenes +  File.separator + nombre_unico));
				InputStream input = part_imagen.getInputStream();
				Archivos.almacenarImagen(input, output);
				ruta_almacenar = "imagenes/" + nombre_unico;
			}
			
			//CREAR UN ARREGLO DE OBJETOS CON LOS PARAMETROS DE LA PETICION (DATOS ACTIVIDAD)
			Object[] datos_nueva_actividad = {
				Integer.parseInt(request.getParameter("select-calendario-crear-actividad")), 
				request.getParameter("detalle-actividad"),
				request.getParameter("fecha-crear-actividad"),
				Double.parseDouble(request.getParameter("hora-inicio")),
				Double.parseDouble(request.getParameter("hora-fin")),
				ruta_almacenar
			};
			//INGRESAR EN LA BASE DE DATOS LA INFORMACION DE LA ACTIVIDAD
			Database DB = Database.getInstances();
			PropertiesReader PR = PropertiesReader.getInstance();
			if(DB.dbPreparedStatement(PR.obtenerPropiedad("crearActividad"), datos_nueva_actividad)) {
				return "{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+"}";
			}else {
				return "{\"resultado\": \"No se pudo ingresar la actividad en la base de datos\", \"status\":"+500+"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+"}";
		}
	}
	
	//METODO PARA MODIFICAR UNA ACTIVIDAD
	public static String modificarActividad(HttpServletRequest request) {		
		String ruta_aplicacion = null;
		String ruta_imagenes = null;
		boolean existe_directorio = false;
		try {
			//OBTENER LA PART DE LA IMAGEN Y COMPROBAR QUE EL NOMBRE DEL ARCHIVO SUBIDO NO SEA VACIO PARA HACER LA CARGA Y ALMACENADO DE LA RUTA
			Part part_imagen = request.getPart("imagen-editar-actividad");
			String nombre_archivo = part_imagen.getSubmittedFileName();
			String ruta_almacenar=null;
			if(!nombre_archivo.equals("")) {
				
				//OBTENER LA RUTA DE LA APLICACION
				ruta_aplicacion = request.getServletContext().getRealPath("");
				//ESTABLECER LA RUTA PARA GUARDAR EL ARCHIVO
				ruta_imagenes = ruta_aplicacion + File.separator + "imagenes";
				
				//VERIFICAR SI EL DIRECTORIO EXISTE, DE NO EXISTIR, SE CREA
				File directorioImagenes = new File (ruta_imagenes);
				existe_directorio = directorioImagenes.exists();
				if(!existe_directorio) {
					directorioImagenes.mkdirs();
				}
				
				//GENERAR UN NOMBRE UNICO PARA LA IMAGEN
				String nombre_unico = Archivos.generarNombreArchivo(nombre_archivo);
				
				//ALMACENAR IMAGEN
				//part_imagen.write(ruta_imagenes +  File.separator + nombre_archivo);				
				FileOutputStream output = new FileOutputStream(new File(ruta_imagenes +  File.separator + nombre_unico));
				InputStream input = part_imagen.getInputStream();
				Archivos.almacenarImagen(input, output);
				ruta_almacenar = "imagenes/" + nombre_unico;
			}
			
			//CREAR UN ARREGLO DE OBJETOS CON LOS PARAMETROS DE LA PETICION (DATOS ACTIVIDAD)
			Object [] datos_edicion_actividad = {
				request.getParameter("detalle-editar-actividad"),
				request.getParameter("fecha-editar-actividad"),
				Double.parseDouble(request.getParameter("hora-inicio")),
				Double.parseDouble(request.getParameter("hora-fin")),
				ruta_almacenar
			};

			Database DB = Database.getInstances();
			PropertiesReader PR = PropertiesReader.getInstance();
			//SI EL PARAMETRO ELIMINAR IMAGEN ACTIVIDAD NO ES NULL, ES DECIR, ESTA "ON" (CHECKBOX ELIMINAR IMAGEN MARCADO) INSERTAR NULL EN EL CAMPO RUTA_IMAGEN O LA NUEVA RUTA DE LA IMAGEN CARGADA (DE HABERSE CARGADO)
			if(request.getParameter("eliminar-imagen-actividad")!=null || ruta_almacenar!=null) {
				if(DB.dbPreparedStatement(PR.obtenerPropiedad("modificarActividad")+Integer.parseInt(request.getParameter("id-actividad")), datos_edicion_actividad)) {
					return "{\"resultado\": \"Actividad modificada con exito\", \"status\":"+200+"}";
				}else {
					return "{\"resultado\": \"La actividad no se pudo modificar\", \"status\":"+500+"}";				
				}	
			}else {
				//SI NO SE ELIMINO LA IMAGEN O SE CAMBIO POR OTRA, NO ALTERAR EL CAMPO DE LA RUTA IMAGEN COPIANDO EL ARRAY DE LOS DATOS A ACTUALIZAR ELIMINANDOLE EL ULTIMO INDICE
				if(DB.dbPreparedStatement(PR.obtenerPropiedad("modificarActividadMantenerImagen")+Integer.parseInt(request.getParameter("id-actividad")), Arrays.copyOf(datos_edicion_actividad, datos_edicion_actividad.length-1))) {
					return "{\"resultado\": \"Actividad modificada con exito\", \"status\":"+200+"}";
				}else {
					return "{\"resultado\": \"La actividad no se pudo modificar\", \"status\":"+500+"}";				
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"resultado\": \"Error al editar actividad\", \"status\":"+500+"}";
		}
	}
	
	//METODO PARA ELIMINAR UNA ACTIVIDAD
	public static String eliminarActividad(HttpServletRequest request) {
		try {
			Database DB = Database.getInstances();
			PropertiesReader PR = PropertiesReader.getInstance();
			if(DB.dbStatement(PR.obtenerPropiedad("eliminarActividad")+Integer.parseInt(request.getParameter("id-actividad")))) {
				return "{\"resultado\": \"Actividad eliminada con exito\", \"status\":"+200+"}";
			}else {
				return "{\"resultado\": \"No se pudo eliminar la actividad\", \"status\":"+500+"}";
			}
		} catch (Exception e) {
			return "{\"resultado\": \"Error al eliminar actividad\", \"status\":"+500+"}";
		}
	}

}
