package molecularEvolution;

import java.util.ArrayList;
import java.util.Scanner;

import util.SimpleTree;

public class NeighborJoining {

	static boolean debug = false;
	static int numNodes;
	
	public static void main(String[] args) {
		
		
		int numLeaves;
		double[][] distanceMatrix;
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			numNodes = numLeaves = reader.nextInt();
			
			distanceMatrix = new double[numLeaves][numLeaves];
			
			for(int i=0; i<numLeaves; i++) {
				for(int k=0; k<numLeaves; k++) {
					distanceMatrix[i][k] = reader.nextInt();
				}
			}

			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		// create distMatrixNodeIds
		
		ArrayList<Integer> distMatrixNodeIds = new ArrayList<Integer>();
		
		for(int i=0; i<numLeaves; i++) {
			distMatrixNodeIds.add(new Integer(i));
		}
		
		SimpleTree tree = neighborJoining(distanceMatrix,distMatrixNodeIds);
		
		tree.print();
		
		
		
	}
	
	
	
	public static SimpleTree neighborJoining(double[][] distMatrix, ArrayList<Integer> distMatrixNodeIds) {
		
		int n = distMatrix.length;
		
		if(debug) System.out.println("distMatrixNodeIds size: " + distMatrixNodeIds.size());
		
		// BC
		
		if(n==2) {
			int firstId = distMatrixNodeIds.get(0);
			int secondId = distMatrixNodeIds.get(1);
			
			SimpleTree tree = new SimpleTree(firstId, secondId, distMatrix[0][1]);
			return tree;
		}
		
		
		
		
		// RC
		
		
		
		
		double[][] neighborJoiningMatrix = getNeighborJoiningMatrix(distMatrix);
		int[] i_j_Ind = argmin(neighborJoiningMatrix);

		int iInd = i_j_Ind[0];
		int jInd = i_j_Ind[1];

		int i = distMatrixNodeIds.get(iInd);
		int j = distMatrixNodeIds.get(jInd);
		
		double[] limbLengths = new double[2];
		
		getLimbLengths(distMatrix,iInd,jInd,limbLengths );
		
		int m = numNodes++;
		
		double[][] newDistMatrix = new double[n-1][n-1];
		
		calcUpdatedDistanceMatrix(distMatrix, distMatrixNodeIds, i_j_Ind, m, newDistMatrix);
		
		
		SimpleTree tree = neighborJoining(newDistMatrix, distMatrixNodeIds);

		
		if(debug) System.out.println("m is " + m);
 
		tree.attachLeafNode(i, m, limbLengths[0]);
		tree.attachLeafNode(j, m, limbLengths[1]);
		
		return tree;
	}
	
	
	
	
	
	/**
	 * 
	 * @param distMatrix min dim, square matrix
	 * @return
	 */
	public static double[][] getNeighborJoiningMatrix(double[][] distMatrix) {
		int dim = distMatrix.length;
		
		double[][] neighborJoiningMatrix = new double[dim][dim];
		double[] totalDistance = new double[dim];
		
		
		// calculate totalDistance
		
		for(int i=0; i<dim; i++) {
			totalDistance[i] = sumArray(distMatrix[i]);
		}
		
		
		// calculate neigherJoiningMatrix
		
		for(int i=0; i<dim; i++) {
			for(int j=0; j<i; j++) {
				
				
				neighborJoiningMatrix[i][j] = distMatrix[i][j]*(dim-2) - totalDistance[i] - totalDistance[j];
				
				neighborJoiningMatrix[j][i] = neighborJoiningMatrix[i][j];
				
			}
		}
		
		
		return neighborJoiningMatrix;
	}
	
	
	
	
	
	/**
	 * 
	 * @param distMatrix
	 * @param iInd index (in distMatrix) of node i
	 * @param jInd index (in distMatrix) of node i
	 * @param $limbLengths for nodes i and j
	 */
	
