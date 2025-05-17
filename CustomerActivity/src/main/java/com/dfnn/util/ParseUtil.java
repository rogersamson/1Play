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

public class ParseUtil {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParseUtil.class);
	private static final FilterMapperUtil MAPPER_UTIL = new FilterMapperUtil();

	public void parseCSV(File source, String destination) {
		try {
			Map<String, Object> recordDetails = this.filterRecords(source, "VENUE_NAME", "IW1PLAY");
			
			this.addField(recordDetails);
			this.replaceValue(recordDetails);
			
			CsvWriter csvWrite = CsvWriter.builder().build(Paths.get(destination + "/" + source.getName()));
			
			List<Object> records =  (List<Object>) recordDetails.get("RECORDS");
			List<String> headersToWrite = new ArrayList<String>();
			int count = 0;
			for(Object record : records) {
				List<String> recordsToWrite = new ArrayList<>();
				LinkedHashMap<String,String> currentRecord = (LinkedHashMap<String, String>) record;
				
				System.out.println(currentRecord);
				for (var entry :  currentRecord.entrySet()) {
					if(count<=0) {
						headersToWrite.add(entry.getKey());
					}
				    recordsToWrite.add( currentRecord.get(entry.getKey() ));
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
	
	public void replaceValue(Map<String, Object> recordDetails) {
		List<Object> records = (List<Object>) recordDetails.get("RECORDS");

		for (Object record : records) {
			Map<String, String> currentRecord = (Map<String, String>) record;

			if (currentRecord.get("VENUE_NAME").equalsIgnoreCase("IW1PLAY")) {
				currentRecord.replace("VENUE_NAME", "IWLU1TER0C");
			}
		}

	}

	private void addField(Map<String, Object> recordDetails) {
		List<Object> records = (List<Object>) recordDetails.get("RECORDS");

		for (Object record : records) {
			Map<String, String> currentRecord = (Map<String, String>) record;
			String code = currentRecord.get("GAME_ID");
			currentRecord.put("GAME_PROVIDER",  MAPPER_UTIL.getValueReplacement().get(code.substring(0, code.indexOf("-"))) );
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
