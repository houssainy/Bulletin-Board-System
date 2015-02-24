package ssh;

import com.jcraft.jsch.*;
import java.io.*;

public class Jssh {

	public void startProcess(final User currentUser) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				performShell(currentUser, "javac ");// for build
				performShell(currentUser, "java ");// for run

			}
		}).start();

	}

	private void performShell(final User currentUser, String commandType) {
		try {

			JSch jsch = new JSch();
			Session session = jsch.getSession(currentUser.getUserName(),
					currentUser.getIp(), 22);
			session.setPassword(currentUser.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			String command = "";
			if (commandType.equals("javac ")) {
				command = "javac " + currentUser.getFilePath();
			} else {
				command = "java " + currentUser.getFilePath();
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
					System.out.println(Thread.currentThread());
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