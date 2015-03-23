package rmi_implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author houssainy
 *
 *         An Bulletin Board implemented using RMI.
 * 
 *         Reference:
 *         http://docs.oracle.com/javase/tutorial/rmi/implementing.html
 */
public class BulletinBoard extends UnicastRemoteObject implements Board {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int sSeq;
	private int numberOfReaders;
	private ReadWriteLock readWriteLock;

	private Log readerLog;
	private Log writerLog;

	protected BulletinBoard() throws RemoteException {
		super();
		this.sSeq = 0;
		this.numberOfReaders = 0;
		this.readWriteLock = new ReentrantReadWriteLock();

		readerLog = new Log("log-readers.txt");
		readerLog.log("sSeq		oVal	rID		rNum\n");
		readerLog.log("-----------------------------\n");

		writerLog = new Log("log-writers.txt");
		writerLog.log("sSeq		oVal	wID\n");
		writerLog.log("--------------------\n");
	}

	@Override
	public String readValue(int id) {
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

		readerLog.log("" + sSeq + "		" + value + "		" + id + "		"
				+ numberOfReaders+"\n");
		readerLog.flush();

		numberOfReaders--;

		readWriteLock.readLock().unlock();
		// **** unLock ****

		return new String(value + "," + sSeq + "," + rSeq);
	}

	@Override
	public String updateValue(int value, int id) {

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

		writerLog.log("" + sSeq + "		" + value + "		" + id+"\n");
		writerLog.flush();

		readWriteLock.writeLock().unlock();
		// *** unLock ***

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("resp", "OK");
		map.put("sSeq", sSeq + "");
		map.put("wSeq", wSeq + "");

		return new String("OK," + sSeq + "," + wSeq);
	}

	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}
		String registryIp = args[0].trim();
		int serverPort = Integer.parseInt(args[1].trim());

		System.setProperty("java.rmi.server.hostname", registryIp);
		try {
			String name = "Board";
			BulletinBoard board = new BulletinBoard();
			Registry registry = LocateRegistry.createRegistry(serverPort);

			registry.rebind(name, board);

			System.out.println("Bullitin Board Started on port " + serverPort
					+ "...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
