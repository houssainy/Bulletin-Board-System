import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

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
	private static final String REGISTRY_IP = "RW.registry";
	private static final String SERVER_PORT = "RW.server.port";
	private static final String NUMBER_OF_READERS = "RW.numberOfReaders";
	private static final String NUMBER_OF_WRITERS = "RW.numberOfWriters";
	private static final String NUMBER_OF_ACCESS = "RW.numberOfAccesses";

	private static int id = 1;

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

	/**
	 * Start the remote server and remote client
	 * 
	 * @param configuration
	 */
	private static void startNetwork(Configuration configuration) {
		Jssh ssh = new Jssh();
		ArrayList<User> users = configuration.getUsersList();

		startUser(ssh, configuration, configuration.getServer());

		try {
			// Sleep period to insure that the server is running before the
			// clients
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();)
			startUser(ssh, configuration, (User) iterator.next());
	}

	private static void startUser(Jssh ssh, Configuration configuration,
			User user) {
		String command = "xterm -hold -e \"cd " + user.getFilePath()
				+ " && javac rmi_implementation/" + user.getFileName() + ".java"
				+ " && java rmi_implementation." + user.getFileName() + " ";
		switch (user.getType()) {
		case User.SERVER_TYPE:
			command = "export DISPLAY=:0.0 && " + command;
			command += configuration.getRegistryIp() + " "
					+ configuration.getPort();
			break;
		case User.CLIENT_WRITER_TYPE:
			command = "export DISPLAY=:0.0 && " + command;
			command += configuration.getRegistryIp() + " "
					+ configuration.getPort() + " " + User.CLIENT_WRITER_TYPE
					+ " " + id++;
			break;
		case User.CLIENT_READER_TYPE:
			command = "export DISPLAY=:0.0 && " + command;
			command += configuration.getRegistryIp() + " "
					+ configuration.getPort() + " " + User.CLIENT_READER_TYPE
					+ " " + id++;
			break;
		}
		command += "\"";
		System.out.println(command);
		ssh.doCommand(user.getUserName(), user.getIp(), user.getSshPort(),
				user.getPassword(), command);
	}

	// Read system properties file, parse it and return the data encapsulated in
	// Configuration object
	private static Configuration readConfigurationFile(String filePath)
			throws FileNotFoundException {
		Configuration configuration = new Configuration();
		Scanner in = new Scanner(new File(filePath));

		String line = "";
		User server = new User(User.SERVER_TYPE);
		while (in.hasNext()) {
			line = in.nextLine();

			String[] configData = line.split("=");

			switch (configData[0]) {
			case SERVER_IP:
				server.setIp(configData[1].trim());
				break;
			case REGISTRY_IP:
				configuration.setRegistryIp(configData[1].trim());
				break;
			case SERVER_PORT:
				configuration.setPort(Integer.parseInt(configData[1].trim()));
				readServerData(in, server);
				configuration.setServer(server);
				break;
			case NUMBER_OF_READERS:
				int numOfReaders = Integer.parseInt(configData[1].trim());
				for (int i = 0; i < numOfReaders && in.hasNext(); i++)
					readUserData(in, configuration, User.CLIENT_READER_TYPE);

				break;
			case NUMBER_OF_WRITERS:
				int numOfWriters = Integer.parseInt(configData[1].trim());
				for (int i = 0; i < numOfWriters && in.hasNext(); i++)
					readUserData(in, configuration, User.CLIENT_WRITER_TYPE);
				break;
			case NUMBER_OF_ACCESS:
				configuration.setNumberOfAccesses(Integer
						.parseInt(configData[1].trim()));
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

		// SSH port
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String sshPort = configData[1].trim();
		server.setSshPort(Integer.parseInt(sshPort));

		// File Path
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String filePath = configData[1].trim();
		server.setFilePath(filePath);
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

		// SSH port
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		int sshPort = Integer.parseInt(configData[1].trim());

		// File Path
		if (!in.hasNext())
			return;
		line = in.nextLine();
		configData = line.split("=");
		String filePath = configData[1].trim();

		configuration.addUser(new User(ip, userName, password, sshPort,
				filePath, type));
	}

}
