package messages_package;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 
 * @author houssainy
 *
 *         Class to generate and parse Bulletin message format.
 */
public class MessageOperator {
	public static final String READ_MSG = "read_msg";
	public static final String WRITE_MSG = "write_msg";
	public static final String BYE_MSG = "bye_msg";

	/**
	 * Message format:
	 * 
	 * write_msg\n <filePath>\n data\n
	 * 
	 * @param data
	 */
	public String generateWriteMessage(String data, String filePath) {
		StringBuilder generatedMsg = new StringBuilder();
		generatedMsg.append(WRITE_MSG + "\n");
		generatedMsg.append(filePath + "\n");
		generatedMsg.append(data);
		return generatedMsg.toString();
	}

	/**
	 * Message format:
	 * 
	 * read_msg\n <filePath>\n
	 */
	public String generateReadMessage(String filePath) {
		StringBuilder generatedMsg = new StringBuilder();
		generatedMsg.append(READ_MSG + "\n");

		generatedMsg.append(filePath);
		return generatedMsg.toString();
	}

	/**
	 * Message format:
	 * 
	 * bye_msg\n
	 * 
	 */
	public String generateByeMessage() {
		StringBuilder generatedMsg = new StringBuilder();
		generatedMsg.append(BYE_MSG + "\n");

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

		Message message = new Message(type);
		String filePath = "";

		switch (type) {
		case READ_MSG:
			if (!in.hasNext())
				return null;
			
			filePath = in.nextLine();
			message.setFilePath(filePath);
			
			break;
		case WRITE_MSG:
			if (!in.hasNext())
				return null;
			
			filePath = in.nextLine();
			StringBuilder parsedMsg = new StringBuilder();
			
			while (in.hasNext())
				parsedMsg.append(in.next());
			
			message.setFilePath(filePath);
			message.setMsg(parsedMsg.toString());
			break;
		}
		return message;
	}

	public class Message {
		private String type;
		private String msg;
		private String filePath;

		public Message(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
	}

	public class UnkownMessage extends Exception {
		public UnkownMessage(String string) {
			super(string);
		}
	}
}
