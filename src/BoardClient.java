
import java.io.IOException;
import java.util.Scanner;

import messages_package.MessageOperator;
import messages_package.MessageOperator.Response;
import communication_package.Client;

public class BoardClient {

	private static final int READ_FILE = 1;
	private static final int WRITE_FILE = 2;
	private static final int CLOSE = 3;

	private static MessageOperator msgOperator;

	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}

		//String serverIp = "127.0.0.1";//args[0];
		String serverIp = args[0];
//		int serverPort = 1992;//Integer.parseInt(args[1].trim());
		int serverPort = Integer.parseInt(args[1].trim());

		// Connect to Server
		Client client = new Client(serverIp, serverPort);
		System.out.println("Connected to Server " + serverIp + " on port "
				+ serverPort);

		int input = 0;
		msgOperator = new MessageOperator();
		Scanner in = new Scanner(System.in);

		boolean done = false;
		try {
			do {
				System.out
						.println("Chose one of the following actions:\n\n1: Read File from server.\n2: Post Data to server.\n3: close.");
				// Read user input
				input = in.nextInt();

				switch (input) {
				case READ_FILE:
					done = !readFileFromServer(in, client);
					break;
				case WRITE_FILE:
					done = !writeDataToServer(in, client);
					break;
				case CLOSE:
					done = true;
					break;
				}
			} while (!done);

			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Closed ...");
	}

	/**
	 * 
	 * @param in
	 * @param client
	 * @return true on success
	 */
	private static boolean writeDataToServer(Scanner in, Client client) {
		// Read file path
		System.out.println("Enter file path in server's device:");
		String filePath = in.nextLine();

		System.out.println("Write your data and end it by writing __done:");
		StringBuilder builder = new StringBuilder();
		String temp = "";
		while ((temp = in.nextLine()) != null && !temp.equals("__done")) {
			builder.append(temp + "\n");
		}

		String data = builder.toString();

		// Generate Write Message
		String msg = msgOperator.generateWriteMessage(data, filePath);
		try {
			client.send(msg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 
	 * @param in
	 * @param client
	 * @return true on success
	 * @throws IOException
	 */
	private static boolean readFileFromServer(Scanner in, Client client)
			throws IOException {
		// Read file path
		System.out.println("Enter file path in server's device:");
		String filePath = in.next();

		// Generate Read Message
		String msg = msgOperator.generateReadMessage(filePath);
		client.send(msg.getBytes());

		// Receive response
		byte[] responseData = client.receive();
		if (responseData == null) {
			System.err.println("ERROR: Server is down!");
			return true;
		}

		Response response = msgOperator.parseResponse(new String(responseData,
				"UTF-8"));
		if (response.getType().equals(MessageOperator.READ_RESPONSE)) {
			System.out.println("Server >\n" + response.getData());
			return true;
		} else if (response.getType().equals(
				MessageOperator.MAX_ACCESS_RESPONSE)) {
			System.out
					.println("Reached the maximum number of accesses on server.");
		}
		return false;
	}
}
