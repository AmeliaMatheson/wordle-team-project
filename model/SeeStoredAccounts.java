package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * Reads in all of the stored accounts in the serialized file and prints them
 * out so they can be easily accessed during development. For development/testing 
 * purposes only.
 * 
 * @author Adrianna Koppes
 * @since April 22, 2023
 */
public class SeeStoredAccounts {

	public static void main(String[] args) {
		readAccounts();
	}
	
	private static void readAccounts() {
		HashMap<String, PlayerAccount> accounts = new HashMap<>();
		FileInputStream fromFile;
		try {
			fromFile = new FileInputStream("accounts.ser");
			ObjectInputStream inFile = new ObjectInputStream(fromFile);
			accounts = (HashMap<String, PlayerAccount>) inFile.readObject();
			
			inFile.close();
			fromFile.close();
			
			printAccounts(accounts);
		} catch (FileNotFoundException err) {
			System.out.println("Input file not found");
		} catch (IOException err) {
			err.printStackTrace();
			System.out.println("Couldn't read from file");
		} catch (ClassNotFoundException err) {
			System.out.println("Incorrect cast");
		}
	}

	private static void printAccounts(HashMap<String, PlayerAccount> accounts) {
		for(String username : accounts.keySet()) {
			System.out.println(username + " : " + accounts.get(username).getPassword());
		}
	}
}
