package helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

	private static PropertiesReader PR;
	
	public Hashing() {
		// TODO Auto-generated constructor stub
	}
	
	public static String obtenerHash(String texto) {
		//OBTENER EL SECRET EN PROPERTIES
		PR = PropertiesReader.getInstance();
		String secret = PR.obtenerPropiedad("hashSecret");
		String cadena = secret + texto;
		try {
			//SE INSTANCIA OBJETO MESSAGEDIGEST Y SE LEEN LOS BYTES DE LA CADENA (SECRET + TEXTO ENTRADA)
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(cadena.getBytes());
			
			//CONVERTIR DE BYTES A CADENA HEXADECIMAL PARA OBTENER EL HASH
		    StringBuilder cadena_Hexadecimal = new StringBuilder(2 * bytes.length);
		    for (int i = 0; i < bytes.length; i++) {
		        String valor_hex = Integer.toHexString(0xff & bytes[i]);
		        if(valor_hex.length() == 1) {
		            cadena_Hexadecimal.append('0');
		        }
		        cadena_Hexadecimal.append(valor_hex);
		    }
		    return cadena_Hexadecimal.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
