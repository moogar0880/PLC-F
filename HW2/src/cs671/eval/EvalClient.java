package cs671.eval;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/11/13
 * Time: 6:28 PM
 *
 * A client to connect to the <code>EvalServer</code>. Receives serialized work from the server over a socket in the
 * form of an <code>EvalTask</code>. It should confirm that the method signatures given are valid, call them,
 * and then send the results back to the server. If the method has no return value, the object upon which methods
 * were called should be returned.
 */
public class EvalClient implements Runnable{
    public String address;
    public Socket connection;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public int port;
    private boolean init;

    /**
     *
     * @param a Address/hostname of the server
     * @param p Port number of the server
     */
    public EvalClient(String a, int p){
        address = a;
        port    = p;
        init    = false;
    }

    /**
     * Checks whether a specific method is valid for a given set of arguments. Should handle all
     * exceptions gracefully and return the correct value still when exceptions occur.
     * @param name - name
     * @param m - method to be checked
     * @param args - args to be passed to m
     * @return - <code>True</code> if the method can be called with those args, <code>False</code> otherwise
     */
    public boolean checkMethod( String name, Method m, Object[] args){
        Class<?>[] params = m.getParameterTypes();
        if( args.length == params.length ){
            for( int i = 0; i < params.length; i++ ){
                if( !params[i].isInstance(args[i]) ){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Asks the server whether more work is available by sending the <code>String</code> "hasWork", and
     * receiving a <code>Boolean</code> value of either <code>true</code> or <code>false</code>. Handles
     * <code>ClassNotFoundException</code> and <code>IOException</code> gracefully, printing an error message.
     * @return <code>True</code> if the server returns <code>True</code>, <code>False</code> otherwise
     */
    public boolean checkWork(){
        return false;
    }

    /**
     * For the method <code>name</code> and set of arguments <code>args</code>, the client attempts to find a method
     * with that signature. If none is found, it should return an error <code>String</code> to notify the server of
     * the problem. If the method is found, the client invokes it and returns the result. For methods with no
     * return value, the <code>target</code> is returned, under the assumption that the method likely altered it.
     * @param c - <code>Class</code> on which the method call is to be made
     * @param target - <code>Object</code> on which the method is to be called
     * @param name - Name of the <code>Method</code> to be called
     * @param args - List of arguments for the <code>Method</code>
     * @return - either the return value of the method or target
     */
    public Object doTask( Class c, Object target, String name, Object[] args){
        boolean possible = false;
        Method[] methods = c.getDeclaredMethods();
        for( Method m : methods){
            if( m.getName().equals(name)){
                Class<?>[] params = m.getParameterTypes();
                if( args.length == params.length ){
                    for( int i = 0; i < params.length; i++ ){
                        if( !params[i].isInstance(args[i]) ){
                            possible = true;
                            break;
                        }
                    }
                    if( possible ){
                        if(m.getReturnType().equals(Void.TYPE)){
                            try {
                                m.invoke(target, args);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            return target;
                        }
                        else{
                            try {
                                return m.invoke(target, args);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                        possible = false;
                }
            }
        }
        return target;
    }

    /**
     * Receives new work from the server, confirms it is a valid <code>EvalTask</code>, and casts it in order
     * to return it. Handles <code>ClassNotFoundException</code> and <code>IOException</code> gracefully,
     * printing an error message.
     * @return - the casted <code>EvalTask</code>
     */
    public EvalTask getWork(){
        return null;
    }

    /**
     * Initializes data structures for the client and establishes the connection to the server. Sets up input and
     * output streams. Gracefully handles any exceptions that arise when attempting to connect and set up streams,
     * printing error messages.
     */
    public void initialize(){
        init = true;
        try {
            connection = new Socket(address, port);
            in = (ObjectInputStream) connection.getInputStream();
            out = (ObjectOutputStream) connection.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method for running the client from command line. Creates a new client using hostname/address and
     * port given at command line. Starts the client in a new <code>Thread</code>.
     * @param args - command line arguments
     */
    public static void main(String[] args){

    }

    /**
     *  Main loop of the client. Uses the <code>checkWork</code> method to test whether there is more work to
     *  be done. If there is more work, receives a serialized <code>EvalTask</code> from the server. Uses
     *  <code>doTask</code> to invoke each method and sends the result or relevant error message to the
     *  server. When no work remains or the connection is closed, client exits. Should handle all exceptions
     *  gracefully, printing a descriptive error message, sending an error message <code>String</code>
     *  notifying the server of the problem, and moving on to the next method or task.
     */
    @Override
    public void run() {
        if( !init )
            throw new IllegalStateException();
    }
}