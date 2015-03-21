package rmi_implementation;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import ssh.User;

public class Client {
	private static final int READ_FILE = 1;
	private static final int WRITE_FILE = 1;
	private static final int CLOSE = 2;

	public static void main(String args[]) {
		if (args == null || args.length < 3) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}
		
		String serverIp = args[0];
		int serverPort = Integer.parseInt(args[1].trim());
		String type = args[2].trim();

		try {
			String name = "Board";
			System.out.println(serverIp);
			Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
			Board board = (Board) registry.lookup(name);

			System.out.println("Starting Client...");

			if (type.equals(User.CLIENT_READER_TYPE))
				startReader(board);
			else if (type.equals(User.CLIENT_WRITER_TYPE))
				startWriter(board);

			System.out.println("Closed ...");
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void startReader(Board board) {
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
					String value = board.executeTask(new ReadTask());
					System.out.println("Value = " + value);
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

	private static void startWriter(Board board) throws RemoteException {
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
					System.out.println("Enter Your values:");

					String resp = board.executeTask(new WriteTask(in.nextInt()
							+ ""));

					if (resp.equals("OK")) {
						System.out.println("Message Sent Successfully.");
						// TODO(houssainy) LOG
					} else {
						System.out.println(resp);
						// TODO(houssainy) LOG
					}
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
