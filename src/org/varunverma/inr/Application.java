/**
 * 
 */
package org.varunverma.inr;

import java.io.IOException;
import java.util.Properties;


/**
 * @author varun
 * 
 */
public class Application {

	private static Application app;
	private Properties properties;
	
	public static Application getInstance() {

		if (app == null) {
			app = new Application();
		}

		return app;

	}

	private Application() {
		
		properties = new Properties();
		
	}
	
	public void initializeApplication() throws IOException{
		
		properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
		
	}
	
	public Properties getApplicationProperties(){
		return properties;
	}

}
