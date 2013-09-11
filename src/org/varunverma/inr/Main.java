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
		
		String mode = "Full";
		
		if(args.length == 0){
			mode = "Full";
			System.out.println("Starting app in full mode");
		}
		else if(args[0].contentEquals("Correction")){
			mode = "Correction";
			System.out.println("Starting app in correction mode");
		}
		
		// Create Application Instance
		Application app = Application.getInstance();
		
		try {
			
			// Initialize Application
			app.initializeApplication();
			
			// Get Pending Queries
			List<QueryItem> pendingList = new PendingQueryItems().getPendingQueryItems(mode);
			
			// Loop and Query the status
			Iterator<QueryItem> i = pendingList.iterator();
			
			while(i.hasNext()){
				
				try{
					
					// Query the Status
					i.next().queryStatus();
					
				} catch(Exception e){
					
					System.out.println("Error while Quering Status");
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				
				// Sleep for some time.
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
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
		
		System.out.println("Process completed");
		
	}

}