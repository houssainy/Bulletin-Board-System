import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Start {

	public Configuration readConfigurationFile(String filePath) throws FileNotFoundException {
		Configuration configuration = new Configuration();
		Scanner in = new Scanner(new File(filePath));
		
		while (in.hasNext()) {
			String nextLine = in.nextLine();
			String[] parts = nextLine.split("=");

		}

		in.close();
		return configuration;
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
