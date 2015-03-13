package ssh;

public class User {
	public static final String SERVER_TYPE = "SERVER_TYPE";
	public static final String CLIENT_READER_TYPE = "client_reader_TYPE";
	public static final String CLIENT_WRITER_TYPE = "client_writer_TYPE";

	private String ip;
	private String userName;
	private String password;
	private String filePath;
	private String fileName;
	private String type;

	public User(String type) {
		this.setType(type);
	}

	public User(String ip, String userName, String password, String filePath,
			String type) {
		this.setIp(ip);
		this.setUserName(userName);
		this.setPassword(password);
		this.setFilePath(filePath);
		this.setType(type);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		int index = filePath.lastIndexOf("/");
		this.setFileName(filePath.substring(index + 1));
		this.filePath = filePath.substring(0, index);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
