package cs671.solve;
import java.awt.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 5/26/13
 * Time: 11:19 PM
 */
public class MazeWorld implements World {
    public int maxX, maxY;
    private MState start;
    private Point goal;
    private ArrayList<Point> blocked;
    ArrayList <Direction> directions;

    /**
     * Builds an instance of the maze for the given start, goal, blocked spaces, and boundaries.
     * @param start - the starting location of the agent
     * @param goal - the location the agent is tasked with moving to
     * @param blocked - array of blocked spaces which the agent cannot enter
     * @param maxX - maximum allowed x value the agent can occupy
     * @param maxY - maximum allowed y value the agent can occupy
     * @throws java.lang.IllegalArgumentException - if the arguments given are not valid
     * (null, boundaries less than 1, start or goal outside the boundaries.)
     */
    public MazeWorld(Point start, Point goal, Point[] blocked, int maxX, int maxY){
        if( start == null || goal == null || blocked == null || maxX < 0 || maxY < 0) {
            throw new IllegalArgumentException();
        }
        else{
            this.start   = new MState(null, start, null, goal);
            this.goal    = goal;
            this.blocked = new ArrayList<Point>(Arrays.asList(blocked));
            this.maxX    = maxX;
            this.maxY    = maxY;
            directions = new ArrayList<Direction>();
            directions.add( Direction.UP );
            directions.add( Direction.DOWN );
            directions.add( Direction.LEFT );
            directions.add( Direction.RIGHT );
        }
    }

    /**
     * Returns the starting state for this problem specification.
     * @return The start <code>MState</code> of this maze
     */
    public MazeWorld.MState getStart(){
        return start;
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

    /**
     * Inner class to represent a specific state in the problem domain, based on the agent's location.
     */
    public class MState implements World.State{
        private Point agentLoc, goal;
        private MState parent;
        private boolean goalLoc;
        private Direction move;

        /**
         * private state constructor for building state map
         * @param par - parent state
         * @param p - x,y coords of the current state
         * @param lastMove - last move taken, to be added to new states path
         */
        private MState( MState par, Point p, Direction lastMove, Point g ){
            parent  = par;
            agentLoc = p;
            goal = g;
            goalLoc = agentLoc.equals(goal);
            move = lastMove;
        }

        /**
         * Determine equality between two MStates
         * @param o - the MState to be compared to
         * @return <code>true</code> if the location of the two states are equal, <code>false</code> otherwise
         */
        public boolean equals(Object o){
            try{
                MState fromObj = (MState)o;
                return this.agentLoc.equals(fromObj.getPoint());
            } catch( ClassCastException c ){
                //c.printStackTrace();
                return false;
            }
        }

        /**
         * get the <code>Point</code> representation of this state
         * @return the <code>Point</code> representation of this state
         */
        private Point getPoint(){
            return agentLoc;
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
                    toRet = new Point(oldX, oldY + 1);
                    break;
                case DOWN:
                    toRet = new Point(oldX, oldY - 1);
                    break;
                case LEFT:
                    toRet = new Point(oldX - 1, oldY);
                    break;
                case RIGHT:
                    toRet = new Point(oldX + 1, oldY);
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
            if( pX >= 0 && pX <= maxX && pY >= 0 && pY <= maxY ){
                if( !blocked.contains(p) ){
                    if( parent == null || !p.equals(parent.getPoint()) )
                        toRet = true;
                }
            }
            return toRet;
        }

        /**
         * Generates and returns zero or more child states from this one as an array.
         * @return zero or more states resulting from taking moves from this state
         */
        public MazeWorld.MState[] getChildren(){
            ArrayList<MState> tmp = new ArrayList<MState>();
            if( goalLoc ){ //if this is the goal, we're done
                return null;
            }
            int arrayCounter = 0;
            for( Direction d: directions ){ // assure each are valid children
                Point p = getNewLocation(d, agentLoc);
                if( isValidChild(p) ){
                    tmp.add(new MState(this, p, d, goal));
                    arrayCounter++;
                }
            }
            Collections.shuffle(tmp);
            MState[] children = tmp.toArray(new MState[arrayCounter]);
            return children;
        }

        /**
         * Determines whether this state achieves the goal of the domain.
         * @return <code>true</code> if this state is the goal, <code>false</code> otherwise.
         */
        public boolean isGoal(){
            return goalLoc;
        }

        /**
         * Gets the series of moves needed to get from the starting state to this one.
         * @return the path to this state
         */
        public String pathString(){
            Deque<Direction> stack = new LinkedList<Direction>();
            stack.push(move);
            MState cur = parent;
            while( cur != null ){
                stack.push(cur.move);
                cur = cur.parent;
            }
            String toRet = "";
            stack.pop();//throw away start state
            for(Direction d : stack ){
                toRet = toRet.concat(" ");
                toRet = toRet.concat(Direction.asString(d));
            }
            return toRet.trim();
        }

        /**
         * Gives a string representation of this state.
         * @return state represented as a string
         */
        public String toString(){
            String out = "";
            for( int i = maxY; i >= 0; i-- ){
                for( int j = 0; j <= maxY; j++){
                    Point thisPoint = new Point(j,i);
                    if( blocked.contains(thisPoint))
                        out += "X";
                    else if( agentLoc.equals(thisPoint))
                        out += "@";
                    else if( goal.equals(thisPoint) )
                        out += "G";
                    else
                        out += "_";
                }
                out += "\n";
            }
            return out.trim();
        }
    }
}