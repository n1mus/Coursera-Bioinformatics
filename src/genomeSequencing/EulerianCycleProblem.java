package genomeSequencing;

import java.util.ArrayList;
import java.util.Scanner;

import util.IOUtilities;
import util.deBruijn.DeBruijnGraph;
import util.*;








/**
 * 
 * 
 * 
 * 
 *
 */
public class EulerianCycleProblem {
	
	
	
	

	public static void main(String[] args) {
		
		
		ArrayList<String> edges = new ArrayList<String>();
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			while(reader.hasNext()) {
				String edge = reader.nextLine().trim();
				edges.add(edge);
			}
		
		
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return;
		}
		
		
		
		DeBruijnGraph dBGraph = new DeBruijnGraph();
		

		dBGraph.constructEulerianDirectedGraph(edges);
		
				
		
		dBGraph.constructEulerianCyclesTree();
		
//		dBGraph.print();
//		dBGraph.printCycleTree();
		
		ArrayList<Integer> eulerianCycle = dBGraph.traverseEulerianCycle();
		String result = formatEulerianCycle(eulerianCycle);
		
		IOUtilities.printArray("eulerian cycle", eulerianCycle);
		IOUtilities.writeToTxtFile(result, "eulerianCycle");
	}
	
	
	
	
	
	
	static String formatEulerianCycle(ArrayList<Integer> nodeIds) {
		String res = nodeIds.get(0).toString();
		
		for(int i=1; i<nodeIds.size(); i++) {
			res += "->" + nodeIds.get(i).toString();
		}
		return res;
	}

}
