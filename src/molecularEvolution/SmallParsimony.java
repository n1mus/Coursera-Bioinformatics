package molecularEvolution;

import java.util.ArrayList;
import java.util.Scanner;

import util.ParsimonyTree;





public class SmallParsimony {

	public static void main(String[] args) {


		
		
		int numLeaves;
		ParsimonyTree tree;
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			numLeaves = reader.nextInt();
			
			tree =   new ParsimonyTree(numLeaves);
			
			while(reader.hasNextLine()) {
				String nextLine = reader.nextLine().trim();
				if(nextLine.length() == 0) continue;
				
				tree.parseAddNodesEdge(nextLine);
			}

			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		tree.smallParsimony();
		ArrayList<String> print = tree.getPrint();
		
		util.IOUtilities.writeToTxtFile(print, "smallParsimony");
		
		
	}

}
