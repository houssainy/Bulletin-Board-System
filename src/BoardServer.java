import java.io.IOException;
import java.io.UnsupportedEncodingException;

import messages_package.MessageOperator;
import messages_package.MessageOperator.Message;
import communication_package.Client;
import communication_package.Server;
import communication_package.Server.TCPClientListner;

public class BoardServer {
	private static int maxNumberOfAccess;
	private static int sequenceNumber;

	private static MessageOperator msgOperator;

	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			System.err.println("ERROR: Missing Arguments!");
			return;
		}
		sequenceNumber = 0;

		msgOperator = new MessageOperator();

		int port = Integer.parseInt(args[0].trim());
		maxNumberOfAccess = Integer.parseInt(args[1].trim());
		Server server = new Server();
		server.setListner(clientListner);
		server.start(port);
	}

	static TCPClientListner clientListner = new TCPClientListner() {

		@Override
		public void onNewClient(Client client) {
			sequenceNumber++;

			int numberOfAccess = 0;

			byte[] data = null;
			String msg = "";

			try {
				data = client.receive();
				msg = new String(data, "UTF-8");
				while (!msg.equals(MessageOperator.BYE_MSG)
						&& numberOfAccess < maxNumberOfAccess) {
					numberOfAccess++;

					handleMessage(msg);

					data = client.receive();
					msg = new String(data, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void handleMessage(String data) {
			Message msg = msgOperator.parseMessage(data);
			if (msg == null) {
				System.err.println("ERROR: Message corrupted.");
				return;
			}
			
		}
	};
}
