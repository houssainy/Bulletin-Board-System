package mpi_implementation;

import java.nio.IntBuffer;
import java.util.Scanner;

import mpi.MPI;
import mpi.MPIException;

public class Main {
	private static final int READ = 1;
	private static final int WRITE = 2;
	private static final int BYE = 3;

	public static void main(String[] args) throws MPIException,
			InterruptedException {
		MPI.Init(args);
		int myrank = MPI.COMM_WORLD.getRank();
		if (myrank == 0)
			handleServer();
		else
			handleClient(5);

		MPI.Finalize();
	}

	private static void handleClient(int i) throws MPIException {
		IntBuffer out = MPI.newIntBuffer(1);
		IntBuffer input = MPI.newIntBuffer(1);

		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Enter your command:");
			int c = in.nextInt();

			if (c == 2)
				break;

			out.put(0, c);
			// MPI.COMM_WORLD.send(x, 1, MPI.INT, 1, 99);
			// MPI.COMM_WORLD.iSend(out, 1, MPI.INT, 1, 99);

			MPI.COMM_WORLD.sendRecv(out, /* length of message */1, MPI.INT,
					1/* Destination */, 99/* TAG */, input, 1, MPI.INT,
					MPI.ANY_SOURCE, 99);
			System.out.println("Received " + input.get(0));
		}
		in.close();
	}

	private static void handleServer() throws MPIException,
			InterruptedException {
		IntBuffer in = MPI.newIntBuffer(1);
		IntBuffer out = MPI.newIntBuffer(1);
		out.put(0, 63);
		while (true) {
			MPI.COMM_WORLD.iRecv(in, 1, MPI.INT, /* Source */MPI.ANY_SOURCE, 99);
			System.out.println("received:" + in.get(0) + ":");
			MPI.COMM_WORLD.wait();
			MPI.COMM_WORLD.send(out, /* length of message */1, MPI.INT,
					1/* Destination */, 99);
		}
	}
}
