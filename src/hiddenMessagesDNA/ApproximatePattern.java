package hiddenMessagesDNA;
import java.util.Scanner;

public class ApproximatePattern {

	public static void main(String[] args) {

		
		
		char[] sequence = "TACGCATTACAAAGCACA".toCharArray(),
				pattern = "AA".toCharArray();
		
		
		System.out.println(Utilities.hammingPatternMatch(sequence, pattern, 1).length);
	}

}
