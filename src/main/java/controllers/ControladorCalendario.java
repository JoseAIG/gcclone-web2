package controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Database;

public class ControladorCalendario {

	public ControladorCalendario() {
		
	}
	
	//METODO PARA OBTENER LOS CALENDARIOS DE UN USUARIO
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
				//OBTENCION ACTIVIDADES DE UN CALENDARIO
				ArrayList<String[]> actividades_calendario = DB.dbObtenerActividadesCalendario(id_calendarios.get(i));
				
				resultado_JSON.append("{");
				resultado_JSON.append(" \"id_calendario\": \""+ id_calendarios.get(i) +"\", ");
				String[] datos_calendario = DB.dbObtenerDatosCalendario(id_calendarios.get(i));
				resultado_JSON.append(" \"nombre_calendario\": \""+ datos_calendario[0] +"\", \"color\": \""+ datos_calendario[1] +"\", \"invitados\":");
				
				//SE OBTIENEN LOS INVITADOS/COLABORADORES QUE POSEE EL CALENDARIO, SE RECORREN Y SE HACE APPEND
				ArrayList<String> invitados_calendario = DB.dbObtenerInvitadosCalendario(usuario,id_calendarios.get(i));
				if(invitados_calendario!=null) {
					resultado_JSON.append("[");
					for(int j=0; j<invitados_calendario.size();j++) {
						resultado_JSON.append(" \""+invitados_calendario.get(j)+"\" ");
						if(j!=(invitados_calendario.size()-1)) {
							resultado_JSON.append(",");
						}
					}
					//resultado_JSON.append("], \"actividades\":[");
					resultado_JSON.append("]");
				}else {
					resultado_JSON.append("null");
				}
				resultado_JSON.append(", \"actividades\":[");
				
				//SE OBTIENEN LAS ACTIVIDADES PERTENECIENTES A UN CALENDARIO, SE RECORREN Y SE HACE APPEND
				ArrayList<String[]> lista_actividades = DB.dbObtenerActividadesCalendario(id_calendarios.get(i));
				for(int j=0; j<lista_actividades.size(); j++) {
					//OBTENER LOS DATOS DE LA ACTIVIDAD j Y HACER APPEND
					String[] actividad = actividades_calendario.get(j);
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
				
				//CERRADO DEL JSON
				if(i==(id_calendarios.size()-1)) {
					resultado_JSON.append("}");
				}else {
					resultado_JSON.append("},");
				}
			}
			resultado_JSON.append("]}");
			System.out.println(resultado_JSON.toString());
			
			return resultado_JSON.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"resultado\": \"Error al obtener calendarios\", \"status\":"+500+"}";
		}
		
	}
	
	//METODO PARA CREAR UN NUEVO CALENDARIO
	public static String crearNuevoCalendrio(HttpServletRequest request) {
		try {
			System.out.println("CalendarController - crearNuevoCalendario");
			Database DB = Database.getInstances();
			
			String nombre_nuevo_calendario = request.getParameter("nombre-calendario");
			System.out.println(nombre_nuevo_calendario);
			
			String color_nuevo_calendario = request.getParameter("color-calendario");
			System.out.println(color_nuevo_calendario);
			
			Object [] datos_calendario = {nombre_nuevo_calendario, color_nuevo_calendario};
			int id_nuevo_calendario = DB.dbCrearCalendario(datos_calendario);
			if(id_nuevo_calendario==0) {
				System.out.println("no se pudo crear el calendario");
				return "{\"resultado\": \"Error al crear calendario\", \"status\":"+500+"}";
			}else {
				System.out.println("calendario creado, id_calendario: " + id_nuevo_calendario);
				//INCLUSION DE LOS DATOS DEL USUARIO (CREADOR DEL CALENDARIO) EN LA TABLA EDICIONES
				HttpSession sesion = request.getSession();
				String [] datos_usuario = DB.dbObtenerDatosUsuario(sesion.getAttribute("usuario").toString());
				Object [] datos_edicion = {datos_usuario[0],datos_usuario[1],id_nuevo_calendario,true};
				System.out.println("Resultado creacion datos edicion calendario: " + DB.dbCrearDatosEdicionCalendario(datos_edicion));
				
				//INCLUSION DE INVITADOS EN EL CALENDARIO DE EDICIONES
				int cantidad_invitados = Integer.parseInt(request.getParameter("cantidad-invitados").toString());
				//System.out.println("invitados: ");
				for(int i=0;i<cantidad_invitados;i++) {
					if(request.getParameter("input-invitado"+i)!=null && !request.getParameter("input-invitado"+i).equals("")) {
						String [] datos_invitado = DB.dbObtenerDatosUsuario(request.getParameter("input-invitado"+i));
						if(datos_invitado[0]!=null) {
							Object [] datos_edicion_invitado = {datos_invitado[0],datos_invitado[1],id_nuevo_calendario,false};
							//System.out.println("Invitado a almacenar: " + datos_invitado[0] +" "+datos_invitado[1]);
							//System.out.println("Resultado creacion datos edicion calendario invitado: " + DB.dbCrearDatosEdicionCalendario(datos_edicion_invitado));
							DB.dbCrearDatosEdicionCalendario(datos_edicion_invitado);
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
			//OBTENER DATOS DE LA PETICION
			HttpSession sesion = request.getSession();
			
			//EJECUTAR ACTUALIZACION DEL CALENDARIO
			Database DB = Database.getInstances();
			System.out.println(DB.dbActualizarCalendario(Integer.parseInt(request.getParameter("id-calendario")), request.getParameter("nombre-editar-calendario"), request.getParameter("color-editar-calendario")));
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
				
				if(datos_invitado[0]!=null && datos_invitado[1]!=null) {
					System.out.println(datos_invitado[0] + " - " + datos_invitado[1]);
					lista_nombres_invitados.add(lista_nombres_invitados.size(), datos_invitado[0]);
					lista_correos_invitados.add(lista_correos_invitados.size(), datos_invitado[1]);	
				}
			}
			System.out.println(DB.dbActualizarDatosEdicion(sesion.getAttribute("usuario").toString(), Integer.parseInt(request.getParameter("id-calendario")), lista_nombres_invitados, lista_correos_invitados));
			
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
			if(Boolean.parseBoolean(request.getParameter("propietario"))) {
				if(DB.dbEliminarCalendario(Integer.parseInt(request.getParameter("id-calendario")))) {
					return "{\"resultado\": \"Calendario eliminado exitosamente\", \"status\":"+200+"}";
				}else {
					return "{\"resultado\": \"No se pudo eliminar el calendario\", \"status\":"+500+"}";
				}
			}else {
				HttpSession sesion = request.getSession();
				if(DB.dbEliminarDatosEdicionCalendario(sesion.getAttribute("usuario").toString(), Integer.parseInt(request.getParameter("id-calendario")))) {
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
