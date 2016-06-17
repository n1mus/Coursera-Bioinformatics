package molecularEvolution;

import java.util.ArrayList;
import java.util.Scanner;
import util.IOUtilities;
import util.ParsimonyTree;
import util.UndirectedTree;






public class LargeParsimony {

	

	static boolean debug = true;


	
	
	public static void main(String[] args) {

		int numLeaves;
		UndirectedTree uTree = new UndirectedTree();


		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			numLeaves = reader.nextInt();
			
			
			while(reader.hasNextLine()) {
				String line = reader.nextLine().trim();
				if( line.length() > 0)
					uTree.parseAddNodesEdgeWithSequences(line);
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}



		int bestScore = Integer.MAX_VALUE;
		ParsimonyTree pTree = uTree.createParsimonyTree();
		int newScore = pTree.smallParsimony();





		ArrayList<String> print = new ArrayList<String>();

		while( newScore < bestScore ) {

			if(debug) {
				System.out.println(newScore);
				uTree.print();
				System.out.println();
			}

			if( bestScore != Integer.MAX_VALUE) { // skip first tree
				print.addAll(pTree.getPrint());
				print.add("\n");
			}

			bestScore = newScore;


			ArrayList<UndirectedTree> nearestNeighbors = uTree.nearestNeighbors();

			
			UndirectedTreeAndParsimonyScore best = calcBestParsimonyScore(nearestNeighbors);

			newScore = best.score;
			uTree = best.uTree;
			pTree = best.pTree;


		}

		IOUtilities.printAbstractCollection("large parsimony", "", print);
		IOUtilities.writeToTxtFile(print,"largeParsimony");


	}








	/**
	 *
	 *
	 *
	 *
	 *
	 *
	 */
	static UndirectedTreeAndParsimonyScore calcBestParsimonyScore(ArrayList<UndirectedTree> uTrees) {

		int bestScore = Integer.MAX_VALUE;
		UndirectedTree bestUTree = null;		
		ParsimonyTree bestPTree = null;	

		UndirectedTreeAndParsimonyScore best = new UndirectedTreeAndParsimonyScore();



		for(UndirectedTree uTree : uTrees) {
			
			ParsimonyTree pTree = uTree.createParsimonyTree();
			int score = pTree.smallParsimony();

			if( score < bestScore ) {

				bestScore = score;
				bestUTree = uTree;
				bestPTree = pTree;

			}

		}

		best.uTree = bestUTree;
		best.pTree = bestPTree;
		best.score = bestScore;

		return best;

	}




}







class UndirectedTreeAndParsimonyScore {

	UndirectedTree uTree;
	ParsimonyTree pTree;
	int score;

}
