package hiddenMessagesDNA;
import java.util.Scanner;

public class HammingDistance {

	
	public static void main(String args[]) {
		
		
		
		String p = "CTACAGCAATACGATCATATGCGGATCCGCAGTGGCCGGTAGACACACGT";
		String q = "CTACCCCGCTGCTCAATGACCGGGACTAAAGAGGCGAAGATTATGGTGTG";
		
		
		System.out.println(Utilities.hammingDistance(p,q));
		
	}
	
	
	
}
