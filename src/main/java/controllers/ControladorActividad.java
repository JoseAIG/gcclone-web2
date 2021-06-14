package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

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
		boolean creacion_directorio = false;
		
		String estado_directorio = null;
		String ruta_imagen_guardar = null;
		boolean resultado_carga = false;
		try {
			
//			String ruta_activity_images= request.getSession().getServletContext().getRealPath("public/activity-images/");
//			System.out.println("Ruta activity images: " + ruta_activity_images);
//			System.out.println("getAbsolutePath: " + new File("public/activity-images/").getAbsolutePath());
			
			//String ruta_a_activity_images_act_21 = request.getSession().getServletContext().getRealPath("public/activity-images/act-21/kitten.jpg");
			//File archprueba = new File(ruta_a_activity_images_act_21);
			//System.out.println(ruta_a_activity_images_act_21);
			//System.out.println("archprueba: " + archprueba.exists());

			//TEST V2
//			Part part_imagen = request.getPart("imagen-crear-actividad");
//			String nombre_archivo = part_imagen.getSubmittedFileName();
//			String ruta_almacenar=null;
//			if(!nombre_archivo.equals("")) {
//				
//				//OBTENER LA RUTA DE LA APLICACION
//				ruta_aplicacion = request.getServletContext().getRealPath("");
//				System.out.println(ruta_aplicacion);
//				//ESTABLECER LA RUTA PARA GUARDAR EL ARCHIVO
//				ruta_imagenes = ruta_aplicacion + File.separator + "imagenes";
//				System.out.println(ruta_imagenes);
//				
//				//VERIFICAR SI EL DIRECTORIO EXISTE, DE NO EXISTIR, SE CREA
//				File directorioImagenes = new File (ruta_imagenes);
//				existe_directorio = directorioImagenes.exists();
//				System.out.println("Existe el directorio?: " + existe_directorio);
//				if(!existe_directorio) {
//					creacion_directorio = directorioImagenes.mkdirs();
//					System.out.println("Creacion del directorio: " + creacion_directorio);
//				}
//				System.out.println("directorio de la carga del archivo: " + directorioImagenes.getAbsolutePath());
//				
//				part_imagen.write(ruta_imagenes +  File.separator + nombre_archivo);
//				
//				ruta_almacenar = "imagenes/" + nombre_archivo;
//			}
			//FIN TEST V2
			
			Part part_imagen = request.getPart("imagen-crear-actividad");
			String nombre_archivo = part_imagen.getSubmittedFileName();
			String ruta_almacenar=null;
			if(!nombre_archivo.equals("")) {
				
				//OBTENER LA RUTA DE LA APLICACION
				ruta_aplicacion = request.getServletContext().getRealPath("");
				System.out.println(ruta_aplicacion);
				//ESTABLECER LA RUTA PARA GUARDAR EL ARCHIVO
				ruta_imagenes = ruta_aplicacion + File.separator + "imagenes";
				System.out.println(ruta_imagenes);
				
				//VERIFICAR SI EL DIRECTORIO EXISTE, DE NO EXISTIR, SE CREA
				File directorioImagenes = new File (ruta_imagenes);
				existe_directorio = directorioImagenes.exists();
				System.out.println("Existe el directorio?: " + existe_directorio);
				if(!existe_directorio) {
					creacion_directorio = directorioImagenes.mkdirs();
					System.out.println("Creacion del directorio: " + creacion_directorio);
				}
				System.out.println("directorio de la carga del archivo: " + directorioImagenes.getAbsolutePath());
				
				//part_imagen.write(ruta_imagenes +  File.separator + nombre_archivo);
				
				FileOutputStream output = new FileOutputStream(new File(ruta_imagenes +  File.separator + nombre_archivo));
				InputStream input = part_imagen.getInputStream();
				System.out.println("resultado almacenar imagen: " + almacenarImagen(input, output));
				
				ruta_almacenar = "imagenes/" + nombre_archivo;
			}
			
			//TEST FINAL V3
			
			//FIN TEST FINAL V3
			
			//TEST V1
			//OBTENER LAS PARTES DEL ARCHIVO IMAGEN DEL CLIENTE
//			Part part_imagen = request.getPart("imagen-crear-actividad");
//			System.out.println(part_imagen.getSize());
//			//System.out.println("Nombre del archivo: " + part_imagen.getSubmittedFileName());
//			System.out.println("Nombre del archivo: " + getSubmittedFileName(part_imagen));
//			
//			//MKDIRS
//			String carpeta_imagenes_raiz = "/imagenes/actividades/";
//			File directorio = new File(carpeta_imagenes_raiz);
//			if(directorio.mkdirs()) {
//				estado_directorio = "ruta creada: " + directorio.getAbsolutePath();
//				System.out.println("ruta creada: " + directorio.getAbsolutePath());
//			}else {
//				estado_directorio= "no se creo la ruta";
//				System.out.println("No se pudo crear la ruta");
//			}
//
//			String ruta_almacenar_db;
//			if(getSubmittedFileName(part_imagen).equals("")) {
//				//SI NO INGRESO UNA IMAGEN SE ALMACENA NULL EN EL CAMPO RUTA EN LA BASE DE DATOS
//				System.out.println("almacenar null");
//				ruta_imagen_guardar = null;
//				ruta_almacenar_db = null;
//				resultado_carga=false;
//			}else {
//				
//				
//				//SI EL ARCHIVO NO ES NULO, ALMACENARLO EN "activity-images/test"
////				String ruta_guardar_imagen = ruta_activity_images+"\\test\\"+getSubmittedFileName(part_imagen);
////				File archivo = new File(ruta_guardar_imagen);
////				System.out.println(archivo.getAbsolutePath());
////				part_imagen.write(ruta_guardar_imagen);
////				//SE COLOCA LA RUTA DEL ARCHIVO EN LA BASE DE DATOS
////				ruta_almacenar_db = "public/activity-images/test/"+getSubmittedFileName(part_imagen);
//				
//				//MKDIRS
//				//part_imagen.write(carpeta_imagenes_raiz+getSubmittedFileName(part_imagen));
//				//System.out.println("La imagen se guerdo en: " + directorio.getAbsolutePath()+"\\"+getSubmittedFileName(part_imagen));
//				InputStream inputStreamPart = part_imagen.getInputStream();
//				FileOutputStream outputStream = new FileOutputStream(carpeta_imagenes_raiz+getSubmittedFileName(part_imagen));
//				System.out.println("Ruta de la imagen que se cargara: " + carpeta_imagenes_raiz+getSubmittedFileName(part_imagen));
//				ruta_imagen_guardar= carpeta_imagenes_raiz+getSubmittedFileName(part_imagen);
//				//ruta_imagen_guardar= directorio.getAbsolutePath()+File.separator+getSubmittedFileName(part_imagen);
//				System.out.println("Ruta absoluta de la imagen a cargar: " + directorio.getAbsolutePath()+File.separator+getSubmittedFileName(part_imagen));
//				//System.out.println("resultado de la carga: " + cargar(inputStreamPart, outputStream));
//				resultado_carga = cargar(inputStreamPart, outputStream);
//				ruta_almacenar_db = carpeta_imagenes_raiz+getSubmittedFileName(part_imagen);
//				//ruta_almacenar_db = directorio.getAbsolutePath()+File.separator+getSubmittedFileName(part_imagen);
//			}
			//FIN TEST V1
			
//			String ruta_guardar_imagen = ruta_activity_images+"\\test\\"+part_imagen.getSubmittedFileName();
//			File archivo = new File(ruta_guardar_imagen);
//			System.out.println(archivo.getAbsolutePath());
//			//File archivo_imagen = new File(part_imagen.getSubmittedFileName());
//			//part_imagen.write(part_imagen.getSubmittedFileName());
//			part_imagen.write(ruta_guardar_imagen);
			
			//CREAR UN ARREGLO DE OBJETOS CON LOS PARAMETROS DE LA PETICION (DATOS ACTIVIDAD)
			Object[] datos_nueva_actividad = {
				Integer.parseInt(request.getParameter("select-calendario-crear-actividad")), 
				request.getParameter("detalle-actividad"),
				request.getParameter("fecha-crear-actividad"),
				Double.parseDouble(request.getParameter("hora-inicio")),
				Double.parseDouble(request.getParameter("hora-fin")),
				//ruta_guardar_imagen
				//null
				ruta_almacenar
			};
			//INGRESAR EN LA BASE DE DATOS LA INFORMACION DE LA ACTIVIDAD
			Database DB = Database.getInstances();
			PropertiesReader PR = PropertiesReader.getInstance();
			if(DB.dbPreparedStatement(PR.obtenerPropiedad("crearActividad"), datos_nueva_actividad)) {
				//String respuesta = "{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+", \"estado-directorio\":\""+estado_directorio+"\", \"ruta-imagen-guardar\":\""+ruta_imagen_guardar+"\", \"resultado-carga\":\""+resultado_carga+"\" }";

//				String respuesta = "{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+", \"ruta-aplicacion\":\""+ruta_aplicacion+"\", \"ruta-imagenes\":\""+ruta_imagenes+"\", \"existe-directorio\":\""+existe_directorio+"\",\"creacion-directorio\":\""+creacion_directorio+"\"}";
//				System.out.println(respuesta);
//				return(respuesta);
				
				return "{\"resultado\": \"Actividad creada satisfactoriamente\", \"status\":"+200+"}";
			}else {
				System.out.println(2);
				//return("{\"resultado\": \"No se pudo ingresar la actividad en la base de datos\", \"status\":"+500+", \"estado-directorio\":\""+estado_directorio+"\", \"ruta-imagen-guardar\":\""+ruta_imagen_guardar+"\", \"resultado-carga\":\""+resultado_carga+"\"}");
				//return("{\"resultado\": \"No se pudo ingresar la actividad en la base de datos\", \"status\":"+500+", \"ruta-aplicacion\":\""+ruta_aplicacion+"\", \"ruta-imagenes\":\""+ruta_imagenes+"\", \"existe-directorio\":\""+existe_directorio+"\",\"creacion-directorio\":\""+creacion_directorio+"\"}");
				return "{\"resultado\": \"No se pudo ingresar la actividad en la base de datos\", \"status\":"+500+"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(3);
			//return("{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+", \"estado-directorio\":\""+estado_directorio+"\", \"ruta-imagen-guardar\":\""+ruta_imagen_guardar+"\", \"resultado-carga\":\""+resultado_carga+"\"}");
			//return("{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+", \"ruta-aplicacion\":\""+ruta_aplicacion+"\", \"ruta_imagenes\":\""+ruta_imagenes+"\", \"existe-directorio\":\""+existe_directorio+"\", \"creacion-directorio\":\""+creacion_directorio+"\" }");
			return "{\"resultado\": \"No se pudo crear la actividad\", \"status\":"+500+"}";
		}
		
	}
	
	//METODO PARA MODIFICAR UNA ACTIVIDAD
	public static String modificarActividad(HttpServletRequest request) {
		System.out.println(request.getParameter("eliminar-imagen-actividad"));
		
		String ruta_aplicacion = null;
		String ruta_imagenes = null;
		boolean existe_directorio = false;
		boolean creacion_directorio = false;
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
					creacion_directorio = directorioImagenes.mkdirs();
				}
				
				//part_imagen.write(ruta_imagenes +  File.separator + nombre_archivo);				
				
				FileOutputStream output = new FileOutputStream(new File(ruta_imagenes +  File.separator + nombre_archivo));
				InputStream input = part_imagen.getInputStream();
				System.out.println("resultado almacenar imagen (editar): " + almacenarImagen(input, output));
				
				ruta_almacenar = "imagenes/" + nombre_archivo;
			}
			
			
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
	
	//METODO PARA CARGAR LA IMAGEN
	private static boolean almacenarImagen(InputStream input, FileOutputStream output) {
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
