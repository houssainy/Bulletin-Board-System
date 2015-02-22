package ssh;
public class User {
	public static final String TYPE_READER = "READER";
	public static final String TYPE_WRITER = "READER";

	private String ip;
	private String userName;
	private String password;
	private String type;

	public User(String ip, String userName, String password, String type) {
		this.setIp(ip);
		this.setUserName(userName);
		this.setPassword(password);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
