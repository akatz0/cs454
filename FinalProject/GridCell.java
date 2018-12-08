import java.io.Console;
import java.io.IOException;
import java.util.*;

/**
Team Members: Darin Brown, Ariel Katz, and Jack Weatherford

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
public class GridCell {

  String location; // keep track of where we are currently attempting to draw the snake from

  int index; // should the gridcell know its own index in the grid?
  boolean t12, t13, t34, t35, t56, t24, t46; //transitions between adjacent gridpoints
  int topValue, bottomValue;
  boolean topCompleted, bottomCompleted; // are top and bottom values met?
  int edgeCount; //how many transitions are true

  public GridCell(int i){
      new GridCell(i, -1, -1); // no values given
  }

  public GridCell(int i, int top, int bottom){
      t12 = false;
      t13 = false;
      t34 = false;
      t35 = false;
      t56 = false;
      t24 = false;
      t46 = false;
      topValue = top;
      bottomValue = bottom;
      edgeCount = 0;
      index = i;
      location = null;
      topCompleted = false;
      bottomCompleted = false;
      //return this;
  }

  public boolean getT12(){
    return t12;
  }

  public void setT12(boolean b){
    updateEdgeCount(b);
    t12 = b;
  }

  public boolean getT13(){
    return t13;
  }

  public void setT13(boolean b){
    updateEdgeCount(b);
    t13 = b;
  }

  public boolean getT34(){
    return t34;
  }

  public void setT34(boolean b){
    updateEdgeCount(b);
    t34 = b;
  }

  public boolean getT35(){
    return t35;
  }

  public void setT35(boolean b){
    updateEdgeCount(b);
    t35 = b;
  }

  public boolean getT56(){
    return t56;
  }

  public void setT56(boolean b){
    updateEdgeCount(b);
    t56 = b;
  }

  public boolean getT24(){
    return t24;
  }

  public void setT24(boolean b){
    updateEdgeCount(b);
    t24 = b;
  }

  public boolean getT46(){
    return t46;
  }

  public void setT46(boolean b){
    updateEdgeCount(b);
    t46 = b;
  }

  public void updateEdgeCount(boolean b){
    if ( b ) edgeCount++; else edgeCount--;
  }

  private void update(GridCell c){
    //set the current cell values to match the cell passed
    setT12(c.getT12());
    setT13(c.getT13());
    setT34(c.getT34());
    setT35(c.getT35());
    setT56(c.getT56());
    setT24(c.getT24());
    setT46(c.getT46());
    location = c.location;
    edgeCount = c.edgeCount;
    //don't copy index value!!
  }

  public String configureKey(){
    return topValue + "_" + bottomValue + "_" + location + "_TODOlocation2" ;
  }

  public boolean makeSnake(String one, String two){
      GridCell c = Grid.table.get(configureKey()); //See if entry exists in table

      // if entry exist in the table just re-use that solution
      if ( c != null) { update(c); return true; }
      if (c.location == "FALSE") return false; // already determined this configuration has no valid solution
      // TODO will need to add capability to have multiple options for same key

      // the cell values are empty
      if( topValue < 0 && bottomValue < 0){
        if (location =="ONE"){
          setT12(true);
          location = "TWO";
          // here insert another option onto stack where we can branch to set T13 to true 
          //or set T13, T35, and T56 to true, etc.
        }
        return true;
      }


      if (topCompleted && bottomCompleted) return true;
      return false;
  }

  public void print(){
    System.out.println("GridCell index = " + index);
    System.out.println("Top Value: "+topValue);
    System.out.println("Bottom Value: "+bottomValue);
  }

  public static void main(String[] args) {

      GridCell test = new GridCell(0);
      test.print();
   }

}
