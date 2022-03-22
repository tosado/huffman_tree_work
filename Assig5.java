import java.io.*;
import java.util.*;

public class Assig5 {
	
	public static void main(String[] args) {
		String filename = args[0];
		Scanner infile = null;
		
		try {
			infile = new Scanner ( new File ( filename ) );
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		BinaryNode<Character> huffmanRoot = createTree(infile);
		String[] huffmanTable = new String[26];
		StringBuilder code = new StringBuilder();
		createTable(huffmanRoot, code, huffmanTable);
		
		System.out.println("The Huffman tree has been restored\n");
		Scanner inScan = new Scanner(System.in);
		String response = "";
		while(!response.equals("3")) {
			
			System.out.println("Please choose from the following:");
			System.out.println("1) Encode a text string");
			System.out.println("2) Decode a Huffman string");
			System.out.println("3) Quit");
			response = inScan.next();
			char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			switch(response) {
			case "1":
				System.out.println("Enter a String from the following characters:");
				
				int i = 0;
				for(String item: huffmanTable) {
					if(huffmanTable[i] == null)
						break;
					System.out.print(letters[i]);
					i++;
				}
				System.out.println();
				String word = inScan.next();
				word = word.toUpperCase();
				if(!checkForEncodeErrors(word, huffmanTable)) {
					System.out.println("There was an error in your text string");
				}
				else {
					System.out.println("Huffman String:");
					String output = encodeString(word, huffmanTable);
					System.out.println(output);
				}
				break;
			case "2": 
				System.out.println("Here is the encoding table:");
				int j = 0;
				for(String item: huffmanTable) {
					if(huffmanTable[j] == null)
						break;
					System.out.println(letters[j] + ": " + huffmanTable[j]);
					j++;
				}
				System.out.println("Please enter a Huffman string (one line, no spaces)");
				String textString = inScan.next();
				String output = decodeString(textString, huffmanRoot);
				if(output==null)
					System.out.println("There was an error in your Huffman string");
				else {
					System.out.println("Text String:");
					System.out.println(output);
				}
				System.out.println();
				break;
			default: break;
			}
		}
		System.out.println("Good-bye");
		inScan.close();
		infile.close();
		
		
	}
	/**
	 * 
	 * @param input
	 * @return the root of the Huffman Tree created from the input file
	 */
	private static BinaryNode<Character> createTree(Scanner input){
		
		BinaryNode<Character> newRoot;
		String [] inputVals = input.nextLine().split(" ");
	
		if(inputVals[0].equals("I")){
			newRoot = new BinaryNode<Character>('0');
			newRoot.setLeftChild(createTree(input));
			newRoot.setRightChild(createTree(input));
			
		}else {
			char [] letter = inputVals[1].toCharArray();
			newRoot = new BinaryNode<Character>(letter[0]);
		}
		return newRoot;
	}
	/**
	 * Creates a Huffman Table from the given Huffman Tree
	 * @param root
	 * @param code
	 * @param table
	 */
	private static void createTable(BinaryNode<Character> root, StringBuilder code, String[] table) {
		if(root.getData()== '0'){
			code.append("0");
			createTable(root.getLeftChild(), code, table);
			code.deleteCharAt(code.length()-1);
			code.append("1");
			createTable(root.getRightChild(), code, table);
			code.deleteCharAt(code.length()-1);
			return;
		}else {
			Character toCheck = root.getData();
			int i = (int)(toCheck.charValue() - 65);//find proper position in table
			table[i] = code.toString();
			return;
		}
	}
	/**
	 * 
	 * @param word
	 * @param table
	 * @return boolean representing whether there are errors in the provided text string
	 */
	private static boolean checkForEncodeErrors(String word, String[] table) {
		char[] wordLetters = word.toCharArray();
		for(Character item: wordLetters) {
			int currPos = (int)(item.charValue()-65);
			if(table[currPos]==null) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * @param word
	 * @param table
	 * @return an encoded Huffman string 
	 */
	private static String encodeString(String word, String[] table) {
		StringBuilder result = new StringBuilder();
		char[] wordLetters = word.toCharArray();
		for(Character item: wordLetters) {
			int currPos = (int)(item.charValue()-65);
			result.append(table[currPos]+"\n");
		}
		return result.toString();
	}
	/**
	 * 
	 * @param huffmanText
	 * @param root
	 * @return a decoded text string from
	 */
	private static String decodeString(String huffmanText, BinaryNode<Character> root) {
		char[] bits = huffmanText.toCharArray();
		StringBuilder result = new StringBuilder();
		BinaryNode<Character> origRoot = root;
		int i = 0;
		for(char bit: bits) {
			
			if(bit == '0') {
				if(!root.hasLeftChild())
					return null;
				
				root = root.getLeftChild();
				if(root.isLeaf()) {
					result.append(root.getData());
					root = origRoot;
					++i;
					continue;
				}
				else if(i ==bits.length - 1)
					return null;
				else {
					++i;
					continue;
				}
			}
			else {
				root = root.getRightChild();
				if(root.isLeaf()) {
					result.append(root.getData());
					root = origRoot;
					++i;
					continue;
				}
				else if(i == bits.length-1)
					return null;
			
				else {
					++i;
					continue;
				}
			}
		
				
		}
		
		
		return result.toString();
		
	}
}
