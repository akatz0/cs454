import java.io.Console;
import java.io.IOException;
import java.util.*;

/**
Team Members: Darin Brown, Ariel Katz, and Jack Weatherford

Game grid contains a series of cells
 */
public class Grid {

  ArrayList<GridCell> gridList = new ArrayList<GridCell>();
  static Hashtable<String, GridCell> table = new Hashtable<String, GridCell>();
  Stack<String> nextSnakeOption;
  boolean solved;

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
      solved = false;
      nextSnakeOption = new Stack<String>();
      nextSnakeOption.push("EMPTY"); // bottom of stack marker
  }

  public boolean solve(){
    int currentIndex = 0;
    GridCell currentCell = gridList.get(0);
    //currentCell.setLocation = GridPoint.ONE; //Initialize snake to start in upper left corner of the grid
    boolean result = currentCell.makeSnake("ONE", null);
    while (result){
      /** TODO -- when moving to solve next cell we need to make sure we copy values of duplicate edges */
      String temp = nextSnakeOption.pop();
      if(temp == "EMPTY") return false; // if there are no more options to try then grid has no alternate solutinos
      result=false;// stop the infinite loop
    }
    return false;
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
