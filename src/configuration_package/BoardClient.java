package configuration_package;

import java.io.IOException;
import java.util.Scanner;

import communication_package.Client;

public class BoardClient {
	public static void main(String[] args) {
		Client client = new Client("197.160.131.129", 1992);

		Scanner in = new Scanner(System.in);
		String msg = "";
		while ((msg = in.nextLine()) != null && !msg.equals("exit")) {
			try {
				System.out.println("ME > " + msg);
				client.send(msg.getBytes());
				
				System.out.println("Server > " + new String(client.receive(), "UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
