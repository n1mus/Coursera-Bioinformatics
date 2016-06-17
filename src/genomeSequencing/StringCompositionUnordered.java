package genomeSequencing;

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

import util.IOUtilities;

public class StringCompositionUnordered {

	public static void main(String[] args) {
		
		
		
		String string;
		int k;
		
		
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
				k = reader.nextInt();
				string = reader.next();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		int numKMers = string.length() - k + 1;
		
		
		
		
		
		
		String[] compositionArr = new String[numKMers];
		

		
		
		
		for(int i=0; i<numKMers; i++) {
			compositionArr[i] = string.substring(i,i+k);
		}
		
		
		
		
		
		
		
		
		
		
		
		IOUtilities.printArray("composition", compositionArr, "\n");
		
		
		
		
		
		
		
		
		
		
		
		

	}

}
