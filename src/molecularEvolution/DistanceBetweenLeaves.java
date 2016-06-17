package molecularEvolution;
import java.util.Scanner;

import util.DAG;
import util.UndirectedTree;



public class DistanceBetweenLeaves {

	public static void main(String[] args) {

		UndirectedTree tree = new UndirectedTree();
		int numLeaves;
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			numLeaves = reader.nextInt();
	
			while(reader.hasNextLine()) {
				String line = reader.nextLine().trim();
				if( line.length() > 0)
					tree.parseAddNodesEdge(line);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		tree.print();
		tree.distanceMatrix();
	}

}
