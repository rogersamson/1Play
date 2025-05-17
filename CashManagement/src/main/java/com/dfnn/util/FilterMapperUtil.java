package com.dfnn.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilterMapperUtil {
	public Map<String, String> getHeaderReplacement() {
		Map<String, String> headerReplacement = new LinkedHashMap<>();
		headerReplacement.put("CASHIER", "PAYMENT TYPE");
		headerReplacement.put("TRANS_ID", "TXN ID");
		headerReplacement.put("VENUE", "OUTLET");
		headerReplacement.put("DATE", "TRANSFER DATE");
		headerReplacement.put("USERNAME", "TERMINAL");
		headerReplacement.put("TRANS_CODE", "TYPE");
		
		return headerReplacement;
	}
	
	
	
	
	
	public Map<String, String> getValueReplacement() {
		Map<String, String> valueReplacement = new HashMap<>();
		
		valueReplacement.put("IW1PLAY", "IWLU1TER0C");
		valueReplacement.put("1", "deposit");
		valueReplacement.put("0", "withdraw");
		
		return valueReplacement;
	}

}
