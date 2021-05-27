package helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hashing {

	public Hashing() {
		// TODO Auto-generated constructor stub
	}
	
	public static String obtenerHash(String texto) {
		String secret = "gcclone-web2";
		String cadena = secret + texto;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(cadena.getBytes());
			
			//CONVERTIR DE BYTES A CADENA HEXADECIMAL PARA OBTENER EL HASH
		    StringBuilder cadena_Hexadecimal = new StringBuilder(2 * bytes.length);
		    for (int i = 0; i < bytes.length; i++) {
		        String hex = Integer.toHexString(0xff & bytes[i]);
		        if(hex.length() == 1) {
		            cadena_Hexadecimal.append('0');
		        }
		        cadena_Hexadecimal.append(hex);
		    }
		    return cadena_Hexadecimal.toString();
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
