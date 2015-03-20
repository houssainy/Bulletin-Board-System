package socket_implementation.communication_package;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;
	private boolean running = false;

	private TCPClientListner listner;
	private Thread thread;

	/**
	 * Start the server in the given port
	 * 
	 * @param port
	 */
	public void start(final int port) {
		if (running)
			return;
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out
							.println("Server Started on port " + port + "...");
					running = true;
					serverSocket = new ServerSocket(port);
					while (running) {
						final Socket socket = serverSocket.accept();
						if (socket != null) {
							if (listner != null) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										listner.onNewClient(new Client(socket));
									}
								}).start();
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public void setListner(TCPClientListner listner) {
		this.listner = listner;
	}

	public void close() throws IOException {
		running = false;
		thread = null;
		serverSocket.close();
		System.out.println("Server Closed!\n Bye.");
	}

	/**
	 * 
	 * @author houssainy
	 *
	 *         Listener interface to handle new client.
	 */
	public interface TCPClientListner {
		// Method will be fired when new client join the server
		public abstract void onNewClient(Client client);
	}
}