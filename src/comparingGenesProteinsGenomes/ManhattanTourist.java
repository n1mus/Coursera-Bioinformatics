package comparingGenesProteinsGenomes;
import java.util.ArrayList;
import java.util.Scanner;

public class ManhattanTourist {

	public static void main(String[] args) {

		int n, m;
		int[][] down, right;
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			reader.useDelimiter("\\s+");

			n = reader.nextInt();
			m = reader.nextInt();
			
			// read down[n+1][m+1]
			// down[i][j] represents the weight of the edge going down into node s_i_j
			
			down = new int[n+1][m+1];
			
			for(int i=1; i<=n; i++) {
				for(int j=0; j<=m; j++) {
					down[i][j] = reader.nextInt();
				}
			}
			

			reader.skip("\\s*-\\s*");
			
			// read right[n+1][m+1]
			
			right = new int[n+1][m+1];
			
			for(int i=0; i<=n; i++) {
				for(int j=1; j<=m; j++) {
					right[i][j] = reader.nextInt();
				}
			}


			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}



		// n+1 x m+1 nodes
		// entry s[i][j] holds length of longest path to node s_i_j
		int s[][] = new int[n+1][m+1]; 
		
		
		// initialize leftmost column of s[][]
		
		for(int i=1; i<=n; i++) {
			s[i][0] = down[i][0] + s[i-1][0];
		}
		
		// init topmost row of s[][]
		
		for(int j=1; j<=m; j++) {
			s[0][j] = right[0][j] + s[0][j-1];
		}
		
		util.IOUtilities.printArray("s",s,3);
		
		
		// calculate s[][]
		
		for(int i=1; i<=n; i++) {
			for(int j=1; j<=m; j++) {
				s[i][j] = max(s[i-1][j]+down[i][j],s[i][j-1]+right[i][j]);
			}
		}
					util.IOUtilities.printArray("down", down,3);
			util.IOUtilities.printArray("right", right,3);
		util.IOUtilities.printArray("s", s, 3);
		
		System.out.println(s[n][m]);
		
		
		
	}
	
	
	
	
	
	static int max (int a, int b) {
		return a > b ? a : b;
	}

}
