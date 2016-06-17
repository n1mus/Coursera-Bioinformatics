package hiddenMessagesDNA;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeSet;

public class Utilities {
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Return Scanner for the file specified in args[0]
	 */
	static Scanner getScanner(String... args) throws IOException {
	    if(args.length < 1) {
	        System.out.println("Error, usage: java ClassName inputfile");
		System.exit(1);
	    }
	   
	    return new Scanner(new FileInputStream(args[0]));
	}
	
	/**
	 * @param filename : path relative to "C:\\Users\\sumin\\workspace\\Bioinformatics\\output\\"
	 * 
	 * Writes character array to the text file, no space delimiters
	 */
	static void writeCharArrayToTxtFile(char[] array, String filename) {
		
	    File file = new File("C:\\Users\\sumin\\workspace\\Bioinformatics\\output\\" + filename);
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(char c : array) {
	    		writer.write(c);
	    	}
	    	
	    } catch (IOException x) {
	        System.err.format("IOException: %s%n", x);
	    }
	}
	
	/**
	 * Write int array, space delimited, to txt file
	 * 
	 * 
	 * @param array of ints
	 * @param filename (excluding the ".txt" extension)
	 */
	static void writeArrayToTxtFile(int[] array, String filename) {
		
	    File file = new File("C:\\Users\\sumin\\workspace\\Bioinformatics\\output\\" + filename + ".txt");
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(int i : array) {
	    		String str = String.valueOf( i);
	    		
	    		writer.write(str, 0, str.length());
	    		writer.write('\n');
	    	}
	    	
	    } catch (IOException x) {
	        System.err.format("IOException: %s%n", x);
	    }
	}
	

	
	/**
	 * 
	 * @param collection AbstractCollection (ArrayList, TreeSet, etc.) of CharSequence (String, StringBuilder, etc.)
	 * @param filename (excluding ".txt" extension)
	 */
	static <T extends CharSequence> void writeToTxtFile(AbstractCollection<T> collection, String filename ) {
		
		System.out.println("Writing to text file " + filename + ".txt");
		System.out.println(collection);
		
	    File file = new File("C:\\Users\\sumin\\workspace\\Bioinformatics\\output\\" + filename + ".txt");
	    
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	
	    	
	    	for(CharSequence cs : collection) {
	    		writer.write(cs.toString() + " ",0,cs.length()+1);
	    	}
	    	
	    } catch (IOException x) {
	        System.err.format("IOException: %s%n", x);
	    }
	}
	
	static <T extends CharSequence> void printAbstractCollection(String name, String delimiter, AbstractCollection<T> collection ){
		if(delimiter==null) delimiter = "\n";
		System.out.println("Printing array: " + name);
		for(CharSequence str : collection) {
			System.out.print(str + delimiter);
		}
		System.out.println();
	}
	
	static void printArray(String name, int[] array) {
		System.out.println("Printing array: " + name);
		for(int i:array) {
			System.out.print(i);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	static void printArray(String name, String delimiter, float[] array) {
		if(delimiter==null) delimiter = "\n";
		System.out.println("Printing array: " + name);
		for(float f:array) {
			System.out.printf("%.3e",f);
			System.out.print(delimiter);
		}
		System.out.println();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Returns the number of times the pattern occurs in the sequence (overlaps allowed)
	 */
	static int patternCount(char[] seqArr, char[] pattArr) {
	    int seqLen = seqArr.length;
	    int pattLen = pattArr.length; 

	    
	    int count = 0;
	    
	    for( int index = 0; index < seqLen - pattLen; ++index)
	    	if( patternMatchAt(seqArr, pattArr, index) )
	    		++count;
	    
	    return count;
	}
	
	
	
	/**
	 * Returns true if the pattern matches the sequence at the index
	 */
	static boolean patternMatchAt(char[] seqArr, char[] pattArr, int index) {
	
		for(int i=0; i<pattArr.length; i++) {
			if(seqArr[index+i] != pattArr[i])
				return false;
		}
		
		
		return true;
	}
	
	
	/**
	 * 
	 * @param sequence
	 * @param pattern
	 * @param d
	 * @return
	 */
	static int[] hammingPatternMatch(char[] sequence, char[] pattern, int d) {
		
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		
		for(int i=0; i<=sequence.length - pattern.length; i++) {
			if(hammingPatternMatchAt(sequence,i,pattern,d))
				indices.add(new Integer(i));
		}
		
		
		return arrayListIntegerToIntArray(indices);
	}
	
	
	
	/**
	 * 
	 * @param sequence larger DNA sequence you're searching through for matches
	 * @param index of sequence testing for approximate match
	 * @param pattern you're looking for
	 * @param d upper bound for Hamming distance so that match is valid
	 * @return
	 */
	static boolean hammingPatternMatchAt(char[] sequence,  int index, char[] pattern, int d) {
		
		
		int mismatches = 0;
		
		for (int i=index, j=0; j<pattern.length; i++, j++) {
			
			if( sequence[i] != pattern[j] && ++mismatches > d)
					return false;
			
		}
		
		
		return true;
	}
	
	
	
	/**
	 * 
	 * @param sequence larger DNA sequence you're searching through for matches
	 * @param index of sequence testing for approximate match
	 * @param pattern you're looking for
	 * @param d upper bound for Hamming distance so that match is valid
	 * @return
	 */
	static boolean hammingPatternMatchAt(CharSequence sequence,  int index, CharSequence pattern, int d) {
		
		
		int mismatches = 0;
		
		for (int i=index, j=0; j<pattern.length(); i++, j++) {
			
			if( sequence.charAt(i) != pattern.charAt(j) && ++mismatches > d)
					return false;
			
		}
		
		
		return true;
	}
	
	
	
	/**
	 * Find max value of array
	 */
	static int max(int[] array) {
		int max = array[0];
		
		for (int i : array) {
			if (i > max)
				max = i;
		}
		
		return max;
	}
	
	

	
	
	/**
	 * @param sequence : the DNA bp sequence
	 * @param k : the length of the k-mer to calculate frequencies for
	 * 
	 * @return : an array of size 4^k, one slot for each possible
	 * k-mer, with the frequency of the corresponding k-mer in each slot
	 * 
	 * Space : 4^k
	 * Runtime : |sequence|
	 */
	static int[] computingFrequencies(char[] sequence, int k) {
		int[] count = new int[(int) Math.pow(4,k)];
		
		for(int i=0; i<=sequence.length-k; i++) {
			int numRep = (int) patternToNumber(sequence,i,k);
			count[numRep]++;
		}
		return count;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param sequence : the DNA bp sequence
	 * @param offset : the position in array that the pattern (k-mer) begins at
	 * @param k : length of pattern (k-mer)
	 * 
	 * @return : number representation of bp pattern
	 */
	static int patternToNumber(char[] sequence, int offset, int k) {
		int numRep = 0;


		
		// index begins at least significant "bit", at the rightmost of pattern
		
		for(int index = offset+k-1, exp = 0; exp <k; --index, exp++) {
			
			int numValue = letterToNum(sequence[index]);
			numRep += numValue * (int) Math.pow(4, exp);
		}
		
		return numRep;
	}
	
	/**
	 * 
	 * @param numRep number representation of DNA pattern
	 * @param k length of pattern
	 * @return
	 */
	static String numberToPattern(int numRep, int k) {
		char[] pattern = new char[k];
		
		for(int i=k-1; i>=0; i--) {
			
			int remainder = numRep % 4;
			pattern[i] = numToDnaChar(remainder);
			
			numRep = numRep/4;
		}
		
		return String.valueOf(pattern);
	}
	
	
	
	static int letterToNum(char c) {
		switch(c) {
			case 'A' : return 0;
			case 'a' : return 0;
			case 'C': return 1;
			case 'c': return 1;
			case 'G' : return 2;
			case 'g' : return 2;
			case 'T' : return 3;
			case 't' : return 3;
			

		}
		
		return Utilities.invalidBasePairError();
	}
	
	/**
	 * Use when encountering invalid base paid in DNA sequence
	 */
	static int invalidBasePairError() {
		System.err.println("Invalid DNA base letter. Must use a c g t A C G or T");
		System.err.println(new Exception().getStackTrace());
		
		return -1;
	}
	
	
	
	static char numToDnaChar(int i) {
		switch(i) {
		case 0: return 'A';
		case 1: return 'C';
		case 2: return 'G';
		case 3: return 'T';
		}
		
		System.err.println("Trying to convert invalid integer " + i + " to DNA letter");
		System.err.println(new Exception());

		return 'A';
	}

	
	/**
	 * Hamming distance counts the number 
	 * of mismatched DNA letters between two patterns
	 * of the same length
	 * 
	 * 
	 * @param p1
	 * @param p2 same length as p1
	 * @return
	 */
	static int hammingDistance(CharSequence p1, CharSequence p2) {
		
		int count = 0;
		
		
		if(p1.length() != p2.length()) {
			System.err.println("Hamming distance errror: two patterns must have same length");
			System.err.println(new Exception().toString());
		}
		
		
		for(int i=0; i<p1.length(); i++) {
			if(p1.charAt(i) != p2.charAt(i))
				count++;
		}
		
		
		return count;
		
	}

	static int[] arrayListIntegerToIntArray(ArrayList<Integer> a) {
		int[] res = new int[a.size()];
		
		for(int i=0; i<a.size(); i++) {
			res[i] = a.get(i).intValue();
		}
		
		return res;
	}
	
	
	
	
	
	
	
	

	/**
	 * Generates all the pattern's neighbors with Hamming distance <= d
	 * including original pattern.
	 * 
	 * Recursive.
	 * 
	 * Output:    1+3^d*Choose(|pattern|,d)   ~   |pattern| ^ d    neighbors
	 *                         ~ polynomial
	 *                         
	 *                         
	 * RUNTIME            ~ polynomial for |pattern|
	 * 
	 * 
	 * 
	 * @param pattern, assume will be small, around 12
	 * @param d Hamming distance, assume will be small, around 3
	 * @return
	 */
	static ArrayList<StringBuilder> neighbors(StringBuilder pattern, int d) {
                                                                                                                                                                                                                                                                                                                                                                                                                             
		
		ArrayList<StringBuilder> neighborhood = new ArrayList<StringBuilder>();
		
		
		// special case: Hamming distance 0
		
		if(d==0) {
			neighborhood.add(pattern);
			return neighborhood;
		}
		
		// base case: pattern down to 1
		
		if(pattern.length() == 1) {
			neighborhood.add( new StringBuilder("A") );
			neighborhood.add( new StringBuilder("C") );
			neighborhood.add( new StringBuilder("G") );
			neighborhood.add( new StringBuilder("T") );
		
			return neighborhood;
		}
		
		
		
		// recursive case
		
		StringBuilder suffix = new StringBuilder(pattern).deleteCharAt(0);
		ArrayList<StringBuilder> suffixNeighborhood = neighbors(suffix,d);
		
		for(StringBuilder suffixNeighbor: suffixNeighborhood ) {
			
			if( hammingDistance(suffixNeighbor, suffix) < d) {
				neighborhood.add( new StringBuilder(suffixNeighbor).insert(0, 'A'));
				neighborhood.add( new StringBuilder(suffixNeighbor).insert(0, 'C'));
				neighborhood.add( new StringBuilder(suffixNeighbor).insert(0, 'G'));
				neighborhood.add( new StringBuilder(suffixNeighbor).insert(0, 'T'));
			} else {
				neighborhood.add(new StringBuilder(suffixNeighbor).insert(0, pattern.charAt(0)));
			}
				
		}
		

		return neighborhood;
		
	}

	static char complement(char c) {
		switch(c){
			case 'A': return 'T';
			case 'a': return 't';
			case 'C': return 'G';
			case 'c': return 'g';
			case 'G': return 'C';
			case 'g': return 'c';
			case 'T': return 'A';
			case 't': return 'a';
		}
		
		return (char) invalidBasePairError();
	}

	static StringBuilder reverseComplement(final StringBuilder pattern) {
		StringBuilder complement = new StringBuilder();
		
		for(int i=0; i<pattern.length(); ++i){
			complement.append(complement(pattern.charAt(i)));
			
		}
		return complement.reverse();
	}

	/**
	 * 
	 * @param sequence DNA letters
	 * @return int array of size sequence.length+1
	 */
	static int[] skew(char[] sequence) {
		int[] skews = new int[sequence.length+1];
		
		int skew = 0;
		
		for(int sequenceIndex=0, skewIndex=1; sequenceIndex<sequence.length; sequenceIndex++, skewIndex++) {
			if(sequence[sequenceIndex] == 'C')
				skew--;
			else if(sequence[sequenceIndex] == 'G')
				skew++;
			
			skews[skewIndex] = skew;
		}
		
		
		
		return skews;
	}
	
	/**
	 * 
	 * @param genome DNA letters
	 * @return int array of size sequence.length+1
	 */
	static int[] skew(CharSequence genome) {
		int[] skews = new int[genome.length()+1];
		
		int skew = 0;
		
		for(int sequenceIndex=0, skewIndex=1; sequenceIndex<genome.length(); sequenceIndex++, skewIndex++) {
			if(genome.charAt(sequenceIndex) == 'C')
				skew--;
			else if(genome.charAt(sequenceIndex) == 'G')
				skew++;
			
			skews[skewIndex] = skew;
		}
		
		
		
		return skews;
	}

	/**
	 * 
	 * @param values
	 * @return array of indices where values[] achieves a minimum
	 */
	static int[] argMin(int[] values) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		
		int min = values[0];
		
		
		// find min in values[]
		for(int index=1; index<values.length; index++) {
			if(values[index] < min)
				min = values[index];
		}
		
		// find indices of the min in values[]
		for(int index=0; index<values.length; index++) {
			if(values[index] == min)
				res.add(new Integer(index));
		}
		
		
		return arrayListIntegerToIntArray(res);
	}

	
	
	
	/**
	 * 
	 * 
	 * Find the most frequent k-mers (with mismatches and reverse complements) in a string.
	 *
	 * 
	 * @param text
	 * @param k
	 * @param d
	 * @return All k-mers Pattern maximizing the sum Countd(Text, Pattern)+ Countd(Text, Pattern)
	  over all possible k-mers.
	 */
	static ArrayList<StringBuilder> frequentKMersMismatchedRC(StringBuilder text, int k, int d) {
		
		ArrayList<StringBuilder> allNeighbors = new ArrayList<StringBuilder>();
		
		StringBuilderComparator sbComparator = new StringBuilderComparator();	
		
		
		// go through all patterns in text
		// add all neighbors of pattern and reverseComplement(pattern)
		// RUNTIME |text| * k ^ d
		for(int i=0; i<= text.length()-k; ++i) {
			StringBuilder pattern = new StringBuilder(text.substring(i, i+k));
			StringBuilder reverseComplement = reverseComplement(pattern);
			
			
			ArrayList<StringBuilder> neighbors = neighbors(pattern, d);
			ArrayList<StringBuilder> neighborsRC = neighbors(reverseComplement, d);
			
			TreeSet<StringBuilder> dupRemover = new TreeSet<StringBuilder>(sbComparator);
			dupRemover.addAll(neighbors);
			dupRemover.addAll(neighborsRC);
			
			
			allNeighbors.addAll(dupRemover);
		}
		
		

		
		
		// k^d * d * lg k
		// sort all neighbors of all patterns
		Collections.sort(allNeighbors, sbComparator);
		
		
		int[] count = new int[allNeighbors.size()]; // corresponds to allNeighbors
		
		
		// go through all neighbors
		// accumulating counts
		
		for(int i=1; i<allNeighbors.size(); ++i){
			StringBuilder prev = allNeighbors.get(i-1);
			StringBuilder curr = allNeighbors.get(i);
			
			if(sbComparator.compare(prev, curr) == 0)
				count[i] = count[i-1]+1;
		}
		
		
		int maxCount = max(count);
		

		
	
		
	
		
		
		ArrayList<StringBuilder> resPatterns = new ArrayList<StringBuilder>();
		ArrayList<Integer> resIndices = new ArrayList<Integer>();
		
		
		
		// iterate through allNeighbors[] / count[]
		// get patterns corresponding to max count
		for(int index =0; index<allNeighbors.size(); ++index) {
			if(count[index] == maxCount) {
				resPatterns.add(allNeighbors.get(index));
				resIndices.add(new Integer(index));
			}
		}
		
		
		
		
		
		return resPatterns;
	}

	/**
	 * Given a DNA sequence and a 4xk profile,
	 * return the pattern that is most likely
	 * according to the profile
	 * 
	 * 
	 * @param sequence
	 * @param k
	 * @param profile 4xk 2d array
	 */
	static String profileMostProbableKMer(String sequence, int k, float[][] profile) {
		String mostProbable = new Exception().getStackTrace().toString();
		double highestProbability = -1;
		
		
		
		// iterate through k-mers in sequence
		for(int index=0; index<=sequence.length()-k; index++) {
			double probability = 1;
			
			// iterate through letters of k-mer 
			for(int i=0; i<k; i++) {
				char letter = sequence.charAt(index+i);
				int letterAsNum = letterToNum(letter);
				probability *= profile[letterAsNum][i];
			}
			
			if(probability > highestProbability) {
				mostProbable = sequence.substring(index,index+k);
				highestProbability = probability;
			}
		}
		
		
		
		return mostProbable;
	}

	/**
	 * Given motifs matrix
	 * Find the most common letter in each column
	 * Add number of disagreeing letters to score
	 * (Lower scores are better) 
	 * 
	 * No pseudocounts
	 * 
	 * 
	 * @param motifs
	 * @return
	 */
	static <T extends CharSequence> int score(ArrayList<T> motifs) {
		int t = motifs.size();
		int k = motifs.get(0).length();
		
		int score = 0;
		
		// iterate through "columns" (positions of the k-mers)
		for(int i=0; i<k; i++) {
			
			int aCount, cCount, gCount, tCount;
			aCount = cCount= gCount = tCount = 0;
			
			// iterate through "rows" (k-mers) to tally counts
			for(int j=0; j<t; j++) {
				switch(motifs.get(j).charAt(i)) {
				case 'A': aCount++; break;
				case 'C': cCount++; break;
				case 'G': gCount ++; break;
				case 'T': tCount++; break;
				}
			}
			
			
			//determine max count, then the
			// score for that column
			if(aCount >= cCount && aCount>=gCount && aCount>=tCount) {
				score += (t-aCount);
			} else if(cCount>=aCount && cCount>=gCount && cCount>=tCount) {
				score += (t-cCount);
			} else if(gCount>=aCount && gCount>=cCount && gCount>=tCount) {
				score += (t-gCount);
			}else {
				score += (t-tCount);
			}
			
		}
		
		return score;
	}
	
	
	
	
	/**
	 * Creates profile
	 * Uses pseudocounts
	 * Scores using entropy
	 * Lower scores are "better"
	 * 
	 * 
	 * @param motifs
	 * @return
	 */
	static <T extends CharSequence> double scoreEntropy(ArrayList<T> motifs) {

		int k = motifs.get(0).length();
		
		float[][] profile = createProfile(motifs);
		
		double score = 0;
		
		// iterate through "columns" of profile (positions of the k-mers)
		for(int i=0; i<k; i++) {
			

			// iterate through "rows" of profile
			for(int j=0; j<4; j++) {
				
				float value = profile[j][i];
				score += value * Math.log(value);

			}

		}

		return score * -1;
	}
	
	
	
	
	

	/**
	 * Uses pseudocounts.
	 * 
	 * 
	 * 
	 * 
	 * @param motifs
	 * @return 4xk float[][]   profile
	 */
	static <T extends CharSequence> float[][] createProfile(ArrayList<T> motifs) {
	
		// lenth of k-mers
		int k = motifs.get(0).length();
		
		float[][] profile = new float[4][k];
		
		
		// iterate through motifs
		for(CharSequence motif : motifs) {
			
			// iterate through letters in motif
			for(int index=0; index<motif.length(); index++) {
				int letterAsNum = letterToNum(motif.charAt(index));
				profile[letterAsNum][index]++;
			}
			
		}
		
		
		// iterate through profile,
		// add pseudocounts,
		// factor in denominator
		float denominator = 4*k + motifs.size()*k; // pseudocounts + actual counts
		for(int i=0; i<4; i++)
			for(int j=0; j<k; j++) {
				profile[i][j]++;
				profile[i][j] /= denominator;
			}
	
		
		
		
		return profile;
	}


	

	
	
	
	
	
	
	
}
