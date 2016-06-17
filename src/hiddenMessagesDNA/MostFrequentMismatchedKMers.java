package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeSet;

public class MostFrequentMismatchedKMers {

	public static void main(String[] args) {

		 StringBuilder text;
		 int k, d;
		 
			try(Scanner reader = Utilities.getScanner(args)) {
				text = new StringBuilder(reader.nextLine());
				k = Integer.valueOf(reader.next());
				d = Integer.valueOf(reader.next());
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
		ArrayList<StringBuilder> kMers = mostFrequentMismatchedKMers(text,k,d);
		Utilities.writeToTxtFile(kMers, "freqMismatchedKMers");
		
	}
	
	
	
	static ArrayList<StringBuilder> mostFrequentMismatchedKMers(StringBuilder text, int k, int d) {
		
		ArrayList<StringBuilder> allNeighbors = new ArrayList<StringBuilder>();
		

		
		
		// go through text
		// add all neighbors of all patterns
		for(int i=0; i<= text.length()-k; ++i) {
			StringBuilder pattern = new StringBuilder(text.substring(i, i+k));
			ArrayList<StringBuilder> neighbors = Utilities.neighbors(pattern, d);
			allNeighbors.addAll(neighbors);
		}
		
		
		StringBuilderComparator sbComparator = new StringBuilderComparator();
		
		// sort all neighbors of all patterns
		Collections.sort(allNeighbors, sbComparator);
		
		
		int[] count = new int[allNeighbors.size()]; // corresponds to allNeighbors
		
		
		// go through all neighbors
		// accumulating counts
		
		for(int i=1; i<allNeighbors.size(); ++i){
			StringBuilder prev = allNeighbors.get(i-1);
			StringBuilder curr = allNeighbors.get(i);
			
			if(sbComparator.compare(prev, curr) == 0)
				count[i] = count[i-1]+1;
		}
		
		
		int maxCount = Utilities.max(count);
		
		System.out.println("max count is " + maxCount);
		

		

		
		
		ArrayList<StringBuilder> res = new ArrayList<StringBuilder>();	
		
			
		// iterate through allNeighbors[] / count[]
		// get patterns corresponding to max count
		for(int index =0; index<allNeighbors.size(); ++index) {
			if(count[index] == maxCount) {
				res.add(allNeighbors.get(index));
			}
		}
		
		
		return res;
	}

}
