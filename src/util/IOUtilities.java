package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Scanner;

public class IOUtilities {

	
	
	
	static String outputDir = "C:\\Users\\sumin\\workspace\\Coursera Bioinformatics\\output\\";
	
	
	
	
	
	
	
	
	
	
	/**
	 * Return Scanner for the file specified in args[0]
	 */
	public static Scanner getScanner(String... args) throws IOException {
	    if(args.length < 1) {
	        System.out.println("Error, usage: java ClassName inputfile");
	        System.exit(1);
	    }
	   
	    return new Scanner(new FileInputStream(args[0]));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param filename : path relative to outputDir
	 * 
	 * Writes character array to the text file, no space delimiters
	 */
	public static void writeCharArrayToTxtFile(char[] array, String filename) {
		
	    File file = new File(outputDir + filename);
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(char c : array) {
	    		writer.write(c);
	    	}
	    	
	    } catch (IOException x) {
	        System.err.format("IOException: %s%n", x);
	    }
	}
	
	/**
	 * Write int array, space delimited, to txt file
	 * 
	 * 
	 * @param array of ints
	 * @param filename (excluding the ".txt" extension)
	 */
	public static void writeArrayToTxtFile(int[] array, String filename) {
		
	    File file = new File(outputDir + filename + ".txt");
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(int i : array) {
	    		String str = String.valueOf( i);
	    		
	    		writer.write(str, 0, str.length());
	    		writer.write('\n');
	    	}
	    	
	    } catch (IOException x) {
	        System.err.format("IOException: %s%n", x);
	    }
	}
	
	
	/**
	 * Default delimiter is newline
	 * 
	 * 
	 * @param collection AbstractCollection (ArrayList, TreeSet, etc.) of CharSequence (String, StringBuilder, etc.)
	 * @param filename (excluding ".txt" extension)
	 * @param delimiter OPTIONAL. default is newline
	 */
	public 
	static 
	<T extends CharSequence> 
	void writeToTxtFile(AbstractCollection<T> collection, String filename, String... delimiter ) {


		String delim = (delimiter.length > 0) ? delimiter[0] : System.getProperty("line.separator");
		
		System.out.println("Writing to text file " + filename + ".txt");
		System.out.println(collection);
		
	    File file = new File(outputDir + filename + ".txt");
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(CharSequence cs : collection) {
	    		
	    		writer.write(cs.toString());
	    		writer.write(delim);
	    	}
	    	
	    }
	    catch (Exception e) {
	    	e.printStackTrace(System.err);
	    }
	}
	
	
	
	
	
	/**
	 * 
	 * @param stringArray array of CharSequence (String, StringBuilder, etc.)
	 * @param filename (excluding ".txt" extension)
	 */
	public static <T extends CharSequence> void writeToTxtFile(T[] stringArray, String filename) {

		System.out.println("Writing to text file " + filename + ".txt");
		
	    File file = new File(outputDir + filename + ".txt");

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(CharSequence cs : stringArray) {
	    		writer.write(cs.toString(),0,cs.length());
	    	}
	    	
	    }
	    catch (Exception e) {
	    	e.printStackTrace(System.err);
	    }

	}
	
	
	
	public static void
	writeToTxtFile(String text, String filename) {
		System.out.println("Writing to text file " + filename + ".txt");
		
	    File file = new File(outputDir + filename + ".txt");

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	writer.write(text);
	    	
	    	
	    }
	    catch (Exception e) {
	    	e.printStackTrace(System.err);
	    }

	}
	
	
	
	
	
	public static <T extends CharSequence> void printAbstractCollection(String name, String delimiter, AbstractCollection<T> collection ){
		if(delimiter==null) delimiter = "\n";
		System.out.println("Printing array: " + name);
		for(CharSequence str : collection) {
			System.out.print(str + delimiter);
		}
		System.out.println();
	}
	
	public static void printArray(String name, ArrayList<Integer> array) {
		System.out.println("Printing array: " + name);
		for(int i:array) {
			System.out.print(i);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	
	
	public static void printArray(String name, int[] array) {
		System.out.println("Printing array: " + name);
		for(int i:array) {
			System.out.print(i);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	public static void printArray(String name, int[][] matrix, int ... formattedColumnSpace) {
		System.out.println("Printing matrix: " + name);
		for(int[] row:matrix) {
			for(int i : row) {
				if(formattedColumnSpace.length>0)
					System.out.printf("%"+formattedColumnSpace[0]+"d ",i);
				else
					System.out.printf("%3d ",i);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printArray(String name, int[][][] matrices, int ... formattedColumnSpace) {
		
		System.out.println("\n\nprinting 3D matrix: " + name + " ...\n");
		
		for(int i=0; i<matrices.length; i++) {
			printArray(name + ", level: " + i, matrices[i], formattedColumnSpace);
		}
		
	}
	
	
	public static void printArray(String name, double[][] matrix, int ... formattedColumnSpace) {
		System.out.println("Printing array: " + name);
		for(double[] row:matrix) {
			for(double i : row) {
				if(formattedColumnSpace.length>0)
					System.out.printf("%"+formattedColumnSpace[0]+"d ",i);
				else
					System.out.printf("%8.3f ",i);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printArray(String name, String delimiter, float[] array) {
		if(delimiter==null) delimiter = "\n";
		System.out.println("Printing array: " + name);
		for(float f:array) {
			System.out.printf("%.3e",f);
			System.out.print(delimiter);
		}
		System.out.println();
	}
	
	
	
	
	public static void printArray(String name, String[] array, String... delimiter) {
		
		String delim = (delimiter.length>0) ? delimiter[0] : " ";
		
		System.out.println("Printing array: " + name);
		for(String s:array) {
			System.out.printf(s + delim);
		}
		System.out.println();
	}
	
	
	
	public static void printArray(String name, char[] array) {
		System.out.println("Printing array: " + name);
		for(char c:array) {
			System.out.print(c);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	

	
}
