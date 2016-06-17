package comparingGenesProteinsGenomes;

import java.util.ArrayList;
import java.util.Scanner;


public class EditDistance {
	
	static boolean debug = false;
	
	
	public static void main(String[] args) {

		

		

		String v, w;
		int vLen, wLen;
		
		
		
		
		// parse v and w from file

		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			v = reader.next();
			w = reader.next();

			vLen = v.length(); wLen = w.length();
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		
		
		
		// s[i][j] represents length of shortest path to node s_i_j
		
		int s[][] = new int[vLen+1][wLen+1];
		
		
		
		
		
		// diagonal[i][j] represents the weight of the diagonal edge going out of s_i_j into s_(i+1)_(j+1)
		
		int diagonal[][] = new int[vLen+1][wLen+1];
		
		
		
		
		
		// initialize diagonal[][] according to v and w
		// diagonal[i][j] = 1 if v_i != w_j
		
		for(int i=0; i<vLen; i++) {
			for(int j=0; j<wLen; j++) {
				if(v.charAt(i) != w.charAt(j))
					diagonal[i][j] = 1;
			}
		}
		
		
		
		
		
		// backtrack[i][j] holds direction of parent node that has the longest path
		
		int[][] backtrack = new int[vLen+1][wLen+1];

		
		
		
		
		
		if(debug) util.IOUtilities.printArray("diagonal",diagonal);
		if(debug) util.IOUtilities.printArray("backtrack",backtrack);
		
		
		
		
		

		
		// initialize uppermost/leftmost row/col of backtrack

		for(int i=0; i<=vLen; i++) backtrack[i][0] = Direction.UP.ordinal();
		for(int j=0; j<=wLen; j++) backtrack[0][j] = Direction.LEFT.ordinal();
		
		
		
		
		// initialize uppermost/leftmost row/col of s
		
		for(int i=1; i<=vLen; i++) s[i][0] = s[i-1][0] + 1;
		for(int j=1; j<=wLen; j++) s[0][j] = s[0][j-1] + 1;
		
		
		
		
		
		// calculate s[][] and backtrack[][]		
		
		for(int i=1; i<=vLen; i++) {
			for(int j=1; j<=wLen; j++) {
				s[i][j] = min(s[i-1][j] + 1, s[i][j-1] + 1, s[i-1][j-1]+diagonal[i-1][j-1]);
				
				
				
				
				if(s[i][j] == s[i-1][j-1]+diagonal[i-1][j-1])
					backtrack[i][j] = Direction.DIAGONAL.ordinal();
				
				
				else if(s[i][j] == s[i][j-1]+1)
					backtrack[i][j] = Direction.LEFT.ordinal();
				
				else if(s[i][j] == s[i-1][j]+1)
					backtrack[i][j] = Direction.UP.ordinal();
				
				else
					new Exception().printStackTrace(System.out);
			}
		}

		
		
		
		
		if(debug) util.IOUtilities.printArray("backtrack",backtrack);
		if(debug) util.IOUtilities.printArray("s", s);
		
		 
		System.out.println("min edit distance " + s[vLen][wLen]);
		
		
		
		
		StringBuilder $v = new StringBuilder();
		StringBuilder $w = new StringBuilder();
		
		mostConservedAlignmentRec(backtrack,v,w,vLen,wLen,$v,$w);
		
		System.out.println($v.toString());
		System.out.println($w.toString());
		
		System.out.println(editDistance($v,$w));
		
	}
	
	
	
	
	

	
	/**
	 * Examines backtrack[i][j] to determine LCS
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
	static void mostConservedAlignmentRec(int[][] backtrack, String v, String w, int i, int j, StringBuilder $v, StringBuilder $w) {
		

		
		// BC source node
		
		if(i==0 && j==0)
			return;
		

		
			
		// RC
		// node with col == 0
		// or go up
		
		if(j==0 || backtrack[i][j]==Direction.UP.ordinal()) {
			
			$v.insert(0, v.charAt(i-1));
			$w.insert(0, '-');
			
			mostConservedAlignmentRec(backtrack,v,w,i-1,j,$v,$w);
			return;
		}
		
		// RC
		// node with row == 0
		// or go left
		
		else if(i==0 || backtrack[i][j]==Direction.LEFT.ordinal()){
			
			$v.insert(0, '-');
			$w.insert(0, w.charAt(j-1));	
			
			mostConservedAlignmentRec(backtrack,v,w,i,j-1,$v,$w);
			return;
		} 
		
		// RC go diagonal
		
		else {
			
			$v.insert(0, v.charAt(i-1));
			$w.insert(0, w.charAt(j-1));

			mostConservedAlignmentRec(backtrack,v,w,i-1,j-1,$v,$w);
			return;
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
	
	static int min (int a, int b, int c) {
		int temp = a < b ? a : b;
		return temp < c ? temp : c;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	static int editDistance(CharSequence a, CharSequence b) {
		int diff = 0;
		
		for(int i=0; i<a.length(); i++) {
			if(a.charAt(i) != b.charAt(i))
				diff++;
		}
		
		return diff;
	}
	

}
