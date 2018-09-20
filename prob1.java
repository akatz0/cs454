import java.math.BigInteger;
import java.math.*;
import java.util.Arrays;

public class prob1{

    // transitional matrix, counts how many transitions between each state
    public static int[][] transMatrix = {};

    // 1 0 0 0 0 ... 0
    public static int[][] startStates = {};

    // 1 1 1 1 1 ... 0
    public static int[][] acceptStates = {};

	/** 38 states, 3 letters in the language 
		int[][0] = next state on input a
		int[][1] = next state on input b
		int[][2] = next state on input c
	**/
	public static int[][] transitionTable = {
			{1,2,3}, /* // start state*/
			{4,5,6}, /* 1 -- state ‘a’ */
			{7,8,9}, /* 2--- state ‘b’*/
			{10,11,12}, /* 3 -- state ‘c’*/
			{37,13,14}, /* 4--- state ‘aa’*/
			{15,16,17}, /* 5 state ‘ab’*/
			{18,19,20}, /* 6 state ‘ac’*/
			{21,22,23}, /* 7 state ‘ba’*/
			{24,37,25}, /* 8 state ‘bb’*/
			{26,27,28}, /* 9 state ‘bc’*/
			{29,30,31}, /* 10 state ‘ca’*/
			{32,33,34}, /* 11 state ‘cb’*/
			{35,36,37}, /* 12 state ‘cc’*/
		/* 3 letter states*/
		/* skipping unnecessary state aaa*/
			{15,16,17}, /* 13 -- state ‘aab’*/
			{18,19,20}, /* 14 -- state ‘aac’*/
			{21,22,23}, /* 15 state ‘aba’*/
			{37,24,25}, /* 16 state 15 -- state ‘abb’*/
			{26,27,28}, /* 17 state ‘abc’*/
			{29,30,31}, /* 18 state ‘aca’*/
			{32,33,34}, /* 19 state ‘acb’*/
			{35,36,37}, /* 20 state ‘acc’*/
			{37,13,14}, /* 21 state 20 -- state ‘baa’*/
			{15,16,17}, /* 22 state ‘bab’*/
			{18,19,20}, /* 23 state ‘bac’*/
			{21,22,23}, /* 24 -- state ‘bba’*/
			/* skipping bbb*/
			{26,27,28}, /* 25 -- state ‘bbc’*/
			{29,30,31}, /* 26 state ‘bca’*/
			{32,33,34}, /* 27 state ‘bcb’*/
			{35,36,37}, /* 28 state ‘bcc’*/
			{37,13,14}, /* 29 state 28 -- state ‘caa’*/
			{15,16,17}, /* 30 state ‘cab’*/
			{18,19,20}, /* 31 state ‘cac’*/
			{21,22,23}, /* 32 state ‘cba’*/
			{37,24,25}, /* 33 state ‘cbb’*/
			{26,27,28}, /* 34 state ‘cbc’*/
			{29,30,31}, /* 35 state ‘cca’*/
			{32,33,34}, /* 36 state ‘ccb’*/
			{37,37,37}}; /* 37 -- fail state */

	public static int main(int n){
	    // initialize 0s in transitional matrix
		for (int j = 0; j < transitionTable.length; j++) {
            for (int k = 0; k < transitionTable.length; k++) {
                transMatrix[j][k] = 0;
            }
        }

        // counts amount of transitions from each states and updates 2D list
        for (int j = 0; j < transitionTable.length; j++) {
            for (int k = 0; k < 3; k++) {
                transMatrix[j][transitionTable[j][k]]++;
            }
        }

        // initializes start state
        startStates[0][0] = 1;
        for (int i = 1; i < transitionTable.length; i++) {
            startStates[i][0] = 0;
        }

        // initializes accepting states
        for (int i = 0; i < transitionTable.length - 1; i++) {
            acceptStates[0][i] = 1;
        }
        acceptStates[0][transitionTable.length-1] = 0;

		int[][] AtoTheNthPower = aToTheN(transMatrix, n);

		int[][] finalMatrix = multiply(multiply(startStates,AtoTheNthPower),acceptStates);

		// return answer
		return finalMatrix[0][0];
    }
        //TODO: test output
    // method to raise transMatrix to power of n
    public static int[][] aToTheN(int[][] a, int n) {
        int[][] b = a;
        for (int i = 0; i < n; i++) {
            b = multiply(a,b);
        }
        return b;
    }

    // array-matrix multiplication (d = A * M)
    public static int[] AmultiplyM(int[] a, int[][] b) {
        int m = b.length;
        int n = b[0].length;
        int[] d = new int[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                d[j] += b[i][j] * a[i];
        return d;
    }

    // matrix-array multiplication (c = M * A)
    public static int[] MmultiplyV(int[][] ma, int[] a) {
        int m = ma.length;
        int n = ma[0].length;
        int[] c = new int[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i] += ma[i][j] * a[j];
        return c;
    }
	// method to multiply two 2D matrices
	public static int[][] multiply(int[][] a, int[][] b) {
		int m1 = a.length;
		int n1 = a[0].length;
		int m2 = b.length;
		int n2 = b[0].length;
		if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
		int[][] c = new int[m1][n2];
		for (int i = 0; i < m1; i++)
			for (int j = 0; j < n2; j++)
				for (int k = 0; k < n1; k++)
					c[i][j] += a[i][k] * b[k][j];
		return c;
	}

}


