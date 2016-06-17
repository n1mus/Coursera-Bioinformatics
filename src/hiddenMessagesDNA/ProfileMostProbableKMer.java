package hiddenMessagesDNA;
import java.util.Scanner;

public class ProfileMostProbableKMer {

	public static void main(String[] args) {
	
		String sequence;
		int k;
		float[][] profile;
		
		try(Scanner reader = Utilities.getScanner(args[0])) {
			sequence = reader.nextLine().trim();
			k = reader.nextInt();
			
			profile = new float[4][k];
			
			for(int row=0; row<4; row++) {
				for (int col=0; col<k; col++) {
					profile[row][col] = reader.nextFloat();
				}
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		String mostProbable = Utilities.profileMostProbableKMer(sequence,k,profile);
		System.out.println(mostProbable);
		
		
	}
	

	
	
	
}