	static void getLimbLengths(double[][] distMatrix, int iInd, int jInd, double[] $limbLengths) {
		
		int dim = distMatrix.length; // num nodes 
		double[] totalDistance = new double[dim];
		
		
		// calculate totalDistance
		
		for(int i=0; i<dim; i++) {
			totalDistance[i] = sumArray(distMatrix[i]);
		}
		
		
		double delta_i_j = (totalDistance[iInd] - totalDistance[jInd] ) / (dim-2);

		$limbLengths[0] = 0.5 * (distMatrix[iInd][jInd] + delta_i_j);
		$limbLengths[1] = 0.5 * (distMatrix[iInd][jInd] - delta_i_j);
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param distMatrix latest version of the distMatrix
	 * @param $distMatrixNodeIds node id's corresponding to the indices of distMatrix
	 * @param i_j_Indices nodes i and j's indices in distMatrix and distMatrixNodeIds, where iInd < jInd
	 * @param m id of new parent node of nodes i and j
	 * 
	 * @param $updatedDistMatrix instantiated, empty matrix 1x1 smaller than distMatrix, i.e. dim-1 x dim-1
	 */
	public static void calcUpdatedDistanceMatrix(double[][] distMatrix, ArrayList<Integer> $distMatrixNodeIds, int[] i_j_Indices, int m, double[][] $updatedDistMatrix) {
		

		int iInd = i_j_Indices[0];
		int jInd = i_j_Indices[1];
		
		int dim = distMatrix.length;
		
		
		
		/*
		 * 
		 * Calculate distances to m before deleting info for nodes i and j
		 * 
		 */
		
		double[] tempDistsToM = new double[dim-2];
		
		
		
		for(int k=0, kNew=0; k<dim; k++) {
			
			if( k!=iInd && k!=jInd ) {
	
				tempDistsToM[kNew] = 0.5 * ( distMatrix[k][iInd] + distMatrix[k][jInd] - distMatrix[iInd][jInd] );
				
				
				kNew++;
			}
			
			
		}
		
		
		
		
		
		
		/*
		 * 
		 * Do deletions pertaining to i and j
		 * 
		 */
		
		
		//
		// update distMatrixNodeIds
		//
		
		
		$distMatrixNodeIds.remove(jInd); // remove larger index first
		$distMatrixNodeIds.remove(iInd);
		
		
		double[][] tempDistMatrix = new double[dim-1][dim];
		
		
		//
		// delete 2 pertinent rows, copy distMatrix->tempDistMatrix
		//
		
		
		
		// iterate through rows of distMatrix
		for(int i=0,iNew=0; i<dim; i++) {
			
			
			if(i!=iInd && i!=jInd) {
				
				// iterate through rows of newDistMatrix
				

				tempDistMatrix[iNew] = distMatrix[i].clone();
				iNew++;
			}
			
		}
		
		
		//
		// delete 2 pertinent columns, copy tempDistMatrix->updatedDistMatrix
		//
		
		
		// iterate through columns of tempDistMatrix
		for(int j=0, jNew=0; j<dim; j++) {
			
			if(j!=iInd && j!=jInd) {
				
				for(int i=0; i<dim-1; i++) {
					$updatedDistMatrix[i][jNew] = tempDistMatrix[i][j];
				}
				
				jNew++;
			}
		}
		
		
		/*
		 * 
		 * Add info for new node m to $updatedDistMatrix
		 * 
		 * 
		 */
		
		$distMatrixNodeIds.add(new Integer(m));
		
		int mInd = dim-2;
		
		for(int i=0; i<mInd; i++) {
			$updatedDistMatrix[i][mInd] = tempDistsToM[i];
			$updatedDistMatrix[mInd][i] = tempDistsToM[i];
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param array min dim
	 * @return
	 */
	static double sumArray(double[] array) {
		double res = 0;
		
		for(double d : array) {
			res += d;
		}
		
		return res;
	}
	
	
	
	
	
	
	/**
	 * Non-diagonal argmin
	 * 
	 * 
	 * @param array
	 * @return {i,j} where i<j
	 */
	
	static int[] argmin(double[][] array) {
		
		int[] argIndices = new int[2];
		
		
		double min = Double.MAX_VALUE;
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[i].length; j++) {
				if( i!=j && array[i][j] < min ) {
					min = array[i][j];
					argIndices[0] = i;
					argIndices[1] = j;

				}
			}
		}

		return argIndices;
	}
	
	
	
	
	
	

}
