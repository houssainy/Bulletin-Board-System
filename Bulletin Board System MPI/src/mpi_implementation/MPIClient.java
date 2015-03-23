package mpi_implementation;

import java.nio.CharBuffer;
import java.util.Scanner;

import ssh.User;
import mpi.MPI;
import mpi.MPIException;

public class MPIClient {
	private static final int READ_FILE = 1;
	private static final int WRITE_FILE = 1;
	private static final int CLOSE = 2;

	private int rank;

	private Log log;

	public MPIClient(int rank, String type) throws MPIException {
		this.rank = rank;

		log = new Log("log" + rank + ".txt");
		log.log("rSeq		sSeq	oVal\n");
		log.log("-----------------------------\n");

		// Send Hello message to open new thread for this client.
		CharBuffer outBuffer = MPI.newCharBuffer(2);
		outBuffer.put(0, Constants.HELLO);
		outBuffer.put(1, (char) rank);

		MPI.COMM_WORLD.send(outBuffer, /* length of message */
				outBuffer.length(), MPI.CHAR, 0/* Destination */, 99);

		if (type.equals(User.CLIENT_READER_TYPE))
			startReader();
		else if (type.equals(User.CLIENT_WRITER_TYPE))
			startWriter();

		outBuffer = MPI.newCharBuffer(1);
		outBuffer.put(0, Constants.BYE);

		MPI.COMM_WORLD.send(outBuffer, /* length of message */
				outBuffer.length(), MPI.CHAR, 0/* Destination */, 99);

		log.close();
		System.out.println("Closed...");
	}

	private void startReader() throws MPIException {
		log = new Log("log" + rank + ".txt");
		log.log("rSeq		sSeq	oVal\n");
		log.log("-----------------------------\n");

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
					CharBuffer inBuffer = MPI.newCharBuffer(3);
					CharBuffer outBuffer = MPI.newCharBuffer(1);

					outBuffer.put(0, Constants.READ);

					/**
					 * Send Read request to the server and receive the requested
					 * value, sSeq and rSer from the server.
					 *
					 * Blocking send and receive, because the client need to
					 * stop receiving or sending any data until he receives its
					 * request back from the server.
					 */
					MPI.COMM_WORLD.sendRecv(outBuffer, /* length of message */
							outBuffer.length(), MPI.CHAR, 0/* Destination */,
							99/* TAG */, inBuffer, inBuffer.length(), MPI.CHAR,
							0, 99);

					char value = inBuffer.get(0);
					char sSeq = inBuffer.get(1);
					char rSeq = inBuffer.get(2);

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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void startWriter() throws MPIException {
		log = new Log("log" + rank + ".txt");
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
					int value = in.nextInt();

					CharBuffer inBuffer = MPI.newCharBuffer(3);
					CharBuffer outBuffer = MPI.newCharBuffer(2);

					outBuffer.put(0, Constants.WRITE);
					outBuffer.put(2, (char) value);

					/**
					 * Send Write request to the server and receive the
					 * requested the OK response, sSeq and wSeq from the server.
					 *
					 * Blocking send and receive, because the client need to
					 * stop receiving or sending any data until he receives its
					 * request back from the server.
					 */
					MPI.COMM_WORLD.sendRecv(outBuffer, /* length of message */
							outBuffer.length(), MPI.CHAR, 0/* Destination */,
							99/* TAG */, inBuffer, inBuffer.length(), MPI.CHAR,
							0, 99);

					char resp = inBuffer.get(0);
					char sSeq = inBuffer.get(1);
					char wSeq = inBuffer.get(2);

					if (resp == Constants.OK) {
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
