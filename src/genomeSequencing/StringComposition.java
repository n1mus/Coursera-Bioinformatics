package genomeSequencing;

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

import util.IOUtilities;

public class StringComposition {

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
		
		
		
		
		
		
		
		
		
		
		
		
		TreeSet<String> compositionTreeSet = new TreeSet<String>();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// iterate through kmers of dna string
		// add kmers to tree set
		
		
		
		for(int i=0; i<= string.length()-k; i++) {
			
			compositionTreeSet.add( string.substring(i,i+k));
		}
		
		
		
		
		
		
		String[] compositionArr = new String[compositionTreeSet.size()];
		

		
		
		
		// convert tree set into String[]
		
		Iterator<String> iter = compositionTreeSet.iterator();
		for(int i=0; i< compositionTreeSet.size(); i++) {
			compositionArr[i] = iter.next();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		IOUtilities.printArray("composition", compositionArr, "\n");
		IOUtilities.writeToTxtFile(compositionTreeSet, "composition", "\n");
		
		
		
		
		
		
		
		
		
		
		
		

	}

}
