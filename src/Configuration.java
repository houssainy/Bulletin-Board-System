import java.util.ArrayList;

/**
 * 
 * @author houssainy
 *
 *         Object to hold the system configuration.
 */
public class Configuration {

	// Server information
	private String serverIp;
	private int serverPort;

	private ArrayList<User> users;

	private int numberOfAccesses;

	public Configuration() {
		users = new ArrayList<User>();
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
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
}
