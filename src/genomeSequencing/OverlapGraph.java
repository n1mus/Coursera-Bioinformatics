package genomeSequencing;

import java.util.ArrayList;
import java.util.Scanner;

import util.DirectedGraph;
import util.IOUtilities;

public class OverlapGraph {

	public static void main(String[] args) {


		
		
		/*
		 * Read in all kmers 
		 */
		
		
		ArrayList<String> kmerList = new ArrayList<String>();
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			while(reader.hasNext()) {
				String kmer = reader.next();
				kmerList.add(kmer);
			}
		
		
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return;
		}
	
		
		

		
		
		
		DirectedGraph<String> dGraph = new DirectedGraph<String> ();
		
		
		
		
		
		/*
		 * add nodes/kmers to graph
		 */
		
		
		dGraph.addNodes(kmerList);
		
		
		
		
		
		
		
		/*
		 * add edges to graph
		 */
		
		addEdges(dGraph, kmerList);
		
		
		
		
		/*
		 * print graph
		 */
		
		ArrayList<String> print = dGraph.getPrint(false, true, false);
		
		IOUtilities.writeToTxtFile(print, "overlap", "\n");

	}
	
	
	
	
	/**
	 * Examines all pairings of nodes
	 * Adds edge when appropriate
	 * 
	 * 
	 * 
	 * @param dGraph
	 * @param kmers
	 */
	static void addEdges(DirectedGraph<String> dGraph, ArrayList<String> kmers) {
		
		
		for(int i=0; i<kmers.size(); i++) { // first element to last el 
			
			for(int j=i; j<kmers.size(); j++) { //   i-th el to last el
				
				String alpha = kmers.get(i);
				String beta = kmers.get(j);
				
				if(isPrefixOf(alpha, beta)) {
					dGraph.addEdge(i, j);
				} else
				if(isPrefixOf(beta,alpha)) {
					dGraph.addEdge(j, i);
				}
				
			}
			
			
		}
			
		
		
	}
	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	static boolean isPrefixOf(String left, String right) {
		return left.regionMatches(1, right, 0, left.length()-1);
	}
	
	

}
