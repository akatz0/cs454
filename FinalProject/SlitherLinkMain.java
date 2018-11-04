import java.io.Console;
import java.io.IOException;
import java.io.File;
import java.util.*;

/**
Team Members: Darin Brown, Ariel Katz, and Jack Weatherford

 */
public class SlitherLinkMain {

   public static void main(String[] args) {

      String prompt = "Select input string type or exit %n" +
                     "1 - Enter grid string on console %n" +
                     "2 - Select file containing grid %n" +
                     "3 - Exit %n";
      
      Console input = System.console();
      String problemSelection = input.readLine(prompt);
      while(!problemSelection.equals("3")){

         if(problemSelection.equals("1")){
            String grid = input.readLine("Enter grid string: \n");
            // Temporary echo until problem is implemented
            input.printf("You entered "+grid +"\n");
            
         } else if(problemSelection.equals("2")){
            String fileName = input.readLine("Enter file containing grid: \n");
            try {
              String gridFile = readFile(fileName);
              // Temporary echo until problem is implemented
              input.printf("You entered "+gridFile +"\n");}
            catch( IOException e){
              System.out.println("There was an error reading the file "+ e);
            }
         }
         else{ input.printf("This selection is invalid. %n");}
         input.printf("%n");
         problemSelection = input.readLine(prompt);
      }
      input.printf("Bye!%n");
   }

   private static String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());        

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }

}
