import java.util.*;
import java.math.*;

/**
Problem 2:
Write a function MinString that takes as input a DFA and outputs a string w of shortest 
length (lexicographically first in case of more than one string) accepted by the DFA. 
 */

public class prob2{

	public static void runProblem2(int n, ArrayList<Integer> digitsAllowed){
		System.out.println("Problem 2");
		
		Collections.sort(digitsAllowed); // sort the digits from least to greatest

		int counter=0;

		// n rows, only columns for the digits allowed
		// even though we expect a big number we can use an int array because we will be 
		// builing the number using a breadth first search and not using math functions
		int[][] modMatrix = new int[n][9];
		for (int i=0; i<modMatrix.length; i++) {
			for (int j=0; j<modMatrix[0].length; j++) {
				modMatrix[i][j] = counter++;
				if(counter == n){
					counter = 0; // reset counter
				}
			}			
		}

		//Attempting to re-write transition array with only columns for the allowed digits
		int digitIndex = 0;
		int[][] modMatrix2 = new int[n][digitsAllowed.size()];
		for (int j = 0; j < modMatrix2.length; j++) {
            for (int k = 0; k < modMatrix2[j].length; k++) {
            	if (digitsAllowed.contains(k)){
            		modMatrix2[j][digitIndex++] = modMatrix[j][k];
            	}
            }
            digitIndex=0;
        }

	}

}