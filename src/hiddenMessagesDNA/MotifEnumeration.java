package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class MotifEnumeration {

	public static void main(String[] args) {
		ArrayList<StringBuilder> dna = new ArrayList<StringBuilder>();
		int k, d;
		
		try(Scanner reader = Utilities.getScanner(args[0])) {

			k = reader.nextInt();
			d = reader.nextInt();
			
			while(reader.hasNextLine()) {
				dna.add(new StringBuilder(reader.next().trim()));
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		
		
		
		
		// to hold the neighbors of the k-mers of first dna strand
		TreeSet<StringBuilder> neighborsOfFirstDNAStrand = new TreeSet<StringBuilder>(new StringBuilderComparator());
		
		StringBuilder firstDNAStrand = dna.remove(0);
		
		// iterate through k-Mers of first DNA strand collecting all neighbors
		for(int i=0; i<=firstDNAStrand.length()-k; i++) {
			
			StringBuilder pattern = new StringBuilder(firstDNAStrand.substring(i, i+k));
			ArrayList<StringBuilder> neighbors = Utilities.neighbors(pattern, d);
			
			neighborsOfFirstDNAStrand.addAll(neighbors);
			
		}
		
		
		// the result:
		// patterns that are present in each DNA strand (with at most d mismatches)
		TreeSet<StringBuilder> hammingUbiquitousPatterns = new TreeSet<StringBuilder>(new StringBuilderComparator());
		
		// iterate through neighbors from first DNA strand
		for(StringBuilder neighbor : neighborsOfFirstDNAStrand) {
			
			
			boolean hammingUbiquitous = true;
			
			
			// iterate through rest of DNA strands
			// check if neighbor is (Hammingly) in all of them
			for(StringBuilder dnaStrand : dna) {
				if(! containsHammingPattern(dnaStrand, neighbor,d)) {
					hammingUbiquitous = false;
					break;
				}
			}
			
			
			if(hammingUbiquitous) hammingUbiquitousPatterns.add(neighbor);
			
			
		}
		
		
		Utilities.printAbstractCollection("hamming ubiquitous patterns", hammingUbiquitousPatterns);
		

	}
	
	/**
	 * Returns true if the DNA sequence contains the pattern with at most d mismatches
	 * 
	 * 
	 * @param dna
	 * @param pattern
	 * @param d
	 * @return
	 */
	static boolean containsHammingPattern(CharSequence dna, CharSequence pattern, int d) {
		
		for(int index=0; index<=dna.length()-pattern.length(); index++) {
			if(Utilities.hammingPatternMatchAt(dna, index, pattern, d)) {
				return true;
			}
		}
		
		return false;
	}


}
