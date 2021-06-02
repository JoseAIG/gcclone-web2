package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import helpers.Database;

public class ControladorCalendario {

	public ControladorCalendario() {
		
	}
	
	public static String obtenerCalendariosUsuario(HttpServletRequest request) {
		HttpSession sesion = request.getSession();
		String usuario = sesion.getAttribute("usuario").toString();
		
		Database DB = Database.getInstances();
		
		//String [] datos_ediciones = DB.dbObtenerDatosEdicionCalendario(usuario);
		ArrayList<Integer> id_calendarios = DB.dbObtenerIDsCalendarios(usuario);
		StringBuilder resultado_JSON = new StringBuilder();
		//resultado_JSON.append(" {\"resultado\":\"Prueba armado json\", \"status\":"+200+", \"calendarios\": [{\"id_calendario\": \"4\", \"nombre_calendario\": \"calendario admin\", \"color\": \"rojo\"}]}");
		
		//CAMBIO A JSONOBJECT
		JSONObject json_respuesta = new JSONObject();
		json_respuesta.put("resultado", "prueba JSONObject");
		json_respuesta.put("status", 200);
		
//		JSONArray arreglo_calendarios = new JSONArray();
//		JSONObject calendario = new JSONObject();
//		calendario.put("id_calendario", "test");
//		calendario.put("nombre_calendario", "test");
//		arreglo_calendarios.put(calendario);
//		
//		json_respuesta.put("calendarios", arreglo_calendarios);
		
		//System.out.println("JSONObject a enviar: " + json_respuesta);
		
//		resultado_JSON.append(" {\"resultado\":\"Prueba armado json\", \"status\":"+200+", \"calendarios\": [");
		
//		for(int i=0; i<id_calendarios.size();i++) {
//			//PRUEBA OBTENCION ACTIVIDADES
//			ArrayList<String[]> actividades_calendario = DB.dbObtenerActividadesCalendario(id_calendarios.get(i));
//			System.out.println("Impresion de las actividades del calendario");
//			for(int j=0; j<actividades_calendario.size(); j++) {
//				String[] actividad = actividades_calendario.get(i);
//				System.out.print("Long de la act: " + actividad.length);
//				System.out.println(" - id_actividad: " + actividad[0] + " id_calendario: " + actividad[1] + " informacion: " + actividad[2] + " fecha: " + actividad[3] + " hora_inicio: " + actividad[4] + " hora_fin: " + actividad[5] + " duracion: " + actividad[6] + " ruta_imagen: " + actividad[7]);
//			}
//			
//			resultado_JSON.append("{");
//			resultado_JSON.append(" \"id_calendario\": \""+ id_calendarios.get(i) +"\", ");
//			String[] datos_calendario = DB.dbObtenerDatosCalendario(id_calendarios.get(i));
//			resultado_JSON.append(" \"nombre_calendario\": \""+ datos_calendario[0] +"\", \"color\": \""+ datos_calendario[1] +"\", \"invitados\": [ ");
//			
//			ArrayList<String> invitados_calendario = DB.dbObtenerInvitadosCalendario(usuario,id_calendarios.get(i));
//			for(int j=0; j<invitados_calendario.size();j++) {
//				resultado_JSON.append(" \""+invitados_calendario.get(j)+"\" ");
//				if(j==(invitados_calendario.size()-1)) {
//				}else {
//					resultado_JSON.append(",");
//				}
//			}
//			resultado_JSON.append("]");
//
//			if(i==(id_calendarios.size()-1)) {
//				resultado_JSON.append("}");
//			}else {
//				resultado_JSON.append("},");
//			}
//		}
//		resultado_JSON.append("]}");
//		System.out.println(resultado_JSON.toString());
		
		//ARMADO JSON CON JSONOBJECT
		JSONArray arreglo_calendarios = new JSONArray();
		
		for(int i=0; i<id_calendarios.size();i++) {
			JSONObject calendario = new JSONObject();
			String[] datos_calendario = DB.dbObtenerDatosCalendario(id_calendarios.get(i));
			calendario.put("id_calendario", id_calendarios.get(i));
			calendario.put("nombre_calendario", datos_calendario[0]);
			calendario.put("color", datos_calendario[1]);

			//INVITADOS DE UN CALENDARIO
			JSONArray invitados = new JSONArray();
			ArrayList<String> invitados_calendario = DB.dbObtenerInvitadosCalendario(usuario,id_calendarios.get(i));
			for(int j=0; j<invitados_calendario.size(); j++) {
				invitados.put(invitados_calendario.get(j));
			}
			calendario.put("invitados", invitados);
			
			//ACTIVIDADES DE UN CALENDARIO
			JSONArray actividades = new JSONArray();
			ArrayList<String[]> actividades_calendario = DB.dbObtenerActividadesCalendario(id_calendarios.get(i));
			for(int j=0; j<actividades_calendario.size(); j++) {
				String[] actividad = actividades_calendario.get(j);
				//System.out.println(" - id_actividad: " + actividad[0] + " id_calendario: " + actividad[1] + " informacion: " + actividad[2] + " fecha: " + actividad[3] + " hora_inicio: " + actividad[4] + " hora_fin: " + actividad[5] + " duracion: " + actividad[6] + " ruta_imagen: " + actividad[7]);
				JSONObject datos_actividad = new JSONObject();
				datos_actividad.put("id_actividad", actividad[0]);
				datos_actividad.put("id_calendario", actividad[1]);
				datos_actividad.put("informacion", actividad[2]);
				datos_actividad.put("fecha", actividad[3]);
				datos_actividad.put("hora_inicio", actividad[4]);
				datos_actividad.put("hora_fin", actividad[5]);
				datos_actividad.put("duracion", actividad[6]);
				datos_actividad.put("ruta_imagen", actividad[7]);
				//INSERTAR DATOS DE LA ACTIVIDAD EN EL ARREGLO DE ACTIVIDADES
				actividades.put(datos_actividad);
			}
			calendario.put("actividades", actividades);

			//AL FINAL SE COLOCA EL CALENDARIO EN EL ARREGLO DE CALENDARIOS
			arreglo_calendarios.put(calendario);
		}
		
		//SE LE COLOCA EL ARREGLO DE CALENDARIOS AL JSON DE RESPUESTA
		json_respuesta.put("calendarios",arreglo_calendarios);
		
		//System.out.println(json_respuesta.toString());
		
		//System.out.println(resultado_JSON.toString());
		return json_respuesta.toString();
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
			//System.out.println(DB.dbCrearCalendario(datos_calendario));
			int id_nuevo_calendario = DB.dbCrearCalendario(datos_calendario);
			if(id_nuevo_calendario==0) {
				System.out.println("no se pudo crear el calendario");
				return "{\"resultado\": \"Error al crear calendario\", \"status\":"+500+"}";
			}else {
				System.out.println("calendario creado, id_calendario: " + id_nuevo_calendario);
				//INCLUSION DE LOS DATOS DEL USUARIO (CREADOR DEL CALENDARIO) EN LA TABLA EDICIONES
				HttpSession sesion = request.getSession();
				String [] datos_usuario = DB.dbExisteUsuario(sesion.getAttribute("usuario").toString());
				Object [] datos_edicion = {datos_usuario[0],datos_usuario[1],id_nuevo_calendario};
				System.out.println("Resultado creacion datos edicion calendario: " + DB.dbCrearDatosEdicionCalendario(datos_edicion));
				
				//INCLUSION DE INVITADOS EN EL CALENDARIO DE EDICIONES
				int cantidad_invitados = Integer.parseInt(request.getParameter("cantidad-invitados").toString());
				System.out.println("invitados: ");
				for(int i=0;i<cantidad_invitados;i++) {
					if(request.getParameter("input-invitado"+i)!=null && !request.getParameter("input-invitado"+i).equals("")) {
						//System.out.println(request.getParameter("input-invitado"+i));
						String [] datos_invitado = DB.dbExisteUsuario(request.getParameter("input-invitado"+i));
						if(datos_invitado[0]!=null) {
							Object [] datos_edicion_invitado = {datos_invitado[0],datos_invitado[1],id_nuevo_calendario};
							System.out.println("Invitado a almacenar: " + datos_invitado[0] +" "+datos_invitado[1]);
							System.out.println("Resultado creacion datos edicion calendario invitado: " + DB.dbCrearDatosEdicionCalendario(datos_edicion_invitado));
						}
					}
				}
				return "{\"resultado\": \"Creacion de calendario exitosa\", \"status\":"+200+", \"redirect\": \"/Dashboard\"}";
			}
			
		} catch (Exception e) {
			return "{\"resultado\": \"Error al crear calendario\", \"status\":"+500+"}";
		}
	}
	
	//METODO PARA EDITAR UN CALENDARIO Y LOS DATOS DE EDICION
	public static String editarCalendarioEdicion(HttpServletRequest request) {
		try {
			//OBTENER DATOS DE LA PETICION
			HttpSession sesion = request.getSession();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String data = br.readLine();
			System.out.println("Controlador calendario - editarCalendarioEdicion: " + data);
			//CONVERTIR DATOS A JSONObject PARA MANEJAR LA INFORMACION
			JSONObject json_editar_calendario = new JSONObject(data);
			System.out.println("La prueba del arreglo es: " + json_editar_calendario.get("arreglo"));
			JSONArray array_invitados = new JSONArray(json_editar_calendario.get("arreglo").toString());
			//System.out.println(array_invitados.get(0));
			for(int k=0; k<array_invitados.length(); k++) {
				System.out.println(array_invitados.get(k));
			}
			
			//EJECUTAR QUERY
			Database DB = Database.getInstances();
			System.out.println(DB.dbActualizarCalendario(json_editar_calendario.getInt("id-calendario"), json_editar_calendario.getString("nombre-editar-calendario"), json_editar_calendario.getString("color-editar-calendario")));
			//System.out.println("ID: " + json_editar_calendario.getInt("id-calendario") + " Nombre: " + json_editar_calendario.getString("nombre-editar-calendario") + " Color: " + json_editar_calendario.getString("color-editar-calendario"));
			ArrayList<String> lista_nombres_invitados = new ArrayList<>();
			ArrayList<String> lista_correos_invitados = new ArrayList<>();
			for(int i=0;i<json_editar_calendario.getInt("cantidad-invitados")+1;i++) {
				String [] datos_invitado = {null, null};
				try {
					datos_invitado = DB.dbExisteUsuario(json_editar_calendario.getString("input-invitado"+i));
				} catch (Exception e) {
					//json_editar_calendario.put("input-invitado"+i, "");
					datos_invitado[0] = null;
					datos_invitado[1] = null;
				}
				
				if(datos_invitado[0]!=null && datos_invitado[1]!=null) {
					System.out.println(datos_invitado[0] + " - " + datos_invitado[1]);
					lista_nombres_invitados.add(lista_nombres_invitados.size(), datos_invitado[0]);
					lista_correos_invitados.add(lista_correos_invitados.size(), datos_invitado[1]);	
				}
			}
			System.out.println(DB.dbActualizarDatosEdicion(sesion.getAttribute("usuario").toString(), json_editar_calendario.getInt("id-calendario"), lista_nombres_invitados, lista_correos_invitados));
			
			//RETORNAR RESPUESTA AL SERVLET PARA ENVIARLA AL CLIENTE
			JSONObject json_respuesta = new JSONObject();
			json_respuesta.put("resultado", "Peticion existosa");
			json_respuesta.put("status", 200);
			
			return json_respuesta.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"resultado\": \"Error al editar calendario\", \"status\":"+500+"}";
		}
	}
	
	//METODO PARA ELIMINAR UN CALENDARIO TRAS LA PETICION DE UN CLIENTE
	public static String eliminarCalendario(HttpServletRequest request) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String data = br.readLine();
			System.out.println("Controlador calendario - eliminarCalendario: " + data);
			JSONObject json_peticion = new JSONObject(data);
			System.out.println(json_peticion.get("id-calendario").toString());
			Database DB = Database.getInstances();
			JSONObject json_respuesta = new JSONObject();
			if(DB.dbEliminarCalendario(Integer.parseInt(json_peticion.get("id-calendario").toString()))) {
				json_respuesta.put("resultado", "Calendario eliminado exitosamente");
				json_respuesta.put("status", 200);
				return json_respuesta.toString();
			}else {
				json_respuesta.put("resultado", "No se pudo eliminar el calendario");
				json_respuesta.put("status", 500);
				return json_respuesta.toString();
			}
		} catch (Exception e) {
			return "{\"resultado\": \"Error al editar calendario\", \"status\":"+500+"}";
		}
	}
	
}
