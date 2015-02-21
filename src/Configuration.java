import java.util.ArrayList;

public class Configuration {

	private String serverIp;
	private int serverportno;
	private int numberOfReaders;
	private ArrayList<String> readersIp;
	private int numberOfwriter;
	private ArrayList<String> writersIp;
	private int numberOfAccesses;

	protected String getServerIp() {
		return serverIp;
	}

	protected void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	protected int getServerportno() {
		return serverportno;
	}

	protected void setServerportno(int serverportno) {
		this.serverportno = serverportno;
	}

	protected int getNumberOfReaders() {
		return numberOfReaders;
	}

	protected void setNumberOfReaders(int numberOfReaders) {
		this.numberOfReaders = numberOfReaders;
	}

	protected ArrayList<String> getReadersIp() {
		return readersIp;
	}

	protected void setReadersIp(ArrayList<String> readersIp) {
		this.readersIp = readersIp;
	}

	protected int getNumberOfwriter() {
		return numberOfwriter;
	}

	protected void setNumberOfwriter(int numberOfwriter) {
		this.numberOfwriter = numberOfwriter;
	}

	protected ArrayList<String> getWritersIp() {
		return writersIp;
	}

	protected void setWritersIp(ArrayList<String> writersIp) {
		this.writersIp = writersIp;
	}

	protected int getNumberOfAccesses() {
		return numberOfAccesses;
	}

	protected void setNumberOfAccesses(int numberOfAccesses) {
		this.numberOfAccesses = numberOfAccesses;
	}

}
