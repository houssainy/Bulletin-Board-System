package ssh;

import com.jcraft.jsch.*;

import java.io.*;

public class Jssh {
	
	public Jssh(){
		
	}
	
//	public static void main(String[] args) {
//		User user = new User("127.0.0.1", "mohamedsaad", "7192", "test");
//		startProcess(user);
//	}

	public  void startProcess(final User currentUser,final String serverIp, final int portNumber) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				performShell(currentUser,serverIp,portNumber, "javac ");// for build
				performShell(currentUser,serverIp,portNumber, "java ");// for run

			}
		}).start();

	}

	private void performShell(final User currentUser, String serverIp, int portNumber,String commandType ) {
		try {

			JSch jsch = new JSch();
			Session session = jsch.getSession(currentUser.getUserName(),
					currentUser.getIp(), 22);
			session.setPassword(currentUser.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			String command = "";
			if (commandType.equals("javac ")) {
				command = "cd Desktop/project/src;javac " + currentUser.getFilePath()+".java";
			} else {
				//command = "java " + currentUser.getFilePath();
				if (currentUser.getType().equals(User.SERVER_TYPE))
					command = "cd Desktop/project/src;java "+currentUser.getFilePath()+ " "+ portNumber + " " + 20;
				else
					command = "cd Desktop/project/src;java "+currentUser.getFilePath()+ " "+ serverIp + " " + portNumber;
			}

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println(commandType);
					System.out.println("exit-status: "
							+ channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);

				} catch (Exception ee) {
				}
			}
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}