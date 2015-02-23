

import java.io.IOException;
import java.util.Scanner;

import communication_package.Client;

public class BoardClient {
	public static void main(String[] args) {
		if(args == null || args.length < 2) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}
		
		String serverIp = args[0];
		int serverPort = Integer.parseInt(args[1].trim());
		
		Client client = new Client(serverIp, serverPort);

		Scanner in = new Scanner(System.in);
		String msg = "";
		while ((msg = in.nextLine()) != null && !msg.equals("exit")) {
			// TODO(houssainy)
//			try {
//				System.out.println("ME > " + msg);
//				client.send(msg.getBytes());
//				
//				System.out.println("Server > " + new String(client.receive(), "UTF-8"));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Closed ...");
	}
}
