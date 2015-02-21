package configuration_package;

import java.io.IOException;

import communication_package.Client;

public class ClientMain {
	public static void main(String[] args) {
		Client client = new Client("127.0.0.1", 8080);
		try {
			client.send("Hello".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
