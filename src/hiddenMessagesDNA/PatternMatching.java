package hiddenMessagesDNA;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PatternMatching {
	public static void main(String[] args){
		
		char[] sequence;
		char[] pattern;
		int seqLen;
		int pattLen;
		
		try( Scanner reader = Utilities.getScanner(args)) {
			pattern = reader.nextLine().toCharArray();
			pattLen = pattern.length;
			sequence = reader.nextLine().toCharArray();
			seqLen = sequence.length;

			System.out.println("pattern is " + String.copyValueOf(pattern));
			System.out.println("sequence is " + String.copyValueOf(sequence));
			System.out.println("length of sequence is " + sequence.length);
			System.out.println("sequence[1000] is " + sequence[1000]);
		} catch(Exception e){
			System.err.println(e);
			return;
		}
		
		
		/*
		 * Write to file as find patterns
		 */
		
	    File file = new File("C:\\Users\\sumin\\workspace\\Bioinformatics\\output\\" + "occurrences.txt");
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(int i=0; i<=seqLen-pattLen; i++) {
	    		
	    		
	    		if(Utilities.patternMatchAt(sequence, pattern, i)){
	    			String occ = Integer.toString(i) + " ";
	    			writer.write(occ,0,occ.length());
	    		}
	    	}
	    	
	    	
	    } catch (IOException x) {
	        System.err.format("IOException: %s%n", x);
	    }
		
		System.out.println("End main");
	}
	

}
