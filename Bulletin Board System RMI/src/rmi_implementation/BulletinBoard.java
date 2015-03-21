package rmi_implementation;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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

	protected BulletinBoard() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// public BulletinBoard() {
	// // super();
	// }

	@Override
	public <T> T executeTask(Task<T> t) throws RemoteException {
		return t.execute();
	}

	public static void main(String[] args) {
		if (args == null || args.length < 1) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}

		int serverPort = Integer.parseInt(args[0].trim());

		System.setProperty("java.rmi.server.useLocalHostname", "true");
		try {
			String name = "Board";
			BulletinBoard board = new BulletinBoard();
			// Board stub = (Board) UnicastRemoteObject.exportObject(board, 0);
			Registry registry = LocateRegistry.createRegistry(serverPort);
			System.out.println("Registry used is " + registry);
			registry.rebind(name, board);

			System.out.println("Bullitin Board Started ...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
