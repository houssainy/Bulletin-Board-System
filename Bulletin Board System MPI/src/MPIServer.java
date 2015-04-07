import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;

public class MPIServer {
	private static int sSeq;
	private static int numberOfReaders;
	private static ReadWriteLock readWriteLock;

	private static Log readerLog;
	private static Log writerLog;

	private static int clientCoutn = 0;

	public MPIServer(int n) throws MPIException, InterruptedException {
		sSeq = 0;
		numberOfReaders = 0;
		readWriteLock = new ReentrantReadWriteLock();

		readerLog = new Log("log-readers.txt");
		readerLog.log("sSeq		oVal	rID		rNum\n");
		readerLog.log("-----------------------------\n");
		readerLog.flush();

		writerLog = new Log("log-writers.txt");
		writerLog.log("sSeq		oVal	wID\n");
		writerLog.log("---------------------------\n");
		writerLog.flush();

		start(n);
	}

	private void start(int n) throws MPIException {
		System.out.println("Start Handling Server...");
		CharBuffer in = MPI.newCharBuffer(2);

		// Non Blocking receive
		while (true) {
			MPI.COMM_WORLD.recv(in, in.length(), MPI.CHAR, /* Source */
					MPI.ANY_SOURCE, 50);
			// System.out.println(req.waitStatus());
			// Request.waitAny(req);
			// if (req.test()) {

			if (in.get(0) == Constants.HELLO) {
				// Associate new thread for the new client to be able to use
				// block
				// receiving functions
				int rank = 0; 
				System.out.println("************************ " + (rank = in.get(1)));
				new Thread(new ClientHandler(rank)).start();
				clientCoutn++;
			} else {
				System.out.println("ERROR: Undefiend Message Received " + in);
			}
			// }
			if (clientCoutn >= n) {
				System.out.println("Breeeeeeeeeeeeeeeeeeeak :D");
				break;
			}
		}

	}

	private static void closeClient(int rank) throws MPIException {
		System.out.println("Client " + rank + " Closed.");
		clientCoutn--;
		if (clientCoutn == 0)
			MPI.Finalize();
	}

	private static class ClientHandler implements Runnable {
		int rank;

		public ClientHandler(int rank) {
			this.rank = rank;
		}

		@Override
		public void run() {
			CharBuffer in = MPI.newCharBuffer(2);

			try {
				while (true) {
					// Blocking receive
					System.out.println("Receiving command from " + rank);
					MPI.COMM_WORLD.recv(in, in.length(), MPI.CHAR, /* Source */
							rank, 99);
					System.out.println("Command Received from " + rank);
					if (in.get(0) == Constants.READ)
						readValue(rank);
					else if (in.get(0) == Constants.WRITE)
						updateValue(in.get(1), rank);
					else if (in.get(0) == Constants.BYE) {
						closeClient(rank);
						break;
					}
				}
			} catch (MPIException e) {
				e.printStackTrace();
			}
		}

		private static void readValue(int rank) throws MPIException {
			System.out.println("Client " + rank + "is reading ...");
			Scanner in;
			int value = 0;

			int rSeq = sSeq;

			// **** Lock ****
			readWriteLock.readLock().lock();

			numberOfReaders++;
			sSeq++;

			try {
				in = new Scanner(new File("news.txt"));
				value = in.nextInt();
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			readerLog.log("" + sSeq + "		" + value + "		" + rank + "		"
					+ numberOfReaders + "\n");
			readerLog.flush();

			numberOfReaders--;

			readWriteLock.readLock().unlock();
			// **** unLock ****

			IntBuffer outBuffer = MPI.newIntBuffer(3);
			outBuffer.put(0, value);
			outBuffer.put(1, sSeq);
			outBuffer.put(2, rSeq);

			MPI.COMM_WORLD.send(outBuffer, /* length of message */
					3, MPI.INT, rank/* Destination */, 99);
			System.out.println("Client " + rank + " Finished reading...");
		}

		private static void updateValue(int value, int rank)
				throws MPIException {

			// *** Lock ***
			readWriteLock.writeLock().lock();
			sSeq++;

			int wSeq = sSeq;
			PrintWriter writer;
			try {
				writer = new PrintWriter(new File("news.txt"));
				writer.println(value);
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			writerLog.log("" + sSeq + "		" + value + "		" + rank + "\n");
			writerLog.flush();

			readWriteLock.writeLock().unlock();
			// *** unLock ***

			IntBuffer outBuffer = MPI.newIntBuffer(3);
			outBuffer.put(0, Constants.OK);
			outBuffer.put(1, sSeq);
			outBuffer.put(2, wSeq);

			MPI.COMM_WORLD.send(outBuffer, /* length of message */
					3, MPI.INT, rank/* Destination */, 99);
		}

	}

}
