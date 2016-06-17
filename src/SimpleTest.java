
import java.util.ArrayList;

import molecularEvolution.UPGMA;

public class SimpleTest {

	public static void main(String[] args) {
		
		double[][] distMatrix = {
				{0,13,21,22},
				{13,0,12,13},
				{21,12,0,13},
				{22,13,13,0}};
		ArrayList<Integer> distMatrixNodeIds = new ArrayList<Integer>();
		
		for(int i=0; i<4; i++) {
			distMatrixNodeIds.add(new Integer(i));
		}
		
		int[] nodeInds = {0,1};
		double[][] updatedDistMatrix = new double[3][3];
		
		molecularEvolution.NeighborJoining.calcUpdatedDistanceMatrix(distMatrix, distMatrixNodeIds, nodeInds, 4, updatedDistMatrix);
		
		
		
		

		util.IOUtilities.printArray("distMatrix", distMatrix);
		util.IOUtilities.printArray("updatedDistMatrix", updatedDistMatrix);
		util.IOUtilities.printArray("distMatrixNodeIds", distMatrixNodeIds);
		
		
	}
}
