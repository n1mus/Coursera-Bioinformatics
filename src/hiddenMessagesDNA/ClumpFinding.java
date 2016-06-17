package hiddenMessagesDNA;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;


/*
 * Clump Finding Problem: Find patterns forming clumps in a string.
 *    Input: A string Genome, and integers k, L, and t.
 *    Output: All distinct k-mers forming (L, t)-clumps in Genome.
 */
public class ClumpFinding {

	
	public static void main(String[] args) {
		char[] sequence;
		int k, L, t;
		
		try( Scanner reader = Utilities.getScanner(args)) {
			k = Integer.parseInt(reader.next());
			L = Integer.parseInt(reader.next());
			t = Integer.parseInt(reader.next());
			reader.nextLine();
			sequence = reader.nextLine().toCharArray();

			System.out.println(k);
			System.out.println(L);
			System.out.println(t);
			System.out.println(sequence.length);
			System.out.println();
			
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
		
		
		
		String[] clumpingPatterns = clumpFind(sequence,k,L,t);
		Utilities.write(clumpingPatterns, "clumps.txt");
		
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param sequence DNA genome/sequence
	 * @param k length of k-mers looking for
	 * @param L length of window looking for clumps
	 * @param t number of recurring k-mers to count as a clump
	 * @return array of k-length patterns that occur as clumps
	 */
	static String[] clumpFind(char[] sequence, int k, int L, int t) {
		
		TreeSet<String> freqPatterns = new TreeSet<String>(); // use set instead of array
																// so don't add duplicate patterns
		int numAllPossibleKMers = (int) Math.pow(4, k);
		boolean[] clump = new boolean[numAllPossibleKMers];
		
		
		
		// initialize frequency and clump arrays
		int[] freqArray = Utilities.computingFrequencies(  Arrays.copyOf(sequence,L)
															, k  );
		
		if(clump.length != freqArray.length) System.err.println("Error\n" + new Exception()); // logic check
		
		for( int i=0; i< freqArray.length; i++ ) {
			if ( freqArray[i] >= t)
				clump[i] = true;
		}
		
		
		// slide window of length L through rest of sequence
		// and update frequency and clump arrays
		
		for( int windowStartIndex=1; windowStartIndex <= sequence.length-L; windowStartIndex++) {
			
			// first pattern in previous window, as a number
			int firstPatternNumRep = Utilities.patternToNumber(sequence, windowStartIndex-1, k);
			freqArray[firstPatternNumRep]--;
			
			// last pattern in current window, as number
			int lastPatternNumRep = Utilities.patternToNumber(sequence, windowStartIndex+L-k, k);
			freqArray[lastPatternNumRep]++;
			
			if(freqArray[lastPatternNumRep] >= t)
				clump[lastPatternNumRep] = true;
			
		}
		
		
		
		
		
		
		// populate "TreeSet<String> freqPatterns" using the "boolean[] clump"
		for( int i=0; i< numAllPossibleKMers; i++){
			if (clump[i]){
				freqPatterns.add(    Utilities.numberToPattern(i,k)   );
			}
		}
		
		
		
		System.out.println(freqPatterns.size());
		return  freqPatterns.toArray(new String[0]);
	}
	
	
	
	
	
	
}
