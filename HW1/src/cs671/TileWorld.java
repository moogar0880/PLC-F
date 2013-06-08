package cs671.solve;

import java.awt.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 5/26/13
 * Time: 11:19 PM
 */
public class TileWorld implements World {
    public int[][] tiles;
    private TState startState;
    private TState goalState;
    private int maxX, maxY;

    /**
     * Builds an instance of the maze for the given start, goal, blocked spaces, and boundaries.
     * @param t - an array of tile numbers, specifying location by location in the array
     * @throws IllegalArgumentException - if the arguments given are not valid (array not properly initialized.)
     */
    public TileWorld(int[][] t){
        if( t == null || t.length == 0 )
            throw new IllegalArgumentException();
        else{
            tiles = t;
            maxX = t.length;
            maxY = t[0].length;
            int[][] tmp = new int[maxX][maxY];
            int counter = 0;
            Point p = null;
            for( int i = 0; i < maxX; i++ ){
                for( int j = 0; j < maxY; j++ ){
                    tmp[i][j] = counter++;
                    if( t[i][j] == 0 ){
                        p = new Point(i,j);
                    }
                }
            }
            if( p == null )
                throw new IllegalArgumentException();
            else{
                goalState  = new TState(tmp);
                startState = new TState(null, tiles, p, null );
            }
        }
    }

    /**
     * Returns the starting state for this problem specification.
     * @return the starting <code>TState</code> for this problem
     */
    public TileWorld.TState getStart(){
        return startState;
    }

    /**
     * enum representation of the possible directions that the agent could
     * move in
     */
    public enum Direction{
        UP, RIGHT, DOWN, LEFT;

        /**
         * useful reverse function for printing the agents path
         * @param d - direction to be reversed
         * @return the opposite direction
         */
        public static Direction reverse(Direction d){
            switch(d){
                case UP:
                    return DOWN;
                case RIGHT:
                    return LEFT;
                case DOWN:
                    return UP;
                case LEFT:
                    return RIGHT;
            }
            return null;
        }

        /**
         *
         * @param d - direction to be printed
         * @return - the <code>String</code> representation of the <code>Direction</code>
         */
        public static String asString(Direction d){
            String toRet = "";
            switch (d){
                case UP:
                    toRet = "up";
                    break;
                case RIGHT:
                    toRet = "right";
                    break;
                case DOWN:
                    toRet = "down";
                    break;
                case LEFT:
                    toRet = "left";
            }
            return toRet;
        }
    }

    public class TState implements World.State{
        private int[][] curState;
        private TState parent;
        private TState[] children;
        private Point index;
        private Direction move;

        /**
         * private constructor to init only the curState array
         * NOTE: only to be used for constructing the goalState var
         * @param arr - arrangement of the current state
         */
        private TState(int[][] arr){
            curState = arr;
        }

        /**
         * private state constructor for building state map
         * @param par - parent state
         * @param arr - arrangement of the current state
         * @param p - Point representation of the blanks location in 2D array
         */
        private TState( TState par, int[][] arr, Point p, Direction lastMove ){
            parent    = par;
            curState  = arr;
            index     = p;
            move      = lastMove;
        }

        /**
         * Returns the <code>Point</code> representation of the blank in this state
         * @return - returns the index of the blank tile in the 2D array
         */
        private Point getIndex(){
            return index;
        }

        /**
         * Determine equality between two MStates
         * @param o - the MState to be compared to
         * @return <code>true</code> if the location of the two states are equal, <code>false</code> otherwise
         */
        public boolean equals(Object o){
            try{
                TState fromObj = (TState)o;
                return Arrays.deepEquals(curState, fromObj.getArrangement());
            } catch( ClassCastException c ){
                //c.printStackTrace();
                return false;
            }
        }

        /**
         * Return the 2D array representation of the current state
         * @return - the ID of the current state
         */
        private int[][] getArrangement(){
            return curState;
        }

