import java.util.*;
import java.math.*;

/**
Problem 2:
Write a function minString that takes as input a DFA and outputs a string w of shortest 
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
		int[][] modMatrix = new int[n][10];
		for (int i=0; i<modMatrix.length; i++) {
			for (int j=0; j<modMatrix[0].length; j++) {
				modMatrix[i][j] = counter++;
				if(counter == n){
					counter = 0; // reset counter
				}
			}			
		}

		//Copy only columns of the transition array with the allowed digits
		int digitIndex = 0;
		int[][] modMatrix2 = new int[n][digitsAllowed.size()];
		for (int j = 0; j < modMatrix2.length; j++) {
			for(Integer io : digitsAllowed ){
				int d = (int)io;
				modMatrix2[j][digitIndex] = modMatrix[j][d];
				digitIndex++;
			}
            digitIndex=0;
        }

        minString(modMatrix2, n, digitsAllowed);
	}

	public static void minString(int[][] delta, int n, ArrayList<Integer> digitsAllowed){

		boolean found = false;
		boolean[] visited = new boolean[n];
		// what are the accept states??
		boolean[] acceptStates = new boolean[n];

		for (boolean a : acceptStates) {
		    a = false;
        }
        // The only accepting state is the initial state
        acceptStates[0] = true;

		for (boolean v : visited ) {
			v = false;
		}

		LinkedList<Integer> q = new LinkedList<Integer>();
		// LinkedList<Integer> label = new LinkedList<Integer>();
		// LinkedList<Integer> parent = new LinkedList<Integer>();
        int[] label = new int[n];
        int[] parent = new int[n];

		q.add(0);
		visited[0] = true;

		while(!q.isEmpty()){
			int current = (int)q.removeFirst(); //cast Integer down to int
			for(int k=0; k<digitsAllowed.size(); k++){
				int next = delta[current][k];
				if(acceptStates[next]){
					// label.add(next, digitsAllowed.get(k));
					// parent.add(next, current);
                    label[next] = digitsAllowed.get(k);
                    parent[next] = current;
					found = true;
					break;
				} else {
					if(!visited[next]){
						// parent.add(next, current);
                        parent[next] = current;
                        visited[next] = true;
						// label.add(next, digitsAllowed.get(k));
                        label[next] = digitsAllowed.get(k);
						q.add(next);
					}
				}

			}
		}

		if(!found){
			System.out.println("No solution could be found");
		} else {
			System.out.println("Solution found");
			//Right now this just loops backwards; might need more complicated traversal
			/*for(int z=parent.size()-1; z>=0; z--){
				int current = (int)parent.get(z);
				System.out.print(label.get(z));
			}*/
			// TODO: Print the correct answer
			for (int z = parent.length - 1; z >= 0; z--) {
			    System.out.print(label[parent[z]]);
            }
		}

	}

}