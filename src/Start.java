import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Start {

	public Configuration readConfigurationFile(String filePath) throws FileNotFoundException {
		Configuration configuration = new Configuration();
		Scanner input = new Scanner(new File(filePath));
		
		int i = 0;
		ArrayList<String> Reader = new ArrayList<String>();
		ArrayList<String> Writer = new ArrayList<String>();
		while (input.hasNext()) {
			String nextLine = input.nextLine();
			String[] parts = nextLine.split("=");
			if (i == 0)
				configuration.setServerIp(parts[1]);
			else if (i == 0)
				configuration.setServerIp(parts[1]);
			else if (i == 1)
				configuration.setServerportno(Integer.parseInt(parts[1].trim()));
			else if (i == 2)
				configuration.setNumberOfReaders(Integer.parseInt(parts[1].trim()));
			else if (i == 3)
				Reader.add(parts[1]);
			else if (i == 4)
				Reader.add(parts[1]);
			else if (i == 5)
				Reader.add(parts[1]);
			else if (i == 6) {
				Reader.add(parts[1]);
				configuration.setReadersIp(Reader);
			} else if (i == 7)
				configuration.setNumberOfwriter(Integer.parseInt(parts[1].trim()));
			else if (i == 8)
				Writer.add(parts[1]);
			else if (i == 9)
				Writer.add(parts[1]);
			else if (i == 10)
				Writer.add(parts[1]);
			else if (i == 11) {
				Writer.add(parts[1]);
				configuration.setWritersIp(Writer);
			} else if (i == 12) {
				configuration.setNumberOfAccesses(Integer.parseInt(parts[1].trim()));
			}

			i++;
		}

		input.close();
		return configuration;
	}

	public static void main(String[] args) {
		Start f = new Start();
		try {
			f.readConfigurationFile("system.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
