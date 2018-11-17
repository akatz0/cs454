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

  enum GridPoint{ ONE, TWO, THREE, FOUR, FIVE, SIX; }
  GridPoint location; // keep track of where we are currently attempting to draw the snake from
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
      location = GridPoint.ONE;
      topCompleted = false;
      bottomCompleted = false;
      //return this;
  }

  public boolean getT12(){
    return t12;
  }

  public void setT12(boolean b){
    t12 = b;
  }

  public boolean getT13(){
    return t13;
  }

  public void setT13(boolean b){
    t13 = b;
  }

  public boolean getT34(){
    return t34;
  }

  public void setT34(boolean b){
    t34 = b;
  }

  public boolean getT35(){
    return t35;
  }

  public void setT35(boolean b){
    t35 = b;
  }

  public boolean getT56(){
    return t56;
  }

  public void setT56(boolean b){
    t56 = b;
  }

  public boolean getT24(){
    return t24;
  }

  public void setT24(boolean b){
    t24 = b;
  }

  public boolean getT46(){
    return t46;
  }

  public void setT46(boolean b){
    t46 = b;
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
