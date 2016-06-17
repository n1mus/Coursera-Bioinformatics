package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Scanner;

public class FrequentKMersMismatchesReverseComplements {

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
			
		ArrayList<StringBuilder> kMers = Utilities.frequentKMersMismatchedReverseComplement(text,k,d);
		Utilities.writeToTxtFile(kMers, "freqMismatchedReverseComplementKMers");
		
		
	}

}
