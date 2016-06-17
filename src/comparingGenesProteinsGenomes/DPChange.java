package comparingGenesProteinsGenomes;

import java.util.ArrayList;
import java.util.Scanner;

public class DPChange {

	public static void main(String[] args) {

		int money;
		int[] coins;
		
		
		try(Scanner reader = util.IOUtilities.getScanner(args[0])) {
			
			reader.useDelimiter("\\p{javaWhitespace}+|,");
			System.out.println(reader.delimiter());

			money = reader.nextInt();
			
			ArrayList<Integer> coinsList = new ArrayList<Integer>();
			
			while(reader.hasNextInt()) {
				coinsList.add(new Integer(reader.nextInt()));
			}
			
			coins = new int[coinsList.size()];
			for(int i=0; i<coinsList.size(); i++) {
				coins[i] = coinsList.get(i);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		System.out.println(money);
		util.IOUtilities.printArray("coins", coins);
		
		
		int[] minNumCoins = new int[money+1];
		
		// populate minNumCoins 
		
		for(int m=1; m<= money; m++) {
			
			minNumCoins[m] = Integer.MAX_VALUE;
			
			for( int coin : coins) 
				
				if(m >= coin) 
				
					if( minNumCoins[m-coin] +1 < minNumCoins[m]) 
						
						minNumCoins[m] = minNumCoins[m-coin] +1;

			
		}
		
		System.out.println(minNumCoins[money]);

	}
	
	
	
	
	
	
	
	
	
	
	

}
