package mpi_implementation;

import java.nio.IntBuffer;
import java.util.Scanner;

import mpi.MPI;
import mpi.MPIException;

public class Main {
	private static final int HELLO = 1;
	private static final int READ = 2;
	private static final int WRITE = 3;
	private static final int BYE = 4;

	public static void main(String[] args) throws MPIException,
			InterruptedException {
		MPI.Init(args);
		int myrank = MPI.COMM_WORLD.getRank();
		if (myrank == 0)
			handleServer();
		else
			handleClient(myrank);

		MPI.Finalize();
	}

	private static void handleClient(int rank) throws MPIException {
		System.out.println("Start Handling Client.");
		IntBuffer input = MPI.newIntBuffer(2);
		IntBuffer out = MPI.newIntBuffer(2);

		out.put(0, HELLO);
		out.put(1, rank);

		// SEND HELLO
		MPI.COMM_WORLD.sendRecv(out, /* length of message */1, MPI.INT,
				0/* Destination */, 99/* TAG */, input, 1, MPI.INT, 0, 99);

		if (input.get(0) == HELLO) {
			System.out.println("Hello.");
		} else {
			System.out.println("ERROR.");
			return;
		}

		Scanner in = new Scanner(System.in);
		int command;
		while (true) {
			System.out
					.println("Enter your command:\n1. For read.\n2. For write.");
			command = in.nextInt();
			if (command == 1) { // READ
				out = MPI.newIntBuffer(1);
				out.put(0, READ);

				MPI.COMM_WORLD
						.sendRecv(out, /* length of message */1, MPI.INT,
								0/* Destination */, 99/* TAG */, input, 1,
								MPI.INT, 0, 99);
				// TODO
			} else if (command == 2) { // Write
				int value = in.nextInt();
				out = MPI.newIntBuffer(2);
				out.put(0, WRITE);
				out.put(1, value);

				MPI.COMM_WORLD
						.sendRecv(out, /* length of message */1, MPI.INT,
								0/* Destination */, 99/* TAG */, input, 1,
								MPI.INT, 0, 99);
				// TODO
			} else {
				out = MPI.newIntBuffer(1);
				out.put(0, BYE);

				MPI.COMM_WORLD.send(out, /* length of message */1, MPI.INT,
						0/* Destination */, 99);
				System.out.println("Closing ...");
				break;
			}
		}

		// IntBuffer out = MPI.newIntBuffer(1);
		// IntBuffer input = MPI.newIntBuffer(1);
		//
		// Scanner in = new Scanner(System.in);
		// while (true) {
		// System.out.println("Enter your command:");
		// int c = in.nextInt();
		// // System.out.println("cccccccccccc");
		// if (c == 2)
		// break;
		//
		// out.put(0, c);
		// // MPI.COMM_WORLD.send(x, 1, MPI.INT, 1, 99);
		// // MPI.COMM_WORLD.iSend(out, 1, MPI.INT, 1, 99);
		//
		// MPI.COMM_WORLD.sendRecv(out, /* length of message */1, MPI.INT,
		// 0/* Destination */, 99/* TAG */, input, 1, MPI.INT, 0, 99);
		// System.out.println("Received " + input.get(0));
		// }
		// in.close();
	}

	private static void handleServer() throws MPIException,
			InterruptedException {
		System.out.println("Start Handling Server.");
		IntBuffer in = MPI.newIntBuffer(1);

		MPI.COMM_WORLD.recv(in, 1, MPI.INT, /* Source */MPI.ANY_SOURCE, 99);
		if (in.get(0) == HELLO)
			new Thread(new ClientHandler(in.get(1))).start();
	}

	private static class ClientHandler implements Runnable {
		int rank;

		public ClientHandler(int rank) {
			this.rank = rank;
		}

		@Override
		public void run() {
			System.out.println("Client Started on Server");
			try {
				// Send hello to client
				MPI.COMM_WORLD.send(new int[] { HELLO }, /* length of message */
						1, MPI.INT, 1/* Destination */, 99);
			} catch (MPIException e) {
				e.printStackTrace();
			}

			IntBuffer in = MPI.newIntBuffer(1);

			// IntBuffer out = MPI.newIntBuffer(1);
			// out.put(0, 63);
			// while (true) {
			// MPI.COMM_WORLD.recv(in, 1, MPI.INT, /* Source */MPI.ANY_SOURCE,
			// 99);
			// System.out.println("*** received:" + in.get(0) + ":");
			// // MPI.COMM_WORLD.wait();
			// MPI.COMM_WORLD.send(out, /* length of message */1, MPI.INT,
			// 1/* Destination */, 99);
			// }

		}

	}
}
