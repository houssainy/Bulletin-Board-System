package rmi_implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Scanner;

public class ReadTask implements Task<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() {
		Scanner in;
		String value = "";
		try {
			in = new Scanner(new File("news.txt"));
			value = in.nextInt() + "";
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return value;
	}

}
