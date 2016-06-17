package hiddenMessagesDNA;
import java.util.Scanner;

public class ReverseComplement {

	public static void main(String[] args) {

		
		char[] sequence;
		int seqLen;
		
		try( Scanner reader = Utilities.getScanner(args)) {
			sequence = reader.nextLine().toCharArray();
			seqLen = sequence.length;
		} catch(Exception e){
			System.err.println(e);
			return;
		}
	

		
		char[] reverseComplement = new char[sequence.length];
		
		for(int i=0, j=seqLen-1; i<seqLen; i++, --j ) {
			reverseComplement[j] = Utilities.complement(sequence[i]);
		}
		
		Utilities.writeCharArrayToTxtFile(reverseComplement, "reverseComplement.txt");
		
	}

}
