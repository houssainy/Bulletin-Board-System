import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import mpi.MPI;
import mpi.MPIException;

public class MPIClient {
	private static final int READ_FILE = 1;
	private static final int WRITE_FILE = 1;
	private static final int CLOSE = 2;

	private int rank;

	private Log log;

	public MPIClient(int rank, String type) throws MPIException,
			InterruptedException, NumberFormatException, IOException {
		System.out.println("I am Client " + rank);
		this.rank = rank;

		log = new Log("log" + rank + ".txt");
		log.log("rSeq		sSeq	oVal\n");
		log.log("-----------------------------\n");

		// Send Hello message to open new thread for this client.
		CharBuffer outBuffer = MPI.newCharBuffer(2);
		outBuffer.put(0, Constants.HELLO);
		outBuffer.put(1, (char) rank);

		System.out.println("Sending Hello to server with my rank.");
		MPI.COMM_WORLD.iSend(outBuffer, /* length of message */
				2, MPI.CHAR, 0/* Destination */, 50);
		System.out.println("Hello sent to server.");

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
		MPI.Finalize();
	}

	private void startReader() throws MPIException, NumberFormatException,
			IOException {
		log = new Log("log" + rank + ".txt");
		log.log("rSeq		sSeq	oVal\n");
		log.log("-----------------------------\n");
		log.flush();

		int input = 0;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File("reader-commands.txt"))));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		boolean done = false;

		while (!done) {
			input = Integer.parseInt(br.readLine());
			System.out.println("//////////////////////////// " + input);
			switch (input) {
			case READ_FILE:
				System.out.println("Reading ...");
				IntBuffer inBuffer = MPI.newIntBuffer(3);
				CharBuffer outBuffer = MPI.newCharBuffer(1);

				outBuffer.put(0, Constants.READ);

				/**
				 * Send Read request to the server and receive the requested
				 * value, sSeq and rSer from the server.
				 *
				 * Blocking send and receive, because the client need to stop
				 * receiving or sending any data until he receives its request
				 * back from the server.
				 */
				MPI.COMM_WORLD.sendRecv(outBuffer, /* length of message */
						outBuffer.length(), MPI.CHAR, 0/* Destination */,
						99/* TAG */, inBuffer, 3, MPI.INT, 0, 99);
				System.out.println("Data Read.");
				int value = inBuffer.get(0);
				int sSeq = inBuffer.get(1);
				int rSeq = inBuffer.get(2);

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

		}

	}

	private void startWriter() throws MPIException, NumberFormatException,
			IOException {
		log = new Log("log" + rank + ".txt");
		log.log("wSeq		sSeq\n");
		log.log("---------------\n");
		log.flush();

		int input = 0;

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File("writer-commands.txt"))));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		boolean done = false;

		while (!done) {
			input = Integer.parseInt(br.readLine());
			System.out.println("//////////////////////////// " + input);
			switch (input) {
			case WRITE_FILE:
				int value = Integer.parseInt(br.readLine());

				System.out.println("Writing " + value);

				IntBuffer inBuffer = MPI.newIntBuffer(3);
				CharBuffer outBuffer = MPI.newCharBuffer(2);

				outBuffer.put(0, Constants.WRITE);
				outBuffer.put(1, (char) value);

				/**
				 * Send Write request to the server and receive the requested
				 * the OK response, sSeq and wSeq from the server.
				 *
				 * Blocking send and receive, because the client need to stop
				 * receiving or sending any data until he receives its request
				 * back from the server.
				 */
				MPI.COMM_WORLD.sendRecv(outBuffer, /* length of message */
						outBuffer.length(), MPI.CHAR, 0/* Destination */,
						99/* TAG */, inBuffer, 3, MPI.INT, 0, 99);

				int resp = inBuffer.get(0);
				int sSeq = inBuffer.get(1);
				int wSeq = inBuffer.get(2);

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
		}

	}

}
