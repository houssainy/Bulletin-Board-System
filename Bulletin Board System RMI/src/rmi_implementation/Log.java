package rmi_implementation;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Log {
	private BufferedWriter br;

	public Log(String filePath) {
		try {
			br = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(filePath))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void log(String logMsg) {
		try {
			br.write(logMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void flush(){
		try {
			br.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
