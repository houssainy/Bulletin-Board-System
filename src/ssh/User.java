package ssh;

public class User {
	public static final String SERVER_TYPE = "SERVER_TYPE";
	public static final String CLIENT_TYPE = "client_TYPE";

	private String ip;
	private String userName;
	private String password;
	private String filePath;
	private String type;

	public User(String type) {
		this.type = type;
	}

	public User(String ip, String userName, String password, String filePath,
			String type) {
		this.ip = ip;
		this.userName = userName;
		this.password = password;
		this.filePath = filePath;
		this.type = type;
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
		this.filePath = filePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
