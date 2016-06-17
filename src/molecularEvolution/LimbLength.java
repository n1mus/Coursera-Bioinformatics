package molecularEvolution;

import java.util.Scanner;

public class LimbLength {

	public static void main(String[] args) {

		int n, j;
		int[][] distanceMatrix;
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			n = reader.nextInt();
			j = reader.nextInt();
			
			distanceMatrix = new int[n][n];
			
			for(int i=0; i<n; i++) {
				for(int k=0; k<n; k++) {
					distanceMatrix[i][k] = reader.nextInt();
				}
			}

			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		int min = Integer.MAX_VALUE;
		
		
		
		
		// iterate through all tri-differing leaf combinations
		
		for(int i=0; i<n; i++) {
			
			if(i==j) continue;
			
			for(int k=0; k<i; k++) {
				
				if(k==j) continue;
					
					
				int D_i_j = distanceMatrix[i][j];
				int D_j_k = distanceMatrix[j][k];
				int D_i_k = distanceMatrix[i][k];
				
				int candidateLimbLength = (D_i_j + D_j_k - D_i_k) / 2;
				
				if(candidateLimbLength < min) min = candidateLimbLength;
					
				
				
			}
		}
		
		System.out.println(min);
		
	}

}
