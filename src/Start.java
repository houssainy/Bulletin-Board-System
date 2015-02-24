import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.jws.Oneway;

import ssh.Jssh;
import ssh.User;

/**
 * 
 * @author houssainy
 *
 *         Responsible for starting the server and the clients. First it create
 *         a thread, which will run the server code. Then, it start clients.
 *         This program will read a configuration file and start up the system
 *         accordingly.
 */
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

		// Start Server
		ssh.startProcess(configuration.getServer(), configuration.getServer().getIp(),
				configuration.getPort());
		
		ArrayList<User> users = configuration.getUsersList();

		for (int i = 0; i < users.size(); i++)
			ssh.startProcess(users.get(i), configuration.getServer().getIp(),
					configuration.getPort());
	}

	// Read system properties file, parse it and return the data encapsulated in
	// Configuration object
	private static Configuration readConfigurationFile(String filePath)
			throws FileNotFoundException {
		Configuration configuration = new Configuration();
		Scanner in = new Scanner(new File(filePath));

		String line = "";
		User server = new User();
		while (in.hasNext()) {
			line = in.nextLine();

			String[] configData = line.split("=");

			switch (configData[0]) {
			case SERVER_IP:
				server.setIp(configData[1].trim());
				break;
			case SERVER_PORT:
				configuration.setPort(Integer.parseInt(configData[1].trim()));
				readServerData(in, server);
				configuration.setServer(server);
				break;
			case NUMBER_OF_READERS:
				int numOfReaders = Integer.parseInt(configData[1].trim());
				for (int i = 0; i < numOfReaders && in.hasNext(); i++)
					readUserData(in, configuration);

				break;
			case NUMBER_OF_WRITERS:
				int numOfWriters = Integer.parseInt(configData[1].trim());
				for (int i = 0; i < numOfWriters && in.hasNext(); i++)
					readUserData(in, configuration);
				break;
			default:
				break;
			}
		}
		in.close();
		return configuration;
	}

	private static void readServerData(Scanner in, User server) {
		String line = "";
		String[] configData;

		// User name
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String userName = configData[1].trim();
		server.setUserName(userName);

		// Password
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String password = configData[1].trim();
		server.setPassword(password);

		// File Path
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String filePath = configData[1].trim();
		server.setFilePath(filePath);
	}

	private static void readUserData(Scanner in, Configuration configuration) {
		String line = "";
		String[] configData;

		// ip
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String ip = configData[1].trim();

		// User name
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String userName = configData[1].trim();

		// Password
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String password = configData[1].trim();

		// File Path
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String filePath = configData[1].trim();

		configuration.addUser(new User(ip, userName, password, filePath));
	}

}
