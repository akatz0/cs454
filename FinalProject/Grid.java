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
      currentIndex++;
      if(currentIndex >= gridList.size()){
        result=false;break;// stop the infinite loop
      } 
      GridCell prevCell = currentCell;
      currentCell = gridList.get(currentIndex);
      copyOverlap(prevCell, currentCell);
      result = currentCell.makeSnake(currentCell.location, null);
    }
    return false;
  }

  /* This method is meant to copy the duplicate information of from one cell to the next 
      (The possible transitions and locations)

    GridPoint ONE ----------- t12 ---------------- GridPoint TWO
    |                                                   |
    |                                                   |
    |                                                   |
    t13                                                t24
    |                                                   |
    |                                                   |
    |                                                   |
    GridPoint THREE ----------t34 -----------------GridPoint FOUR
    |                                                   |
    |                                                   |
    |                                                   |
    t35                                                t46
    |                                                   |
    |                                                   |
    |                                                   |
    GridPoint FIVE ----------t56 ------------------GridPoint SIX

  */
  public void copyOverlap(GridCell first, GridCell second){
    second.setT13(first.getT24());
    second.setT35(first.getT46());
    if(first.location == "TWO") second.location = "ONE";
    if(first.location == "FOUR") second.location = "THREE";
    if(first.location == "SIX") second.location = "FIVE";
  }

  public void print(){
    for ( GridCell g : gridList) {
      g.print();
    }
  }

  public static void main(String[] args) {
      Grid test = new Grid("[3,-1][-1, 2][-1, 3][1, 2][3,-1]");
      test.print();

      //Manually inputting some cells for the solution table (one location only)
      GridCell temp = new GridCell(0, 0, 0); // any cell with 0 and 0 as values will result in a failed grid
      temp.location = "FALSE";
      table.put("0_0_ONE", temp);
      table.put("0_0_THREE", temp);
      table.put("0_0_FIVE", temp);
      temp = new GridCell(0, 1, 1);
      temp.setT34 = true;
      temp.location = "FOUR";
      table.put("1_1_THREE", temp);
      temp.setT34 = false;
      temp.location = "FALSE";
      table.put("1_1_ONE", temp);
      table.put("1_1_FIVE", temp);

      temp = new GridCell(0, 1, 2);// two valid solutions for each location, how will we store in table?

   }

}
