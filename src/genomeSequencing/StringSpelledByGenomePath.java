package genomeSequencing;

import java.util.ArrayList;
import java.util.Scanner;

import util.IOUtilities;

public class StringSpelledByGenomePath {

	public static void main(String[] args) {


		
		ArrayList<String> kmers = new ArrayList<String>();
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			while(reader.hasNext()) {
				kmers.add(reader.next().trim());
			}
		
		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	
		
		
		
		
		StringBuffer result = new StringBuffer(kmers.get(0));
		
		
		
		int n = kmers.size(); // num kmers
		int k = kmers.get(0).length();
		
		
		
		for(int i = 1; i < n ; i++) {
			String kmer = kmers.get(i); // get i-th kmer 
			char newLetter = kmer.charAt(k-1); // get last letter of kmer
			
			result.append(newLetter);
		}
		
		
		
		
		
		StringBuffer[] out = {result};
		
		
		IOUtilities.writeToTxtFile(out, "concat");
		
		
		

	}

}
