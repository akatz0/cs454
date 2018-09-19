
public class prob1{

    // transitional matrix, counts how many transitions between each state
    public static int[][] transMatrix = {};

    // 1 0 0 0 0 ... 0
    public static int[][] startStates = {};

    // 0 0 0 0 0 ... 1
    public static int[][] acceptStates = {};

	/** 38 states, 3 letters in the language 
		int[][0] = next state on input a
		int[][1] = next state on input b
		int[][2] = next state on input c
	**/
	public static int[][] transitionTable = {{3,4,5}, /*start state ‘a’ */
			{6,7,8}, /* state ‘b’*/
			{9,10,11}, /* state ‘c’*/
			{37,12,13}, /* state 3 -- state ‘aa’*/
			{14,15,16}, /* state ‘ab’*/
			{17,18,19}, /* state ‘ac’*/
			{20,21,22}, /* state ‘ba’*/
			{23,37,24}, /* state ‘bb’*/
			{25,26,27}, /* state ‘bc’*/
			{28,29,30}, /* state ‘ca’*/
			{31,32,33}, /* state ‘cb’*/
			{34,35,37}, /* state ‘cc’*/
		/* 3 letter states*/
		/* skipping unnecessary state aaa*/
			{14,15,16}, /* state 12 -- state ‘aab’*/
			{17,18,19}, /* state ‘aac’*/
			{20,21,22}, /* state ‘aba’*/
			{23,37,24}, /* state 15 -- state ‘abb’*/
			{25,26,27}, /* state ‘abc’*/
			{28,29,30}, /* state ‘aca’*/
			{31,32,33}, /* state ‘acb’*/
			{34,35,37}, /* state ‘acc’*/
			{37,12,13}, /* state 20 -- state ‘baa’*/
			{14,15,16}, /* state ‘bab’*/
			{17,18,19}, /* state ‘bac’*/
			{20,21,22}, /* state 23 -- state ‘bba’*/
			/* skipping bbb*/
			{25,26,27}, /* state 24 -- state ‘bbc’*/
			{28,29,30}, /* state ‘bca’*/
			{31,32,33}, /* state ‘bcb’*/
			{34,35,37}, /* state ‘bcc’*/
			{37,12,13}, /* state 28 -- state ‘caa’*/
			{14,15,16}, /* state ‘cab’*/
			{17,18,19}, /* state ‘cac’*/
			{20,21,22}, /* state ‘cba’*/
			{23,37,24}, /* state ‘cbb’*/
			{25,26,27}, /* state ‘cbc’*/
			{28,29,30}, /* state ‘cca’*/
			{31,32,33}, /* state ‘ccb’*/
			{0,0,0},{37,37,37}};

	public static void main(String[] args){

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
            acceptStates[0][i] = 0;
        }
        acceptStates[0][transitionTable.length-1] = 1;


        //TODO: matrix multiplication startStates . transMatrix ^ n . acceptStates



	}


}


