package ssh;

public class User {

	private String ip;
	private String userName;
	private String password;
	private String filePath;

	public User(String ip, String userName, String password, String filePath) {
		this.ip = ip;
		this.userName = userName;
		this.password = password;
		this.filePath = filePath;
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
}
