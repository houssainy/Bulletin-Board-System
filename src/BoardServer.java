
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import communication_package.Client;
import communication_package.Server;
import communication_package.Server.TCPClientListner;

public class BoardServer {
	private static int maxNumberOfAccess;
	private static int sequenceNumber;

	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}
		sequenceNumber = 0;

		int port = Integer.parseInt(args[0].trim());
		maxNumberOfAccess = Integer.parseInt(args[1].trim());
		Server server = new Server();
		server.setListner(clientListner);
		server.start(port);
	}

	static TCPClientListner clientListner = new TCPClientListner() {

		@Override
		public void onNewClient(Client client) {
			// TODO(houssainy)
			// byte[] data = null;
			// String msg = "";
			// try {
			// do {
			// data = client.receive();
			// System.out.println("Client > " + new String(data, "UTF-8"));
			// client.send(data);
			// System.out.println("Me > " + new String(data, "UTF-8"));
			// } while (!msg.equals("exit"));
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
	};
}