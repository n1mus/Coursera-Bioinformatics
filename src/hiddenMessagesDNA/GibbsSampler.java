package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GibbsSampler {

	public static void main(String[] args) {

	
		
		
		int k, t, N;
		ArrayList<String> dna = new ArrayList<String>();
		
		
		try(Scanner reader = Utilities.getScanner(args[0])) {
			k = reader.nextInt();
			t = reader.nextInt();
			N = reader.nextInt();
			while(reader.hasNext()) {
				dna.add(reader.next());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		double bestScore = Double.MAX_VALUE;
		ArrayList<String> bestMotifs = new ArrayList<String>();
		
		for(int i=0; i<80; i++){
			ArrayList<String> motifs = gibbsSampler(dna, k, t, N);
			double score = Utilities.scoreEntropy(motifs);
			if(score < bestScore) {
				bestScore = score;
				bestMotifs = motifs;
			}
		}
		
		Utilities.printAbstractCollection("motifs", "\n", bestMotifs);
		
		
		
		
		
	}
	
	
	
	
	static ArrayList<String> gibbsSampler(ArrayList<String> dna, int k, int t, int N) {
		Random random = new Random();
		ArrayList<String> bestMotifs = new ArrayList<String>();
		
		// randomly select k-mers Motifs = (Motif1, …, Motift) in each string
        // from Dna
		
		for(String string : dna) {
			int l = string.length();
			int indexRange = l - k + 1;
			int randomIndex = random.nextInt(indexRange);
			String motif = string.substring(randomIndex, randomIndex+k);
			bestMotifs.add(motif);
		}
		

		
		for(int j=1; j<N; j++) {
			ArrayList<String> motifs = new ArrayList<String>(bestMotifs);
			int i = random.nextInt(t);
			String dna_i = motifs.remove(i);
			float[][] profile = Utilities.createProfile(motifs);
			String newDna_i = profileRandomlyGeneratedKMer(profile,dna.get(i));
			motifs.add(i,newDna_i);
			if(Utilities.scoreEntropy(motifs) < Utilities.scoreEntropy(bestMotifs))
				bestMotifs = motifs;
		}
		
		return bestMotifs;
		
	}
	
	
	/**
	 * Gibbs sampler
	 * Determine likelihood of each k-mer in sequence
	 * Randomly select a k-mer according to those probabilities
	 * 
	 * 
	 * @param profile
	 * @param sequence
	 * @return
	 */
	static String profileRandomlyGeneratedKMer(float[][] profile, String sequence) {
		
		Random random = new Random();
		int l = sequence.length();
		int k = profile[0].length;
		int numLikelihoods = l-k+1;
		
		float[] likelihoods = new float[numLikelihoods];
		
		// iterate through dna strand
		// calculate probability of k-mer according to profile
		// populate the likelihoods
		// (can be optimized)
		
		for(int i=0; i<numLikelihoods; i++) {
			likelihoods[i] = likelihoodAt(sequence, i, k, profile);
		}
		

		
		//turn likelihoods into a CDF
		for(int i=1; i<numLikelihoods; i++) {
			likelihoods[i] += likelihoods[i-1];
		}
		
		
		// gibbs sample
		float g = random.nextFloat() * likelihoods[numLikelihoods-1];
		for(int i=0; i<numLikelihoods; i++) {
			if(g <= likelihoods[i]) {
				return sequence.substring(i,i+k);
			}
				
		}
		
		String e = (new Exception()).getStackTrace().toString();
		System.err.println(e);
		return e;
	}
	
	
	/**
	 * Given DNA pattern, compute likelihood of k-mer located at index using
	 * profile
	 * 
	 * 
	 * 
	 * @param pattern
	 * @param index
	 * @param k
	 * @param profile
	 * @return
	 */
	static float likelihoodAt(String pattern, int index, int k, float[][] profile) {
		float likelihood = 1;
		
		for(int i=0; i<k; i++) {
			char letter = pattern.charAt(index+i);
			int letterAsNum = Utilities.letterToNum(letter);
			likelihood *= profile[letterAsNum][i];
		}
		
		return likelihood;
	}

}
