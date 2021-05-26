package controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Database;

public class ControladorCalendario {

	public ControladorCalendario() {
		
	}
	
	public static String obtenerCalendariosUsuario(String usuario) {
		String resultado;
		Database DB = Database.getInstances();
		
		//String [] datos_ediciones = DB.dbObtenerDatosEdicionCalendario(usuario);
		ArrayList<Integer> id_calendarios = DB.dbObtenerDatosEdicionCalendario(usuario);
		StringBuilder resultado_JSON = new StringBuilder();
		//resultado_JSON.append(" {\"resultado\":\"Prueba armado json\", \"status\":"+200+", \"calendarios\": [{\"id_calendario\": \"4\", \"nombre_calendario\": \"calendario admin\", \"color\": \"rojo\"}]}");
		
		
		
		resultado_JSON.append(" {\"resultado\":\"Prueba armado json\", \"status\":"+200+", \"calendarios\": [");
		
		for(int i=0; i<id_calendarios.size();i++) {
			resultado_JSON.append("{");
			resultado_JSON.append(" \"id_calendario\": \""+ id_calendarios.get(i) +"\", ");
			//System.out.print("Calendario ID: " + id_calendarios.get(i));
			String[] datos_calendario = DB.dbObtenerDatosCalendario(id_calendarios.get(i));
			resultado_JSON.append(" \"nombre_calendario\": \""+ datos_calendario[0] +"\", \"color\": \""+ datos_calendario[1] +"\" ");
			
			if(i==(id_calendarios.size()-1)) {
				resultado_JSON.append("}");
			}else {
				resultado_JSON.append("},");
			}
			//System.out.println(" Nombre: " + datos_calendario[0] + " Color: " + datos_calendario[1]);
		}
		resultado_JSON.append("]}");
		//System.out.println(resultado_JSON.toString());
		
		//resultado = "{\"resultado\": \"ID's de calendarios satisfactorio\", \"status\":"+200+"}";

		
//		String [] datos_calendarios = DB.dbObtenerDatosCalendario(Integer.parseInt(datos_ediciones[2]));
//		System.out.println("Datos de ediciones: " + datos_calendarios[0]+datos_calendarios[1]+datos_calendarios[2]);
//		resultado = "{\"resultado\": \"Datos calendarios satisfactorio\", \"status\":"+200+", \"nombre\":\""+datos_calendarios[1]+"\", \"color\":\""+datos_calendarios[2]+"\"}";
		
		return resultado_JSON.toString();
	}
	
	//METODO PARA CREAR UN NUEVO CALENDARIO
	public static void crearNuevoCalendrio(HttpServletRequest request) {
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
		}else {
			System.out.println("calendario creado, id_calendario: " + id_nuevo_calendario);
		}
		
		//INCLUSION DE LOS DATOS DEL USUARIO (CREADOR DEL CALENDARIO) EN LA TABLA EDICIONES
		HttpSession sesion = request.getSession();
		String [] datos_usuario = DB.dbExisteUsuario(sesion.getAttribute("usuario").toString());
		Object [] datos_edicion = {datos_usuario[0],datos_usuario[1],id_nuevo_calendario};
		System.out.println("Resultado creacion datos edicion calendario: " + DB.dbCrearDatosEdicionCalendario(datos_edicion));
		
		
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
	}
	
}
