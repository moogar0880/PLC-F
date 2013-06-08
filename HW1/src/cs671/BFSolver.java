package cs671.solve;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 5/26/13
 * Time: 11:05 PM
 */
public class BFSolver<S extends World.State> implements Solver<S>{
    private boolean init, solved;
    private S currentState;
    private S solution;
    private ArrayList<S> list;


    public BFSolver(S s){
        init = solved = false;
        currentState = s;
    }

    /**
     * Returns the solution found by the solver.
     * @throws IllegalStateException - if called before <code>initialize</code> or after a solution is found
     * @return the <code>solution</code> found by the solver.
     */
    public S getSolution(){
        if( !init || !solved )
            throw new IllegalStateException();
        else
            return solution;
    }

    /**
     * Determines whether the solver has found a solution.
     * @return <code>true</code> if solved, <code>false</code> otherwise
     * @throws java.lang.IllegalStateException if called before <code>initialize</code>
     */
    public boolean hasSolved(){
        if( !init )
            throw new IllegalStateException();
        else
            return solved;
    }

    /**
     * Initializes variables of the object. Must be called before any other methods.
     */
    @SuppressWarnings("unchecked")
    public void initialize(){
        list = new ArrayList<S>();
        list.add(currentState);
        init = true;
        if( currentState.isGoal() ){
            solved = true;
        }
    }

    /**
     * Takes the next move from the front of the open list and stores child states
     * at the end. Due to some possible casting requirements, it is acceptable for
     * this method to use <code>@SuppressWarnings("unchecked")</code>.
     * @throws IllegalStateException - if called before <code>initialize</code> or after a solution is found
     */
    @SuppressWarnings("unchecked")
    public void nextMove(){
        if( !init || solved )
            throw new IllegalStateException();
        else{
            try{
                S cur = list.remove(0);
                if( cur.isGoal() ){
                    solved = true;
                    solution = cur;
                }
                else{
                    S[] children = (S[])cur.getChildren();
                    for( S s : children ){
                        list.add(s);
                    }
                }
            }catch(ClassCastException e){
                e.printStackTrace();
            }
        }
    }
}