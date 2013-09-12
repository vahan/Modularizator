package modularizator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Used to make logs
 * @author vahan
 *
 */
public class Logger {
	/**
	 * The log message
	 */
	private String log = "";
	
	/**
	 * Default constructor
	 */
	public Logger() {
		
	}
	
	public String getLog() {
		return log;
	}
	
	/**
	 * Adds the message to the log in a new line.
	 * Also prints it out the default system output
	 * @param msg	the log message to be added
	 */
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
