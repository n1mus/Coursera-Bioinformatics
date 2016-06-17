package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Scanner;

public class GreedyMotifSearch {

	
	
	public static void main(String[] args) {
		int k, t;
		ArrayList<String> dna = new ArrayList<String>();
		
		
		try(Scanner reader = Utilities.getScanner(args[0])) {
			k = reader.nextInt();
			t = reader.nextInt();
			while(reader.hasNext()) {
				dna.add(reader.next());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		ArrayList<String> motifs = greedyMotifSearch(dna, k, t);
		Utilities.printAbstractCollection("motifs", "\n", motifs);
		
		
	}
	
	
	static ArrayList<String> greedyMotifSearch(ArrayList<String> dna, int k, int t) {
		
		int bestScore = Integer.MAX_VALUE;
		ArrayList<String> bestMotifs = new ArrayList<String>();
		String dna1 = dna.get(0);
		int len = dna1.length();
		
		
		// iterate through dna1
		// to start with different k-mers in dna1
		for (int i=0; i<=len-k; i++ ) {
			ArrayList<String> motifs = new ArrayList<String>();
			String motif1 = dna1.substring(i,i+k);
			motifs.add(motif1);
			float[][] profile = Utilities.createProfile(motifs);
			
			// iterate through dna2 through dna_t
			// to add the best fitting k-mer to motifs+profile
			for(int j=1; j<t; j++) {
				String dna_j = dna.get(j);
				String motif_j = Utilities.profileMostProbableKMer(dna_j, k, profile);
				motifs.add(motif_j);
				profile = Utilities.createProfile(motifs);
			}
			
			int score = Utilities.score(motifs);
			if(score < bestScore) {
				bestScore = score;
				bestMotifs = motifs;
			}
		}
		
		return bestMotifs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static ArrayList<String> greedyMotifSearchNoPseudoCounts(ArrayList<String> dna, int k, int t) {
		
		int bestScore = Integer.MAX_VALUE;
		ArrayList<String> bestMotifs = new ArrayList<String>();
		String dna1 = dna.get(0);
		int len = dna1.length();
		
		
		// iterate through dna1
		// to start with different k-mers in dna1
		for (int i=0; i<=len-k; i++ ) {
			ArrayList<String> motifs = new ArrayList<String>();
			String motif1 = dna1.substring(i,i+k);
			motifs.add(motif1);
			float[][] profile = createProfileNoPseudoCounts(motifs);
			
			// iterate through dna2 through dna_t
			// to add the best fitting k-mer to motifs+profile
			for(int j=1; j<t; j++) {
				String dna_j = dna.get(j);
				String motif_j = Utilities.profileMostProbableKMer(dna_j, k, profile);
				motifs.add(motif_j);
				profile = createProfileNoPseudoCounts(motifs);
			}
			
			int score = Utilities.score(motifs);
			if(score < bestScore) {
				bestScore = score;
				bestMotifs = motifs;
			}
		}
		
		return bestMotifs;
	}
	
	
	
	
	
	static <T extends CharSequence> float[][] createProfileNoPseudoCounts(ArrayList<T> motifs) {
		
		// lenth of k-mers
		int k = motifs.get(0).length();
		
		float[][] profile = new float[4][k];
		
		
		// iterate through motifs
		for(CharSequence motif : motifs) {
			
			// iterate through letters in motif
			for(int index=0; index<motif.length(); index++) {
				int letterAsNum = Utilities.letterToNum(motif.charAt(index));
				profile[letterAsNum][index]++;
			}
			
		}
		
		
		// iterate through profile,
		// factor in denominator
		float denominator =  motifs.size()*k;
		for(int i=0; i<4; i++)
			for(int j=0; j<k; j++) {
				profile[i][j] /= denominator;
			}
	
		
		
		
		return profile;
	}
	
}
