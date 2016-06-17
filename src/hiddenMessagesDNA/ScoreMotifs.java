package hiddenMessagesDNA;
import java.util.ArrayList;
import java.util.Scanner;

public class ScoreMotifs {

	public static void main(String[] args) {
		
		ArrayList<String> myAnswers = new ArrayList<String>(),
				answers = new ArrayList<String>();
		
		int t;
		
		
		try(Scanner reader = Utilities.getScanner(args[0])) {
			t = reader.nextInt();

			while(reader.hasNext()) {
				for(int i=0; i<t; i++) {
					answers.add(reader.next().trim());
				}
				for(int i=0; i<t; i++) {
					myAnswers.add(reader.next().trim());
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		double score = Utilities.score(answers);
		double myScore = Utilities.score(myAnswers);
		
		System.out.println("score : " + score + ", my score: " + myScore);

	}

}
