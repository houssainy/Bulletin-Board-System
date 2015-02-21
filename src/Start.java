import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Start {
	private static final String SERVER_IP = "RW.server";
	private static final String SERVER_PORT = "RW.server.port";
	private static final String NUMBER_OF_READERS = "RW.numberOfReaders";
	private static final String NUMBER_OF_WRITERS = "RW.numberOfWriters";

	public static void main(String[] args) {
		try {
			// TODO(houssainy)
			Configuration config = readConfigurationFile("system.properties");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Configuration readConfigurationFile(String filePath)
			throws FileNotFoundException {
		Configuration configuration = new Configuration();
		Scanner in = new Scanner(new File(filePath));

		String line = "";
		while (in.hasNext()) {
			line = in.nextLine();

			String[] configData = line.split("=");
			switch (configData[0]) {
			case SERVER_IP:
				configuration.setServerIp(configData[1].trim());
				break;
			case SERVER_PORT:
				configuration.setServerPort(Integer.parseInt(configData[1]
						.trim()));
				break;
			case NUMBER_OF_READERS:
				int numOfReaders = Integer.parseInt(configData[1].trim());
				for (int i = 0; i < numOfReaders && in.hasNext(); i++) {
					line = in.nextLine();
					configData = line.split("=");

					configuration.addReader(configData[1].trim());
				}
				break;
			case NUMBER_OF_WRITERS:
				int numOfWriters = Integer.parseInt(configData[1].trim());
				for (int i = 0; i < numOfWriters && in.hasNext(); i++) {
					line = in.nextLine();
					configData = line.split("=");

					configuration.addWriter(configData[1].trim());
				}
				break;
			default:
				break;
			}
		}
		in.close();
		return configuration;
	}

}
