package configuration_package;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import communication_package.Client;
import communication_package.Server;
import communication_package.Server.TCPClientListner;

public class ServerMain {
	public static void main(String[] args) {
		Server server = new Server();
		server.setListner(clientListner);
		server.start(1992);
	}

	static TCPClientListner clientListner = new TCPClientListner() {

		@Override
		public void onNewClient(Client client) {
			byte[] data = null;
			String msg = "";
			try {
				do {
					data = client.receive();
					System.out.println("Client > " + new String(data, "UTF-8"));
					client.send(data);
					System.out.println("Me > " + new String(data, "UTF-8"));
				} while (!msg.equals("exit"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
