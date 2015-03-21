
import java.io.IOException;
import java.util.Scanner;

import communication_package.Client;

import ssh.User;

public class BoardClient {
	private static final int READ_FILE = 1;
	private static final int WRITE_FILE = 1;
	private static final int CLOSE = 2;

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}

		String serverIp = args[0];
		int serverPort = Integer.parseInt(args[1].trim());
		String type = args[2].trim();
		try {
			System.out.println("Starting Client...");
			// Connect to Server
			Client client = new Client(serverIp, serverPort);
			System.out.println("Connected!");
			byte[] idData = client.receive();
			client.setClientId(Integer.parseInt(new String(idData, "UTF-8")));

			System.out.println("Welcome client " + client.getClientId());
			if (type.equals(User.CLIENT_READER_TYPE))
				startReader(client);
			else if (type.equals(User.CLIENT_WRITER_TYPE))
				startWriter(client);

			client.send("bye".getBytes());
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Closed ...");
	}

	private static void startReader(Client client) {
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
					done = !readValueFromServer(client);
					break;
				case CLOSE:
					done = true;
					break;
				default:
					System.out.println("Invalid input!");
				}
				System.out.println("... ... ...");
				Thread.sleep(1000);
			} while (!done);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

	private static void startWriter(Client client) {
		int input = 0;
		Scanner in = new Scanner(System.in);

		boolean done = false;
		try {
			do {
				System.out
						.println("Chose one of the following actions:\n\n1: Update Bulletin Value.\n2: close.");
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
				System.out.println("... ... ...");
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
	private static boolean readValueFromServer(Client client)
			throws IOException {
		client.send("read".getBytes());

		// Receive response
		byte[] respData = client.receive();
		if (respData == null) {
			System.err.println("ERROR: Server is down!");
			return false;
		}

		String resp = new String(respData, "UTF-8");
		if (resp.equals("MAX_ACC")) {
			System.out.println("You reached the maximum number of access!");
			return false;
			// TODO(houssainy) LOG
		} else {
			System.out.println("Value = " + resp);
			// TODO(houssainy) LOG
		}
		return true;
	}
}
