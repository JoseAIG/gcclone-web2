package controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Database;
import helpers.PropertiesReader;

public class ControladorCalendario {

	public ControladorCalendario() {
		
	}
	
	//METODO PARA OBTENER LOS CALENDARIOS DE UN USUARIO (CON INVITADOS Y ACTIVIDADES)
	public static String obtenerCalendariosUsuario(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			String usuario = sesion.getAttribute("usuario").toString();
			
			Database DB = Database.getInstances();
			
			//OBTENER LOS ID'S DE LOS CALENDARIOS A LOS QUE TIENE ACCESO UN USUARIO
			ArrayList<Integer> id_calendarios = DB.dbObtenerIDsCalendarios(usuario);
			
			//OBJETO STRINGBUILDER PARA ARMAR LA RESPUESTA CON LOS DATOS DE LOS CALENDARIOS (INCLUYENDO ACTIVIDADES)
			StringBuilder resultado_JSON = new StringBuilder();
			resultado_JSON.append(" {\"resultado\":\"Armado json StringBuilder\", \"status\":"+200+", \"calendarios\": [");
			
			//SE RECORREN LOS CALENDARIOS
			for(int i=0; i<id_calendarios.size();i++) {
				
				//SE AGREGAN LOS VALORES BASICOS DEL CALENDARIO
				resultado_JSON.append("{");
				resultado_JSON.append(" \"id_calendario\": \""+ id_calendarios.get(i) +"\", ");
				String[] datos_calendario = DB.dbObtenerDatosCalendario(id_calendarios.get(i));
				resultado_JSON.append(" \"nombre_calendario\": \""+ datos_calendario[0] +"\", \"color\": \""+ datos_calendario[1] +"\", \"invitados\":");
				
				//SE OBTIENEN LOS INVITADOS QUE POSEE EL CALENDARIO, SI EL USUARIO NO ES PROPIETARIO DE UN CALENDARIO, LOS INVITADOS SON NULL, DE SER PROPIETARIO, SE RECORREN Y SE HACE APPEND
				//EN CASO DE QUE UN PROPIETARIO NO TENGA INVITADOS EN SU CALENDARIO, LOS INVITADOS SON UN ARREGLO VACIO.
				ArrayList<String> invitados_calendario = DB.dbObtenerInvitadosCalendario(usuario,id_calendarios.get(i));
				if(invitados_calendario!=null) {
					resultado_JSON.append("[");
					for(int j=0; j<invitados_calendario.size();j++) {
						resultado_JSON.append(" \""+invitados_calendario.get(j)+"\" ");
						if(j!=(invitados_calendario.size()-1)) {
							resultado_JSON.append(",");
						}
					}
					resultado_JSON.append("]");
				}else {
					resultado_JSON.append("null");
				}
				resultado_JSON.append(", \"actividades\":[");
				
				//SE OBTIENEN LAS ACTIVIDADES PERTENECIENTES A UN CALENDARIO, SE RECORREN Y SE HACE APPEND
				ArrayList<String[]> lista_actividades = DB.dbObtenerActividadesCalendario(id_calendarios.get(i));
				for(int j=0; j<lista_actividades.size(); j++) {
					//OBTENER LOS DATOS DE LA ACTIVIDAD j Y HACER APPEND
					String[] actividad = lista_actividades.get(j);
					resultado_JSON.append("{");
					resultado_JSON.append("\"id_actividad\": \""+actividad[0]+"\",");
					resultado_JSON.append("\"id_calendario\": \""+actividad[1]+"\",");
					resultado_JSON.append("\"informacion\": \""+actividad[2]+"\",");
					resultado_JSON.append("\"fecha\": \""+actividad[3]+"\",");
					resultado_JSON.append("\"hora_inicio\": \""+actividad[4]+"\",");
					resultado_JSON.append("\"hora_fin\": \""+actividad[5]+"\"");
					if(actividad[6]!=null) {
						resultado_JSON.append(",\"ruta_imagen\": \""+actividad[6]+"\"");					
					}
					resultado_JSON.append("}");
					if(j!=(lista_actividades.size()-1)) {
						resultado_JSON.append(",");
					}
				}
				resultado_JSON.append("]");
				
				//CERRADO DEL JSON ARMADO CON STRINGBUILDER
				if(i==(id_calendarios.size()-1)) {
					resultado_JSON.append("}");
				}else {
					resultado_JSON.append("},");
				}
			}
			resultado_JSON.append("]}");
			//System.out.println(resultado_JSON.toString());
			
			return resultado_JSON.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"resultado\": \"Error al obtener calendarios\", \"status\":"+500+"}";
		}
		
	}
	
	//METODO PARA CREAR UN NUEVO CALENDARIO
	public static String crearNuevoCalendrio(HttpServletRequest request) {
		try {
			Database DB = Database.getInstances();
			PropertiesReader PR = PropertiesReader.getInstance();
			
			//OBTENER POR PARAMETROS LOS DATOS DEL CALENDARIO PARA CREARLO
			String nombre_nuevo_calendario = request.getParameter("nombre-calendario");			
			String color_nuevo_calendario = request.getParameter("color-calendario");
			Object [] datos_calendario = {nombre_nuevo_calendario, color_nuevo_calendario};
			int id_nuevo_calendario = DB.dbCrearCalendario(datos_calendario);
			if(id_nuevo_calendario==0) {
				return "{\"resultado\": \"Error al crear calendario\", \"status\":"+500+"}";
			}else {
				//INCLUSION DE LOS DATOS DEL USUARIO (CREADOR DEL CALENDARIO) EN LA TABLA EDICIONES
				HttpSession sesion = request.getSession();
				String [] datos_usuario = DB.dbObtenerDatosUsuario(sesion.getAttribute("usuario").toString());
				Object [] datos_edicion = {datos_usuario[0],datos_usuario[1],id_nuevo_calendario,true};
				DB.dbPreparedStatement(PR.obtenerPropiedad("crearDatosEdicion"), datos_edicion);
				
				//INCLUSION DE INVITADOS EN EL CALENDARIO DE EDICIONES
				int cantidad_invitados = Integer.parseInt(request.getParameter("cantidad-invitados").toString());
				for(int i=0;i<cantidad_invitados;i++) {
					//SE VUELVE A COMPROBAR QUE EL INPUT DEL INVITADO SEA VALIDO, SE OBTIENEN LOS DATOS DEL USUARIO Y DE SER EXISTENTE, SE INCLUYEN LOS DATOS DE EDICION
					if(request.getParameter("input-invitado"+i)!=null && !request.getParameter("input-invitado"+i).equals("")) {
						String [] datos_invitado = DB.dbObtenerDatosUsuario(request.getParameter("input-invitado"+i));
						if(datos_invitado[0]!=null) {
							Object [] datos_edicion_invitado = {datos_invitado[0],datos_invitado[1],id_nuevo_calendario,false};
							DB.dbPreparedStatement(PR.obtenerPropiedad("crearDatosEdicion"), datos_edicion_invitado);
						}
					}
				}
				return "{\"resultado\": \"Creacion de calendario exitosa\", \"status\":"+200+", \"redirect\": \"/Dashboard\"}";
			}
			
		} catch (Exception e) {
			return "{\"resultado\": \"Error al crear calendario\", \"status\":"+500+"}";
		}
	}
	
	//METODO PARA EDITAR UN CALENDARIO Y DATOS DE EDICION DE CALENDARIO
	public static String editarCalendarioEdicion(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			
			//EJECUTAR ACTUALIZACION DEL CALENDARIO
			Database DB = Database.getInstances();
			PropertiesReader PR = PropertiesReader.getInstance();
			Object[] datos_calendario = {request.getParameter("nombre-editar-calendario"), request.getParameter("color-editar-calendario")};
			DB.dbPreparedStatement(PR.obtenerPropiedad("actualizarCalendario")+Integer.parseInt(request.getParameter("id-calendario")), datos_calendario);
			
			//OBTENER DATOS DE LOS INVITADOS 
			ArrayList<String> lista_nombres_invitados = new ArrayList<>();
			ArrayList<String> lista_correos_invitados = new ArrayList<>();
			for(int i=0;i<Integer.parseInt(request.getParameter("cantidad-invitados")+1);i++) {
				String [] datos_invitado = {null, null};
				try {
					datos_invitado = DB.dbObtenerDatosUsuario(request.getParameter("input-invitado"+i));
				} catch (Exception e) {
					datos_invitado[0] = null;
					datos_invitado[1] = null;
				}
				
				//SI LOS DATOS DEL INVITADO NO SON NULOS, AGREGAR DATOS A LA LISTA PARA POSTERIORMENTE ACTUALIZAR LOS DATOS DE EDICION
				if(datos_invitado[0]!=null && datos_invitado[1]!=null) {
					lista_nombres_invitados.add(lista_nombres_invitados.size(), datos_invitado[0]);
					lista_correos_invitados.add(lista_correos_invitados.size(), datos_invitado[1]);	
				}
			}
			DB.dbActualizarDatosEdicion(sesion.getAttribute("usuario").toString(), Integer.parseInt(request.getParameter("id-calendario")), lista_nombres_invitados, lista_correos_invitados);
			
			return "{\"resultado\": \"Calendario editado satisfactoriamente\", \"status\":"+200+"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"resultado\": \"Error al editar calendario\", \"status\":"+500+"}";
		}
	}
	
	//METODO PARA ELIMINAR UN CALENDARIO TRAS LA PETICION DE UN CLIENTE	
	public static String eliminarCalendario(HttpServletRequest request) {
		try {
			Database DB = Database.getInstances();
			PropertiesReader PR = PropertiesReader.getInstance();
			
			//SI UN PROPIETARIO DESEA ELIMINAR SU CALENDARIO, ELIMINARLO COMPLETAMENTE
			if(Boolean.parseBoolean(request.getParameter("propietario"))) {
				if(DB.dbStatement(PR.obtenerPropiedad("eliminarCalendario")+Integer.parseInt(request.getParameter("id-calendario")))) {
					return "{\"resultado\": \"Calendario eliminado exitosamente\", \"status\":"+200+"}";
				}else {
					return "{\"resultado\": \"No se pudo eliminar el calendario\", \"status\":"+500+"}";
				}
			}else {
				//SI UN USUARIO NO PROPIETARIO ELIMINA EL CALENDARIO, BORRAR UNICAMENTE LOS DATOS DE EDICION DEL USUARIO RESPECTIVO
				HttpSession sesion = request.getSession();
				if(DB.dbStatement("DELETE FROM ediciones WHERE (nombre_usuario='"+sesion.getAttribute("usuario").toString()+"' OR correo='"+sesion.getAttribute("usuario").toString()+"') AND id_calendario="+Integer.parseInt(request.getParameter("id-calendario")))) {
					System.out.println("datos edicion elim optimizados");
					return "{\"resultado\": \"Datos edicion eliminados exitosamente\", \"status\":"+200+"}";
				}else {
					return "{\"resultado\": \"No se pudo eliminar los datos de edicion\", \"status\":"+500+"}";
				}
			}
		} catch (Exception e) {
			return "{\"resultado\": \"Error al editar calendario\", \"status\":"+500+"}";
		}
	}
	
}
