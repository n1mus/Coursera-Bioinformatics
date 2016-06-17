package genomeSequencing;

import java.util.ArrayList;
import java.util.Scanner;

import util.IOUtilities;
import util.deBruijn.DeBruijnGraph;




















/**
 * DNA sequence -> de Bruijn graph
 * de Bruijn graph is like an overlay graph, but switch edges and nodes, then aggregate nodes
 * 
 * 
 * 
 * 
 * DNA sequence ->
 * kmers for edges, k-1mers for nodes ->
 * create data structure ->
 * aggregate nodes ->
 * output adjacency list
 * 
 * 
 * 
 * 
 * 
 * 
 *
 */
public class DeBruijnGraphFromString {

	
	
	
	public static void main(String[] args) {
		
		
		DeBruijnGraph dBGraph = new DeBruijnGraph();
		
		
		// given
		int k;
		String dnaSequence;
		
		
		

		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			k = reader.nextInt();
			dnaSequence = reader.next().trim();
		
		
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return;
		}
		
		
		
		// fragment dna sequence into kmers and (k-1)mers
		// for edges and nodes, respectively
		ArrayList<String> kmers = getComposition(dnaSequence, k);
		ArrayList<String> nodesReads = getComposition(dnaSequence, k-1);
		
		
		// add the nodes and edges to the deBruijnGraph
		dBGraph.constructGraph(kmers,nodesReads);
		
		
		
		dBGraph.mergeRepeatNodes();
		
		
		dBGraph.print();
		
		
		ArrayList<String> print = dBGraph.getPrint();
		
		
		IOUtilities.writeToTxtFile(print, "deBruijn");
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param sequence
	 * @param k
	 * @return
	 */
	static ArrayList<String> getComposition(String sequence, int k) {
		
		
		int numKmers = sequence.length() - k + 1;
		
		ArrayList<String> kmersList = new ArrayList<String> ();
		
		
		for( int i=0; i<numKmers; i++ ) {
			String kmer = sequence.substring(i, i+k);
			kmersList.add(kmer);
		}
		
		
		return kmersList; 
		
	}
	
	
	
	
	
	
	
	
	
}
