import java.io.Console;
import java.io.IOException;
import java.util.*;

/**
Team Members: Darin Brown, Ariel Katz, and Jack Weatherford

Game grid contains a series of cells
 */
public class Grid {

  ArrayList<GridCell> gridList = new ArrayList<GridCell>();

  public Grid(String s){
    System.out.println(s);
      String[] arrOfStr = s.split("\\[|,|\\]"); 
      int i=0;
      int j=1; // first value is empty because of split
      while (j < arrOfStr.length-1){
          //trim white space and convert input to integers
          gridList.add(new GridCell(i, Integer.parseInt(arrOfStr[j].trim()), Integer.parseInt(arrOfStr[j+1].trim())));
          i++; //increment index
          j += 3; //skip two values we've already processed and empty value
      }
  }

  public void print(){
    for ( GridCell g : gridList) {
      g.print();
    }
  }

  public static void main(String[] args) {
      Grid test = new Grid("[3,-1][-1, 2][-1, 3][1, 2][3,-1]");
      test.print();
   }

}
