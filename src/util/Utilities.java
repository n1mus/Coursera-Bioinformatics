package util;

public class Utilities {
	
	
	
	static public double[][] copyMatrix(int[][] matrix) {
		
		double[][] newMatrix = new double[matrix.length][];
		
		for(int i=0; i<matrix.length; i++) {
			int rowLength = matrix[i].length;
			
			double[] newRow = new double[rowLength];
			
			for(int j=0; j<rowLength; j++) {
				newRow[j] = matrix[i][j];
			}
			
			newMatrix[i] = newRow;
		}
		
		return newMatrix;
	}
	
	
	public static int argmin(int[] array) {
		
		int min=Integer.MAX_VALUE;
		int argmin = -1;
		for(int i=0; i<array.length; i++) {
			if(array[i] < min)
			{
				min = array[i];
				argmin = i;
			}
		}
		
		return argmin;
	}
}
