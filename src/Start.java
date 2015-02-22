import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import ssh.Jssh;
import ssh.User;

public class Start {
	private static final String SERVER_IP = "RW.server";
	private static final String SERVER_PORT = "RW.server.port";
	private static final String NUMBER_OF_READERS = "RW.numberOfReaders";
	private static final String NUMBER_OF_WRITERS = "RW.numberOfWriters";

	public static void main(String[] args) {
		try {
			Configuration config = readConfigurationFile("system.properties");
			if (config == null) {
				System.err
						.println("ERROR: error in system.properties file format!");
				return;
			}
			startNetwork(config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void startNetwork(Configuration configuration) {
		Jssh ssh = new Jssh();

		ArrayList<User> users = configuration.getUsersList();

		for (int i = 0; i < users.size(); i++) {
			// ssh.startProcess(users.get(i));
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
				for (int i = 0; i < numOfReaders && in.hasNext(); i++)
					readUserData(in, configuration, User.TYPE_READER);

				break;
			case NUMBER_OF_WRITERS:
				int numOfWriters = Integer.parseInt(configData[1].trim());
				for (int i = 0; i < numOfWriters && in.hasNext(); i++)
					readUserData(in, configuration, User.TYPE_WRITER);
				break;
			default:
				break;
			}
		}
		in.close();
		return configuration;
	}

	private static void readUserData(Scanner in, Configuration configuration,
			String type) {
		String line = "";
		String[] configData;

		// ip
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String ip = configData[1].trim();

		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String userName = configData[1].trim();

		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String password = configData[1].trim();

		configuration.addUser(new User(ip, userName, password, type));
	}

}
