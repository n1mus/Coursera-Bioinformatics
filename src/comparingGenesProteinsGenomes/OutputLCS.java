package comparingGenesProteinsGenomes;

import java.util.ArrayList;
import java.util.Scanner;

public class OutputLCS {

	public static void main(String[] args) {

		
		final boolean DEBUG = false;
		

		String v = "CTCGAT", w = "TACGTC";
		int vLen = v.length(), wLen = w.length();
		
		
		

		
		
		
		
		
		// s[i][j] represents length of longest path to node s_i_j
		int s[][] = new int[vLen+1][wLen+1];
		
		
		// diagonal[i][j] represents the weight of the diagonal edge going out of s_i_j into s_(i+1)_(j+1)
		int diagonal[][] = new int[vLen+1][wLen+1];
		
		
		
		// initialize diagonal[][] according to v and w
		// diagonal[i][j] = 1 if v_i == w_i
		for(int i=0; i<vLen; i++) {
			for(int j=0; j<wLen; j++) {
				if(v.charAt(i) == w.charAt(j))
					diagonal[i][j] = 1;
			}
		}
		
		// backtrack[i][j] holds direction of parent node that has the longest path
		int[][] backtrack = new int[vLen+1][wLen+1];

		if(DEBUG) util.IOUtilities.printArray("diagonal",diagonal);
		if(DEBUG) util.IOUtilities.printArray("backtrack",backtrack);
		
		
		// calculate s[][]
		
		for(int i=1; i<=vLen; i++) {
			for(int j=1; j<=wLen; j++) {
				s[i][j] = max(s[i-1][j], s[i][j-1], s[i-1][j-1]+diagonal[i-1][j-1]);
				
				if(s[i][j] == s[i-1][j])
					backtrack[i][j] = Direction.UP.ordinal();
				
				else if(s[i][j] == s[i][j-1])
					backtrack[i][j] = Direction.LEFT.ordinal();
				else
					backtrack[i][j] = Direction.DIAGONAL.ordinal();
			}
		}

		
		if(DEBUG) util.IOUtilities.printArray("backtrack",backtrack);
		if(DEBUG) util.IOUtilities.printArray("s", s);
		
		System.out.println(outputLCS(backtrack,v,vLen,wLen));
		 
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param backtrack
	 * @param v the alignment word that corresponds to the rows
	 * @param i row
	 * @param j col
	 * @return
	 */
	static String outputLCS(int[][] backtrack, String v, int i, int j) {
		if(i==0 || j==0)
			return "";
		
		if(backtrack[i][j] == Direction.UP.ordinal()) {
			return outputLCS(backtrack,v,i-1,j);
		} else if( backtrack[i][j] == Direction.LEFT.ordinal()){
			return outputLCS(backtrack,v,i,j-1);
		} else {
			return outputLCS(backtrack,v,i-1,j-1) + v.charAt(i-1);
		}
		
	}
	
	
	
	
	
	
	enum Direction {
		UP,
		LEFT,
		DIAGONAL;
	}
	
	static int max (int a, int b, int c) {
		int temp = a > b ? a : b;
		return temp > c ? temp : c;
	}
	

}
