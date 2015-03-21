
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import communication_package.Client;
import communication_package.Server;
import communication_package.Server.TCPClientListner;

/**
 * 
 * @author houssainy
 *
 */
public class BoardServer {
	// Server parameters
	private static int maxNumberOfAccess;
	private static int sequenceNumber;

	/**
	 * Main function of the server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}
		sequenceNumber = 0;

		int port = Integer.parseInt(args[0].trim());
		maxNumberOfAccess = Integer.parseInt(args[1].trim());

		Server server = new Server();
		server.setListner(clientListner);
		server.start(port);
	}

	//
	private static TCPClientListner clientListner = new TCPClientListner() {

		@Override
		public void onNewClient(Client client) {
			sequenceNumber++;
			try {
				// Assign Id to the new client
				client.setClientId(sequenceNumber);
				// Send id
				client.send(("" + sequenceNumber).getBytes());

				System.out.println("New Client with client Id = "
						+ client.getClientId() + " connected.");

				// Initialize variables.
				int numberOfAccess = 0;

				byte[] data = null;
				String msg = "";

				while (true) {
					data = client.receive();

					// No data received so connection disconnected.
					if (data == null) {
						System.out.println("Client with id "
								+ client.getClientId() + " disconnected.");
						return;
					}

					msg = new String(data, "UTF-8");
					if (msg.equals("bye")) {
						System.out.println("Client with id "
								+ client.getClientId() + " disconnected.");
						return;
					}

					numberOfAccess++;
					if (numberOfAccess > maxNumberOfAccess) {
						System.out.println("Client with id "
								+ client.getClientId()
								+ " reached max number of accesses.");
						client.send("MAX_ACC".getBytes());
						return;
					}

					String[] dataMsg = msg.split(" ");
					if (dataMsg[0].equals("read"))
						readMessage(client);
					else if (dataMsg[0].equals("write"))
						writeMessage(client, dataMsg[1]);

				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private static synchronized void writeMessage(Client client, String value) throws IOException {
		System.out.println("Client " + client.getClientId() + " is Writing "
				+ value + "...");
		// TODO(houssainy) LOG
		PrintWriter writer = new PrintWriter(new File("news.txt"));
		writer.println(value);
		writer.close();
		
		client.send("OK".getBytes());
	}

	private static void readMessage(Client client) throws IOException {
		System.out.println("Client " + client.getClientId() + " is Reading...");
		// TODO(houssainy) LOG
		Scanner in = new Scanner(new File("news.txt"));
		client.send((in.nextInt() +"").getBytes());
		in.close();
	}

}
