package com.dfnn.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import de.siegmar.fastcsv.writer.CsvWriter;
import jdk.javadoc.doclet.Reporter;

public class ParseUtil {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParseUtil.class);
	private static final FilterMapperUtil MAPPER_UTIL = new FilterMapperUtil();

	public void parseCSV(File source, String destination) {
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("VENUE", "IW1PLAY");
		filters.put("VENUE_NAME", "IW1PLAY");
//		filters.put("TRANS_CODE", "1");

		String reportType = "";

		if (source.getName().toUpperCase().contains("CASH_MANAGEMENT")) {
			reportType = "CASH_MANAGEMENT";
		} else if (source.getName().toUpperCase().contains("CUSTOMER_ACTIVITY")) {
			reportType = "CUSTOMER_ACTIVITY";
		} else if (source.getName().toUpperCase().contains("PROGRESSIVE_JACKPOT")) {
			reportType = "PROGRESSIVE_JACKPOT";
		}

		try {
			List<Map<String, String>> records = this.filterRecords(source, filters);

			this.addField(records, reportType);
			this.replaceValue(records, reportType);
			records = this.arrangeFields(records, reportType);
			this.replaceValue(records, reportType);

			for (Map<String, String> record : records) {
				log.info(record.toString());
			}
			
			String sourceFileName = source.getName();
			if (reportType.equalsIgnoreCase("CASH_MANAGEMENT")) {
				sourceFileName = source.getName();
				sourceFileName = sourceFileName.replace(reportType.toLowerCase() , reportType.toLowerCase() + "_main");
			}
			CsvWriter csvWrite = CsvWriter.builder().build(Paths.get( destination + "/" + sourceFileName));
			List<String> headersToWrite = new ArrayList<String>();

			int count = 0;

			for (Object record : records) {
				List<String> recordsToWrite = new ArrayList<>();
				LinkedHashMap<String, String> currentRecord = (LinkedHashMap<String, String>) record;
				System.out.println(currentRecord);
				for (var entry : currentRecord.entrySet()) {
					if (count <= 0) {
						headersToWrite.add(entry.getKey());
					}
					recordsToWrite.add(currentRecord.get(entry.getKey()));
				}

				if (count <= 0) {
					csvWrite.writeRecord(headersToWrite);
				}

				csvWrite.writeRecord(recordsToWrite);
				count++;

			}

			csvWrite.flush();
			csvWrite.close();

			log.info("TOTAL RECORDS : {}", records.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<Map<String, String>> arrangeFields(List<Map<String, String>> records, String reportType) {
		Map<String, String> arrangedHeader = null;
		List<Map<String, String>> newRecords = new ArrayList<>();
		if (reportType.equalsIgnoreCase("PROGRESSIVE_JACKPOT")) {
			arrangedHeader = MAPPER_UTIL.getJackpotHeaderReplacement();
		} else if (reportType.equalsIgnoreCase("CASH_MANAGEMENT")) {
			arrangedHeader = MAPPER_UTIL.getCashManHeaderReplacement();
		} else if (reportType.equalsIgnoreCase("CUSTOMER_ACTIVITY")) {
			arrangedHeader = MAPPER_UTIL.getCustActivityHeaderReplacement();
		}

		for (Map<String, String> record : records) {
			Map<String, String> arrangedRecord = new LinkedHashMap<String, String>();
			for (var entry : arrangedHeader.entrySet()) {
				arrangedRecord.put(arrangedHeader.get(entry.getKey()), record.get(entry.getKey()));
			}
			newRecords.add(arrangedRecord);
		}
		return newRecords;
	}

	private void addField(List<Map<String, String>> records, String reportType) {

		for (Object record : records) {
			Map<String, String> currentRecord = (Map<String, String>) record;
			String code = currentRecord.get("GAME_ID");
			if (code != null) {
				currentRecord.put("GAME_PROVIDER",
						MAPPER_UTIL.getValueReplacement().get(code.substring(0, code.indexOf("-"))));
			}

			if (reportType.equalsIgnoreCase("CASH_MANAGEMENT")) {
				currentRecord.put("PAYMENT_TYPE", currentRecord.get("CASHIER"));
			}

			if (reportType.equalsIgnoreCase("PROGRESSIVE_JACKPOT")) {
				currentRecord.put("JACKPOT_BET", "" + (

				Double.valueOf(currentRecord.get("JACKPOT_CONTRIBUTION_MINI"))
						+ Double.valueOf(currentRecord.get("JACKPOT_CONTRIBUTION_MINOR"))
						+ Double.valueOf(currentRecord.get("JACKPOT_CONTRIBUTION_MAJOR"))
						+ Double.valueOf(currentRecord.get("JACKPOT_CONTRIBUTION_GRAND"))
						+ Double.valueOf(currentRecord.get("JACKPOT_CONTRIBUTION_SUPER_GRAND")))

				);
			}

		}
	}

	private void replaceValue(List<Map<String, String>> records, String reportType) {
		for (Object record : records) {
			Map<String, String> currentRecord = (Map<String, String>) record;

			if (reportType.equalsIgnoreCase("CASH_MANAGEMENT")) {
				currentRecord.replace("TRANS_CODE",
						MAPPER_UTIL.getValueReplacement().get(currentRecord.get("TRANS_CODE")));
				currentRecord.replace("AMOUNT", "" + Math.abs(Double.valueOf(currentRecord.get("AMOUNT"))));
				currentRecord.replace("CASHIER", currentRecord.get("TERMINAL"));

				if (currentRecord.get("VENUE") != null && currentRecord.get("VENUE").equalsIgnoreCase("IW1PLAY")) {
					currentRecord.replace("VENUE", "IWLU1TER0C");
				}

			} else if (reportType.equalsIgnoreCase("CUSTOMER_ACTIVITY")) {
				if (currentRecord.get("VENUE_NAME") != null
						&& currentRecord.get("VENUE_NAME").equalsIgnoreCase("IW1PLAY")) {
					currentRecord.replace("VENUE_NAME", "IWLU1TER0C");
				}

			} else if (reportType.equalsIgnoreCase("PROGRESSIVE_JACKPOT")) {
				if (currentRecord.get("VENUE") != null && currentRecord.get("VENUE").equalsIgnoreCase("IW1PLAY")) {
					currentRecord.replace("VENUE", "IWLU1TER0C");
				}
			}
		}
	}

	public List<Map<String, String>> filterRecords(File source, Map<String, String> filters) throws IOException {
		List<Map<String, String>> records = new ArrayList<>();
		CsvReader<NamedCsvRecord> csvNamedReader = CsvReader.builder()
				.ofNamedCsvRecord(Paths.get(source.getAbsolutePath()));
		for (NamedCsvRecord rec : csvNamedReader) {
			Map<String, String> record = rec.getFieldsAsMap();
			boolean isIncluded = false;
			for (var entry : filters.entrySet()) {
				Set<String> recordKeys = record.keySet();
				if (recordKeys.contains(entry.getKey())) {

					if (record.get(entry.getKey()).equalsIgnoreCase(filters.get(entry.getKey()))) {
						if (!isIncluded) {
							isIncluded = true;
						}
					}
				}

			}

			if (isIncluded) {
				records.add(record);
			}
		}
		csvNamedReader.close();
		return records;
	}

}
