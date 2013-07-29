package cs671.eval;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/11/13
 * Time: 7:27 PM
 * A container for a "task" to be passed from an <code>EvalServer</code> to an <code>EvalClient</code>. Stores
 * the object to be used, names of parameters, and arguments to pass.
 */
public class EvalTask implements Serializable {
    public Object[][] args; //Arguments to be passed to methods.
	public String[] methods;//Names of methods to be called.
	public Object target;   //Object to be the target of the methods called.

	public EvalTask(Object t, String[] m, Object[][] a){
		target  = t;
		methods = m;
		args 	= a;
	}

    /**
     * Returns the array of method names
     * @return - array of method names
     */
    public String[] getMethods(){
        return methods;
    }

    /**
     * Returns the 2D array of args for methods
     * @return - 2D array of args for methods
     */
    public Object[][] getArgs(){
        return args;
    }

    /**
     * Returns the target <code>Object</code>
     * @return - target <code>Object</code>
     */
    public Object getTarget(){
        return target;
    }

    public String toString(){
        String out = "====TASK====\nMethods:\n";
        for(String m : methods)
            out += "\t" + m + "\n";
        out += "Arguments:\n";
        for(int i = 0; i < args.length; i++){
            for( int j = 0; j < args[i].length; j++){
                out += "\t" + args[i][j] + "\n";
            }
        }
        return out;
    }

}
