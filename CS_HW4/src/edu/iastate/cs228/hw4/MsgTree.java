package edu.iastate.cs228.hw4;

/**
 * The constructor of the class MsgTree, when called,  
 * builds a tree with internal nodes and leaf characters
 * 
 * There a various methods that can be called upon a MsgTree object
 * MsgTree can be iterated through and printed based on a specific encodedString
 * @author Charlene Baes
 *
 */
public class MsgTree
{
	protected char payloadChar; //payload character of given node
	protected MsgTree left; //left subnode of node
	protected MsgTree right; //right subnode of node
	protected boolean isLeaf; //true if leaf, false if not
	protected static MsgTree root; //stores the root of the tree

	protected static int staticCharIdx = 0; //keeps track of index in encodingString
	protected static String path = ""; //keeps track of the path taken to reach a node
	
	protected static int totalBits = 0; //stats, returns the 
	protected static int totalPrinted = 0; //stats, returns the total characters printed to screen
	protected static int totalCharacterCodes = 0;
	protected char[] arr; //local var keeps track of encodingString
	
	/**
	 * Constructor for building the tree from a string
	 * @param encodingString
	 */
	public MsgTree(String encodingString)
	{
		arr = encodingString.toCharArray();
		build(encodingString);
	}
	
	/**
	 * Constructor for building a MsgTree with given char payload
	 * a MsgTree with a char payload is a leaf
	 * @param payloadChar
	 */
	public MsgTree(char payloadChar)
	{
		this.left = null;
		this.right = null;
		this.payloadChar = payloadChar;
		this.isLeaf = true;
	}
	
	/**
	 * Constructor of an internal node
	 * Left and Right child become null
	 */
	public MsgTree()
	{
		this.left = null;
		this.right = null;
	}
	
	/**
	 * Decode method is used to print encoded message to the screen
	 * Done by iterating right (1) or left (0) until a leaf is reached
	 * 
	 * If a leaf is detected, print the character payload of the leaf
	 * Otherwise, iterate until a leaf is found using 1s and 0s given by the encodingString
	 * 
	 * @param node
	 * @param code
	 */
	public void decode(MsgTree node, String code)
	{
		char[] arr = code.toCharArray();
		root = node;
		
		while(staticCharIdx < arr.length)
		{
			findPayload(root,code);
		}
	}
	
	/**
	 * If the node is a leaf, print out the characters and increment the totalChars printed
	 * Depending on encoding string, use recursion to findPayload of the leaf
	 * 
	 * @param node
	 * @param code
	 */
	public static void findPayload(MsgTree node, String code)
	{
		char[] arr = code.toCharArray();
		
		//if the node is a leaf, print out the character and add to totalChars printed
		if(node.isLeaf)
		{
			if(node.payloadChar == '\n')
			{
				System.out.println();
				totalPrinted++;
				return;
			}
			else
			{
			System.out.print(node.payloadChar);
			totalPrinted++;
			return;
			}
		}
		
		//while program has not iterated through the complete encoded message
		while(staticCharIdx < arr.length)
		{
			char bit = arr[staticCharIdx]; //the bit can either be 1 or 0
			
			//recursively call findPayload on either node.right or node.left depending on 1 or 0
			if(bit == '1') 
			{
				staticCharIdx++;
				findPayload(node.right, code);
				return;
			}
			else if(bit == '0')
			{
				staticCharIdx++;
				findPayload(node.left, code);
				return;
			}
		}
	}
	
	/**
	 * if the bit is a ^, create a new node
	 *		1. check if left child is null, if not, create new MsgTree in right child
	 *		2. increment staticCharIdx to continue iterating encodingString
	 *		3. use recursion to continue to build more child trees on given side
	 *		4. call build(encodingString) to return to parent nodes
	 *
	 * if the bit is not a ^, create new node with a payload
	 * 		1. check if left child is null, if not, create new MsgTree(payload) in right child
	 * 		2. increment staticCharIdx to continue iterating encodingString
	 * 		3. call build(encodingString) to return to parent nodes
	 * @param encodingString
	 */
	protected void build(String encodingString)
	{
		/*
		 * break case: if left and right are occupied 
		 * and encodingString has been iterated, return
		 */
		if(isFull(this) || staticCharIdx - 1 == encodingString.length())
			return;
		
		this.arr = encodingString.toCharArray();
		char bit = arr[staticCharIdx + 1]; //stores current encodingString value
		
		if(bit == '^') //create a new node, either in left or right subtree
		{
			if(this.left == null)
			{
				this.left = new MsgTree();
				staticCharIdx++;
				this.left.build(encodingString);
				build(encodingString);
			}
			else
			{
				this.right = new MsgTree();
				staticCharIdx++;
				this.right.build(encodingString);
				build(encodingString);
			}
		}
		else //create a new noad with char payload in left or right subtree
		{
			if(this.left == null)
			{
				this.left = new MsgTree(bit);
				staticCharIdx++;
				build(encodingString);
			}
			else
			{
				this.right = new MsgTree(bit);
				staticCharIdx++;
				build(encodingString);
			}
		}
	}
	
	/**
	 * This method prints the codes of each character using preorder traversal of the MsgTree
	 * 
	 * If the node is a leaf, it prints out the payload character
	 * If the node is not a leaf, recursively iterate through left and right subtrees of node
	 * @param node
	 */
	public void printCodes(MsgTree node, String code)
	{
		//break case, if node is null, return
		if(node == null)
			return;
		
		//if node is a leaf, print out the payloadChar
		//exceptions for \n and \s, characters and code is formatted in output
		if(node.isLeaf)
		{
			if(node.payloadChar == '\n')
			{
				System.out.println("\\n" + "         " + code);
			}
			else if(node.payloadChar == ' ')
			{
				System.out.println("\\s" + "         " + code);
			}
			else
			{
				System.out.printf("%-10s %-10s\n", node.payloadChar, code);
			}
			
			totalBits += code.length(); 
			code = "";
			totalCharacterCodes++;
			return;
		}
		
		printCodes(node.left, code + "0"); //recursively call printCodes and add 0 to code since going left
		printCodes(node.right, code + "1");		//recursively call printCodes and add 1 to code since going right	
	}
	
	/**
	 * Returns true if the left and right subnodes of given node are occupied
	 * @param node
	 * @return
	 */
	private boolean isFull(MsgTree node)
	{
		return node.left != null && node.right != null;
	}
	
	/**
	 * This method prints the stats of a given MsgTree
	 * Average Bits per Character
	 * Total Characters Printed to Screen
	 * Space Savings %
	 * 
	 * @param compressed
	 */
	protected void stats(String compressed)
	{
		//total num of bits in the code column of the table / total num of characters in compressed message
		double avgBits = ((double) totalBits / totalCharacterCodes);
		System.out.printf("Avg bits/char: %.2f\n", avgBits);
		
		//total characters printed to console
		System.out.println("Total characters: " + totalPrinted);
		
		//space savings % == 1 - avgBits / 16 * 100
		double output = (1 - ((double) avgBits / 16)) * 100 ;
		System.out.printf("Space savings: %.2f" , output);
		System.out.print("%");
	}
}