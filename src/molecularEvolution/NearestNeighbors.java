package molecularEvolution;

import java.util.ArrayList;
import java.util.Scanner;

import util.IOUtilities;
import util.UndirectedTree;










public class NearestNeighbors {

	
	
	

	
	

	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		 UndirectedTree uTree = new UndirectedTree();;
		 int alphaId, betaId;	
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			alphaId = reader.nextInt();
			betaId = reader.nextInt();
			
			
			while(reader.hasNextLine()) {
				String line = reader.nextLine().trim();
				if( line.length() > 0)
					uTree.parseAddNodesEdge(line);
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		ArrayList<UndirectedTree> nearestNeighbors = uTree.nearestNeighbors(alphaId, betaId);
		
		for(UndirectedTree tree : nearestNeighbors) {
			tree.print();
		}
		

	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
