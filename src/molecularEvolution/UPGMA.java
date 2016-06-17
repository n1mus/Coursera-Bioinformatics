package molecularEvolution;

import java.util.ArrayList;
import java.util.Scanner;

import util.DirectedTree;


/*
 * 
 * 
 * 
 * 
 */
public class UPGMA {

	static boolean debug = false;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {


		int numLeaves;
		int numNodes;
		int[][] distanceMatrix;

		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
					
					numNodes = numLeaves = reader.nextInt();
					
					distanceMatrix = new int[numLeaves][numLeaves];
					
					
					
					for(int i=0; i<numLeaves; i++) {
						for(int k=0; k<numLeaves; k++) {
							while(! reader.hasNextInt()) {
								reader.next();
							}
							distanceMatrix[i][k] = reader.nextInt();
						}
					}
		
					
					
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
				
		if(debug) util.IOUtilities.printArray("distanceMatrix", distanceMatrix);		
		
		
				
		ArrayList<DirectedTree> clusters = new ArrayList<DirectedTree>();
		
		
		
		// turn leaves into individual trees
		// add to clusters
		
		for(int i=0; i<numLeaves; i++) {
			DirectedTree tree = new DirectedTree(i,0);
			clusters.add(tree);
		}
		
		
		
		
		
		
		//
		//
		// iterate until only 1 tree left in clusters
		//
		
		for(double[][] mutableDistMatrix = intArrayToDouble(distanceMatrix); 
				clusters.size()>1 ; ) {
			
			
			int[] closestTrees = argmin(mutableDistMatrix);
			
			
			
			if(debug) util.IOUtilities.printArray("mutDistMatrix",mutableDistMatrix);
			if(debug) util.IOUtilities.printArray("closestTrees", closestTrees);
			
			// mutableDistMatrix elements bijective with clusters
			// (each element represents a cluster)
			
			
			DirectedTree left = clusters.remove(closestTrees[1]);
			DirectedTree right = clusters.remove(closestTrees[0]);
			mutableDistMatrix = deleteFromDistanceMatrix(mutableDistMatrix,closestTrees[0], closestTrees[1]);
			
			
			
			DirectedTree newCluster = DirectedTree.join(left, right, distanceMatrix, numNodes++);
			
			
			addClusterToDistMatrix(distanceMatrix, mutableDistMatrix, clusters, newCluster); // mutableDistMatrix is altered
			clusters.add(newCluster);
			
		}
		
		
		
		
		
		DirectedTree finalTree = clusters.get(0);
		
		finalTree.calculateEdgeLengths();
		
		if(debug) System.out.println("num leaves: " + numLeaves);
		finalTree.print();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param array
	 * @return
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
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Remove from $clusters and return the DirectedTree with rootId
	 * 
	 * @param $clusters
	 * @param rootId
	 * @return
	 */
	static DirectedTree removeTree(ArrayList<DirectedTree> $clusters, int rootId) {
		
		for(int i=0; i<$clusters.size(); i++) {
			
			DirectedTree tree = $clusters.get(i);
			
			if(tree.getRootId() == rootId)
				return $clusters.remove(i);
			
		}
		
		
		
		// error
		
		new Exception().printStackTrace(System.out);
		
		return null;
	}
	
	
	
	
	
	
	
	
	/**
	 * Delete two rows and two columns
	 * Reduce dimensions by 1x1
	 * 
	 * 
	 * @param mutDistMatrix most recent distance matrix
	 * @param leftTreeIndex index of one of the trees removed to make a new cluster
	 * @param rightTreeIndex index of other tree removed to make a new cluster
	 * @return
	 */
	public static double[][] deleteFromDistanceMatrix(double[][] mutDistMatrix, int leftTreeIndex, int rightTreeIndex) {
		
		
		/*
		 * 
		 * Delete the rows and columns pertaining to the tree clusters that were removed
		 * 
		 * 
		 */
		
		
		int dim = mutDistMatrix.length;
		
		double[][] newDistMatrix = new double[dim-1][dim];
		double[][] newNewDistMatrix = new double[dim-1][dim-1];
		
		
		//
		// delete 2 pertinent rows, copy distMatrix->newDistMatrix
		//
		
		
		
		// iterate through rows of distMatrix
		for(int i=0,iNew=0; i<dim; i++) {
			
			
			if(i!=leftTreeIndex && i!=rightTreeIndex) {
				
				// iterate through rows of newDistMatrix
				

				newDistMatrix[iNew] = mutDistMatrix[i].clone();
				iNew++;
			}
			
		}
		
		
		//
		// delete 2 pertinent columns, copy newDistMatrix->newNewDistMatrix
		//
		
		
		// iterate through columns of newDistMatrix
		for(int j=0, jNew=0; j<dim; j++) {
			
			if(j!=leftTreeIndex && j!=rightTreeIndex) {
				
				for(int i=0; i<dim-1; i++) {
					newNewDistMatrix[i][jNew] = newDistMatrix[i][j];
				}
				
				jNew++;
			}
		}
		
		
		
		
		
		
		return newNewDistMatrix;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param distMatrix
	 * @param $mutDistMatrix
	 * @param clusters
	 * @param newestCluster
	 */
	static void addClusterToDistMatrix(final int[][] distMatrix, double[][] $mutDistMatrix, ArrayList<DirectedTree> clusters, DirectedTree newestCluster) {
		
		int numClusters = clusters.size();
		int[] newestClusterLeafIds = newestCluster.getLeafIds();
		
		// iterate through old clusters 
		
		for(int i=0; i<numClusters; i++ ) {
			DirectedTree cluster = clusters.get(i);
			int[] clusterLeafIds = cluster.getLeafIds();
			
			double totalDist = 0;
			double numLeafPairs = 0;
			
			// iterate through leaf pairings between old cluster and new cluster
			
			for(int newId : newestClusterLeafIds) {
				for(int id : clusterLeafIds) {
					totalDist += distMatrix[id][newId];
					numLeafPairs++;
					
				}
			}
			
			// update distance between new cluster and old cluster
			
			$mutDistMatrix[i][numClusters] = totalDist / numLeafPairs;
			$mutDistMatrix[numClusters][i] = $mutDistMatrix[i][numClusters];
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * For square matrices
	 * 
	 * @param array
	 * @return
	 */
	static double[][] intArrayToDouble(int[][] array) {
		int dim = array.length;
		double res[][] = new double[dim][dim];
		
		
		for(int i=0; i<dim; i++) {
			for(int j=0; j<dim; j++) {
				res[i][j] = array[i][j];
			}
		}
		
		
		return res;
	}

}
