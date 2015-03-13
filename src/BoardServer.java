
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import messages_package.MessageOperator;
import messages_package.MessageOperator.Message;
import communication_package.Client;
import communication_package.Server;
import communication_package.Server.TCPClientListner;

/**
 * 
 * @author houssainy
 *
 */
public class BoardServer {
	// Constants
	private static final int DONE = 0;
	private static final int DATA_CORRUPTED = -1;
	private static final int CONTINUE = 1;

	// Server parameters
	private static int maxNumberOfAccess;
	private static int sequenceNumber;

	private static MessageOperator msgOperator;
	
	private Log log;
	/**
	 * Main function of the server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		if (args == null || args.length < 2) {
//			System.err.println("ERROR: Missing Arguments!");
//			return;
//		}
		sequenceNumber = 0;

		msgOperator = new MessageOperator();

		//int port = 8007;//
		int port = 7003;//Integer.parseInt(args[0].trim());
		//maxNumberOfAccess = 20;//
		maxNumberOfAccess = 20;//Integer.parseInt(args[1].trim());
		Server server = new Server();
		server.setListner(clientListner);
		server.start(port);
	}

	//
	private static TCPClientListner clientListner = new TCPClientListner() {

		@Override
		public void onNewClient(Client client) {
			sequenceNumber++;

			// Assign Id to the new client
			client.setClientId(sequenceNumber);
			System.out.println("New Client with client Id = "
					+ client.getClientId() + " connected.");

			// Initialize variables.
			int numberOfAccess = 0;
			int state = Integer.MIN_VALUE;

			byte[] data = null;
			String msg = "";

			try {
				do {
					data = client.receive();

					// No data received so connection disconnected.
					if (data == null) {
						System.out.println("Client with id "
								+ client.getClientId() + " disconnected.");
						return;
					}

					msg = new String(data, "UTF-8");

					state = handleMessage(msg, client);
					if (state == DONE) {
						System.out.println("Client with id "
								+ client.getClientId() + " disconnected.");
						return;
					} else if (state == DATA_CORRUPTED)
						continue;

					numberOfAccess++;
				} while (numberOfAccess < maxNumberOfAccess);

				System.out.println("Client with id " + client.getClientId()
						+ " reached max number of accesses.");
				client.send(msgOperator.generateResponse(
						MessageOperator.MAX_ACCESS_RESPONSE).getBytes());
				System.out.println("Client with id " + client.getClientId()
						+ " disconnected.");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Method to handle the request from client
		private int handleMessage(String data, Client client)
				throws IOException {
			Message msg = msgOperator.parseMessage(data);
			if (msg == null) {
				System.err.println("ERROR: Received Message is corrupted.");
				return DATA_CORRUPTED;
			}

			switch (msg.getType()) {
			case MessageOperator.READ_MSG:
				System.out.println("Client "+ client.getClientId() + " is Reading...");
				String file = getFileData(msg.getFilePath());
				String dataResponse = msgOperator.generateResponse(
						MessageOperator.READ_RESPONSE, file);
				client.send(dataResponse.getBytes());
				break;
			case MessageOperator.WRITE_MSG:
				System.out.println("Client "+ client.getClientId() + " is Writing...");
				writeDataToFile(msg.getFilePath(), msg.getMsg());
				break;
			}
			return CONTINUE;
		}

		// Write msg data to the given file path
		private void writeDataToFile(String filePath, String msg) {
			// TODO(houssainy) use BufferedWriter instead of PrintWriter
			PrintWriter writer;
			try {
				writer = new PrintWriter(new File(filePath));
				writer.println(msg);
				writer.close();
			} catch (FileNotFoundException e) {
				System.err.println("ERROR: File <" + filePath + "> not found!");
			}
		}

		// Read file all the data in the given file path and return it.
		private String getFileData(String filePath) {
			StringBuilder data = new StringBuilder("");

			Scanner in;
			try {
				in = new Scanner(new File(filePath));
				while (in.hasNext())
					data.append(in.nextLine() + "\n");

				in.close();
			} catch (FileNotFoundException e) {
				System.err.println("ERROR: File <" + filePath + "> not found!");
				data.append("ERROR: File <" + filePath + "> not found!");
			}
			return data.toString();
		}
	};
}
