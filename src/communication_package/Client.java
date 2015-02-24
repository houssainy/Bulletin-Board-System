package communication_package;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 
 * @author houssainy
 *
 */
public class Client {

	private BufferedOutputStream bos;
	private BufferedInputStream bis;

	// Socket connected to server
	private Socket socket;

	private int clientId;

	/**
	 * Connect to the server with IP and port number.
	 * 
	 * @param ip
	 * @param port
	 */
	public Client(String ip, int port) {
		try {
			socket = new Socket(InetAddress.getByName(ip), port);
System.out.println("Connected to "+ip + "port " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Client(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Send data to server.
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void send(byte[] data) throws IOException {
		byte[] size = new byte[4];
		int arraySize = data.length;

		// Convert integer to byte array.
		for (int i = 0; i < size.length; i++) {
			size[i] = (byte) (arraySize & 0xff);
			arraySize >>= 8;
		}

		if (bos == null)
			bos = new BufferedOutputStream(socket.getOutputStream());
		// ---------size-------
		bos.write(size);
		// ---------data-------
		bos.write(data);
		bos.flush();
	}

	/**
	 * Receive data from server.
	 * 
	 * First receive
	 * 
	 * @return data
	 * 
	 *         NOTE: Blocking method!
	 * @throws IOException
	 */
	public synchronized byte[] receive() throws IOException {
		if (bis == null)
			bis = new BufferedInputStream(socket.getInputStream());

		// Receive size of data
		// byte array to carry size of data
		byte[] size = new byte[4];

		int check = bis.read(size);
		if (check == -1)
			return null;

		// convert received size from byte array to integer
		int dataLenght = 0;
		for (int i = size.length - 1; i >= 0; i--) {
			dataLenght |= (size[i] & 0xff);
			if (i != 0)
				dataLenght <<= 8;
		}
		// ---------- receive data ----------
		// byte array to carry received data
		byte[] data = new byte[dataLenght];

		int offset = 0;
		int numRead = 0;
		while (offset < data.length
				&& (numRead = bis.read(data, offset, data.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < data.length)
			return null;

		return data;
	}

	public void close() throws IOException {
		if (socket != null) {
			socket.close();
			socket = null;
		}

		if (bos != null) {
			bos.close();
			bos = null;
		}

		if (bis != null) {
			bis.close();
			bis = null;
		}
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
}
