import java.util.ArrayList;

import ssh.User;

/**
 * 
 * @author houssainy
 *
 *         Object to hold the system configuration.
 */
public class Configuration {

	// Server information
	private User server;
	private int port;
	private ArrayList<User> users;

	private int numberOfAccesses;

	public Configuration() {
		users = new ArrayList<User>();
	}

	public void setServer(User server) {
		this.server = server;
	}

	public User getServer() {
		return server;
	}

	public void addUser(User user) {
		users.add(user);
	}

	public ArrayList<User> getUsersList() {
		return users;
	}

	public int getNumberOfAccesses() {
		return numberOfAccesses;
	}

	public void setNumberOfAccesses(int numberOfAccesses) {
		this.numberOfAccesses = numberOfAccesses;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
