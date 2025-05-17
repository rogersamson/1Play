package com.dfnn.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilterMapperUtil {
	public Map<String, String> getHeaderReplacement() {
		Map<String, String> headerReplacement = new LinkedHashMap<>();
		headerReplacement.put("DATE", "DATE");
		headerReplacement.put("PROGRESSIVE_NAME", "PROGRESSIVE_NAME");
		headerReplacement.put("GAME_NAME", "GAME_NAME");
		headerReplacement.put("VENUE", "VENUE");
		headerReplacement.put("USERNAME", "USER_NAME");
		headerReplacement.put("STAKE", "STAKE");
		headerReplacement.put("JACKPOT_CONTRIBUTION_MINI", "PC1");
		headerReplacement.put("JACKPOT_CONTRIBUTION_MINOR", "PC2");
		headerReplacement.put("JACKPOT_CONTRIBUTION_MAJOR", "PC3");
		headerReplacement.put("JACKPOT_CONTRIBUTION_GRAND", "PC4");
		headerReplacement.put("JACKPOT_CONTRIBUTION_SUPER_GRAND", "PC5");
		headerReplacement.put("JACKPOT_WINNINGS", "JACKPOT_WINNINGS");
		headerReplacement.put("PAYOUT", "PAYOUT");
		headerReplacement.put("GAME_ID", "GAME_ID"); 
		headerReplacement.put("FG_BET/EXTRA_BET", "EXTRA_BET"); 
		return headerReplacement;
	}
	
	public Map<String, String> getValueReplacement() {
		Map<String, String> valueReplacement = new HashMap<>();
		
		valueReplacement.put("JL", "JILI");
		valueReplacement.put("BP", "BIGPOT");
		valueReplacement.put("RTG", "RTG");
		valueReplacement.put("BO", "BNG");
		valueReplacement.put("CQ9", "CQ9");
		valueReplacement.put("ELB", "ELBET");
		valueReplacement.put("FC", "FACHAI");
		valueReplacement.put("HA", "HABANERO");
		valueReplacement.put("PP", "PRAGMATIC PLAY");
		valueReplacement.put("SG", "SPADEGAMING");
		valueReplacement.put("JDB", "JDB");
		valueReplacement.put("NS", "NEXTSPIN");
		valueReplacement.put("PGS", "PGSOFT");
		valueReplacement.put("WZN", "WAZDAN");
		valueReplacement.put("YGG", "YGGDRASIL");
		valueReplacement.put("PLS", "PLAYSON");
		valueReplacement.put("NLC", "NO LIMIT CITY");
		valueReplacement.put("PS", "PLAYSTAR");
		valueReplacement.put("NET", "NETENT");
		valueReplacement.put("BTG", "BTG");
		valueReplacement.put("TPG", "TPG");
		valueReplacement.put("AFB", "AFB LIVE");
		
		valueReplacement.put("IW1PLAY", "IWLU1TER0C");
		
		return valueReplacement;
	}

}
