package hiddenMessagesDNA;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ComputingFrequencies {

	public static void main(String[] args) throws IOException {

		
		
	    Scanner reader = Utilities.getScanner(args);
	    
	    char[] sequence = reader.nextLine().toCharArray();
	    int k = Integer.parseInt(reader.nextLine());

	    
	    
	    int[] frequencies = Utilities.computingFrequencies(sequence,k);

	    
	    
	    File file = new File("C:\\Users\\sumin\\workspace\\Bioinformatics\\output\\frequencies.txt");
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(int i : frequencies) {
	    		writer.write((String) (i+" "),0,2);
	    	}
	    	
	    } catch (IOException x) {
	        System.err.format("IOException: %s%n", x);
	    }

	    
		reader.close();
		

		
	}
	


}
