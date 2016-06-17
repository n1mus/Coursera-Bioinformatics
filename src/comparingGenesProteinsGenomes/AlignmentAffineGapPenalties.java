package comparingGenesProteinsGenomes;

import java.util.Scanner;
import comparingGenesProteinsGenomes.GlobalAlignment.Direction;



public class AlignmentAffineGapPenalties {
	
	
	
	
	
	
	
	
	
	
	
	
	static boolean debug = false;
	
	static int NUM_A_A = 20;
	static char[] aminoAcidLetters = new char[NUM_A_A];
	static int[][] scoringMatrix = new int[NUM_A_A][NUM_A_A];
	
	
	
	static String v, w;
	static int vLen, wLen;
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		

		
		

		// parse file for scoringMatrix
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
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
	
		
		
		
		
		
		
		
		
		
		
		
		// parse file for words to find alignment for
		
		try(Scanner reader = util.IOUtilities.getScanner(args[1])) {
			
			v = reader.next();
			w = reader.next();

			vLen = v.length(); wLen = w.length();
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		

		if(debug) System.out.println(v);
		if(debug) System.out.println(w);
		
		
		
		
		
		
		
		
		for(int gapOpeningPenalty = 5; gapOpeningPenalty <= 15; gapOpeningPenalty += 2) {
			for(int gapExtensionPenalty = 1; gapExtensionPenalty < gapOpeningPenalty; gapExtensionPenalty += 2) {

				System.out.println("\ngap opening penalty: " + gapOpeningPenalty);
				System.out.println("gap extension penalty: " + gapExtensionPenalty);
				
				alignmentAffineGapPenalties(gapOpeningPenalty, gapExtensionPenalty);
				
			}
		}
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param gapOpeningPenalty
	 * @param gapExtensionPenalty
	 */
	static void alignmentAffineGapPenalties(int gapOpeningPenalty, int gapExtensionPenalty) {

		
		// score[i][j] represents the weight of the score edge going out of middle_ij into middle_(i+1)(j+1)
		int score[][] = new int[vLen+1][wLen+1];
		
		
		
		// initialize score[][] according to v, w, and scoring matrix
		
		for(int i=0; i<vLen; i++) {
			for(int j=0; j<wLen; j++) {
				
				score[i][j] = getScore(v.charAt(i),w.charAt(j));
				
			}
		}		
		

		
		
		
		
		
		

		
		
		// backtrack[i][j] holds direction of parent node that has the longest path
		int[][][] backtrack = new int[3][vLen+1][wLen+1];
		
		int[][] backtrackLower = backtrack[Level.LOWER.ordinal()];
		int[][] backtrackMiddle = backtrack[Level.MIDDLE.ordinal()];
		int[][] backtrackUpper = backtrack[Level.UPPER.ordinal()];
		
		

		
		
		
		
		
		

		int s[][][] = new int[3][vLen+1][wLen+1];
		
		int[][] lower = s[Level.LOWER.ordinal()]; // alignment graph with arrows going right, meaning inserting w characters
		int[][] middle = s[Level.MIDDLE.ordinal()];
		int[][] upper = s[Level.UPPER.ordinal()]; // alignment graph with arrows going down, meaning inserting v characters
		
		
		
		

		
		
		
		// initialize left edge of upper, top edge of lower
		// to min values, since they're unreachable
		
		
		
		for(int i=1; i<=vLen; i++) {
			upper[i][0] = Integer.MIN_VALUE / 2;
			backtrackUpper[i][0] = Direction.NONE.ordinal();
		}
		
		for(int j=1; j<=wLen; j++) {
			lower[0][j] = Integer.MIN_VALUE / 2;
			backtrackLower[0][j] = Direction.NONE.ordinal();
		}
		
		
		// initialize top edge of upper, left edge of lower
		// to -Sigma - (k-1)*epsilon
		
		upper[0][1] = -1 * gapOpeningPenalty;
		for(int j=2; j<=wLen; j++) {
			upper[0][j] = upper[0][j-1] - gapExtensionPenalty;
			backtrackUpper[0][j] = Direction.WEST.ordinal();
		}
		
		lower[1][0] = -1* gapOpeningPenalty;
		for(int i=2; i<=vLen; i++) {
			lower[i][0] = lower[i-1][0] - gapExtensionPenalty;
			backtrackLower[i][0] = Direction.NORTH.ordinal();
		}
		
		

		
		// initialize middle[][] top and left edges to match upper[][] and lower[][] (except for middle[0][0])
		
		for(int i=1; i<=vLen; i++) {
			middle[i][0] = lower[i][0];
			backtrackMiddle[i][0] = Direction.DOWN.ordinal();
		}
		
		for(int j=1; j<=wLen; j++) {
			middle[0][j] = upper[0][j];
			backtrackMiddle[0][j] = Direction.UP.ordinal();
		}
		
		
		
		

		
		
		
		// calculate the rest of the s[][][] matrices
		
		
		for(int i=1; i<=vLen; i++) {
			
			for(int j=1; j<=wLen; j++) {
				
				
				lower[i][j] = max(lower[i-1][j] - gapExtensionPenalty, middle[i-1][j] - gapOpeningPenalty);
				
				if(lower[i][j] == lower[i-1][j] - gapExtensionPenalty) {
					backtrackLower[i][j] = Direction.NORTH.ordinal();
				} else
				if(lower[i][j] == middle[i-1][j] - gapOpeningPenalty) {
					backtrackLower[i][j] = Direction.UP.ordinal();
				}
				
				
				
				upper[i][j] = max(upper[i][j-1] - gapExtensionPenalty, middle[i][j-1] - gapOpeningPenalty);
				
				if(upper[i][j] == upper[i][j-1] - gapExtensionPenalty) {
					backtrackUpper[i][j] = Direction.WEST.ordinal();
				} else
				if(upper[i][j] == middle[i][j-1] - gapOpeningPenalty) {
					backtrackUpper[i][j] = Direction.DOWN.ordinal();
				}
				
				
				
				middle[i][j] = max(middle[i-1][j-1] + score[i-1][j-1], lower[i][j], upper[i][j]);
				
				if(middle[i][j] == lower[i][j]) {
					backtrackMiddle[i][j] = Direction.DOWN.ordinal();
				} else
				if(middle[i][j] == middle[i-1][j-1] + score[i-1][j-1]) {
					backtrackMiddle[i][j] = Direction.DIAGONAL.ordinal();
				} else
				if(middle[i][j] == upper[i][j]) {
					backtrackMiddle[i][j] = Direction.UP.ordinal();
				}
				
			}
			
			
			
		}
		
		
		
		if(debug) util.IOUtilities.printArray("s (lower/middle/upper)", s);
		if(debug) util.IOUtilities.printArray("backtrack (lower/middle/upper)", backtrack);
		
		
		System.out.println(s[1][vLen][wLen]);
		
		StringBuilder $v = new StringBuilder(), $w = new StringBuilder();
		
		backTrace(vLen, wLen, Level.MIDDLE.ordinal(), backtrack, v, w, $v, $w);

		System.out.println($v);
		System.out.println($w);
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * @param i
	 * @param j
	 * @param level
	 * @param backtrack
	 * @param v
	 * @param w
	 * @param $v
	 * @param $w
	 */
	static void backTrace(int i, int j, int level, int[][][] backtrack, String v, String w, StringBuilder $v, StringBuilder $w) {
		
		// BC
		
		if(i==0 && j==0 && level==Level.MIDDLE.ordinal()) {
			return;
		}
		
		
		//RC
		
		
		if(level == Level.LOWER.ordinal()) {
			
			$v.insert(0, v.charAt(i-1));
			$w.insert(0, '-');
			
			if(backtrack[Level.LOWER.ordinal()][i][j] == Direction.NORTH.ordinal()) {
				backTrace(i-1, j, Level.LOWER.ordinal(), backtrack, v, w, $v, $w);
			} else
			if(backtrack[Level.LOWER.ordinal()][i][j] == Direction.UP.ordinal()) {
				backTrace(i-1, j, Level.MIDDLE.ordinal(), backtrack, v, w, $v, $w);
			}
			
			return;
			
			
		} else
			
		if(level == Level.UPPER.ordinal()) {
			
			$v.insert(0, '-');
			$w.insert(0, w.charAt(j-1));
			
			if(backtrack[Level.UPPER.ordinal()][i][j] == Direction.WEST.ordinal()) {
				backTrace(i, j-1, Level.UPPER.ordinal(), backtrack, v, w, $v, $w);
			} else
			if(backtrack[Level.UPPER.ordinal()][i][j] == Direction.DOWN.ordinal()) {
				backTrace(i, j-1, Level.MIDDLE.ordinal(), backtrack, v, w, $v, $w);
			}
			
			return;
			
		} else
			
		if(level == Level.MIDDLE.ordinal()) {
			
			if(backtrack[Level.MIDDLE.ordinal()][i][j] == Direction.UP.ordinal()) {
				backTrace(i, j, Level.UPPER.ordinal(), backtrack, v, w, $v, $w);
			} else
			if(backtrack[Level.MIDDLE.ordinal()][i][j] == Direction.DOWN.ordinal()) {
				backTrace(i, j, Level.LOWER.ordinal(), backtrack, v, w, $v, $w);
			} if(backtrack[Level.MIDDLE.ordinal()][i][j] == Direction.DIAGONAL.ordinal()) {
				$v.insert(0, v.charAt(i-1));
				$w.insert(0, w.charAt(j-1));
				backTrace(i-1, j-1, Level.MIDDLE.ordinal(), backtrack, v, w, $v, $w);
			}
			
			return;
			
		}
		
		
		
		// error
		new Exception().printStackTrace(System.out);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static int getScore(char firstAALetter, char secondAALetter) {
		
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
	 * For backtrack[][] matrices
	 * 
	 */
	enum Direction {
		NORTH,
		WEST,
		DIAGONAL,
		UP,
		DOWN,
		NONE; // for unreachable nodes, no direction to backtrack
	}
	
	/**
	 * Levels in ascending order
	 *
	 */
	enum Level {
		LOWER,
		MIDDLE,
		UPPER;
	}
	
	
	
	
	
	
	
	
	static int max (int a, int b, int c) {
		int temp = a > b ? a : b;
		return temp > c ? temp : c;
	}

	static int max(int a, int b) {
		return a > b ? a : b;
	}
	
	
	
	
	
	
	
	
	
}
