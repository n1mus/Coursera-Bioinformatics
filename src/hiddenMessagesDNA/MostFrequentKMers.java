package hiddenMessagesDNA;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Arrays;

public class MostFrequentKMers {

	

	

	
	
	public static void main(String[] args) throws IOException {
		
	   
	    Scanner reader = Utilities.getScanner(args);
	    
	    char[] sequence = "TAAACGTGAGAGAAACGTGCTGATTACACTTGTTCGTGTGGTAT".toCharArray();
	    int k = 3;
	    
	    System.out.println(mostFrequentKMers(sequence,k));
	    
	    
	    reader.close();
	    

	}
	
	
	static TreeSet<String> mostFrequentKMers(char[] sequence, int k) {
		
		TreeSet<String> mostFreq = new TreeSet<String>(); // most frequent letter patterns
		int[] count = new int[sequence.length-k];
		
		
		// count frequency of every possible k-mer
		// RUNTIME    |sequence|^2
		// (sorting method would be |sequence| )
		for(int i=0; i<sequence.length-k; i++){
			char[] pattern = Arrays.copyOfRange(sequence,i,i+k);
			count[i] = Utilities.patternCount(sequence, pattern);
		}
		
		
		// determine max frequency
		// RUNTIME    4^k
		// (sorting method would be |sequence| * log |sequence| )
		
		int max = Utilities.max(count);
		
		for( int i=0; i<count.length; i++) {
			if(count[i] == max) {
				String freqSeq = String.copyValueOf(sequence,i,k);
				mostFreq.add(freqSeq);
			}
		}
		
		
		return mostFreq;
	}

}
