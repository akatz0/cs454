
import java.math.*;
import java.math.BigInteger;

import java.util.Arrays;
import java.lang.Object;
import java.util.Comparator;

/**
 Team Members: Darin Brown, Ariel Katz, and Jack Weatherford

 Problem 1:
 In any substring of length 4 of w, all three letters a, b and c occur at least once. For
 example, strings like abbcaabca satisfy the property, but a string like bacaabbcabac
 does not satisfy the property since the substring a abb does not have a c.
 */


public class prob1{

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
            {37,37,17}, /* 13 -- state ‘aab’*/
            {37,19,37}, /* 14 -- state ‘aac’*/
            {37,37,23}, /* 15 state ‘aba’*/
            {37,37,25}, /* 16 state -- state ‘abb’*/
            {26,27,28}, /* 17 state ‘abc’*/
            {37,30,37}, /* 18 state ‘aca’*/
            {32,33,34}, /* 19 state ‘acb’*/
            {37,36,37}, /* 20 state ‘acc’*/
            {37,37,14}, /* 21 state -- state ‘baa’*/
            {37,37,17}, /* 22 state ‘bab’*/
            {18,19,20}, /* 23 state ‘bac’*/
            {37,37,23}, /* 24 -- state ‘bba’*/
            /* skipping bbb*/
            {26,37,37}, /* 25 -- state ‘bbc’*/
            {29,30,31}, /* 26 state ‘bca’*/
            {32,37,37}, /* 27 state ‘bcb’*/
            {35,37,37}, /* 28 state ‘bcc’*/
            {37,13,37}, /* 29 state -- state ‘caa’*/
            {15,16,17}, /* 30 state ‘cab’*/
            {37,19,37}, /* 31 state ‘cac’*/
            {21,22,23}, /* 32 state ‘cba’*/
            {24,37,37}, /* 33 state ‘cbb’*/
            {26,37,37}, /* 34 state ‘cbc’*/
            {37,30,37}, /* 35 state ‘cca’*/
            {32,37,37}, /* 36 state ‘ccb’*/
            {37,37,37}}; /* 37 -- fail state */

    //Helper method to print out the contents of a 2D integer matrix
    public static void print2DMatrix(BigInteger[][] testMatrix){
        for (int j = 0; j < testMatrix.length; j++) {
            for (int k = 0; k < testMatrix[j].length; k++) {
                System.out.print( testMatrix[j][k] + " ");
            }
            System.out.println();
        }
    }

    public static void runProblem1(int n){
        int MATRIX_SIZE = 38;
        BigInteger[][] transMatrix = new BigInteger[MATRIX_SIZE][MATRIX_SIZE];

        // initialize 0s in transitional matrix
        for (int j = 0; j < transMatrix.length; j++) {
            for (int k = 0; k < transMatrix[j].length; k++) {
                transMatrix[j][k] = BigInteger.valueOf(0);
            }
        }

        // counts amount of transitions from each states and updates 2D list
        for (int j = 0; j < transitionTable.length; j++) {
            for (int k = 0; k < 3; k++) {
                transMatrix[j][transitionTable[j][k]] = transMatrix[j][transitionTable[j][k]].add(BigInteger.valueOf(1));
            }
        }

        // initializes start state
        BigInteger[][] startStates = new BigInteger[1][MATRIX_SIZE]; //one row, n columns
        startStates[0][0] = BigInteger.valueOf(1);
        for (int i = 1; i < MATRIX_SIZE; i++) {
            startStates[0][i] = BigInteger.valueOf(0);
        }

        // initializes accepting states
        BigInteger[][] acceptStates = new BigInteger[MATRIX_SIZE][1]; //n rows, 1 column
        for (int i = 0; i < acceptStates.length - 1; i++) {
            acceptStates[i][0] = BigInteger.valueOf(1);
        }
        acceptStates[MATRIX_SIZE-1][0] = BigInteger.valueOf(0);

        BigInteger[][] atoTheNthPower = aToTheN(transMatrix, n);

        BigInteger[][] finalMatrix = (BigInteger[][]) multiplyBIM(multiplyBIM(startStates,atoTheNthPower),acceptStates);
        print2DMatrix(finalMatrix);
    }

    public static void main(String[] args){

        System.out.println("n = 3, the output should be 24");
        runProblem1(3);
        System.out.println("n = 4, the output should be 36");
        runProblem1(4);
        System.out.println("n = 5, the output should be 72");
        runProblem1(5);
        System.out.println("n = 45, the output should be 2747581609272");
        runProblem1(45);
    }
    //TODO: test output
    // method to raise transMatrix to power of n
    public static BigInteger[][] aToTheN(BigInteger[][] a, int n) {
        BigInteger[][] b = a;
        for (int i = 0; i < n-1; i++) {
            b = multiplyBIM(a,b);
        }
        return b;
    }

    // array-matrix multiplication (d = A * M)
    public static BigInteger[] AmultiplyM(BigInteger[] a, BigInteger[][] b) {
        int m = b.length;
        int n = b[0].length;
        BigInteger[] d = new BigInteger[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                d[j] = d[j].add(b[i][j].multiply(a[i]));
        return d;
    }

    // matrix-array multiplication (c = M * A)
    public static BigInteger[] MmultiplyV(BigInteger[][] ma, BigInteger[] a) {
        int m = ma.length;
        int n = ma[0].length;
        BigInteger[] c = new BigInteger[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i] = c[i].add(ma[i][j].multiply(a[j]));
        return c;
    }
    // method to multiply two 2D matrices
    public static BigInteger[][] multiplyBIM(BigInteger[][] a, BigInteger[][] b) {
        int m1 = a.length; // a rows
        int n1 = a[0].length; // a columns
        int m2 = b.length; // b rows
        int n2 = b[0].length; // b columns
        // if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        BigInteger[][] c = new BigInteger[m1][n2];
        for (int j = 0; j < c.length; j++) {
            for (int k = 0; k < c[j].length; k++) {
                c[j][k] = BigInteger.valueOf(0);
            }
        }
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++) {
                    if (a[i][k] != null && b[k][j] != null)
                        c[i][j] = c[i][j].add(a[i][k].multiply(b[k][j]));
                }
        return c;
    }

}

