package com.dfnn;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dfnn.util.ParseUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class CashManagementApplication {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CashManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CashManagementApplication.class, args);
		
		String pathSource = args[0];
		String pathDestination = args[1];
		String pathArchive = "archived";

		try {
			File directory = new File(pathSource);

			File[] files = null;
			if (directory.isDirectory()) {
				files = directory.listFiles();
			} else {
				log.info("Not a directory.");
			}

			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {

						if (FilenameUtils.getExtension(file.getAbsoluteFile().toString()).equalsIgnoreCase("CSV")) {
							ParseUtil parser = new ParseUtil();
							parser.parseCSV(file, pathDestination);
						}
					}
					
				}
			} else {
				log.info("Directory is empty.");
			}
			log.info("*** Finished  ***!");
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
		}
	}

}
