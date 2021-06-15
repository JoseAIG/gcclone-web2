package helpers;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	private static PropertiesReader propertiesReader = new PropertiesReader();
	private static Properties properties;

	//CONSTRUCTOR
	private PropertiesReader() {
		try {
			properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/resources/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//METODO PARA RETORNAR INSTANCIA DE PROPERTIESREADER
	public static PropertiesReader getInstance() {
		return propertiesReader;
	}
	
	//METODO PARA OBTENER UNA PROPIEDAD (KEY) Y RETORNAR EL VALOR
	public String obtenerPropiedad(String key) {
		return properties.getProperty(key);
	}

}
