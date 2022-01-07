package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class contains the main method of this program
 * The user will be promopted to provide the name of a file to decode with 2-3 lines 
 * @author Charlene Baes
 */
public class ArchivedMessage
{
	public static void main(String[] args) throws IOException
	{
		Scanner scnr;
		System.out.println("Archived Message Reconstruction\nPlease enter filename to decode: ");
		
		scnr = new Scanner(System.in);
		File fileName = new File(scnr.next());
		FileReader.readFile(fileName);
		
		scnr.close();
	}
}

