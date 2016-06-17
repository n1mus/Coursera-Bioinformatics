package hiddenMessagesDNA;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class SalmonellaEnterica {

	public static void main(String[] args) {

		StringBuilder genome = new StringBuilder();
		
		try(Scanner reader = Utilities.getScanner(args[0])) {
			
			while(reader.hasNext())
				genome.append(reader.next().trim());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		int[] skews = Utilities.skew(genome);
		int[] minSkewLoci = Utilities.argMin(skews);
		
		Utilities.printArray("possible oriC indices ", minSkewLoci);
		
		
		int oriCIndex = arrayAverage(minSkewLoci);
		
		int windowLength = 200;
		
		StringBuilder abridgedGenome = new StringBuilder(genome.substring(oriCIndex-windowLength,oriCIndex+windowLength));
		
		for(int k=15; k<=20; k++) {
			ArrayList<StringBuilder> kMers = Utilities.frequentKMersMismatchedRC(abridgedGenome, k, 1);
			Utilities.printAbstractCollection("frequent k-mers for k=" + k, kMers);
		}
		
		System.out.println("?");
		
	}
	
	
	static int arrayAverage(int[] array) {
		int sum = 0;
		
		for(int i: array) {
			sum += i;
		}
		
		return sum / array.length;
	}

}
