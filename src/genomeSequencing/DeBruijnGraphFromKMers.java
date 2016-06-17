package genomeSequencing;

import java.util.ArrayList;
import java.util.Scanner;

import util.IOUtilities;
import util.deBruijn.DeBruijnGraph;











/**
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *
 */
public class DeBruijnGraphFromKMers {

	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * 
	 */
	public static void main(String[] args) {


		
		ArrayList<String> kmers = new ArrayList<String>();
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
				
				while(reader.hasNext()) {
					kmers.add(reader.next().trim());
				}
			
			
			} catch (Exception e) {
				e.printStackTrace(System.err);
				return;
			}


		
		
		
		
		
		DeBruijnGraph dBGraph = new DeBruijnGraph();
		
		dBGraph.constructGraph(kmers);
		dBGraph.mergeRepeatNodes();
		
		dBGraph.print();
		
		ArrayList<String> print = dBGraph.getPrint();
		
		IOUtilities.writeToTxtFile(print, "debruijn");


	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
