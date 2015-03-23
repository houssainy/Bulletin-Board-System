package rmi_implementation;

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

	private static int id = -1;

	private static Log log;

	public static void main(String args[]) {
		if (args == null || args.length < 4) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}

		String registryServerIp = args[0];
		int registryServerPort = Integer.parseInt(args[1].trim());
		String type = args[2].trim();
		id = Integer.parseInt(args[3].trim());

		System.setProperty("java.rmi.server.hostname", registryServerIp);
		try {
			String name = "Board";
			System.out.println("Locing for Registry ...");
			Registry registry = LocateRegistry.getRegistry(registryServerIp,
					registryServerPort);
			System.out.println("Locing for " + name + " in\n " + registry);
			Board board = (Board) registry.lookup(name);

			System.out.println("Welcome Your id is " + id);

			if (type.equals(User.CLIENT_READER_TYPE))
				startReader(board);
			else if (type.equals(User.CLIENT_WRITER_TYPE))
				startWriter(board);

			log.close();
			System.out.println("Closed ...");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	private static void startReader(Board board) {
		log = new Log("log" + id + ".txt");
		log.log("rSeq		sSeq	oVal\n");
		log.log("-----------------------\n");

		int input = 0;
		Scanner in = new Scanner(System.in);

		boolean done = false;
		try {
			do {
				System.out
						.println("Chose one of the following actions:\n\n1: Read File from server.\n2: close.");
				input = in.nextInt();

				switch (input) {
				case READ_FILE:
					String[] msg = board.readValue(id).split(",");
					String value = msg[0];
					String sSeq = msg[1];
					String rSeq = msg[2];

					log.log("" + rSeq + "		" + sSeq + "		" + value + "\n");
					log.flush();

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
		} catch (InterruptedException | RemoteException e) {
			e.printStackTrace();
		}
	}

	private static void startWriter(Board board) throws RemoteException {
		log = new Log("log" + id + ".txt");
		log.log("wSeq		sSeq\n");
		log.log("---------------\n");

		int input = 0;
		Scanner in = new Scanner(System.in);

		boolean done = false;
		try {
			do {
				System.out
						.println("Chose one of the following actions:\n\n1: Update Bulletin Value.\n2: close.");
				input = in.nextInt();

				switch (input) {
				case WRITE_FILE:
					System.out.println("Enter Your values:");

					String[] msg = board.updateValue(in.nextInt(), id).split(
							",");

					String resp = msg[0];
					String sSeq = msg[1];
					String wSeq = msg[2];

					if (resp.equals("OK")) {
						log.log(wSeq + "		" + sSeq + "\n");
						log.flush();

						System.out.println("Message Sent Successfully.");
					} else {
						System.out.println(resp);
					}
					break;
				case CLOSE:
					done = true;
					break;
				default:
					System.out.println("Invalid input!");
				}
				System.out.println("Loading...");
				Thread.sleep(1000);
			} while (!done);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
