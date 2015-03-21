package rmi_implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

public class WriteTask implements Task<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String value;

	public WriteTask(String value) {
		this.value = value;
	}

	@Override
	public synchronized String execute() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File("news.txt"));
			writer.println(value);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "OK";
	}

}
