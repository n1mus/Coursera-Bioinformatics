package hiddenMessagesDNA;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/*
 * Sliding window to find pattern
 */
public class PatternCount {
	


	public static void main(String[] args) throws IOException {

	    // first check to see if the program was run with the command line argument
	    if(args.length < 1) {
	        System.out.println("Error, usage: java ClassName inputfile");
		System.exit(1);
	    }
	   
	    Scanner reader = new Scanner(new FileInputStream(args[0]));
	    
	    
		char[] sequence = reader.nextLine().toCharArray();
		char[] pattern = reader.nextLine().toCharArray();
	    reader.close();
	    
	    System.out.println("Count is " + Utilities.patternCount(sequence, pattern));
	    



	    	
	}

	

	
	
}
