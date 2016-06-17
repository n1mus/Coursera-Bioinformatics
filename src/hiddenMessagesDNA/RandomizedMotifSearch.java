package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RandomizedMotifSearch {

	public static void main(String[] args) {

		int k, t;
		ArrayList<String> dna = new ArrayList<String>();
		
		
		try(Scanner reader = Utilities.getScanner(args[0])) {
			k = reader.nextInt();
			t = reader.nextInt();
			while(reader.hasNextLine()) {
				String nextLine = reader.nextLine().trim();
				if(nextLine.length() > 0)
					dna.add(nextLine);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		ArrayList<String> motifs = runRandomizedMotifSearch(dna, k, t, 10000);
		
		
		
	}

	
	
	/**
	 * 
	 * @param dna
	 * @param k
	 * @param t
	 * @param n
	 * @return
	 */
	static ArrayList<String> runRandomizedMotifSearch(ArrayList<String> dna, int k, int t, int n) {
		ArrayList<String> bestMotifs = null;
		double bestScore = Double.MAX_VALUE;
		
		
		for(int i=0; i<n; i++) {
			ArrayList<String> motifs = randomizedMotifSearch(dna,k,t);
			double score = Utilities.score(motifs);
			
			if(score < bestScore) {
				bestMotifs = motifs;
				bestScore = score;
			}
				
		}
		
		return bestMotifs;
	}
	
	
	/**
	 * 
	 * @param dna
	 * @param k
	 * @param t
	 * @return
	 */
	static ArrayList<String> randomizedMotifSearch(ArrayList<String> dna, int k, int t) {
		Random randomGen = new Random();
		ArrayList<String> bestMotifs = new ArrayList<String>();
		
		// iterate through dna regions
		// choosing random k-mers
		// as starting motifs set
		for(String dnaRegion : dna) {
			int kMersRange = dnaRegion.length()-k+1;
			int randomIndex = randomGen.nextInt(kMersRange);
			bestMotifs.add(dnaRegion.substring(randomIndex,randomIndex+k));
		}
		
		
		
		//
		while(true){
			float[][] profile = Utilities.createProfile(bestMotifs);
			ArrayList<String> motifs = motifsFromProfile(profile,dna);

			if(Utilities.score(motifs) < Utilities.score(bestMotifs)) {
				bestMotifs = motifs;
			} else {
				return bestMotifs;
			}
		}
	}
	
	
	
	/**
	 * Given profile and collection of dna strings
	 * Find best matching motifs from each dna string (according to profile)
	 * 
	 * 
	 * @param profile 4xk matrix, non-zero entries due to pseudocounts
	 * @param dna
	 */
	static <T extends CharSequence> ArrayList<String> motifsFromProfile(float[][] profile, ArrayList<T> dna) {
		
		int k = profile[0].length;
		ArrayList<String> motifs = new ArrayList<String>();
		
		
		// iterate through dna regions in dna
		for(T dnaRegion : dna) {
			
			String bestMotif = motifFromProfile(profile, dnaRegion);
			motifs.add(bestMotif);
		}
		
		return motifs;
	}
	
	/**
	 * 
	 * @param profile 4xk matrix, non-zero entries due to pseudocounts
	 * @param dna
	 * @return
	 */
	static <T extends CharSequence> String motifFromProfile(float[][] profile, T dna) {
		int k = profile[0].length;

		float bestLikelihood = 0; // all likelihoods will be >0 as non-zero entries in profile
		String motif = new Exception().getStackTrace().toString();
		
		// iterate through k-mers in dna
		for(int index=0; index<=dna.length()-k; index++) {
			
			float likelihood = 1;			
			
			// iterate through letters of k-mer
			for(int i=0; i<k; i++) {
				int letterAsNum = Utilities.letterToNum(dna.charAt(index+i));
				likelihood *= profile[letterAsNum][i];
			}
			
			// see if k-mer is best fit so far
			if(likelihood > bestLikelihood) {
				bestLikelihood = likelihood;
				motif = dna.subSequence(index, index+k).toString();
			}
			
		}
		
		
		
		return motif;
	}
	
}
