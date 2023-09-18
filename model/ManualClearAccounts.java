package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Clears the serialized file with the accounts in it manually. This program is
 * intended to be used only by the developers during testing and development (eg
 * when a class is changed and the old instances of this class are stored).
 * 
 * @author Adrianna Koppes
 * @since April 21, 2023
 */
public class ManualClearAccounts {

	public static void main(String[] args) {
		clear();
	}
	
	private static void clear() {
		HashMap<String, PlayerAccount> empty = new HashMap<>();
		FileOutputStream output;
		try {
			output = new FileOutputStream("accounts.ser");
			ObjectOutputStream outFile = new ObjectOutputStream(output);
			outFile.writeObject(empty);

			outFile.close();
			output.close();
		} catch (FileNotFoundException err) {
			System.out.println("Output file not found");
		} catch (IOException err) {
			err.printStackTrace();
			System.out.println("Couldn't write to file");
		}
	}
}
