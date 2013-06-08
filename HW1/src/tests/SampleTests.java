// $Id: SampleTests.java 326 2010-09-01 20:14:00Z charpov $
package tests;

import charpov.grader.Test;
import charpov.grader.Tester;
import cs671.solve.BFSolver;
import cs671.solve.DFSolver;
import cs671.solve.MazeWorld;
import cs671.solve.TileWorld;

import java.awt.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.testng.Assert.*;

public class SampleTests {
  
  StringWriter w;
  Point origin = new Point(0,0);
  MazeWorld solvedMaze;
  String solvedMazeString = "__\n@_";
  MazeWorld notSolvedMaze;
  String notSolvedMazeString = "_G\n@X";
  MazeWorld narrowMaze;
  MazeWorld harderMaze;
  TileWorld solvedTile;
  String solvedTileString = "_ 1 2\n3 4 5\n6 7 8";
  TileWorld notSolvedTile;
  String notSolvedTileString = "1 _ 2\n3 4 5\n6 7 8";
  TileWorld sampleTile;
  TileWorld harderTile;
    

  public void BEFORE() {
      try{
          solvedMaze = new MazeWorld(origin, new Point(0, 0), 
                                     new Point[0], 1, 1);
          notSolvedMaze = new MazeWorld(origin, new Point(1, 1), 
                                        new Point[]{new Point(1,0)}, 1, 1);
          narrowMaze = new MazeWorld(origin, new Point(0,4), 
                                     new Point[0], 0, 4);
          harderMaze = makeMaze("harderMaze.txt");
          solvedTile = new TileWorld(new int[][]{{0,1,2},{3,4,5},{6,7,8}});
          notSolvedTile = new TileWorld(new int[][]{{1,0,2},{3,4,5},{6,7,8}});
          sampleTile = new TileWorld(new int[][]{{7,0,5},{1,6,2},{3,8,4}});
          harderTile = new TileWorld(new int[][]{{5,0,4},{3,1,7},{2,8,6}});
      }
      catch(Exception ex){
          System.err.println("Trouble setting up the tests. This is bad.");
          System.err.println(ex);
      }
  }

  public void AFTER() {
  }

  public static void main (String[] args) throws Exception {
    java.util.logging.Logger.getLogger("charpov.grader")
      .setLevel(java.util.logging.Level.WARNING);
    Tester tester = new Tester(SampleTests.class);
    tester.setConcurrencyLevel(Runtime.getRuntime().availableProcessors());
    tester.run();
  }

  @Test(val=1) //valid args in MazeWorld constructor
  public void sample1 () {
      new MazeWorld(origin, new Point(4, 2), new Point[0], 5, 5);
      new MazeWorld(origin, new Point(4, 2), 
                    new Point[]{new Point(1,1), new Point(2,2)}, 5, 5);
      new MazeWorld(origin, new Point(0, 0), new Point[0], 1, 1);
  }

  @Test(val=1) //valid args in TileWorld constructor
  public void sample2 () {
      new TileWorld(new int[][]{{3,2,1},{5,4,6},{7,8,0}});
     new TileWorld(new int[][]{{0,1,2},{3,4,5},{6,7,8}});
  }

  @Test(val=1) //invalid args in MazeWorld constructor
  public void sample3 () {
      try{
          new MazeWorld(null, new Point(4, 2), new Point[0], 5, 5);
          throw new AssertionError("expected IllegalArgumentException");
      }
      catch(IllegalArgumentException ex){}
      try{
          new MazeWorld(null, new Point(4, 2), new Point[0], -1, 5);
          throw new AssertionError("expected IllegalArgumentException");
      }
      catch(IllegalArgumentException ex){}
      try{
          new MazeWorld(null, new Point(4, 2), null, -1, 5);
          throw new AssertionError("expected IllegalArgumentException");
      }
      catch(IllegalArgumentException ex){}
  }

  @Test(val=1) //invalid args in TileWorld constructor
  public void sample4 () {
      try{
          new TileWorld(null);
          throw new AssertionError("expected IllegalArgumentException");
          
      }
      catch(IllegalArgumentException ex){}
      try{
          new TileWorld(new int[0][0]);
          throw new AssertionError("expected IllegalArgumentException");
      }
      catch(IllegalArgumentException ex){}
      try{
          new TileWorld(new int[][]{{1,2},{3,4}});
          throw new AssertionError("expected IllegalArgumentException");
      }
      catch(IllegalArgumentException ex){}
  }

