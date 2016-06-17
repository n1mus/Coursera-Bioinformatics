package hiddenMessagesDNA;

public class ComputingHammingFrequencies {

	public static void main(String[] args) {

		
		char[] sequence = "AACAAGCTGATAAACATTTAAAGAG".toCharArray();
		char[] pattern = "AAAAA".toCharArray();
		int d = 2;
		
		
		int[] hammingIndices = Utilities.hammingPatternMatch(sequence, pattern, d);
		System.out.println(hammingIndices.length);
	}

}
