package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Scanner;

public class MedianString {

	public static void main(String[] args) {

		int k;
		ArrayList<String> dna = new ArrayList<String>();
		
		
		try(Scanner reader = Utilities.getScanner(args[0])) {
			k = reader.nextInt();
			while(reader.hasNextLine()) {
				dna.add(new String(reader.nextLine().trim()));
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		

	}
	
	
	
	/**
	 * Given a collection of upstream regions, find a motif of length k
	 * that occurs in each upstream region
	 * 
	 * such that the total Hamming distance between the consensus string and
	 * the motifs in the upstream regions
	 * is minimized
	 * 
	 * 
	 * @param dna collection of upstream regions
	 * @param k length of consensus string searching for
	 */
	static ArrayList<String> medianString(ArrayList<String> dna, int k) {
		
		// want to minimize this
		// total Hamming distance between consensus string
		// and motifs (one chosen from each upstream region)
		int minMotifsDist = Integer.MAX_VALUE;
		ArrayList<String> medians = new ArrayList<String>();
		
		// iterate through all possible patterns (inefficient)
		for(int i=0; i<Math.pow(4, k); i++) {
			
			
			String pattern = Utilities.numberToPattern(i, k);

			// find least possible pattern-motifs distance
			int distance = minTotalHammingDistances(pattern,dna);
			
			if(distance<minMotifsDist){
				minMotifsDist = distance;
				medians.clear();
				medians.add(pattern);
			} else if(distance == minMotifsDist) {
				medians.add(pattern);
			}
			
		}
		return medians;
		
	}
	
	
	
	
	
	
	/**
	 * Given pattern and an array of upstream regions,
	 *  find the minimum total Hamming distance
	 * between the pattern and closest set of candidate motifs
	 * (1 candidate motif from each upstream region)
	 * 
	 * @param pattern 
	 * @param dna Array of upstream regions
	 * @return
	 */
	static int minTotalHammingDistances(String pattern, ArrayList<String> dna) {
		
		int k = pattern.length();
		int minTotalHammingDistances = 0;
		
		// iterate through all upstream regions

		for( String region : dna) {
			
			 // to find sequence with least Hamming distance (compared to pattern)
				// from each region
			
			int minHammingDist = Integer.MAX_VALUE;
			
			for(int i=0; i<=region.length()-k; i++) {
				String candidateMotif = region.substring(i,i+k);
				int hammingDist = Utilities.hammingDistance(pattern, candidateMotif);
				if(hammingDist < minHammingDist) minHammingDist = hammingDist;
			}
			
			
			minTotalHammingDistances += minHammingDist;
		}
		
		
		
		return minTotalHammingDistances;
	}
}
