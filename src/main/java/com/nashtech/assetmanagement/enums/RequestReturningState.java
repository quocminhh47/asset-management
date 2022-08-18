package com.nashtech.assetmanagement.enums;

import java.util.HashMap;
import java.util.Map;

public enum RequestReturningState {
	COMPLETED, WAITING_FOR_RETURNING;
	
	public static Map<String, String> getRequestReturningState(){
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("COMPLETED", "Completed");
		hashMap.put("WAITING_FOR_RETURNING","Waiting for returning");
		return hashMap;
	}
}
