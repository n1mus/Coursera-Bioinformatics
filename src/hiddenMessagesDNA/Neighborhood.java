package hiddenMessagesDNA;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeSet;

public class Neighborhood {

	
	
	/*
	 * args[0] file of problem
	 * args[1] answer to 
	 */
	public static void main(String[] args) {
		
		StringBuilder p = new StringBuilder("ACGT");
		ArrayList<StringBuilder> neighbors = Utilities.neighbors(p,3);
		Utilities.printAbstractCollection("neighbors", neighbors);
		System.out.println(neighbors.size());
	}

	
	

}
