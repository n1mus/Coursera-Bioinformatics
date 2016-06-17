package hiddenMessagesDNA;
import java.util.Scanner;

public class Skew {

	public static void main(String[] args) {
		
		char[] sequence = "GATACACTTCCCGAGTAGGTACTG".toCharArray();
		
		int[] skews = Utilities.skew(sequence);
		int[] minIndices = Utilities.argMin(skews);
		
		Utilities.printArray("min skew indices ", minIndices);
		
		

	}
	
}
