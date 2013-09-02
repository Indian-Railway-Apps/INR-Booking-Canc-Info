package org.varunverma.inr;

import org.json.JSONObject;

public class AvailabilityInfo {
	
	public String TrainNo, JourneyDate, Class, LookupDate, LookupTime, fromStation, toStation;
	public String Availability, grossAvType, netAvType;
	public int grossAvCount, netAvCount; 

	protected AvailabilityInfo clone(){
		
		AvailabilityInfo ai = new AvailabilityInfo();
		
		ai.TrainNo = TrainNo;
		ai.JourneyDate = JourneyDate;
		ai.Class = Class;
		ai.LookupDate = LookupDate;
		ai.LookupTime = LookupTime;
		ai.fromStation = fromStation;
		ai.toStation = toStation;
		
		return ai;
	}
	
	public void setAvailability(String avail){
		
		Availability = avail;
		String cnt = "0";
		int index;
		
		avail = Availability.replaceAll(" ", "");
		String[] availability = avail.split("/");
		
		String grossAvail = availability[0];
		String netAvail = availability[1];
		
		if(grossAvail.contains("WL")){
			grossAvType = "WL";
			index = grossAvail.indexOf("WL");
			cnt = grossAvail.substring(index);
			if (cnt.contentEquals("")) {
				grossAvCount = 999;
			} else {
				if (cnt.contentEquals("")) {
					grossAvCount = 0;
				} else {
					grossAvCount = Integer.valueOf(cnt);
				}
			}
		}
		
		if(grossAvail.contains("RAC")){
			grossAvType = "RAC";
			index = grossAvail.indexOf("RAC");
			cnt = grossAvail.substring(index);
			grossAvCount = Integer.valueOf(cnt);
		}
		
		if(grossAvail.contains("AVAILABLE")){
			grossAvType = "CNF";
			index = grossAvail.indexOf("AVAILABLE");
			cnt = grossAvail.substring(index);
			grossAvCount = Integer.valueOf(cnt);
		}
		
		
		if (netAvail.contains("WL")) {
			netAvType = "WL";
			index = netAvail.indexOf("WL");
			cnt = netAvail.substring(index);
			netAvCount = Integer.valueOf(cnt);
		}
		
		if (netAvail.contains("RAC")) {
			netAvType = "RAC";
			index = netAvail.indexOf("RAC");
			cnt = netAvail.substring(index);
			netAvCount = Integer.valueOf(cnt);
		}
		
		if(netAvail.contains("AVAILABLE")){
			netAvType = "CNF";
			index = netAvail.indexOf("AVAILABLE");
			cnt = netAvail.substring(index);
			if (cnt.contentEquals("")) {
				netAvCount = 0;
			}
			else{
				netAvCount = Integer.valueOf(cnt);
			}
			
		}
		
	}

	public JSONObject jsonify() {

		JSONObject json = new JSONObject();
		
		json.put("TrainNo", TrainNo);
		json.put("JourneyDate", JourneyDate);
		json.put("JClass", Class);
		json.put("LookupDate", LookupDate);
		json.put("FromStations", fromStation);
		json.put("ToStation", toStation);
		json.put("GrossAvType", grossAvType);
		json.put("GrossAvCount", grossAvCount);
		json.put("NetAvType", netAvType);
		json.put("NetAvCount", netAvCount);
		
		return json;
		
	}
	
}