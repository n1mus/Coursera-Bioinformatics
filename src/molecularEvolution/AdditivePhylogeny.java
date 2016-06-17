package molecularEvolution;

import java.util.Scanner;
import util.SimpleTree;;


public class AdditivePhylogeny {

	static boolean debug = false;
	
	
	public static void main(String[] args) {


		int n;
		int[][] distanceMatrix;
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			n = reader.nextInt();
			
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
		
		
		
		
		SimpleTree tree = additivePhylogenyRec(distanceMatrix, n);
		tree.print();

		
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param distanceMatrix size n x n, where n is the number of leaves left
	 * 			gets progressively narrowed down by 1 x 1 each call
	 * @param n remaining number of leaves to deal with
	 * @return
	 */
	static SimpleTree additivePhylogenyRec(int[][] distanceMatrix, int n) {
		
		
		
		// BC only two nodes left
		
		if( n == 2) {
			
			SimpleTree tree = new SimpleTree(distanceMatrix[0][1],distanceMatrix.length);
			if(debug) tree.print();
			
			return tree;
		}
		
		// RC
		
		
		int j = n-1; // index of current leaf 
					// use last leaf in distanceMatrix
		
		
		// calculate limb length of leaf
		
		int limbLength = limbLength(n, distanceMatrix);
		
		
		
		
		// subtract limb length from all distances to leaf j
		
		for(int i=0; i<n; i++) {
			if (i == j) continue; // skip the diagonal of 0s
			
			distanceMatrix[i][j] -= limbLength;
			distanceMatrix[j][i] = distanceMatrix[i][j];
		}
		

		
		
		// (i,j,k) <- three different leaves such that D_i,k = D_i,j + D_j,k
		
		
		int x = -1;
		
		int iId = -1, kId = -1;
		
		for(int i=0; i<j; i++) {
			for(int k=0; k<i; k++) {
				if(distanceMatrix[i][k] == distanceMatrix[i][j] + distanceMatrix[j][k]) {
					x = distanceMatrix[i][j];
					iId = i;
					kId = k;
				}
			}
		}
		
		if(x == -1) new Exception().printStackTrace();
		if(iId == -1) new Exception().printStackTrace();
		if(kId == -1) new Exception().printStackTrace();
		
		
		
		
		
		
		//
		
		SimpleTree tree = additivePhylogenyRec(distanceMatrix,n-1);
		
		
		if(debug) {
			tree.print();
			util.IOUtilities.printArray("distance matrix", distanceMatrix, 3);		
			System.out.println("i " + iId);
			System.out.println("k " + kId);
			System.out.println("x " + x);
			System.out.println("limb length " + limbLength);
		}
		
		
		// find path between nodes i and k
		// find/insert new node v length x after node i
		
		int vId = tree.insertNode(iId, kId, x);

		
		// attach node j to node v with limb length
		
		tree.attachLeafNode(j, vId, limbLength);
		
		
		
		return tree;
	}
	

	
	
	
	
	
	
	
	/**
	 * Given a distance matrix, find the limb length of last leaf in matrix
	 * 
	 * 
	 * 
	 * 
	 * @param n number of leaves represented by distanceMatrix
	 * @param j index of leaf that you're computing limb length for
	 * @param distanceMatrix of size n x n
	 * @return
	 */
	static int limbLength(int n, int[][] distanceMatrix) {
		int limbLength = Integer.MAX_VALUE;
		
		int j = n-1;
		
		
		
		// iterate through all leaf combinations with 3 differing leaves
		
		for(int i=0; i<n; i++) {
			
			if(i==j) continue;
			
			for(int k=0; k<i; k++) {
				
				if(k==j) continue;
					
					
				int D_i_j = distanceMatrix[i][j];
				int D_j_k = distanceMatrix[j][k];
				int D_i_k = distanceMatrix[i][k];
				
				int candidateLimbLength = (D_i_j + D_j_k - D_i_k) / 2;
				
				if(candidateLimbLength < limbLength) limbLength = candidateLimbLength;
					
				
				
			}
		}
		
		return limbLength;
	}
	
	
	
	

}
