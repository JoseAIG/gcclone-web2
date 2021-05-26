package controllers;

import java.util.ArrayList;

import helpers.Database;

public class CalendarController {

	public CalendarController() {
		
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
	
}
