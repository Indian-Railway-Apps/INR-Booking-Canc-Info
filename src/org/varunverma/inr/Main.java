/**
 * 
 */
package org.varunverma.inr;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author varun
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Create Application Instance
		Application app = Application.getInstance();
		
		try {
			
			// Initialize Application
			app.initializeApplication();
			
			// Get Pending Queries
			List<QueryItem> pendingList = new PendingQueryItems().getPendingQueryItems();
			
			// Loop and Query the status
			Iterator<QueryItem> i = pendingList.iterator();
			
			while(i.hasNext()){
				
				// Query the Status
				i.next().queryStatus();
				
			}
			
			// Save the Status
			new SaveQueryStatus().saveStatus(pendingList);
			
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		} catch (ParserConfigurationException e) {
			
			System.out.println(e.getMessage());
			e.printStackTrace();

		} catch (SAXException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
