package com.dfnn.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilterMapperUtil {

	
	public Map<String, String> getCashManHeaderReplacement() {
		Map<String, String> headerReplacement = new LinkedHashMap<>();
		headerReplacement.put("TRANS_ID", "TXN ID");
		headerReplacement.put("VENUE", "OUTLET");
		headerReplacement.put("INSERT", "CASHIER");
		headerReplacement.put("DATE", "TRANSFER DATE");
		headerReplacement.put("USERNAME", "TERMINAL");
		headerReplacement.put("AMOUNT", "AMOUNT");
		headerReplacement.put("TRANS_CODE", "TYPE");
		headerReplacement.put("PAYMENT_TYPE", "PAYMENT TYPE");
		
		return headerReplacement;
	}
	
	public Map<String, String> getJackpotHeaderReplacement() {

		Map<String, String> headerReplacement = new LinkedHashMap<>();
		headerReplacement.put("DATE", "DATE");
		headerReplacement.put("PROGRESSIVE_NAME", "PROGRESSIVE_NAME");
		headerReplacement.put("GAME_NAME", "GAME_NAME");
		headerReplacement.put("VENUE", "VENUE");
		headerReplacement.put("USERNAME", "USER_NAME");
		headerReplacement.put("STAKE", "STAKE");
		headerReplacement.put("JACKPOT_BET", "JACKPOT_BET");
		headerReplacement.put("JACKPOT_WINNINGS", "JACKPOT_WINNINGS");
		headerReplacement.put("PAYOUT", "PAYOUT");
		headerReplacement.put("GAME_ID", "GAME_ID");
		headerReplacement.put("JACKPOT_CONTRIBUTION_MINI", "PC1");
		headerReplacement.put("JACKPOT_CONTRIBUTION_MINOR", "PC2");
		headerReplacement.put("JACKPOT_CONTRIBUTION_MAJOR", "PC3");
		headerReplacement.put("JACKPOT_CONTRIBUTION_GRAND", "PC4");
		headerReplacement.put("JACKPOT_CONTRIBUTION_SUPER_GRAND", "PC5");
		headerReplacement.put("GAME_PROVIDER", "GAME_PROVIDER");
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

		valueReplacement.put("IW1PLAY", "IWLU1TER0C");
		valueReplacement.put("1", "deposit");
		valueReplacement.put("0", "withdraw");

		return valueReplacement;
	}

	public Map<String, String> getCustActivityHeaderReplacement() {
		Map<String, String> headerReplacement = new LinkedHashMap<>();
		headerReplacement.put("CLASS", "CLASS");
		headerReplacement.put("GAME_NAME", "GAME_NAME");
		headerReplacement.put("GAME_ID", "GAME_ID");
		headerReplacement.put("DATE", "DATE");
		headerReplacement.put("VENUE_NAME", "VENUE_NAME");
		headerReplacement.put("USERNAME", "USERNAME");
		headerReplacement.put("AMOUNT", "AMOUNT");
		headerReplacement.put("GAME_PROVIDER", "GAME_PROVIDER");
		return headerReplacement;
	}

}
