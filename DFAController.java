import java.io.Console;
import java.io.IOException;
import java.util.*;

/**
Problem 1:
In any substring of length 4 of w, all three letters a, b and c occur at least once. For 
example, strings like abbcaabca satisfy the property, but a string like bacaabbcabac
does not satisfy the property since the substring a abb does not have a c. 

Problem 2:
Write a function MinString that takes as input a DFA and outputs a string w of shortest 
length (lexicographically first in case of more than one string) accepted by the DFA. 
 */
public class DFAController {

   public static void main(String[] args) {

      String prompt = "Enter the digit of the operation you'd like to do: %n" +
                     "1 - Problem 1 %n" +
                     "2 - Problem 2 %n" +
                     "3 - Exit %n";
      
      Console input = System.console();
      String problemSelection = input.readLine(prompt);
      while(!problemSelection.equals("3")){

         if(problemSelection.equals("1")){
            String nStr = input.readLine("Enter an n value between 1 and 300: ");
            int n = Integer.parseInt(nStr);
            if ( n > 0 && n < 301){
               // Temporary echo until problem is implemented
              input.printf("You entered "+nStr +"\n");

              // New prob1 object to access prob1 main
              prob1 P = new prob1();

              // Call prob1 passing it user entered n
              P.runProblem1(n);

              // print answer returned by prob1 main
              //input.printf("Answer %d", answer);
            }
         } else if(problemSelection.equals("2")){
            String kStr = input.readLine("Enter a k value: ");
            String pdStr = input.readLine("Enter a permitted digits (delimited by spaces: ");
               // Temporary echo until problem is implemented
              input.printf("You entered k="+kStr + " permitted digits - "+pdStr);
              String[] digits = pdStr.split(" ");
              int[] nums = new int[digits.length];
              ArrayList<Integer> a = new ArrayList<Integer>();
              for (int i=0; i< digits.length; i++) {
                nums[i] = Integer.parseInt(digits[i]);
                a.add(new Integer(digits[i]));
              }
              prob2 p2 = new prob2();
              p2.runProblem2(Integer.parseInt(kStr), a);
            
         }
         else{ input.printf("This selection is invalid. %n");}
         input.printf("%n");
         problemSelection = input.readLine(prompt);
      }
      input.printf("Bye!%n");
   }

}
