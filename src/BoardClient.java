import java.io.IOException;
import java.util.Scanner;

import ssh.User;

import communication_package.Client;

public class BoardClient {
	private static final int READ_FILE = 1;
	private static final int WRITE_FILE = 2;
	private static final int CLOSE = 3;

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}

		String serverIp = args[0];
		int serverPort = Integer.parseInt(args[1].trim());
		String type = args[2].trim();

		// Connect to Server
		Client client = new Client(serverIp, serverPort);

		if (type.equals(User.CLIENT_READER_TYPE))
			startReader(client);
		else if (type.equals(User.CLIENT_WRITER_TYPE))
			startWriter(client);

		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Closed ...");
	}

	private static void startWriter(Client client) {
		int input = 0;
		Scanner in = new Scanner(System.in);

		boolean done = false;
		try {
			do {
				System.out
						.println("Chose one of the following actions:\n\n1: Read File from server.\n2: close.");
				// Read input from user
				input = in.nextInt();

				switch (input) {
				case READ_FILE:
					readValueFromServer(in, client);
					break;
				case CLOSE:
					done = true;
					break;
				default:
					System.out.println("Invalid input!");
				}
				Thread.sleep(1000);
			} while (!done);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

	private static void startReader(Client client) {
		int input = 0;
		Scanner in = new Scanner(System.in);

		boolean done = false;
		try {
			do {
				System.out
						.println("Chose one of the following actions:\n\n1: Post Data to server.\n3: close.");
				// Read input from user
				input = in.nextInt();

				switch (input) {
				case WRITE_FILE:
					writeDataToServer(in, client);
					break;
				case CLOSE:
					done = true;
					break;
				default:
					System.out.println("Invalid input!");
				}
				Thread.sleep(1000);
			} while (!done);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param in
	 * @param client
	 * @return true on success
	 * @throws IOException
	 */
	private static void writeDataToServer(Scanner in, Client client)
			throws IOException {
		// Read file path
		System.out.println("Enter Your values:");

		String s = "write " + in.nextInt();
		client.send(s.getBytes());
		byte[] respData = client.receive();
		if (respData == null) {
			System.err.println("ERROR: Server is down!");
			return;
		}

		String resp = new String(respData, "UTF-8");
		if (resp.equals("OK")) {
			System.out.println("Message Sent Successfully.");
			// TODO(houssainy) LOG
		} else {
			System.out.println(resp);
			// TODO(houssainy) LOG
		}
	}

	/**
	 * 
	 * @param in
	 * @param client
	 * @return true on success
	 * @throws IOException
	 */
	private static void readValueFromServer(Scanner in, Client client)
			throws IOException {
		client.send("read".getBytes());

		// Receive response
		byte[] respData = client.receive();
		if (respData == null) {
			System.err.println("ERROR: Server is down!");
			return;
		}

		String resp = new String(respData);
		if (resp.equals("MAX_ACC")) {
			System.out.println("You reached the maximum number of access!");
			// TODO(houssainy) LOG
		} else {
			System.out.println("Value = " + resp);
			// TODO(houssainy) LOG
		}
	}
}
