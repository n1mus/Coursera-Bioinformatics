package comparingGenesProteinsGenomes;

import java.util.Scanner;



public class LocalAlignment {
	
	static boolean debug = false;
	
	
	public static void main(String[] args) {
	

		int NUM_A_A = 20;
		
		char[] aminoAcidLetters = new char[NUM_A_A];
		int[][] scoringMatrix = new int[NUM_A_A][NUM_A_A];
		int indelPenalty = -5;

		// parse file for scoringMatrix
		
		try(Scanner reader = util.IOUtilities.getScanner(args[1])) {
			
			String[] aminoAcidLettersStr = reader.nextLine().trim().split("\\s+");
			
			
			for(int i=0; i<NUM_A_A; i++) {
				aminoAcidLetters[i] = aminoAcidLettersStr[i].charAt(0);
			}
			
			for (int aARow=0; aARow<NUM_A_A; aARow++) {
				
				
				String nextLine = reader.nextLine().trim();
				
				String[] scores = nextLine.split("\\s+");
				
				
				
				for( int aACol=0; aACol<NUM_A_A; aACol++) {
				

					scoringMatrix[aARow][aACol] = Integer.parseInt(scores[aACol+1]);
					
					
				}
				
				
			}

			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if(debug) util.IOUtilities.printArray("amino acid letters", aminoAcidLetters);
		if(debug) util.IOUtilities.printArray("scoring matrix", scoringMatrix, 3);
	
		
		
		
		
		
		
		String v, w;
		int vLen, wLen;
		
		// parse file for words to find LCS for
		
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
		
		
		
		// initialize diagonal[][] according to v, w, and scoring matrix
		
		for(int i=0; i<vLen; i++) {
			for(int j=0; j<wLen; j++) {
				
				diagonal[i][j] = getScore(v.charAt(i),w.charAt(j),aminoAcidLetters,scoringMatrix);
				
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// backtrack[i][j] holds direction of parent node that has the longest path
		int[][] backtrack = new int[vLen+1][wLen+1];
		
		
		
		if(debug) util.IOUtilities.printArray("diagonal",diagonal,3);
		
		
		// calculate s[][]
		// s[i][j] represents length of longest path to node s_i_j
		
		for(int i=1; i<=vLen; i++) {
			s[i][0] = s[i-1][0] + indelPenalty;
		}
		
		for(int j=1; j<=wLen; j++) {
			s[0][j] = s[0][j-1] + indelPenalty;
		}
		
		for(int i=1; i<=vLen; i++) {
			for(int j=1; j<=wLen; j++) {
				s[i][j] = max(0, s[i-1][j] + indelPenalty, s[i][j-1] + indelPenalty, s[i-1][j-1]+diagonal[i-1][j-1]);
				
				if(s[i][j] == 0)
					backtrack[i][j] = Backtrack.SOURCE.ordinal();
				
				else if(s[i][j] == s[i-1][j]+indelPenalty)
					backtrack[i][j] = Backtrack.UP.ordinal();
				
				else if(s[i][j] == s[i][j-1]+indelPenalty)
					backtrack[i][j] = Backtrack.LEFT.ordinal();
				
				else
					backtrack[i][j] = Backtrack.DIAGONAL.ordinal();
			}
		}

		
		if(debug) util.IOUtilities.printArray("backtrack",backtrack);
		if(debug) util.IOUtilities.printArray("s", s,3);
		

		int indices[] = argmax(s);
		int i = indices[0], j = indices[1];
		
		
		StringBuilder $v = new StringBuilder(); // LCS from v
		StringBuilder $w = new StringBuilder(); // LCS from w
		
		computeLCSRec(backtrack,v,w,i,j,$v,$w);

		
		System.out.println(s[i][j]);
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
		
		
		// BC skip to source node
		
		if( backtrack[i][j] == Backtrack.SOURCE.ordinal() )
			return;
		
			
		// RC node with col == 0
		// or go up
		
		if(j==0 || backtrack[i][j]==Backtrack.UP.ordinal()) {
			
			$v.insert(0, v.charAt(i-1));
			$w.insert(0, '-');
			
			computeLCSRec(backtrack,v,w,i-1,j,$v,$w);
			return;
		}
		
		// RC node with row == 0
		// or go left
		
		else if(i==0 || backtrack[i][j]==Backtrack.LEFT.ordinal()){
			
			$v.insert(0, '-');
			$w.insert(0, w.charAt(j-1));	
			
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
	
	
	/**
	 * For backtrack[][]
	 * backtrack[][] holds the direction of the node with the longest path
	 *
	 */
	enum Backtrack {
		UP, // go to above node
		LEFT, // go to left node
		DIAGONAL, // go to diagonally previous node
		SOURCE; // skip to source node
	}
	
	static int max (int a, int b, int c, int d) {
		int temp = a > b ? a : b;
		int temp2 = temp > c ? temp : c;
		return temp2 > d ? temp2 : d;
	}
	
	/**
	 * 
	 * @param s
	 * @return int[2] holding indices i and j that maximize s[][]
	 */
	static int[] argmax(int[][] s) {
		
		int indices[] = new int[2];
		int max = Integer.MIN_VALUE;
		
		for(int i=0; i<s.length; i++) {
			for(int j=0; j<s[0].length; j++) {
				if(s[i][j] > max) {
					max = s[i][j];
					indices[0] = i;
					indices[1] = j;
				}
			}
		}
		
		return indices;
	}
}