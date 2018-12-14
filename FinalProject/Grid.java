import java.io.Console;
import java.io.IOException;
import java.util.*;

/**
Team Members: Darin Brown, Ariel Katz, and Jack Weatherford

Game grid contains a series of cells
 */
public class Grid {

  ArrayList<GridCell> gridList = new ArrayList<GridCell>();
  static Hashtable<String, Object> table = new Hashtable<String, Object>();
  Stack<GridCell> nextSnakeOption;
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
      nextSnakeOption = new Stack<GridCell>();
      populateTable();
  }

  public void pushSnakeStack(GridCell c){
    nextSnakeOption.push(c);
  }

  // Replace a cell in the list
  // Potentially dangerous method...
  private void replaceCell(int index, GridCell cell){
    cell.index = index;
    gridList.remove(index);
    gridList.add(index, cell);
  }

  public boolean solve(){
    int debugging_counter=0;
    int currentIndex = 0;
    GridCell currentCell = gridList.get(currentIndex);
    //currentCell.setLocation = GridPoint.ONE; //Initialize snake to start in upper left corner of the grid
    boolean result = currentCell.makeSnake(this, currentIndex);
    while (result){
      // move to next cell
      currentIndex++;
      if(currentIndex >= gridList.size()){ // end of board was reached
        //If you made it all the way through and still have result== true then there is a solution
        return true;
      } 
      if(currentIndex == gridList.size() -1){ // if its last cell in the board extra options are available
        currentCell.location = "END";
      }

      GridCell prevCell = currentCell;
      currentCell = gridList.get(currentIndex);
      copyOverlap(prevCell, currentCell);
      result = currentCell.makeSnake(this, currentIndex);

      //  no solution found for current cell lets backtrack...
      while (!result && debugging_counter<100){ // while you haven't found a solution keep looping...
        if(nextSnakeOption.isEmpty()) return false; // if there are no more options to try then grid has no alternate solutions
        GridCell temp = nextSnakeOption.pop(); // get the next configuration to try
        debugging_counter++;

        replaceCell(currentIndex, temp);

        prevCell = gridList.get(currentIndex);
        currentIndex++; //try again from the one after the one you just changed
        if( currentIndex < gridList.size()){
          currentCell = gridList.get(currentIndex);
          copyOverlap(prevCell, currentCell);
          result = currentCell.makeSnake(this, currentIndex);
        } else { return true; }// ???? }
      }

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
    if(second.index == gridList.size() -1){ // if its last cell in the board extra options are available
        second.location = "END"; return;
      }
    if(first.location == "TWO") second.location = "ONE";
    if(first.location == "FOUR") second.location = "THREE";
    if(first.location == "SIX") second.location = "FIVE";
    if(first.location2 == "FOUR") second.location2 = "THREE";
    if(first.location2 == "SIX") second.location2 = "FIVE";
  }

  public void print(){
    for ( GridCell g : gridList) {
      g.print();
    }
  }

  public ArrayList<GridCell> concatenate(ArrayList<GridCell> first, ArrayList<GridCell> second){
    for (GridCell c : second) {
      first.add(c);
    }
    return first;
  }

  //method to add all configurations to table with start and end
  //pass top and bottom values as a string like "0_0" or "2_1"
  private void populateEnds(String values, Object o){
    table.put(values+"_START", o);
    table.put(values+"_END", o);
  }

  public void populateTable(){

      GridCell temp = new GridCell(0, 0, 0); // any cell with 0 and 0 as values will result in a failed grid
      temp.location = "FALSE";
      table.put("0_0_ONE", temp);
      table.put("0_0_THREE", temp);
      table.put("0_0_FIVE", temp);
      populateEnds("0_0", temp);

      ArrayList<GridCell> t1b3 = new ArrayList<GridCell>(); // contains all middle t1b3 configurations
      ArrayList<GridCell> t1b3_start = new ArrayList<GridCell>(); // key "1_2_START"
      ArrayList<GridCell> t1b3_end = new ArrayList<GridCell>(); // key "1_2_END"
      // ariel13_configs.jpg 24
      temp = new GridCell(0, 1, 3);
      temp.setT12(true); // single top line and all three on bottom half
      temp.setT35(true);
      temp.setT56(true);
      temp.setT46(true);
      temp.location = "TWO"; //setting the final locations
      temp.location2 = "FOUR";
      table.put("1_3_ONE_THREE", temp); // only valid solution with these values and two locations
      t1b3.add(temp);

      // ariel13_configs.jpg 22
      temp = new GridCell(0, 1, 3);
      temp.setT13(true); 
      temp.setT35(true);
      temp.setT56(true);
      temp.setT46(true);
      temp.location = "THREE"; //setting the final location
      table.put("1_3_ONE", temp); // only valid solution with these values and location
      t1b3.add(temp);

      // ariel13_configs.jpg 23
      temp = new GridCell(0, 1, 3);
      temp.setT35(true);
      temp.setT56(true);
      temp.setT46(true);
      temp.setT24(true);
      temp.location = "TWO"; 
      table.put("1_3_THREE", temp); // only valid solution with these values and location
      t1b3.add(temp);

      // ariel13_configs.jpg 35
      GridCell temp35 = new GridCell(0, 1, 3);
      temp35.setT34(true);
      temp35.setT35(true);
      temp35.setT56(true);
      temp35.location = "FOUR";
      temp35.location2 = "SIX";
      t1b3_start.add(temp35);

      // ariel13_configs.jpg 36
      GridCell temp36 = new GridCell(0, 1, 3);
      temp36.setT34(true);
      temp36.setT46(true);
      temp36.setT56(true);
      temp36.location = "END";
      t1b3_end.add(temp36);

      t1b3_start = concatenate(t1b3_start, t1b3);
      t1b3_end = concatenate(t1b3_end, t1b3);
      table.put("1_3_START", t1b3_start);
      table.put("1_3_END", t1b3_end);

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

      GridCell temp14 = new GridCell(0, 2, -1);
      temp14.setT13(true); 
      temp14.setT34(true);
      temp14.setT46(true);
      temp14.location = "SIX"; 
      t2bneg1.add(temp14);

      table.put("2_-1_ONE", t2bneg1);

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

      table.put("2_-1_THREE", t2bneg1_loc3);
      t2bneg1 = concatenate(t2bneg1, t2bneg1_loc3);

      ArrayList<GridCell> t2bneg1_loc5 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_-1_FIVE"
      GridCell temp7 = new GridCell(0, 2, -1);
      temp7.setT35(true);
      temp7.setT34(true);
      temp7.setT24(true);
      temp7.location = "TWO";
      t2bneg1_loc5.add(temp7);

      GridCell temp10 = new GridCell(0, 2, -1);
      temp10.setT35(true);
      temp10.setT34(true);
      temp10.setT24(true);
      temp7.location = "TWO";
      t2bneg1_loc5.add(temp10);
      table.put("2_-1_FIVE", t2bneg1_loc5);
      t2bneg1 = concatenate(t2bneg1, t2bneg1_loc5);

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

      table.put("2_-1_ONE_THREE", t2bneg1_1_3);
      t2bneg1 = concatenate(t2bneg1, t2bneg1_1_3);

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

      table.put("2_-1_THREE_FIVE", t2bneg1_3_5);
      t2bneg1 = concatenate(t2bneg1, t2bneg1_3_5);

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

      table.put( "2_-1_ONE_FIVE", t2bneg1_1_5);
      t2bneg1 = concatenate(t2bneg1, t2bneg1_1_5);
      table.put("2_-1_START", t2bneg1);
      t2bneg1.add(temp);
      table.put("2_-1_END", t2bneg1);

      ArrayList<GridCell> t2b0 = new ArrayList<GridCell>();
      //top value 2, bottom value 0
      temp = new GridCell(0, 2, 0);
      temp.setT12(true);
      temp.setT24(true);
      temp.location = "FOUR";
      table.put("2_0_ONE", temp);
      t2b0.add(temp);

      temp = new GridCell(0, 2, 0);
      temp.setT13(true);
      temp.setT12(true);
      temp.location = "TWO";
      table.put("2_0_THREE", temp);
      t2b0.add(temp);
      populateEnds("2_0", t2b0);

      ///// top 2 bottom 1
      /* For numbering of temps see image file top2_bneg1.jpg */
      ArrayList<GridCell> t2b1 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_1_ONE"
      temp1 = new GridCell(0, 2, 1);
      temp1.setT13(true); 
      temp1.setT34(true);
      temp1.location = "FOUR"; 
      t2b1.add(temp1);

      temp2 = new GridCell(0, 2, 1);
      temp2.setT12(true); 
      temp2.setT24(true);
      temp2.location = "FOUR"; 
      t2b1.add(temp2);

      temp4 = new GridCell(0, 2, 1);
      temp4.setT12(true); 
      temp4.setT24(true);
      temp4.setT46(true);
      temp4.location = "SIX"; 
      t2b1.add(temp4);

      table.put( "2_1_ONE", t2b1);

      ArrayList<GridCell> t2b1_loc3 = new ArrayList<GridCell>(); //TODO add to hash table, key "2_1_THREE"
      temp8 = new GridCell(0, 2, 1);
      temp8.setT12(true); 
      temp8.setT24(true);
      temp8.setT56(true);
      temp8.location = "FOUR"; 
      t2b1_loc3.add(temp8);

      temp12 = new GridCell(0, 2, 1);
      temp12.setT24(true); 
      temp12.setT34(true);
      temp12.location = "TWO"; 
      t2b1_loc3.add(temp12);
      table.put( "2_1_THREE", t2b1_loc3);

      t2b1 = concatenate(t2b1, t2b1_loc3);

      temp10 = new GridCell(0, 2, 1);
      temp10.setT35(true);
      temp10.setT34(true);
      temp10.setT24(true);
      table.put("2_1_FIVE", temp10);
      t2b1.add(temp10);

      temp5 = new GridCell(0, 2, 1);
      temp5.setT12(true);
      temp5.setT34(true);
      temp5.location = "TWO";
      temp5.location2 = "FOUR";
      table.put("2_1_ONE_THREE", temp5);
      t2b1.add(temp5);
      populateEnds("2_1", t2b1);

      ArrayList<GridCell> t2b2 = new ArrayList<GridCell>(); 
      // top value 2, bottom value 2
      temp7 = new GridCell(0, 2, 2);
      temp7.setT35(true);
      temp7.setT34(true);
      temp7.setT24(true);
      temp7.location = "TWO";
      table.put("2_2_FIVE", temp7);
      t2b2.add(temp7);

      temp14 = new GridCell(0, 2, 2);
      temp14.setT13(true); 
      temp14.setT34(true);
      temp14.setT46(true);
      temp14.location = "SIX"; 
      table.put("2_2_ONE", temp14);
      t2b2.add(temp14);

      temp11 = new GridCell(0, 2, 2);
      temp11.setT12(true);
      temp11.setT24(true);
      temp11.setT35(true);
      temp11.setT56(true);
      temp11.location = "FOUR";
      temp11.location2 = "SIX";
      table.put("2_2_ONE_THREE", temp11);
      t2b2.add(temp11);

      temp9 = new GridCell(0, 2, 2);
      temp9.setT13(true);
      temp9.setT12(true);
      temp9.setT46(true);
      temp9.setT56(true);
      temp9.location = "TWO";
      temp9.location2 = "SIX";
      table.put("2_2_THREE_FIVE", temp9);
      t2b2.add(temp9);

      temp = new GridCell(0, 2, 2); //This would ONLY work as the last cell...
      temp.setT12(true);
      temp.setT24(true);
      temp.setT46(true);
      temp.setT56(true);
      temp.location = "ONE";
      temp.location2 = "FIVE";
      table.put("2_2_ONE_FIVE", temp);
      t2b2.add(temp);
      populateEnds("2_2", t2b2);

      //top value 2, bottom value 3
      temp3 = new GridCell(0, 2, 3);
      temp3.setT13(true); 
      temp3.setT35(true);
      temp3.setT56(true); 
      temp3.setT46(true);
      temp3.setT24(true);
      temp3.location = "TWO"; 
      table.put("2_3_ONE", temp3);

      //TODO end only options to be encoded...

      GridCell temp25 = new GridCell(0, 3, 0);
      temp25.setT13(true);
      temp25.setT12(true);
      temp25.setT24(true);
      temp25.location = "FOUR";
      table.put("3_0_THREE", temp25);
      populateEnds("3_0", temp25);

      ArrayList<GridCell> all_t3bneg1 = new ArrayList<GridCell>();
      ArrayList<GridCell> t3bneg1_loc3 = new ArrayList<GridCell>(); //TODO add to hash table, key "3_-1_THREE"
      GridCell temp27 = new GridCell(0, 3, -1);
      temp27.setT13(true); 
      temp27.setT12(true);
      temp27.setT24(true);
      temp27.setT46(true);
      temp27.location = "SIX"; 
      t3bneg1_loc3.add(temp27);

      temp25 = new GridCell(0, 3, -1);
      temp25.setT13(true);
      temp25.setT12(true);
      temp25.setT24(true);
      temp25.location = "FOUR";
      t3bneg1_loc3.add(temp25);
      table.put("3_-1_THREE", t3bneg1_loc3);
      all_t3bneg1 = concatenate(all_t3bneg1, t3bneg1_loc3);

      ArrayList<GridCell> t3bneg1_loc5 = new ArrayList<GridCell>(); //TODO add to hash table, key "3_-1_FIVE"
      GridCell temp26 = new GridCell(0, 3, -1);
      temp26.setT13(true); 
      temp26.setT12(true);
      temp26.setT24(true);
      temp26.setT35(true);
      temp26.location = "FOUR"; 
      t3bneg1_loc5.add(temp26);

      GridCell temp32b = new GridCell(0, 3, -1);
      temp32b.setT13(true); 
      temp32b.setT12(true);
      temp32b.setT56(true);
      temp32b.setT34(true);
      temp32b.setT46(true);
      temp32b.location = "TWO"; 
      t3bneg1_loc5.add(temp32b);

      GridCell temp29 = new GridCell(0, 3, -1);
      temp29.setT13(true); 
      temp29.setT12(true);
      temp29.setT24(true);
      temp29.setT35(true);
      temp29.setT46(true);
      temp29.location = "SIX"; 
      t3bneg1_loc5.add(temp29);
      table.put("3_-1_FIVE", t3bneg1_loc5);
      all_t3bneg1 = concatenate(all_t3bneg1, t3bneg1_loc5);

      GridCell temp31b = new GridCell(0, 3, -1);
      temp31b.setT24(true); 
      temp31b.setT12(true);
      temp31b.setT56(true);
      temp31b.setT34(true);
      temp31b.setT35(true);
      temp31b.location = "SIX"; 
      table.put("3_-1_ONE", temp31b);
      all_t3bneg1.add(temp31b);

      ArrayList<GridCell> t3bneg1_start = new ArrayList<GridCell>(); //TODO add to hash table, key "3_-1_START"
      ArrayList<GridCell> t3bneg1_end = new ArrayList<GridCell>(); //TODO add to hash table, key "3_-1_END"
      GridCell temp32 = new GridCell(0, 3, -1);
      temp32.setT13(true); 
      temp32.setT12(true);
      temp32.setT34(true);
      temp32.setT46(true);
      temp32.location = "TWO"; 
      temp32.location2 = "SIX";
      t3bneg1_start.add(temp32);

      GridCell temp31 = new GridCell(0, 3, -1);
      temp31.setT24(true); 
      temp31.setT12(true);
      temp31.setT34(true);
      temp31.setT35(true);
      temp31.location = "END"; 
      t3bneg1_end.add(temp31);

      t3bneg1_start = concatenate(t3bneg1_start, all_t3bneg1);
      t3bneg1_end = concatenate(t3bneg1_end, all_t3bneg1);
      table.put("3_-1_START", t3bneg1_start);
      table.put("3_-1_END", t3bneg1_end);
      
      ArrayList<GridCell> all_t3b1 = new ArrayList<GridCell>();
    /* top3_bot1_through_top3_bot3.jpg */
    // Configuration index 1 in top3_bot1_through_top3_bot3.jpg
    temp = new GridCell(0, 3, 1);
    temp.setT13(true);
    temp.setT34(true);
    temp.setT24(true);
    temp.location = "TWO"; //setting the final location
    table.put("3_1_ONE", temp); // only valid solution with these values and location
    all_t3b1.add(temp);

    // 2
    temp = new GridCell(0, 3, 1);
    temp.setT12(true);
    temp.setT24(true);
    temp.setT34(true);
    temp.location = "ONE";
    temp.location2 = "THREE";
    table.put("3_1_ONE_THREE", temp);
    all_t3b1.add(temp);
    
    // 3
    temp = new GridCell(0, 3, 1);
    temp.setT12(true);
    temp.setT13(true);
    temp.setT24(true);
    temp.setT35(true);
    temp.location = "FOUR";
    table.put("3_1_FIVE", temp);
    all_t3b1.add(temp);
    
    // 4
    temp = new GridCell(0, 3, 1);
    temp.setT12(true);
    temp.setT13(true);
    temp.setT24(true);
    temp.setT56(true);
    temp.location = "FOUR";
    temp.location2 = "SIX";
    table.put("3_1_THREE_FIVE", temp);
    all_t3b1.add(temp);
    
    // 5
    temp = new GridCell(0, 3, 1);
    temp.setT12(true);
    temp.setT13(true);
    temp.setT24(true);
    temp.setT46(true);
    temp.location = "SIX";
    table.put("3_1_THREE", temp);
    all_t3b1.add(temp);
    
    // 6
    temp = new GridCell(0, 3, 1);
    temp.setT12(true);
    temp.setT13(true);
    temp.setT34(true);
    temp.location = "TWO";
    temp.location2 = "FOUR";
    table.put("3_1_TWO_FOUR", temp);
    all_t3b1.add(temp);

    populateEnds("3_1", all_t3b1);

    ArrayList<GridCell> t3b2 = new ArrayList<GridCell>();

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
    t3b2.add(temp);
    
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
    
    table.put("3_2_ONE_FIVE", t3b2_1_5);
    t3b2 = concatenate( t3b2, t3b2_1_5);
    
    // 10 
    temp = new GridCell(0, 3, 2);
    temp.setT12(true);
    temp.setT13(true);
    temp.setT34(true);
    temp.setT46(true);
    temp.location = "TWO";
    temp.location2 = "SIX";
    table.put("3_2_TWO_SIX", temp);
    t3b2.add(temp);
    
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
    t3b2.add(temp);
    populateEnds("3_2", t3b2);
    
    // TODO: 12 and 13
    
    ArrayList<GridCell> t3b3 = new ArrayList<GridCell>();
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
    t3b3.add(temp);
    
    // 15
    temp = new GridCell(0, 3, 3);
    temp.setT12(true);
    temp.setT24(true);
    temp.setT34(true);
    temp.setT35(true);
    temp.setT56(true);
    temp.location = "SIX";
    table.put("3_3_ONE", temp);
    t3b3.add(temp);
    populateEnds("3_3", t3b3);
    
	  // Top -1 Bottom -1
	  ArrayList<GridCell> tneg1bneg1 = new ArrayList<GridCell>();
	  
	  // Starting at location ONE
	  ArrayList<GridCell> tneg1bneg1_loc1 = new ArrayList<GridCell>();
	  
	  // Ending at location TWO
	  temp = new GridCell(0, -1, -1);
      temp.setT12(true);
      temp.location = "TWO";
	  tneg1bneg1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc1.add(temp);
	  
	  // Ending at location FOUR
	  temp = new GridCell(0, -1, -1);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc1.add(temp);
	  
	  // Ending at location SIX
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc1.add(temp);
	  
      temp = new GridCell(0, -1, -1);
      temp.setT12(true);
      temp.setT24(true);
      temp.setT34(true);
      temp.setT35(true);
      temp.setT56(true);
      temp.location = "SIX";
	  tneg1bneg1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc1.add(temp);
	  
	  table.put("-1_-1_ONE", tneg1bneg1_loc1);
	  tneg1bneg1 = concatenate(tneg1bneg1, tneg1bneg1_loc1);
	
	  // Starting at location THREE
	  ArrayList<GridCell> tneg1bneg1_loc3 = new ArrayList<GridCell>();
	  
	  // Ending at location TWO
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT34(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc3.add(temp);
	  
	  // Ending at location FOUR
	  temp = new GridCell(0, -1, -1);
	  temp.setT34(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc3.add(temp);
	  
	  // Ending at location SIX
	  temp = new GridCell(0, -1, -1);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc3.add(temp);
	  
	  table.put("-1_-1_THREE", tneg1bneg1_loc3);
	  tneg1bneg1 = concatenate(tneg1bneg1, tneg1bneg1_loc3);
	  
	  // Starting at location FIVE
	  ArrayList<GridCell> tneg1bneg1_loc5 = new ArrayList<GridCell>();
	  
	  // Ending at location TWO
	  temp = new GridCell(0, -1, -1);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.setT56(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT12(true);
	  temp.setT13(true);
	  temp.setT35(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc5.add(temp);
	  
      temp = new GridCell(0, -1, -1);
      temp.setT12(true);
      temp.setT13(true);
      temp.setT34(true);
      temp.setT46(true);
      temp.setT56(true);
      temp.location = "TWO";
	  tneg1bneg1_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT24(true);
	  temp.setT34(true);
	  temp.setT35(true);
	  temp.location = "TWO";
	  tneg1bneg1_loc5.add(temp);
	  
	  // Ending at location FOUR
	  temp = new GridCell(0, -1, -1);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT35(true);
	  temp.setT34(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT35(true);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.location = "FOUR";
	  tneg1bneg1_loc5.add(temp);
	  
	  // Ending at location SIX
	  temp = new GridCell(0, -1, -1);
	  temp.setT56(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT35(true);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, -1);
	  temp.setT35(true);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1bneg1_loc5.add(temp);
	  
	  table.put("-1_-1_FIVE", tneg1bneg1_loc5);
	  tneg1bneg1 = concatenate(tneg1bneg1, tneg1bneg1_loc5);
	  
	  // TODO: Starting at ONE_THREE, ONE_FIVE, THREE_FIVE
	  
	  populateEnds("-1_-1", tneg1bneg1);
	  
	  // Top -1 Bottom 0
	  ArrayList<GridCell> tneg1b0 = new ArrayList<GridCell>();
	  
	  // Starting at location ONE
	  ArrayList<GridCell> tneg1b0_loc1 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 0);
	  temp.setT12(true);
	  temp.location = "TWO";
	  tneg1b0_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, 0);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.location = "FOUR";
	  tneg1b0_loc1.add(temp);
	  
	  table.put("-1_0_ONE", tneg1b0_loc1);
	  tneg1b0 = concatenate(tneg1b0, tneg1b0_loc1);
	  
	  // Starting at location THREE
	  ArrayList<GridCell> tneg1b0_loc3 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 0);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.location = "TWO";
	  tneg1b0_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, 0);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.location = "FOUR";
	  tneg1b0_loc3.add(temp);
	  
	  table.put("-1_0_THREE", tneg1b0_loc3);
	  tneg1b0 = concatenate(tneg1b0, tneg1b0_loc3);
	  
	  populateEnds("-1_0", tneg1b0);
	  
	  // Top -1 Bottom 1
	  ArrayList<GridCell> tneg1b1 = new ArrayList<GridCell>();
	  
	  // Starting at location ONE
	  ArrayList<GridCell> tneg1b1_loc1 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 1);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.location = "FOUR";
	  tneg1b1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, 1);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1b1_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, 1);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1b1_loc1.add(temp);
	  
	  table.put("-1_1_ONE", tneg1b1_loc1);
	  tneg1b1 = concatenate(tneg1b1, tneg1b1_loc1);
	  
	  // Starting at location THREE
	  ArrayList<GridCell> tneg1b1_loc3 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 1);
	  temp.setT34(true);
	  temp.location = "FOUR";
	  tneg1b1_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, 1);
	  temp.setT34(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1b1_loc3.add(temp);
	  
	  table.put("-1_1_THREE", tneg1b1_loc3);
	  tneg1b1 = concatenate(tneg1b1, tneg1b1_loc3);
	  
	  // Starting at location FIVE
	  ArrayList<GridCell> tneg1b1_loc5 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 1);
	  temp.setT56(true);
	  temp.location = "SIX";
	  tneg1b1_loc5.add(temp);
	  
	  table.put("-1_1_FIVE", tneg1b1_loc5);
	  tneg1b1 = concatenate(tneg1b1, tneg1b1_loc5);

	  populateEnds("-1_1", tneg1b1);
	  
	  // Top -1 Bottom 2
	  ArrayList<GridCell> tneg1b2 = new ArrayList<GridCell>();
	  
	  // Starting at location ONE
	  ArrayList<GridCell> tneg1b2_loc1 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT13(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.location = "SIX";
	  tneg1b2_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT13(true);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1b2_loc1.add(temp);
	  
	  table.put("-1_2_ONE", tneg1b2_loc1);
	  tneg1b2 = concatenate(tneg1b2, tneg1b2_loc1);
	  
	  // Starting at location THREE
	  ArrayList<GridCell> tneg1b2_loc3 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1b2_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.location = "SIX";
	  tneg1b2_loc3.add(temp);
	  
	  table.put("-1_2_THREE", tneg1b2_loc3);
	  tneg1b2 = concatenate(tneg1b2, tneg1b2_loc3);
	  
	  // Starting at location FIVE
	  ArrayList<GridCell> tneg1b2_loc5 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.location = "FOUR";
	  tneg1b2_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1b2_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT35(true);
	  temp.setT34(true);
	  temp.location = "FOUR";
	  tneg1b2_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT35(true);
	  temp.setT34(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1b2_loc5.add(temp);
	  
	  temp = new GridCell(0, -1, 2);
	  temp.setT35(true);
	  temp.setT13(true);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1b2_loc5.add(temp);
	  
	  table.put("-1_2_FIVE", tneg1b2_loc5);
	  tneg1b2 = concatenate(tneg1b2, tneg1b2_loc5);
	  
	  // TODO: Starting at ONE_THREE, ONE_FIVE, THREE_FIVE
	  
	  populateEnds("-1_2", tneg1b2);
	  
	  // Top -1 Bottom 3
	  ArrayList<GridCell> tneg1b3 = new ArrayList<GridCell>();
	  
	  // Starting at location ONE
	  ArrayList<GridCell> tneg1b3_loc1 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 3);
	  temp.setT13(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.location = "FOUR";
	  tneg1b3_loc1.add(temp);
	  
	  temp = new GridCell(0, -1, 3);
	  temp.setT12(true);
	  temp.setT24(true);
	  temp.setT34(true);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.location = "SIX";
	  tneg1b3_loc1.add(temp);
	  
	  table.put("-1_3_ONE", tneg1b3_loc1);
	  tneg1b3 = concatenate(tneg1b3, tneg1b3_loc1);
	  
	  // Starting at location THREE
	  ArrayList<GridCell> tneg1b3_loc3 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 3);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.location = "FOUR";
	  tneg1b3_loc3.add(temp);
	  
	  temp = new GridCell(0, -1, 3);
	  temp.setT35(true);
	  temp.setT56(true);
	  temp.setT46(true);
	  temp.setT24(true);
	  temp.location = "TWO";
	  tneg1b3_loc3.add(temp);
	  
	  table.put("-1_3_THREE", tneg1b3_loc3);
	  tneg1b3 = concatenate(tneg1b3, tneg1b3_loc3);
	  
	  // Starting at location FIVE
	  ArrayList<GridCell> tneg1b3_loc5 = new ArrayList<GridCell>();
	  
	  temp = new GridCell(0, -1, 3);
	  temp.setT35(true);
	  temp.setT34(true);
	  temp.setT46(true);
	  temp.location = "SIX";
	  tneg1b3_loc5.add(temp);
	  
	  table.put("-1_3_FIVE", tneg1b3_loc5);
	  tneg1b3 = concatenate(tneg1b3, tneg1b3_loc5);
	  
	  // TODO: Starting at ONE_THREE, ONE_FIVE, THREE_FIVE
	  
	  populateEnds("-1_3", tneg1b3);
	  
      /*  For numbering diagram see TOBneg1_through_T1B2.pdf */
      ArrayList<GridCell> t0bneg1 = new ArrayList<GridCell>();
      // Top 0  Bottom -1
      //0neg11
      ArrayList<GridCell> t0bneg1_loc3 = new ArrayList<GridCell>(); //TODO add to hash table, key "0_neg1_THREE"
      GridCell temp0neg11 = new GridCell(0, 0, -1);
      temp0neg11.setT35(true);
      temp0neg11.setT56(true);
      temp0neg11.setT46(true);
      temp0neg11.location = "FOUR";
      table.put("0_-1_THREE", temp0neg11);
      t0bneg1_loc3.add(temp0neg11);

      //0neg12
      GridCell temp0neg12 = new GridCell(0, 0, -1);
      temp0neg12.setT35(true);
      temp0neg12.setT56(true);
      temp0neg12.location = "SIX";
      t0bneg1_loc3.add(temp0neg12);

      ArrayList<GridCell> t0bneg1_loc5 = new ArrayList<GridCell>(); //TODO add to hash table, key "0_neg1_FIVE"
      //0neg13
      GridCell temp0neg13 = new GridCell(0, 0, -1);
      temp0neg13.setT46(true);
      temp0neg13.setT56(true);
      temp0neg13.location = "FOUR";
      t0bneg1_loc5.add(temp0neg13);

      //0neg14
      GridCell temp0neg14 = new GridCell(0, 0, -1);
      temp0neg14.setT56(true);
      temp0neg14.location = "SIX";
      t0bneg1_loc5.add(temp0neg14);
      table.put("0_-1_THREE", t0bneg1_loc3);
      table.put("0_-1_FIVE", t0bneg1_loc5);
      t0bneg1 = concatenate(t0bneg1_loc3, t0bneg1_loc5);

      populateEnds( "0_-1", t0bneg1);

      // Top 0  Bottom 0 has no solution

      // Top 0  Bottom 3
      //031
      GridCell temp031 = new GridCell(0, 0, 3);
      temp031.setT35(true);
      temp031.setT56(true);
      temp031.setT46(true);
      temp031.location = "FOUR";
      table.put("0_3_THREE", temp031);
      populateEnds("0_3", temp031);

      ArrayList<GridCell> t0b1 = new ArrayList<GridCell>();
      //Top 0  Bottom 1
      //011
      GridCell temp011 = new GridCell(0, 0, 1);
      temp011.setT56(true);
      temp011.location = "SIX";
      table.put("0_1_FIVE", temp011);
      t0b1.add(temp011);

      //012
      GridCell temp012 = new GridCell(0, 0, 1);
      temp012.setT35(true);
      temp012.location = "END";
      t0b1.add(temp012);
      table.put("0_1_END", t0b1);

      ArrayList<GridCell> t0b1_start = new ArrayList<GridCell>();
      t0b1_start.add(temp011);

      GridCell temp013 = new GridCell(0, 0, 1);
      temp013.setT46(true);
      temp013.location = "SIX";
      t0b1_start.add(temp013);
      table.put("0_1_START", t0b1_start);

      ArrayList<GridCell> t0b2 = new ArrayList<GridCell>();
      //Top 0  Bottom 2
      //021
      GridCell temp021 = new GridCell(0, 0, 2);
      temp021.setT56(true);
      temp021.setT35(true);
      temp021.location = "SIX";
      table.put("0_1_THREE", temp021);
      t0b2.add(temp021);

      //022
      GridCell temp022 = new GridCell(0, 0, 2);
      temp022.setT56(true);
      temp022.setT46(true);
      temp022.location = "FOUR";
      table.put("0_1_FIVE", temp022);
      t0b2.add(temp022);
      populateEnds( "0_2", t0b2);



      //Top 1  Bottom -1
      //1neg11
      ArrayList<GridCell> t1bneg1_start = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_START"
      ArrayList<GridCell> t1bneg1_end = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_END"
      ArrayList<GridCell> t1bneg1_loc1 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_ONE"
      ArrayList<GridCell> t1bneg1_loc3 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_THREE"
      ArrayList<GridCell> t1bneg1_loc5 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_FIVE"
      ArrayList<GridCell> t1bneg1_35 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_THREE_FIVE"
      ArrayList<GridCell> t1bneg1_15 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_THREE_FIVE"

      GridCell temp1neg11 = new GridCell(0, 1, -1);
      temp1neg11.setT35(true);
      temp1neg11.setT34(true);
      temp1neg11.location = "FOUR";
      t1bneg1_loc5.add(temp1neg11);

      //1neg12
      GridCell temp1neg12 = new GridCell(0, 1, -1);
      temp1neg12.setT35(true);
      temp1neg12.setT34(true);
      temp1neg12.setT46(true);
      temp1neg12.location = "SIX";
      t1bneg1_loc5.add(temp1neg12);

      //1neg13  == start only cell
      GridCell temp1neg13 = new GridCell(0, 1, -1);
      temp1neg13.setT35(true);
      temp1neg13.setT34(true);
      temp1neg13.setT56(true);
      temp1neg13.location = "FOUR";
      temp1neg13.location2 = "SIX";
      t1bneg1_start.add(temp1neg13);

      //1neg14
      GridCell temp1neg14 = new GridCell(0, 1, -1);
      temp1neg14.setT34(true);
      temp1neg14.setT56(true);
      temp1neg14.location2 = "SIX";
      temp1neg14.location = "FOUR";
      t1bneg1_35.add(temp1neg14);

      //1neg15
      GridCell temp1neg15 = new GridCell(0, 1, -1);
      temp1neg15.setT34(true);
      temp1neg15.setT46(true);
      temp1neg15.location = "SIX";
      table.put("1_-1_THREE", temp1neg15);
      t1bneg1_loc3.add(temp1neg15);

      //1neg16  === ONLY VALID FOR LAST CELL
      GridCell temp1neg16 = new GridCell(0, 1, -1);
      temp1neg16.setT56(true);
      temp1neg16.setT34(true);
      temp1neg16.setT46(true);
      temp1neg16.location = "END";
      t1bneg1_end.add(temp1neg16);


      /*/1neg17 ===== INVALID CELL
      GridCell temp1neg17 = new GridCell(0, 0, -1);
      temp1neg17.setT56(true);
      temp1neg17.setT35(true);
      temp1neg17.setT34(true);
      temp1neg17.setT46(true);
      temp1neg17.location = "END";
      table.put("1_-1_THREE_FIVE", temp1neg17);
      t1bneg1_loc3.add(temp1neg17);*/

      //1neg18 === ONLY VALID FOR LAST CELL
      GridCell temp1neg18 = new GridCell(0, 1, -1);
      temp1neg18.setT35(true);
      temp1neg18.setT13(true);
      temp1neg18.location = "END";
      t1bneg1_end.add(temp1neg18);

      //1neg19
      GridCell temp1neg19 = new GridCell(0, 1, -1);
      temp1neg19.setT56(true);
      temp1neg19.setT35(true);
      temp1neg19.setT13(true);
      temp1neg19.location = "SIX";
      t1bneg1_loc1.add(temp1neg19);

      //1neg110
      GridCell temp1neg110 = new GridCell(0, 1, -1);
      temp1neg110.setT12(true);
      temp1neg110.location = "TWO";
      t1bneg1_loc1.add(temp1neg110);

      //1neg111
      GridCell temp1neg111 = new GridCell(0, 1, -1);
      temp1neg111.setT56(true);
      temp1neg111.setT12(true);
      temp1neg111.location = "TWO";
      temp1neg111.location2 = "SIX";
      t1bneg1_15.add(temp1neg111);

      //1neg112
      GridCell temp1neg112 = new GridCell(0, 1, -1);
      temp1neg112.setT56(true);
      temp1neg112.setT34(true);
      temp1neg112.setT12(true);
      temp1neg112.location = "TWO";
      temp1neg112.location2 = "FOUR";
      t1bneg1_15.add(temp1neg112);

      //1neg113
      GridCell temp1neg113 = new GridCell(0, 1, -1);
      temp1neg113.setT12(true);
      temp1neg113.setT35(true);
      temp1neg113.setT56(true);
      temp1neg113.setT46(true);
      temp1neg113.location = "TWO";
      temp1neg113.location2 = "FOUR";
      table.put("1_-1_ONE_THREE", temp1neg113); //only config so adding to start and end as well
      t1bneg1_start.add(temp1neg113);
      t1bneg1_end.add(temp1neg113);

      //1neg114
      GridCell temp1neg114 = new GridCell(0, 1, -1);
      temp1neg114.setT56(true);
      temp1neg114.setT24(true);
      temp1neg114.location = "FOUR";
      temp1neg114.location2 = "SIX";
      t1bneg1_35.add(temp1neg114);

      //1neg115
      GridCell temp1neg115 = new GridCell(0, 1, -1);
      temp1neg115.setT56(true);
      temp1neg115.setT24(true);
      temp1neg115.setT35(true);
      temp1neg115.location = "FOUR";
      temp1neg115.location2 = "SIX";
      t1bneg1_loc3.add(temp1neg115);

      //1neg116
      GridCell temp1neg116 = new GridCell(0, 1, -1);
      temp1neg116.setT56(true);
      temp1neg116.setT24(true);
      temp1neg116.setT35(true);
      temp1neg116.setT46(true);
      temp1neg116.location = "TWO";
      t1bneg1_loc3.add(temp1neg116);

      //1neg117
      GridCell temp1neg117 = new GridCell(0, 1, -1);
      temp1neg117.setT56(true);
      temp1neg117.setT24(true);
      temp1neg117.setT46(true);
      temp1neg117.location = "TWO";
      t1bneg1_loc5.add(temp1neg117);

      table.put("1_-1_ONE", t1bneg1_loc1);
      table.put("1_-1_THREE", t1bneg1_loc3);
      table.put("1_-1_FIVE", t1bneg1_loc5);
      table.put("1_-1_ONE_FIVE", t1bneg1_15);
      table.put("1_-1_THREE_FIVE", t1bneg1_35);

      ArrayList<GridCell> t1bneg1 = new ArrayList<GridCell>(); 
      t1bneg1 = concatenate(t1bneg1_loc1, t1bneg1_loc3);
      t1bneg1 = concatenate(t1bneg1, t1bneg1_loc5);
      t1bneg1 = concatenate(t1bneg1, t1bneg1_15);
      t1bneg1 = concatenate(t1bneg1, t1bneg1_35);
      t1bneg1_start = concatenate(t1bneg1, t1bneg1_start);
      t1bneg1_end = concatenate(t1bneg1, t1bneg1_end);

      table.put("1_-1_START", t1bneg1_start);
      table.put("1_-1_END", t1bneg1_end);

      // Top 1  Bottom 0
      ArrayList<GridCell> t1b0_start = new ArrayList<GridCell>();
      ArrayList<GridCell> t1b0_end = new ArrayList<GridCell>(); 

      //101  =====  VALID FOR LAST CELL ONLY
      GridCell temp101 = new GridCell(0, 1, 0);
      temp101.setT13(true);
      temp101.location = "END";
      t1b0_end.add(temp101);

      //102
      GridCell temp102 = new GridCell(0, 1, 0);
      temp102.setT12(true);
      temp102.location = "TWO";
      table.put("1_0_ONE", temp102);
      t1b0_start.add(temp102);
      t1b0_end.add(temp102);

      /*GridCell temp103 = new GridCell(0, 1, 0);
      temp103.setT35(true);
      temp103.location = "FIVE";
      t1b0.add(temp103);*/
      temp = new GridCell(0, 1, 0);
      temp.setT24(true);
      temp.location = "FOUR";
      t1b0_start.add(temp);
      
      table.put("1_0_START", t1b0_start);
      table.put("1_0_END", t1b0_end);

      // Top 1  Bottom 1
      //111
      ArrayList<GridCell> t1b1_loc1 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_ONE"
      ArrayList<GridCell> t1b1_loc3 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_THREE"
      ArrayList<GridCell> t1b1_loc5 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_FIVE"
      ArrayList<GridCell> t1b1_end = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_FIVE"
      ArrayList<GridCell> t1b1_start = new ArrayList<GridCell>(); //TODO add to hash table, key "1_neg1_FIVE"

      GridCell temp111 = new GridCell(0, 1, 1);
      temp111.setT34(true);
      temp111.location = "FOUR";
      t1b1_loc3.add(temp111);

      //112
      GridCell temp112 = new GridCell(0, 1, 1);
      temp112.setT12(true);
      temp112.location = "TWO";
      table.put("1_0_ONE", temp112);
      t1b1_loc1.add(temp112);

      //113 ====>  ONLY VALID FOR LAST CELL
      GridCell temp113 = new GridCell(0, 1, 1);
      temp113.setT13(true);
      temp113.setT35(true);
      temp113.location = "END";
      t1b1_end.add(temp113);

      //113 reverse ====>  First CELL only
      GridCell temp113rev = new GridCell(0, 1, 1);
      temp113rev.setT24(true);
      temp113rev.setT46(true);
      temp113rev.location = "TWO";
      temp113rev.location2 = "SIX";
      t1b1_start.add(temp113rev);

      //114
      GridCell temp114 = new GridCell(0, 1, 1);
      temp114.setT56(true);
      temp114.setT24(true);
      temp114.location = "FOUR";
      temp114.location2 = "SIX";
      t1b1_loc5.add(temp114);

      //115
      GridCell temp115 = new GridCell(0, 1, 1);
      temp115.setT12(true);
      temp115.setT46(true);
      temp115.location = "TWO";
      temp115.location2 = "SIX";
      t1b1_loc1.add(temp115);

      table.put("1_1_ONE", t1b1_loc1);
      table.put("1_1_THREE", t1b1_loc3);
      table.put("1_1_FIVE", t1b1_loc5);
      ArrayList<GridCell> t1b1 = concatenate( t1b1_loc1, t1b1_loc3);
      t1b1 = concatenate( t1b1, t1b1_loc5);
      t1b1_start = concatenate( t1b1_start, t1b1);
      t1b1_end = concatenate( t1b1_end, t1b1);
      table.put("1_1_START", t1b1_start);
      table.put("1_1_END", t1b1_end);

      //Top 1  Bottom 2
      //121
      ArrayList<GridCell> t1b2_loc1 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_2_ONE"
      ArrayList<GridCell> t1b2_loc3 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_2_THREE"
      ArrayList<GridCell> t1b2_loc5 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_2_FIVE"
      ArrayList<GridCell> t1b2_15 = new ArrayList<GridCell>(); //TODO add to hash table, key "1_2_ONE_FIVE"

      GridCell temp121 = new GridCell(0, 1, 2);
      temp121.setT13(true);
      temp121.setT35(true);
      temp121.setT56(true);
      temp121.location = "SIX";

      table.put("1_2_ONE", temp121);
      t1b1_loc1.add(temp121);

      //122 turned out to be invalid

      //123
      GridCell temp123 = new GridCell(0, 1, 2);
      temp123.setT12(true);
      temp123.setT35(true);
      temp123.setT56(true);
      temp123.location2 = "SIX";
      temp123.location = "TWO";
      table.put("1_2_ONE_THREE", temp123);
      t1b2_loc1.add(temp123);

      //124
      GridCell temp124 = new GridCell(0, 1, 2);
      temp124.setT12(true);
      temp124.setT46(true);
      temp124.setT56(true);
      temp124.location2 = "FOUR";
      temp124.location = "TWO";
      t1b2_15.add(temp124);

      //125
      GridCell temp125 = new GridCell(0, 1, 2);
      temp125.setT34(true);
      temp125.setT35(true);
      temp125.location = "FOUR";
      table.put("1_2_FIVE", temp125);
      t1b2_loc5.add(temp125);

      //126
      GridCell temp126 = new GridCell(0, 1, 2);
      temp126.setT34(true);
      temp126.setT46(true);
      temp126.location = "SIX";
      table.put("1_2_THREE", temp126);
      t1b2_loc3.add(temp126);

      //127
      GridCell temp127 = new GridCell(0, 1, 2);
      temp127.setT35(true);
      temp127.setT46(true);
      temp127.setT12(true);
      temp127.location = "TWO";
      temp127.location2 = "FOUR";
      temp127.location3 = "SIX";
      table.put("1_2_ONE_THREE_FIVE", temp127);
      t1b2_loc1.add(temp127);

      //128
      GridCell temp128 = new GridCell(0, 1, 2);
      temp128.setT34(true);
      temp128.setT46(true);
      temp128.location = "FOUR";
      temp128.location2 = "SIX";
      table.put("1_2_THREE_FIVE", temp128);
      t1b2_loc3.add(temp128);

      //129
      GridCell temp129 = new GridCell(0, 1, 2);
      temp129.setT24(true);
      temp129.setT46(true);
      temp129.setT56(true);
      temp129.location = "TWO";
      table.put("1_2_FIVE", temp129);
      t1b2_loc5.add(temp129);

      //1210
      GridCell temp1210 = new GridCell(0, 1, 2);
      temp1210.setT24(true);
      temp1210.setT35(true);
      temp1210.setT56(true);
      temp1210.location = "FOUR";
      temp1210.location2 = "SIX";
      t1b2_loc3.add(temp1210);

      //1210 reverse
      GridCell temp1210rev = new GridCell(0, 1, 2);
      temp1210rev.setT13(true);
      temp1210rev.setT46(true);
      temp1210rev.setT56(true);
      temp1210rev.location = "FOUR";
      t1b2_15.add(temp1210rev);

      table.put("1_2_ONE", t1b2_loc1);
      table.put("1_2_THREE", t1b2_loc3);
      table.put("1_2_FIVE", t1b2_loc5); 
      table.put("1_2_ONE_FIVE", t1b2_15); 

      t1b2_loc1 = concatenate(t1b2_loc1, t1b2_loc3);
      t1b2_loc1 = concatenate(t1b2_loc1, t1b2_loc5);
      t1b2_loc1 = concatenate(t1b2_loc1, t1b2_15);
      populateEnds("1_2", t1b2_loc1);

  }

  // Return a string representation of the grid
  public void graphicPrint(){
    int initLength = gridList.size() * 3;
    StringBuilder aboveRow = new StringBuilder(initLength);
    StringBuilder topRow = new StringBuilder(initLength);
    StringBuilder middleRow = new StringBuilder(initLength);
    StringBuilder bottomRow = new StringBuilder(initLength);
    StringBuilder belowRow = new StringBuilder(initLength);

    for ( GridCell c : gridList) {
      if(c.getT12()){
        aboveRow.append(" ---- ");
      } else {
        aboveRow.append(" .... ");
      }
      if(c.getT13()){
        topRow.append("| ");
      } else {
        topRow.append(". ");
      }
      topRow.append(c.topValue);
      if(c.getT24()){
        topRow.append(" |");
      } else {
        topRow.append(" .");
      }
      if(c.getT34()){
        middleRow.append(" ---- ");
      } else {
        middleRow.append(" .... ");
      }
      if(c.getT35()){
        bottomRow.append("| ");
      } else {
        bottomRow.append(". ");
      }
      bottomRow.append(c.bottomValue);
      if(c.getT46()){
        bottomRow.append(" |");
      } else {
        bottomRow.append(" .");
      }
      if(c.getT56()){
        belowRow.append(" ---- ");
      } else {
        belowRow.append(" .... ");
      }
    }
    System.out.println(aboveRow.toString());
    System.out.println(topRow.toString());
    System.out.println(middleRow.toString());
    System.out.println(bottomRow.toString());
    System.out.println(belowRow.toString());

  }

  public static void main(String[] args) {
      Grid test = new Grid("[3,-1][1,2][1,-1][1,2]");
      test.graphicPrint();
      //Grid test = new Grid("[3,-1][-1, 2][-1, 3][1, 2][3,-1]");
      boolean solution = test.solve();
      if(solution) test.graphicPrint();
      else System.out.println("No solution found");

      test = new Grid("[1, 3][1, 2][1, -1][2, -1][1,1]");
      solution = test.solve();
      if(solution) test.graphicPrint();
      else System.out.println("No solution found");

   }

}
