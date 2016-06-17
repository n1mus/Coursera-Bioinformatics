package comparingGenesProteinsGenomes;

import java.util.ArrayList;
import java.util.Scanner;

import util.DAG;

public class DAGLongestPath {

	public static void main(String[] args) {

		int sourceNode, sinkNode;
		DAG dag ;
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			sourceNode = reader.nextInt();
			sinkNode = reader.nextInt();
			
			dag = new DAG(sourceNode, sinkNode);
			
			while(reader.hasNextLine()) {
				String line = reader.nextLine().trim();
				if( line.length() > 0)
					dag.parseAddNodesEdge(line);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		String[] $longestPath = {null};
		int longestPathLength = dag.longestPath($longestPath);
		
		System.out.println(longestPathLength);
		System.out.println($longestPath[0]);
		
	}
	


}
