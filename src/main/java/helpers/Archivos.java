package helpers;

import java.io.FileOutputStream;
import java.io.InputStream;

public class Archivos {

	public Archivos() {
		// TODO Auto-generated constructor stub
	}
	
	//METODO PARA CARGAR LA IMAGEN
	public static boolean almacenarImagen(InputStream input, FileOutputStream output) {
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
	
	//METODO PARA GENERAR UN NOMBRE DE ARCHIVO UNICO
	public static String generarNombreArchivo(String nombre_original) {
		String nuevo_nombre = System.currentTimeMillis() + nombre_original;
		return nuevo_nombre;
	}

}