  @Test(val=1) //DFSolver.nextMove() called before initialize()
  public void sample5 () {
      try{
          DFSolver<MazeWorld.MState> df =
              new DFSolver<MazeWorld.MState>(solvedMaze.getStart());
          df.nextMove();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
  }

  @Test(val=1) //BFSolver.nextMove() called before initialize()
  public void sample6 () {
      try{
          BFSolver<MazeWorld.MState> bf =
              new BFSolver<MazeWorld.MState>(solvedMaze.getStart());
          bf.nextMove();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
  }

  @Test(timeout=1000,val=2) //DFSolver.nextMove() called after solution found
  public void sample7 () {
      try{
          DFSolver<MazeWorld.MState> df = 
              new DFSolver<MazeWorld.MState>(solvedMaze.getStart());
          df.initialize();
          df.nextMove();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
  }

  @Test(timeout=1000,val=2) //BFSolver.nextMove() called after solution found
  public void sample8 () {
      try{
          BFSolver<MazeWorld.MState> bf = 
              new BFSolver<MazeWorld.MState>(solvedMaze.getStart());
          bf.initialize();
          bf.nextMove();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
    
  }

  @Test(val=1) //DFSolver.hasSolution() called before initialize()
  public void sample9 () {
      try{
          DFSolver<MazeWorld.MState> df = 
              new DFSolver<MazeWorld.MState>(solvedMaze.getStart());
          df.hasSolved();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
  }

  @Test(val=1) //BFSolver.hasSolution() called before initialize()
  public void sample10 () {
      try{
          BFSolver<MazeWorld.MState> bf = 
              new BFSolver<MazeWorld.MState>(solvedMaze.getStart());
          bf.hasSolved();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
  }

  @Test(val=1) //DFSolver.getSolution() called before solution found
  public void sample11 () {
      try{
          DFSolver<MazeWorld.MState> df = 
              new DFSolver<MazeWorld.MState>(notSolvedMaze.getStart());
          df.initialize();
          df.getSolution();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
  }

  @Test(val=1) //BFSolver.getSolution() called before solution found
  public void sample12 () {
      try{
          BFSolver<MazeWorld.MState> bf = 
              new BFSolver<MazeWorld.MState>(notSolvedMaze.getStart());
          bf.initialize();
          bf.getSolution();
      }
      catch(IllegalStateException ex){ return; }
      throw new AssertionError("expected IllegalStateException");
  }

  @Test(val=1) //MState toString() works correctly
  public void sample13 () {
      assertEquals(notSolvedMaze.getStart().toString(), 
                   notSolvedMazeString);
      assertEquals(solvedMaze.getStart().toString(), 
                   solvedMazeString);
  }

  @Test(val=1) //TState toString() works correctly
  public void sample14 () {
      assertEquals(notSolvedTile.getStart().toString(),
                   notSolvedTileString);
      assertEquals(solvedTile.getStart().toString(),
                   solvedTileString);
  }

  @Test(val=1) //MState avoids generating parent
  public void sample15 () {
      MazeWorld.MState orig = narrowMaze.getStart();

      MazeWorld.MState next = orig.getChildren()[0];
      MazeWorld.MState[] children = next.getChildren();
      assertEquals(children.length, 1);
      assertNotEquals(children[0].toString(), orig.toString());
  }

  @Test(val=1) //TState avoids generating parent
  public void sample16 () {
      TileWorld.TState orig = sampleTile.getStart();

      TileWorld.TState next = orig.getChildren()[0];
      TileWorld.TState[] children = next.getChildren();
      for(TileWorld.TState s: children)
          assertNotEquals(s.toString(), orig.toString());
  }

  @Test(timeout=1000,val=1) //DFSolver solves simple maze problem
  public void sample17 () {
      DFSolver<MazeWorld.MState> df = 
          new DFSolver<MazeWorld.MState>(notSolvedMaze.getStart());
      df.initialize();
      while(!df.hasSolved())
          df.nextMove();
      String path = df.getSolution().pathString();
      MazeChecker mc = new MazeChecker(origin, new Point(1, 1), 
                                       new Point[]{new Point(1,0)}, 1, 1);
      assertTrue(mc.checkSolution(path));
  }

  @Test(timeout=1000,val=1) //DFSolver solves simple tile problem
      public void sample18 () {
      DFSolver<TileWorld.TState> df = 
          new DFSolver<TileWorld.TState>(notSolvedTile.getStart());
      df.initialize();
      while(!df.hasSolved()){
          df.nextMove();
      }
      String path = df.getSolution().pathString();
      TileChecker tc = new TileChecker(new int[][]{{1,0,2},{3,4,5},{6,7,8}});
      assertTrue(tc.checkSolution(path));
  }

  @Test(timeout=1000,val=1) //BFSolver solves simple maze problem
  public void sample19 () {
      BFSolver<MazeWorld.MState> bf = 
          new BFSolver<MazeWorld.MState>(notSolvedMaze.getStart());
      bf.initialize();
      while(!bf.hasSolved())
          bf.nextMove();
      String path = bf.getSolution().pathString();
      MazeChecker mc = new MazeChecker(origin, new Point(1, 1), 
                                       new Point[]{new Point(1,0)}, 1, 1);
      assertTrue(mc.checkSolution(path));
  }

  @Test(timeout=1000,val=1) //BFSolver solves simple tile problem
  public void sample20 () {
      BFSolver<TileWorld.TState> bf = 
          new BFSolver<TileWorld.TState>(notSolvedTile.getStart());
      bf.initialize();
      while(!bf.hasSolved())
          bf.nextMove();
      String path = bf.getSolution().pathString();
      TileChecker tc = new TileChecker(new int[][]{{1,0,2},{3,4,5},{6,7,8}});
      assertEquals(path.split(" ").length, 1);
      assertTrue(tc.checkSolution(path));
  }

  @Test(timeout=15000,val=1) //BFSolver solves harder maze problem
  public void sample21 () {
      BFSolver<MazeWorld.MState> bf =
          new BFSolver<MazeWorld.MState>(harderMaze.getStart());
      bf.initialize();
      int count = 0;
      while(!bf.hasSolved()){
          bf.nextMove();
      }
      String path = bf.getSolution().pathString();
      assertEquals(path.split(" ").length, 15);
      MazeChecker mc = new MazeChecker("harderMaze.txt");
      assertTrue(mc.checkSolution(path));
  }

  @Test(timeout=7000,val=1) //BFSolver solves harder tile problem
  public void sample22 () {
      BFSolver<TileWorld.TState> bf = 
          new BFSolver<TileWorld.TState>(harderTile.getStart());
      bf.initialize();
      while(!bf.hasSolved())
          bf.nextMove();
      String path = bf.getSolution().pathString();
      TileChecker tc = new TileChecker(new int[][]{{5,0,4},{3,1,7},{2,8,6}});
      assertEquals(path.split(" ").length, 23);
      assertTrue(tc.checkSolution(path));
  }

  @Test(timeout=10000,val=1) //DFSolver solves harder maze problem
  public void sample23 () {
      DFSolver<MazeWorld.MState> df = 
          new DFSolver<MazeWorld.MState>(harderMaze.getStart());
      df.initialize();
      while(!df.hasSolved()){
          df.nextMove();
      }
      String path = df.getSolution().pathString();
      MazeChecker mc = new MazeChecker("harderMaze.txt");
      assertTrue(mc.checkSolution(path));
  }

  public static MazeWorld makeMaze(String fileName){
      MazeWorld w = null;
      
      try{
          Scanner scan = new Scanner(new java.io.File(fileName));
          
          Point startLoc=null, goalLoc=null;
          ArrayList<Point> blocked = new ArrayList<Point>();
          ArrayList<String> lines = new ArrayList<String>();
          while(scan.hasNext()){
              lines.add(scan.nextLine());
          }
          int x=0, y=lines.size()-1;
          for(String l: lines){
              x = l.length();
              for(x=0; x < l.length(); x++){
                  switch(l.charAt(x)){
                  case '@':
                      startLoc = new Point(x, y);
                      break;
                  case 'G':
                      goalLoc = new Point(x, y);
                      break;
                  case 'X':
                      blocked.add(new Point(x, y));
                      break;
                  }
              }
              y--;
          }
          w = new MazeWorld(startLoc, goalLoc, 
                            blocked.toArray(new Point[blocked.size()]), 
                            x-1, lines.size()-1);
      }
      catch(java.io.IOException ex){
          System.err.printf("Cannot open file %s%n", fileName);
      }
      
      return w;
  }

}