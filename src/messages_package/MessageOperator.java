package messages_package;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 
 * @author houssainy
 *	
 *		Class to generate and parse Bulletin message format.
 */
public class MessageOperator {
	public static final String READ_MSG = "read_msg";
	public static final String WRITE_MSG = "write_msg";
	public static final String BYE_MSG = "bye_msg";

	public String generateMessage(String msg, String type) throws UnkownMessage {
		StringBuilder generatedMsg = new StringBuilder();
		if (type.equals(READ_MSG))
			generatedMsg.append(READ_MSG + "\n");
		else if (type.equals(WRITE_MSG))
			generatedMsg.append(WRITE_MSG + "\n");
		else if (type.equals(BYE_MSG))
			generatedMsg.append(BYE_MSG + "\n");
		else
			throw new UnkownMessage("ERROR: Unkown Message Type!");

		generatedMsg.append(msg);
		return generatedMsg.toString();
	}

	public Message parseMessage(String msg) {
		InputStream stream = new ByteArrayInputStream(
				msg.getBytes(StandardCharsets.UTF_8));
		Scanner in = new Scanner(stream);

		if (!in.hasNext())
			return null;
		String type = in.nextLine();

		if (!type.equals(READ_MSG) && !type.equals(WRITE_MSG)
				&& !type.equals(BYE_MSG))
			return null;

		StringBuilder parsedMsg = new StringBuilder();
		while (in.hasNext())
			parsedMsg.append(in.next());

		return new Message(type, parsedMsg.toString());
	}

	public class Message {
		private String type;
		private String msg;

		public Message(String msg, String type) {
			this.msg = msg;
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public String getMsg() {
			return msg;
		}
	}

	public class UnkownMessage extends Exception {
		public UnkownMessage(String string) {
			super(string);
		}
	}
}
