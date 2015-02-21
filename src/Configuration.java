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

	// Readers list
	private ArrayList<String> readersIps;

	// Writers list
	private ArrayList<String> writersIps;

	private int numberOfAccesses;

	public Configuration() {
		readersIps = new ArrayList<String>();
		writersIps = new ArrayList<String>();
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

	public void addReader(String ip) {
		readersIps.add(ip);
	}

	public ArrayList<String> getReaders() {
		return readersIps;
	}

	public void addWriter(String ip) {
		writersIps.add(ip);
	}

	public ArrayList<String> getWriters() {
		return writersIps;
	}

	public int getNumberOfAccesses() {
		return numberOfAccesses;
	}

	public void setNumberOfAccesses(int numberOfAccesses) {
		this.numberOfAccesses = numberOfAccesses;
	}
}
