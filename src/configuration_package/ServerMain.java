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
		server.start(8080);
	}

	static TCPClientListner clientListner = new TCPClientListner() {

		@Override
		public void onNewClient(Client client) {
			byte[] data = null;
			try {
				data = client.receive();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				System.out.println(new String(data, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
