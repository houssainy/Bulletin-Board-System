public class User {
	private String ip;
	private String userName;
	private String password;

	public User(String ip, String userName, String password) {
		this.setIp(ip);
		this.setUserName(userName);
		this.setPassword(password);
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
}
