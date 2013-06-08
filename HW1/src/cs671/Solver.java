package cs671.solve;

/** An interface for solving state-based search problems. It is set up
 * by the <code>initialize</code> method, then moves are taken one at
 * a time by calling <code>nextMove</code>. When a goal state has been
 * found, <code>hasSolved</code> will return true, and
 * <code>getSolution</code> will return the path taken to reach the
 * goal state.
 *
 * @author  Sofia Lemons
 * @version 1.0
 * @see solve.DFSolver
 * @see solve.BFSolver
 */
public interface Solver<S extends World.State>{
    /** Initializes variables of the object. Must be called before any
     * other methods.
     */
    public void initialize();
    /** Takes the next move for the solver's algorithm and stores child states.
     *
     * @throws java.lang.IllegalStateException if called before
     * <code>initialize</code> or after a solution is found
     */
    public void nextMove();
    /** Determines whether the solver has found a solution.
     *
     * @throws java.lang.IllegalStateException if called before
     * <code>initialize</code>
     * @returns <code>true</code> if a solution has been found,
     * <code>false</code> otherwise
     */
    public boolean hasSolved();
    /** Returns the solution found by the solver.
     *
     * @throws java.lang.IllegalStateException if called before
     * <code>initialize</code> or before a solution is found
     * @returns solution state
     */
    public S getSolution();
}