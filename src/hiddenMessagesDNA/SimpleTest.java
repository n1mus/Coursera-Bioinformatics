package hiddenMessagesDNA;
import java.util.ArrayList;

public class SimpleTest {

	public static void main(String[] args) {
		
		
		
		String dna1= "CTCGATGAGTAGGAAAGTAGTTTCACTGGGCGAACCACCCCGGCGCTAATCCTAGTGCCC",
				dna2="GCAATCCTACCCGAGGCCACATATCAGTAGGAACTAGAACCACCACGGGTGGCTAGTTTC",
				dna3="GGTGTTGAACCACGGGGTTAGTTTCATCTATTGTAGGAATCGGCTTCAAATCCTACACAG";
		ArrayList<String> dna = new ArrayList<String>();
		dna.add(dna1);dna.add(dna2);dna.add(dna3);
		
		ArrayList<String> medians = MedianString.medianString(dna, 7);
		Utilities.printAbstractCollection("medians", null, medians);
		
		

		
	}

}
