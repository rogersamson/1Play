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
			
			Map<String,String> headers = (Map<String, String>) recordDetails.get("HEAD");
			CsvWriter csvWrite = CsvWriter.builder().build(Paths.get(destination + "/" + source.getName()));
			List<String> headerToWrite = new ArrayList<>();
			for (var entry :  headers.entrySet()) {
			   
				headerToWrite.add( headers.get(entry.getKey() ));
			}
			csvWrite.writeRecord(headerToWrite);

			
			List<Object> records =  (List<Object>) recordDetails.get("RECORDS");
			for(Object record : records) {
				List<String> recordsToWrite = new ArrayList<>();
				LinkedHashMap<String,String> currentRecord = (LinkedHashMap<String, String>) record;
				System.out.println(currentRecord);
				for (var entry :  currentRecord.entrySet()) {
				    recordsToWrite.add(currentRecord.get(entry.getKey() ));
				}
				csvWrite.writeRecord(recordsToWrite);
			}
			csvWrite.flush();
			csvWrite.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private Map<String, Object> arrangeFields(Map<String, Object> recordDetails) {

		Map<String, Object> newRecordDetails = new LinkedHashMap<String, Object>();

		Map<String, String> orderedHead = (Map<String, String>) recordDetails.get("HEAD");

		Map<String, String> arrangedHead = new LinkedHashMap<>();
		arrangedHead.put("TRANS_ID", orderedHead.get("TRANS_ID"));
		arrangedHead.put("VENUE", orderedHead.get("VENUE"));
		arrangedHead.put("CASHIER", "CASHIER");
		arrangedHead.put("DATE", orderedHead.get("DATE"));
		arrangedHead.put("USERNAME", orderedHead.get("USERNAME"));
		arrangedHead.put("AMOUNT", orderedHead.get("AMOUNT"));
		arrangedHead.put("TRANS_CODE", orderedHead.get("TRANS_CODE"));
		arrangedHead.put("PAYMENT TYPE", orderedHead.get("CASHIER"));

		newRecordDetails.put("HEAD", arrangedHead);
		log.info("ARRANGED : {} ", arrangedHead.toString());

		List<Object> arrangedRecords = new ArrayList<>();

		List<Object> records = (List<Object>) recordDetails.get("RECORDS");
		for (Object x : records) {
			Map<String, String> rec = (Map<String, String>) x;
			Map<String, String> arrangedRecord = new LinkedHashMap<String, String>();
			arrangedRecord.put(arrangedHead.get("TRANS_ID"), rec.get("TRANS_ID"));
			arrangedRecord.put(arrangedHead.get("VENUE"), rec.get("VENUE"));
			arrangedRecord.put("CASHIER", rec.get("USERNAME") );
			arrangedRecord.put(arrangedHead.get("DATE"), rec.get("DATE"));
			arrangedRecord.put(arrangedHead.get("USERNAME"), rec.get("USERNAME"));
			arrangedRecord.put(arrangedHead.get("AMOUNT"), rec.get("AMOUNT"));
			arrangedRecord.put(arrangedHead.get("TRANS_CODE"), rec.get("TRANS_CODE"));
			arrangedRecord.put("PAYMENT TYPE", rec.get("CASHIER"));
			arrangedRecords.add(arrangedRecord);
		}
		newRecordDetails.put("RECORDS", arrangedRecords);
		return newRecordDetails;
	}
	
	public void replaceValue(Map<String, Object> recordDetails) {
		List<Object> records = (List<Object>) recordDetails.get("RECORDS");

		for (Object record : records) {
			Map<String, String> currentRecord = (Map<String, String>) record;

			if (currentRecord.get("TRANS_CODE").equalsIgnoreCase("0")) {
				currentRecord.replace("TRANS_CODE", "withdrawal");
			} else if (currentRecord.get("TRANS_CODE").equalsIgnoreCase("1")) {
				currentRecord.replace("TRANS_CODE", "deposit");
			}

			if (currentRecord.get("VENUE").equalsIgnoreCase("IW1PLAY")) {
				currentRecord.replace("VENUE", "IWLU1TER0C");
			}
			
			currentRecord.replace("AMOUNT",  ""+ Math.abs(Double.valueOf(currentRecord.get("AMOUNT")))   );
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
