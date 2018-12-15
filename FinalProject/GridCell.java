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
  String location2;
  String location3;

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
      if (index == 0) location = "START";
      else location = null;
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
    location2 = c.location2;
    //edgeCount = c.edgeCount; we're not actually using currently...may be unneccesary
    //don't copy index value!!
  }

  private boolean checkOverlap(GridCell existing, GridCell possible){
    if (existing.index == 0) return true; // If its the first cell then don't worry about overlap
    if (possible == null) return false;
    if (existing.getT13() != possible.getT13()) return false;
    if (existing.getT35() != possible.getT35()) return false;
    return true;
  }

  private boolean checkContinuity(GridCell previous, GridCell current){
    if (previous.getT24() != current.getT13()) return false;
    if (previous.getT46() != current.getT35()) return false;
    return true;
  }

  public String configureKey(){
    if(location2 == null)
      return topValue + "_" + bottomValue + "_" + location;
    return topValue + "_" + bottomValue + "_" + location + "_" + location2;
  }

    public boolean makeSnake(Grid gridBoard, int index){
      boolean result = false;
      boolean setConfig = false;
      boolean continuous = true;
      String key = configureKey();
      //System.out.println("Key: "+key);
      Object o = gridBoard.table.get(key); //See if entry exists in table
      GridCell c = gridBoard.gridList.get(index) ;
      ArrayList<GridCell> options;
      if ( o == null) return false;
      if(o instanceof ArrayList){
        options = (ArrayList<GridCell>) o;
        for (GridCell g : options ) {
          if( index > 0)
            continuous = checkContinuity( gridBoard.gridList.get(index-1), g);
          if (checkOverlap(c, g) && continuous && !setConfig){
            c.update(g); // use the configuration that has matching overlap
            setConfig = true;
            result = true;
          } else {
            g.index = index; // set index so we know where we were going to try this configuration
            gridBoard.pushSnakeStack(g); // push to stack
          }
        }
        if (setConfig == false){
          //we looped through all options and none matched
          //so we can't proceed with what the path has been so far
          result = false;
        }
      } else if ( o instanceof GridCell){
        GridCell g = (GridCell)o;
        if (g.location == "FALSE") return false;
        if (checkOverlap(c, g)){
            c.update(g); // use the configuration that has matching overlap
            result = true;
          } else {
            //System.out.println("NO SOLUTION works with this config so far. try back tracking");
            result = false;
          }
      } else { System.out.println("Object in table not recognized format."); }

      return result;
  }

  public boolean isValid(Grid gridBoard, int index){
    boolean result = true;
    GridCell c = gridBoard.gridList.get(index);
    if (index > 0 ){
      result = checkContinuity( gridBoard.gridList.get(index-1), c);
    } else {
      return true; /// in theory anything should be valid for first cell
    }
    if ( topValue != -1){
      int topCount = 0;
      if(getT12()) topCount++;
      if(getT13()) topCount++;
      if(getT34()) topCount++;
      if(getT24()) topCount++;
      if(topCount != topValue) return false;
    } 
    if (bottomValue != -1){
      int bottomCount = 0;
      if(getT34()) bottomCount++;
      if(getT35()) bottomCount++;
      if(getT56()) bottomCount++;
      if(getT46()) bottomCount++;
      if(bottomValue != bottomCount) return false;
    }
    return result;
  }

  public void print(){
    System.out.println("GridCell index = " + index);
    System.out.println("Top Value: "+topValue);
    System.out.println("Bottom Value: "+bottomValue);
    System.out.println("T12 "+getT12());
    System.out.println("T13 "+getT13());
    System.out.println("T35 "+getT35());
    System.out.println("T34 "+getT34());
    System.out.println("T56 "+getT56());
    System.out.println("T46 "+getT46());
    System.out.println("location "+location);
    System.out.println("location2 "+location2);
  }

  public static void main(String[] args) {

      GridCell test = new GridCell(0);
      test.print();
   }

}
