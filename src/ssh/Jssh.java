package ssh;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Jssh {

	public static void main(String[] arg) {
		Thread x = new Thread(new Runnable() {
			public void run() {
				User x = new User("192.168.1.24", "houssainy", "mohamed1992",
						"test1.py","");
				startProcess(x);
			}

		});
		x.start();

		Thread y = new Thread(new Runnable() {
			public void run() {
				User t = new User("192.168.1.15", "mohamedsaad", "7192",
						"test.py", "");
				startProcess(t);
			}

		});
		y.start();
		Thread z = new Thread(new Runnable() {
			public void run() {
				User x = new User("192.168.1.24", "houssainy", "mohamed1992",
						"test.py","");
				startProcess(x);
			}

		});
		z.start();
		// t();
		// t();
	}

	public static void startProcess(User currentUser) {
		try {
			// String ip = currentUser.g;
			// ,String User,String Password,String fileName;

			JSch jsch = new JSch();
			Session session = jsch.getSession(currentUser.getUserName(),
					currentUser.getIp(), 22);
			session.setPassword(currentUser.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			String command = "python " + currentUser.getFilePath();
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

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return passwd;
		}

		public boolean promptYesNo(String str) {
			Object[] options = { "yes", "no" };
			int foo = JOptionPane.showOptionDialog(null, str, "Warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]);
			return foo == 0;
		}

		String passwd;
		JTextField passwordField = (JTextField) new JPasswordField(20);

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			Object[] ob = { passwordField };
			int result = JOptionPane.showConfirmDialog(null, ob, message,
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				passwd = passwordField.getText();
				return true;
			} else {
				return false;
			}
		}

		public void showMessage(String message) {
			JOptionPane.showMessageDialog(null, message);
		}

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);
		private Container panel;

		public String[] promptKeyboardInteractive(String destination,
				String name, String instruction, String[] prompt, boolean[] echo) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx = 0;
			panel.add(new JLabel(instruction), gbc);
			gbc.gridy++;

			gbc.gridwidth = GridBagConstraints.RELATIVE;

			JTextField[] texts = new JTextField[prompt.length];
			for (int i = 0; i < prompt.length; i++) {
				gbc.fill = GridBagConstraints.NONE;
				gbc.gridx = 0;
				gbc.weightx = 1;
				panel.add(new JLabel(prompt[i]), gbc);

				gbc.gridx = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weighty = 1;
				if (echo[i]) {
					texts[i] = new JTextField(20);
				} else {
					texts[i] = new JPasswordField(20);
				}
				panel.add(texts[i], gbc);
				gbc.gridy++;
			}

			if (JOptionPane.showConfirmDialog(null, panel, destination + ": "
					+ name, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
				String[] response = new String[prompt.length];
				for (int i = 0; i < prompt.length; i++) {
					response[i] = texts[i].getText();
				}
				return response;
			} else {
				return null; // cancel
			}
		}
	}

	// public void startProcess(User user) {
	// // TODO Auto-generated method stub
	//
	// }
}