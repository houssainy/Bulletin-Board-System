package mpi_implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import mpi.MPI;
import mpi.MPIException;

public class MPIServer {
	private static int sSeq;
	private static int numberOfReaders;
	private static ReadWriteLock readWriteLock;

	private static Log readerLog;
	private static Log writerLog;

	public MPIServer() throws MPIException {
		sSeq = 0;
		numberOfReaders = 0;
		readWriteLock = new ReentrantReadWriteLock();

		readerLog = new Log("log-readers.txt");
		readerLog.log("sSeq		oVal	rID		rNum\n");
		readerLog.log("-----------------------------\n");

		writerLog = new Log("log-writers.txt");
		writerLog.log("sSeq		oVal	wID\n");
		writerLog.log("---------------------------\n");

		start();
	}

	private void start() throws MPIException {
		System.out.println("Start Handling Server.");
		CharBuffer in = MPI.newCharBuffer(2);

		// Non Blocking receive
		MPI.COMM_WORLD.iRecv(in, 1, MPI.CHAR, /* Source */MPI.ANY_SOURCE, 99);
		if (in.get(0) == Constants.HELLO) {
			// Associate new thread for the new client to be able to use block
			// receiving functions
			new Thread(new ClientHandler(in.get(1))).start();
		}
	}

	private static void readValue(int rank) throws MPIException {
		Scanner in;
		String value = "";

		int rSeq = sSeq;

		// **** Lock ****
		readWriteLock.readLock().lock();

		numberOfReaders++;
		sSeq++;

		try {
			in = new Scanner(new File("news.txt"));
			value = in.nextInt() + "";
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

		CharBuffer outBuffer = MPI.newCharBuffer(3);
		outBuffer.put(0, value.charAt(0));
		outBuffer.put(1, (char) sSeq);
		outBuffer.put(2, (char) rSeq);

		MPI.COMM_WORLD.send(outBuffer, /* length of message */
				outBuffer.length(), MPI.CHAR, rank/* Destination */, 99);
	}

	private static void updateValue(int value, int rank) throws MPIException {

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

		CharBuffer outBuffer = MPI.newCharBuffer(3);
		outBuffer.put(0, Constants.OK);
		outBuffer.put(1, (char) sSeq);
		outBuffer.put(2, (char) wSeq);

		MPI.COMM_WORLD.send(outBuffer, /* length of message */
				outBuffer.length(), MPI.CHAR, rank/* Destination */, 99);
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
					MPI.COMM_WORLD.recv(in, in.length(), MPI.CHAR, /* Source */
							MPI.ANY_SOURCE, 99);

					if (in.get(0) == Constants.READ)
						readValue(rank);
					else if (in.get(0) == Constants.WRITE)
						updateValue(in.get(1), rank);
					else if (in.get(0) == Constants.BYE)
						break;
				}
			} catch (MPIException e) {
				e.printStackTrace();
			}
		}
	}

}
