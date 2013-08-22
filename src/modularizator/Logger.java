package modularizator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	private String log = "";
	
	public Logger() {
		
	}
	
	public String getLog() {
		return log;
	}
	
	public void addLog(String msg) {
		log += msg + "\n";
		System.out.println(msg);
	}
	
	/**
	 * Saves the log into the output folder
	 */
	public void save(String fileName) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(log);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
