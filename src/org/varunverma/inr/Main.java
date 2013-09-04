/**
 * 
 */
package org.varunverma.inr;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
				
				try{
					
					// Query the Status
					i.next().queryStatus();
					
				} catch(Exception e){
					
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				
			}
			
			// Save the Status
			new SaveQueryStatus().saveStatus(pendingList);
			
			
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		} catch (Exception e) {

			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
