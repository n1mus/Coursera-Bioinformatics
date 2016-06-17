package comparingGenesProteinsGenomes;

import java.util.Scanner;


// repurposed from FittingAlignment

public class OverlapAlignment {

	static boolean debug = false;
	
	
	
	
	public static void main(String[] args) {

		int mismatchIndelPenalty = 2;
		
		
		String v, w;
		int vLen, wLen;
		
		// parse file for words to find alignment for
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			v = reader.next();
			w = reader.next();

			vLen = v.length(); wLen = w.length();
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	

		if(debug) System.out.println(v);
		if(debug) System.out.println(w);
		
		
		
		
		
		
		
		
		
		
		
		// s[i][j] represents length of longest path to node s_i_j
		int s[][] = new int[vLen+1][wLen+1];
		
		
		// diagonal[i][j] represents the weight of the diagonal edge going out of s_i_j into s_(i+1)_(j+1)
		int diagonal[][] = new int[vLen+1][wLen+1];
		
		
		
		// initialize diagonal[][] according to v, w, and scores/penalties
		
		for(int i=0; i<vLen; i++) {
			for(int j=0; j<wLen; j++) {
				
				diagonal[i][j] = v.charAt(i)==w.charAt(j) ? 1 : mismatchIndelPenalty * -1;
				
			}
		}
		
		
		if(debug) util.IOUtilities.printArray("diagonal",diagonal,3);
		
		
		
		
		
		
		
		
		
		
		
		
		
		// backtrack[i][j] holds direction of parent node that has the longest path
		int[][] backtrack = new int[vLen+1][wLen+1];
		
		
		
		
		
		////////////////////////////////////////
		
		// calculate s[][]
		// s[i][j] represents length of longest path to node s_i_j
		
		
		// leave leftmost column of s[][] as 0's
		// "Free taxi rides"

		
		// top row of s[][]
		
		for(int j=1; j<=wLen; j++) {
			s[0][j] = s[0][j-1] - mismatchIndelPenalty;
		}
		
		
		// general case for s[][]
		
		for(int i=1; i<vLen; i++) {
			for(int j=1; j<=wLen; j++) {
				s[i][j] = max(s[i-1][j] - mismatchIndelPenalty, s[i][j-1] - mismatchIndelPenalty, s[i-1][j-1]+diagonal[i-1][j-1]);
				
				
				
				if(s[i][j] == s[i-1][j-1]+diagonal[i-1][j-1])
					backtrack[i][j] = Direction.DIAGONAL.ordinal();
				
				
				
				else if(s[i][j] == s[i][j-1]-mismatchIndelPenalty)
					backtrack[i][j] = Direction.LEFT.ordinal();
				else if(s[i][j] == s[i-1][j]-mismatchIndelPenalty)
					backtrack[i][j] = Direction.UP.ordinal();
				else 
					new Exception().printStackTrace(System.out);
			}
		}
	
		
		// "Free taxi rides" along bottom row of s[][]
		
		for(int j=1, i=vLen; j<=wLen; j++) {
			s[i][j] = max(s[i-1][j] - mismatchIndelPenalty, s[i][j-1], s[i-1][j-1]+diagonal[i-1][j-1]);
			
			if(s[i][j] == s[i-1][j-1]+diagonal[i-1][j-1])
				backtrack[i][j] = Direction.DIAGONAL.ordinal();
			
			
			else if(s[i][j] == s[i][j-1])
				backtrack[i][j] = Direction.LEFT.ordinal();
			else if(s[i][j] == s[i-1][j]-mismatchIndelPenalty)
				backtrack[i][j] = Direction.UP.ordinal();
			else
				new Exception().printStackTrace(System.out);
				
		}
		
		
		
		
		
		
		
		
		
		
		if(debug) util.IOUtilities.printArray("backtrack",backtrack);
		if(debug) util.IOUtilities.printArray("s", s,3);
		
	
		
		
		
		
		
		StringBuilder $v = new StringBuilder();
		StringBuilder $w = new StringBuilder();
		
		computeLCSRec(backtrack,v,w,vLen,wLen,$v,$w);
	
		
		System.out.println(s[vLen][wLen]);
		System.out.println($v.toString());
		System.out.println($w.toString());
		
	}







	
	/**
	 * Examines s[i][j] and backtrack[i][j]
	 * 
	 * 
	 * @param backtrack
	 * @param v the alignment word that corresponds to the rows
	 * @param w the alignment word that corresponds to the cols
	 * @param i row
	 * @param j col
	 * @param $v (payload) alignment formatted v
	 * @param $w (payload) alignment formatted w
	 * 
	 */
	static void computeLCSRec(int[][] backtrack, String v, String w, int i, int j, StringBuilder $v, StringBuilder $w) {
		
		// BC source node
		
		if(i==0 && j==0)
			return;
		
	
		
			
		// RC node with col == 0
		// or go up
		
		if(j==0 || backtrack[i][j]==Direction.UP.ordinal()) {
			
			
			if (j!=0) { // if it's the first column, don't add any letters; just a free taxi ride
				
				$v.insert(0, v.charAt(i-1));
				$w.insert(0, '-');
			}
			
			computeLCSRec(backtrack,v,w,i-1,j,$v,$w);
			
			return;
		}
		
		// RC node with row == 0
		// or go left
		
		else if(i==0 || backtrack[i][j]==Direction.LEFT.ordinal()){
			
			if(i != v.length()) // if it's the last row, don't add any characters; just a free taxi ride
			{
				$v.insert(0, '-');
				$w.insert(0, w.charAt(j-1));	
			}
			computeLCSRec(backtrack,v,w,i,j-1,$v,$w);
			return;
		} 
		
		// RC go diagonal
		
		else {
			
			$v.insert(0, v.charAt(i-1));
			$w.insert(0, w.charAt(j-1));
	
			computeLCSRec(backtrack,v,w,i-1,j-1,$v,$w);
			return;
		}
		
	}



	
	static int getScore(char firstAALetter, char secondAALetter, char[] aminoAcidLetters, int[][] scoringMatrix) {
		
		int firstIndex = aminoAcidLetterToIndex(firstAALetter,aminoAcidLetters);
		int secondIndex = aminoAcidLetterToIndex(secondAALetter,aminoAcidLetters);
		
		return scoringMatrix[firstIndex][secondIndex];
	}
	
	static int aminoAcidLetterToIndex(char letter, char[] aminoAcidLetters) {
		for(int i=0; i<aminoAcidLetters.length; i++) {
			if(letter==aminoAcidLetters[i])
				return i;
		}
		
		new Exception().printStackTrace(System.err);
		return -1; // error
	}
	
	enum Direction {
		UP,
		LEFT,
		DIAGONAL,
		STAY;
	}
	
	static int max (int a, int b, int c) {
		int temp = a > b ? a : b;
		return temp > c ? temp : c;
	}

}