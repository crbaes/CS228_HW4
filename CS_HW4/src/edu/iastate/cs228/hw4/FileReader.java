package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * This class reads the file provided by the user input
 * The number of lines are counted to account for the two different types of inputs
 * The character codes and hidden message given by the compressed code are printed to the screen
 * 
 * @author Charlene Baes
 */
public class FileReader 
{
	protected static String encoded = ""; 
	protected static String compressed = "";
	
	FileReader(){}
	
	public static void readFile(File fileName) throws FileNotFoundException
	{
		int lineCount = 0;
		
		Scanner scnrLine = new Scanner(fileName);
		Scanner scnrFile = new Scanner(fileName);

		/*
		 * Count the amount of lines inside of a file
		 */
		while(scnrLine.hasNextLine())
		{
			scnrLine.nextLine();
			lineCount++;
		}
		
		//if there are 2 lines, first is encoded, second is compressed
		if(lineCount == 2)
		{
			encoded = scnrFile.nextLine();
			compressed = scnrFile.nextLine();
		}
		//else if there are 3 lines, first 2 are apart of encoded, third is compressed
		else if(lineCount == 3)
		{
			encoded = scnrFile.nextLine() + "\n" + scnrFile.nextLine();
			compressed = scnrFile.nextLine();
		}
		
		scnrFile.close();
		scnrLine.close();
		
		MsgTree tree = new MsgTree(encoded); //create a new MsgTree
		
		//Prints out the characters and their binary codes to the screen
		System.out.print("character  code\n" + "-------------------------\n");
		tree.printCodes(tree, "");
		tree.staticCharIdx = 0;
		
		//Prints out the message given by the compressed message of binary bits
		System.out.print("\nMESSAGE: ");
		tree.decode(tree, compressed);
		
		//Prints out the Average Bits per Character, the Total Characters, and The Space Savings %
		System.out.print("\nSTATISTICS:\n");
		tree.stats(compressed);
		
		
	}
}