        /**
         * Given a <code>Direction</code> and the current point, returns the new <code>Point</code>
         * location of the agent after moving in the specified <code>Direction</code>
         * @param d - <code>Direction</code> to be moved in
         * @param p - current location of the agent
         * @return - new location of the agent after moving in the specified <code>Direction</code>
         */
        private Point getNewLocation(Direction d, Point p ){
            Point toRet = null;
            int oldX = (int)p.getX(), oldY = (int)p.getY();
            switch (d){
                case UP:
                    toRet = new Point(oldX - 1, oldY);
                    break;
                case DOWN:
                    toRet = new Point(oldX + 1, oldY);
                    break;
                case LEFT:
                    toRet = new Point(oldX, oldY - 1);
                    break;
                case RIGHT:
                    toRet = new Point(oldX, oldY + 1);
            }
            return toRet;
        }

        /**
         * Determines whether <code>Point</code> p is a valid child location
         * @param p - possible child location to be checked
         * @return - <code>true</code> if p is a valid child location, <code>false</code> otherwise
         */
        private boolean isValidChild(Point p){
            boolean toRet = false;
            int pX = (int)p.getX(), pY = (int)p.getY();
            if( pX >= 0 && pX < maxX && pY >= 0 && pY < maxY ){
                if( parent == null || !p.equals(parent.getIndex()) )
                    toRet = true;
                }
            return toRet;
        }

        /**
         * Swaps blank tile with neighbor at specified index
         * @param p - x,y coords of tile to be swapped
         * @return - new board arrangement
         */
        private int[][] getNewBoard(Point p){
            int[][] toRet = new int[maxX][maxY];
            for( int i = 0; i < maxX; i++){
                for( int j = 0; j < maxY; j++ ){
                    toRet[i][j] = curState[i][j];
                }
            }
            int x = (int)p.getX(), y = (int)p.getY();
            int curX = (int)index.getX(), curY = (int)index.getY();
            toRet[curX][curY] = toRet[x][y];
            toRet[x][y] = 0;
            return toRet;
        }

        /**
         * Generates and returns zero or more child states from this one as an array.
         * @return zero or more states resulting from taking moves from this state
         */
        public TileWorld.TState[] getChildren(){
            ArrayList<TState> tmp = new ArrayList<TState>();

            int arrayCounter = 0;
            if( this.equals(goalState) ){ //if this is the goal, we're done
                return null;
            }
            ArrayList<Direction> directions = new ArrayList<Direction>();
            directions.add( Direction.UP );
            directions.add( Direction.DOWN );
            directions.add( Direction.LEFT );
            directions.add( Direction.RIGHT );

            for( Direction d: directions ){ // assure each are valid children
                Point p = getNewLocation(d, index);
                if( isValidChild(p) ){
                    int[][] newBoard = getNewBoard(p);
                    tmp.add(new TState(this, newBoard, p, d));
                    arrayCounter++;
                }
            }
            Collections.shuffle(tmp);
            children = tmp.toArray(new TState[arrayCounter]);
            return children;
        }

        /**
         * Determines whether this state achieves the goal of the domain.
         * @return <code>true</code> if this state is the goal, <code>false</code> otherwise.
         */
        public boolean isGoal(){
            return Arrays.deepEquals(curState, goalState.getArrangement());
        }

        /**
         * Gets the series of moves needed to get from the starting state to this one.
         * @return the path to this state
         */
        public String pathString(){
            Deque<Direction> stack = new LinkedList<Direction>();
            stack.push(move);
            TState cur = parent;
            while( cur != null ){
                stack.push(cur.move);
                cur = cur.parent;
            }
            String toRet = "";
            stack.pop();//throw away start state
            for(Direction d : stack ){
                toRet = toRet.concat(" ");
                toRet = toRet.concat(Direction.asString(Direction.reverse(d)));
            }
            return toRet.trim();
        }

        /**
         * Gives a string representation of this state.
         * @return this state represented as a string
         */
        public String toString(){
            String out = "";
            int counter = 1;
            for( int i = 0; i < maxX; i++ ){
                for( int j = 0; j < maxY; j++){
                    if( curState[i][j] == 0 ){
                        out += "_";
                        if(counter++ == maxX){
                            out += "\n";
                            counter = 1;
                        }
                        else
                            out += " ";
                    }
                    else{
                        out += curState[i][j];
                        if(counter++ == maxX){
                            out += "\n";
                            counter = 1;
                        }
                        else
                            out += " ";
                    }
                }
            }
            return out.trim();
        }
    }

}