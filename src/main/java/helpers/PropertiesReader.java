package helpers;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	private static PropertiesReader propertiesReader = new PropertiesReader();
	private static Properties properties;

	
	private PropertiesReader() {
		try {
			properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/resources/db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PropertiesReader getInstance() {
		return propertiesReader;
	}
	
	public String obtenerPropiedad(String key) {
		return properties.getProperty(key);
	}

}
