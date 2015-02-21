import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Start {
	Configuration con = new Configuration();

	void readConfigurationFile(String infile) throws FileNotFoundException {
		File file = new File(infile);
		Scanner input = new Scanner(file);
		int i = 0;
		ArrayList<String> Reader = new ArrayList<String>();
		ArrayList<String> Writer = new ArrayList<String>();
		while (input.hasNext()) {
			String nextLine = input.nextLine();
			String[] parts = nextLine.split("=");
			if (i == 0)
				con.setServerIp(parts[1]);
			else if (i == 0)
				con.setServerIp(parts[1]);
			else if (i == 1)
				con.setServerportno(Integer.parseInt(parts[1].trim()));
			else if (i == 2)
				con.setNumberOfReaders(Integer.parseInt(parts[1].trim()));
			else if (i == 3)
				Reader.add(parts[1]);
			else if (i == 4)
				Reader.add(parts[1]);
			else if (i == 5)
				Reader.add(parts[1]);
			else if (i == 6) {
				Reader.add(parts[1]);
				con.setReadersIp(Reader);
			} else if (i == 7)
				con.setNumberOfwriter(Integer.parseInt(parts[1].trim()));
			else if (i == 8)
				Writer.add(parts[1]);
			else if (i == 9)
				Writer.add(parts[1]);
			else if (i == 10)
				Writer.add(parts[1]);
			else if (i == 11) {
				Writer.add(parts[1]);
				con.setWritersIp(Writer);
			} else if (i == 12) {

				con.setNumberOfAccesses(Integer.parseInt(parts[1].trim()));
			}

			i++;
		}

		input.close();
	}

	public static void main(String[] args) {
		Start f = new Start();
		try {
			f.readConfigurationFile("system.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
