package cs671.solve;

import java.util.List;

/** An interface for state-based search problem domains. The inner
 * class <code>State</code> specifies a specific configuration and
 * allows generation of child states by taking moves.
 *
 * @author  Sofia Lemons
 * @version 1.0
 * @see solve.MazeWorld
 * @see solve.TileWorld
 */
public interface World{

    /** Inner class to represent a specific state in the problem
     * domain.
     */
    public interface State{

        /** Generates and returns zero or more child states from this
         * one as an array. Children should be based on taking all
         * possible moves from this state, and should be valid for the
         * domain. For efficiency purposes, children generated should
         * not be the same as the direct parent state of this one.
         *
         * @return zero or more states resulting from taking moves
         * from this state
         */
        public State[] getChildren();

        /** Gives a string representation of this state. Should follow
         * the same conventions as the original world input.
         *
         * @return state represented as a string
         */
        public String toString();

        /** Gets the series of moves needed to get from the starting
         * state to this one.
         *
         * @return the path to this state
         */
        public String pathString();

        /** Determines whether this state achieves the goal of the
         * domain.
         * 
         * @return <code>true</code> if this state is the goal,
         * <code>false</code> otherwise.
         */
        public boolean isGoal();
    }

    /** Returns the starting state for this problem specification.
     *
     * @return the starting <code>State</code> for this problem
     */
    public State getStart();
}