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
      if(temp == "EMPTY") return false; // if there are no more options to try then grid has no alternate solutions
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
      temp.setT34(true);
      temp.location = "FOUR";
      table.put("1_1_THREE", temp);
      temp.setT34(false);
      temp.location = "FALSE";
      table.put("1_1_ONE", temp);
      table.put("1_1_FIVE", temp);

      temp = new GridCell(0, 1, 3);
      temp.setT12(true); // single top line and all three on bottom half
      temp.setT35(true);
      temp.setT56(true);
      temp.setT46(true);
      temp.location = "TWO"; //setting the final locations
      temp.location2 = "FOUR";
      table.put("1_3_ONE_THREE", temp); // only valid solution with these values and two locations

      temp = new GridCell(0, 1, 3);
      temp.setT13(true); 
      temp.setT35(true);
      temp.setT56(true);
      temp.setT46(true);
      temp.location = "THREE"; //setting the final location
      table.put("1_3_ONE", temp); // only valid solution with these values and location

      temp = new GridCell(0, 1, 3);
      temp.setT35(true);
      temp.setT56(true);
      temp.setT46(true);
      temp.setT24(true);
      temp.location = "TWO"; 
      table.put("1_3_THREE", temp); // only valid solution with these values and location

      /* For numbering of temps see image file top2_bneg1.jpg */
      ArrayList<GridCell> t2bneg1 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_-1_ONE"
      GridCell temp1 = new GridCell(0, 2, -1);
      temp1.setT13(true); 
      temp1.setT34(true);
      temp1.location = "FOUR"; 
      t2bneg1.add(temp1);

      GridCell temp2 = new GridCell(0, 2, -1);
      temp2.setT12(true); 
      temp2.setT24(true);
      temp2.location = "FOUR"; 
      t2bneg1.add(temp2);

      GridCell temp3 = new GridCell(0, 2, -1);
      temp3.setT13(true); 
      temp3.setT35(true);
      temp3.setT56(true); 
      temp3.setT46(true);
      temp3.setT24(true);
      temp3.location = "TWO"; 
      t2bneg1.add(temp3);

      GridCell temp4 = new GridCell(0, 2, -1);
      temp4.setT12(true); 
      temp4.setT24(true);
      temp4.setT46(true);
      temp4.location = "SIX"; 
      t2bneg1.add(temp4);

      /// add list to hash table here

      ArrayList<GridCell> t2bneg1_loc3 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_-1_THREE"
      GridCell temp8 = new GridCell(0, 2, -1);
      temp8.setT12(true); 
      temp8.setT24(true);
      temp8.location = "FOUR"; 
      t2bneg1_loc3.add(temp8);

      GridCell temp12 = new GridCell(0, 2, -1);
      temp12.setT24(true); 
      temp12.setT34(true);
      temp12.location = "TWO"; 
      t2bneg1_loc3.add(temp12);

      /// add array to hash table
      ArrayList<GridCell> t2bneg1_loc5 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_-1_FIVE"
      GridCell temp7 = new GridCell(0, 2, -1);
      temp7.setT35(true);
      temp7.setT34(true);
      temp7.setT24(true);
      t2bneg1_loc5.add(temp7);

      GridCell temp10 = new GridCell(0, 2, -1);
      temp10.setT35(true);
      temp10.setT34(true);
      temp10.setT24(true);
      t2bneg1_loc5.add(temp10);
      //add array to hash table

      ArrayList<GridCell> t2bneg1_1_3 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_-1_ONE_THREE"
      GridCell temp5 = new GridCell(0, 2, -1);
      temp5.setT12(true);
      temp5.setT34(true);
      temp5.location = "TWO";
      temp5.location2 = "FOUR";
      t2bneg1_1_3.add(temp5);

      GridCell temp11 = new GridCell(0, 2, -1);
      temp11.setT12(true);
      temp11.setT24(true);
      temp11.setT35(true);
      temp11.setT56(true);
      temp11.location = "FOUR";
      temp11.location2 = "SIX";
      t2bneg1_1_3.add(temp11);

      //add array to hashtable

      ArrayList<GridCell> t2bneg1_3_5 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_-1_THREE_FIVE"
      GridCell temp9 = new GridCell(0, 2, -1);
      temp9.setT13(true);
      temp9.setT12(true);
      temp9.setT46(true);
      temp9.setT56(true);
      temp9.location = "TWO";
      temp9.location2 = "SIX";
      t2bneg1_3_5.add(temp9);

      GridCell temp13 = new GridCell(0, 2, -1);
      temp13.setT24(true);
      temp13.setT34(true);
      temp13.setT56(true);
      temp13.location = "TWO";
      temp13.location2 = "SIX";
      t2bneg1_3_5.add(temp13);

      ArrayList<GridCell> t2bneg1_1_5 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_-1_ONE_FIVE"
      GridCell temp6 = new GridCell(0, 2, -1);
      temp6.setT12(true);
      temp6.setT34(true);
      temp6.setT35(true);
      temp6.location = "TWO";
      temp6.location2 = "FOUR";
      t2bneg1_1_5.add(temp6);

      temp = new GridCell(0, 2, -1); //This would ONLY work as the last cell...
      temp.setT12(true);
      temp.setT24(true);
      temp.setT46(true);
      temp.setT56(true);
      temp.location = "ONE";
      temp.location2 = "FIVE";
      t2bneg1_1_5.add(temp);

      //add array to hashtable
	  
	  
	  /* top3_bot1_through_top3_bot3.jpg */
	  // Configuration index 1 in top3_bot1_through_top3_bot3.jpg
	  temp = new GridCell(0, 3, 1);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT24(true);
	  temp.location = "TWO"; //setting the final location
	  table.put("3_1_ONE", temp); // only valid solution with these values and location

	  // 2
	  temp = new GridCell(0, 3, 1);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT34(true);
	  temp.location = "ONE";
	  temp.location2 = "THREE";
	  table.put("3_1_ONE_THREE", temp);
	  
	  // 3
	  temp = new GridCell(0, 3, 1);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT24(true);
	  temp.setT35(true);
	  temp.location = "FOUR";
	  table.put("3_1_FIVE", temp);
	  
	  // 4
	  temp = new GridCell(0, 3, 1);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT24(true);
	  temp.setT56(true);
	  temp.location = "FOUR";
	  temp.location2 = "SIX";
	  table.put("3_1_THREE_FIVE", temp);
	  
	  // 5
	  temp = new GridCell(0, 3, 1);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  table.put("3_1_THREE", temp);
	  
	  // 6 (Not sure if correct)
	  temp = new GridCell(0, 3, 1);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.location = "TWO";
	  temp.location2 = "FOUR";
	  table.put("3_1_TWO_FOUR", temp);

	  // top3_bot2
	  // 7
	  temp = new GridCell(0, 3, 2);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.setT56(true);
	  temp.location = "THREE";
	  temp.location2 = "FIVE";
	  table.put("3_2_THREE_FIVE", temp);
	  
	  ArrayList<GridCell> t3b2_1_5 = new ArrayList<GridCell>(); //TODO add to hash table, key "3_2_ONE_FIVE"
	  // 8
	  temp = new GridCell(0, 3, 2);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT34(true);
	  temp.setT35(true);
	  temp.location = "ONE";
	  temp.location2 = "FIVE";
	  t3b2_1_5.add(temp);
	  
	  // 9
	  temp = new GridCell(0, 3, 2);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT24(true);
	  temp.setT56(true);
	  temp.location = "TWO";
	  temp.location2 = "SIX";
	  t3b2_1_5.add(temp);
	  
	  //add array to hashtable
	  
	  // 10 
	  temp = new GridCell(0, 3, 2);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.location = "TWO";
	  temp.location2 = "SIX";
	  table.put("3_2_TWO_SIX", temp);
	  
	  // 11
	  temp = new GridCell(0, 3, 2);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT24(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.location = "FOUR";
	  temp.location2 = "SIX";
	  table.put("3_2_FOUR_SIX", temp);
	  
	  // TODO: 12 and 13
	  
	  // top3_bot3
	  // 14
	  temp = new GridCell(0, 3, 3);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.setT56(true);
	  temp.location = "TWO";
	  table.put("3_3_FIVE", temp);
	  
	  // 15
	  temp = new GridCell(0, 3, 3);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT34(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.location = "SIX";
	  table.put("3_3_ONE", temp);
	  
	  // TODO: -1's
   }

}
