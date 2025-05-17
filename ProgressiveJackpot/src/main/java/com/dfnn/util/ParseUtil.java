package com.dfnn.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import de.siegmar.fastcsv.writer.CsvWriter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParseUtil {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParseUtil.class);
	private static final FilterMapperUtil MAPPER_UTIL = new FilterMapperUtil();

	public void parseCSV(File source, String destination) {
		try {
			Map<String, Object> recordDetails = this.filterRecords(source, "VENUE", "IW1PLAY");
			this.replaceHeader(recordDetails);
			this.replaceValue(recordDetails);
			recordDetails = this.arrangeFields(recordDetails);
			
			CsvWriter csvWrite = CsvWriter.builder().build(Paths.get(destination + "/" + source.getName()));
			
			Map<String,String> headers = (Map<String, String>) recordDetails.get("HEAD");
			List<String> headersToWrite = new ArrayList<String>();
			
			int count = 0;
			
			List<Object> records =  (List<Object>) recordDetails.get("RECORDS");
			for(Object record : records) {
				List<String> recordsToWrite = new ArrayList<>();
				LinkedHashMap<String,String> currentRecord = (LinkedHashMap<String, String>) record;
				System.out.println(currentRecord);
				for (var entry :  currentRecord.entrySet()) {
					if(count<=0) {
						headersToWrite.add(entry.getKey());
					}
				    recordsToWrite.add(currentRecord.get(entry.getKey() ));
				}
				
				if(count<=0) {
					csvWrite.writeRecord(headersToWrite);
				}
				
				csvWrite.writeRecord(recordsToWrite);
				count++;

			}
			
			csvWrite.flush();
			csvWrite.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	private Map<String, Object> arrangeFields(Map<String, Object> recordDetails) {

		Map<String, Object> newRecordDetails = new LinkedHashMap<String, Object>();

		Map<String, String> orderedHead = (Map<String, String>) recordDetails.get("HEAD");
		
		log.info("ORDERED HEAD : {}", orderedHead );

		Map<String, String> arrangedHead = new LinkedHashMap<>();
		arrangedHead.put("DATE", orderedHead.get("DATE") );
		arrangedHead.put("PROGRESSIVE_NAME", orderedHead.get("PROGRESSIVE_NAME") );
		arrangedHead.put("GAME_NAME", orderedHead.get("GAME_NAME") );
		arrangedHead.put("VENUE", orderedHead.get("VENUE") );
		arrangedHead.put("USERNAME", orderedHead.get("USERNAME") );
		arrangedHead.put("STAKE", orderedHead.get("STAKE") );
		arrangedHead.put("JACKPOT_BET", "JACKPOT_BET" );
		arrangedHead.put("JACKPOT_WINNINGS", orderedHead.get("JACKPOT_WINNINGS") );
		arrangedHead.put("PAYOUT", orderedHead.get("PAYOUT") );
		arrangedHead.put("GAME_ID", orderedHead.get("GAME_ID") );
		
		
		
		arrangedHead.put("JACKPOT_CONTRIBUTION_MINI", orderedHead.get("JACKPOT_CONTRIBUTION_MINI") );
		arrangedHead.put("JACKPOT_CONTRIBUTION_MINOR", orderedHead.get("JACKPOT_CONTRIBUTION_MINOR") );
		arrangedHead.put("JACKPOT_CONTRIBUTION_MAJOR", orderedHead.get("JACKPOT_CONTRIBUTION_MAJOR") );
		arrangedHead.put("JACKPOT_CONTRIBUTION_GRAND", orderedHead.get("JACKPOT_CONTRIBUTION_GRAND") );
		arrangedHead.put("JACKPOT_CONTRIBUTION_SUPER_GRAND", orderedHead.get("JACKPOT_CONTRIBUTION_SUPER_GRAND") );
		arrangedHead.put("GAME_PROVIDER", "GAME_PROVIDER" );
		newRecordDetails.put("HEAD", arrangedHead);
		log.info("ARRANGED : {} ", arrangedHead.toString());

		List<Object> arrangedRecords = new ArrayList<>();

		List<Object> records = (List<Object>) recordDetails.get("RECORDS");
		for (Object x : records) {
			Map<String, String> rec = (Map<String, String>) x;
			Map<String, String> arrangedRecord = new LinkedHashMap<String, String>();
			arrangedRecord.put(arrangedHead.get("DATE"), rec.get("DATE"));
			arrangedRecord.put(arrangedHead.get("PROGRESSIVE_NAME"), rec.get("PROGRESSIVE_NAME"));
			arrangedRecord.put(arrangedHead.get("GAME_NAME"), rec.get("GAME_NAME"));
			arrangedRecord.put(arrangedHead.get("VENUE"), rec.get("VENUE"));
			arrangedRecord.put(arrangedHead.get("USERNAME"), rec.get("USERNAME"));
			arrangedRecord.put(arrangedHead.get("STAKE"), rec.get("STAKE"));
			
			arrangedRecord.put("JACKPOT_BET", "" + (
					
					Double.valueOf(rec.get("JACKPOT_CONTRIBUTION_MINI") ) + 
					Double.valueOf(rec.get("JACKPOT_CONTRIBUTION_MINOR") ) + 
					Double.valueOf(rec.get("JACKPOT_CONTRIBUTION_MAJOR") ) + 
					Double.valueOf(rec.get("JACKPOT_CONTRIBUTION_GRAND") ) + 
					Double.valueOf(rec.get("JACKPOT_CONTRIBUTION_SUPER_GRAND") ) 
					)
					
					);
			arrangedRecord.put(arrangedHead.get("JACKPOT_WINNINGS"), rec.get("JACKPOT_WINNINGS"));
			arrangedRecord.put(arrangedHead.get("PAYOUT"), rec.get("PAYOUT"));
			arrangedRecord.put(arrangedHead.get("GAME_ID"), rec.get("GAME_ID"));
			arrangedRecord.put(arrangedHead.get("JACKPOT_CONTRIBUTION_MINI"), rec.get("JACKPOT_CONTRIBUTION_MINI"));
			arrangedRecord.put(arrangedHead.get("JACKPOT_CONTRIBUTION_MINOR"), rec.get("JACKPOT_CONTRIBUTION_MINOR"));
			arrangedRecord.put(arrangedHead.get("JACKPOT_CONTRIBUTION_MAJOR"), rec.get("JACKPOT_CONTRIBUTION_MAJOR"));
			arrangedRecord.put(arrangedHead.get("JACKPOT_CONTRIBUTION_GRAND"), rec.get("JACKPOT_CONTRIBUTION_GRAND"));
			arrangedRecord.put(arrangedHead.get("JACKPOT_CONTRIBUTION_SUPER_GRAND"), rec.get("JACKPOT_CONTRIBUTION_SUPER_GRAND"));
			arrangedRecord.put(arrangedHead.get("STAKE"), rec.get("STAKE"));
			String code = rec.get("GAME_ID");
			arrangedRecord.put("GAME_PROVIDER", MAPPER_UTIL.getValueReplacement().get(code.substring(0, code.indexOf("-")))  );
			
			arrangedRecords.add(arrangedRecord);
		}
		newRecordDetails.put("RECORDS", arrangedRecords);
		return newRecordDetails;
	}
	
	
	public void replaceValue(Map<String, Object> recordDetails) {
		List<Object> records = (List<Object>) recordDetails.get("RECORDS");

		for (Object record : records) {
			Map<String, String> currentRecord = (Map<String, String>) record;

			if (currentRecord.get("VENUE").equalsIgnoreCase("IW1PLAY")) {
				currentRecord.replace("VENUE", "IWLU1TER0C");
			}
		}

	}

	public void replaceHeader(Map<String, Object> recordDetails) {
		Map<String, String> orderedHead = (Map<String, String>) recordDetails.get("HEAD");
		log.info(orderedHead.toString());

		for (String key : orderedHead.keySet()) {
			String column = MAPPER_UTIL.getHeaderReplacement().get(key);
			if (column != null) {
				orderedHead.replace(key, MAPPER_UTIL.getHeaderReplacement().get(key));
			} else {
				orderedHead.replace(key, key);
			}
		}
	}

	public Map<String, Object> filterRecords(File source, String filterFied, String filterValue) throws IOException {

		Map<String, Object> recordDetails = new LinkedHashMap<String, Object>();
		CsvReader<CsvRecord> csvReader = CsvReader.builder().ofCsvRecord(Paths.get(source.getAbsolutePath()));
		Map<String, String> orderedHead = new LinkedHashMap<String, String>();

		for (CsvRecord rec : csvReader) {
			for (int x = 0; x < rec.getFieldCount(); x++) {
				orderedHead.put(rec.getField(x), "");
			}
			break;
		}
		recordDetails.put("HEAD", orderedHead);
		csvReader.close();

		List<Object> records = new ArrayList<Object>();
		CsvReader<NamedCsvRecord> csvNamedReader = CsvReader.builder()
				.ofNamedCsvRecord(Paths.get(source.getAbsolutePath()));
		for (NamedCsvRecord rec : csvNamedReader) {
			if (rec.getField(filterFied).equalsIgnoreCase(filterValue)) {
				Map<String, String> currentRecord = new LinkedHashMap<String, String>();
				for (String head : orderedHead.keySet()) {
					currentRecord.put(head, rec.getField(head));
				}
				records.add(currentRecord);
			}
		}
		recordDetails.put("RECORDS", records);
		csvNamedReader.close();
		return recordDetails;
	}

}
