package cs671.eval;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/11/13
 * Time: 7:02 PM
 * A server for remote computation. Listens on a given port for clients to connect and creates new
 * <code>EvalConnection</code> instances in new threads to communicate with each client. When all work is done
 * and no clients remain connected, the server shuts down.
 */
public class EvalServer implements Runnable{
	
	public ArrayList<Thread> clients; // Threads for each client connection.
	public int port;                  // Port number to listen on.
	public ArrayList<Object> results; // List of results from client computations.
	public ServerSocket serv;         // Server socket to listen on for new clients.
	public LinkedList<EvalTask> work; // Tasks remaining to be distributed to clients.
    private boolean init;
    private final Object workLock = new Object(), resultsLock = new Object();

    /**
     *
     * @param p - port number to listen on
     */
    public EvalServer( int p ){
        init = false;
        port = p;
    }

    /**
     * Adds new <code>EvalTask</code> objects to the <code>work</code> list. Can be called by multiple threads.
     * @param t
     */
    public void addWork(EvalTask t){
        if( !init )
            throw new IllegalStateException();

        synchronized(workLock){
            work.add(t);
        }
    }

    /**
     * Checks whether any clients are still connected and running by checking the status of the threads 
     * running their <code>EvalConnection</code>.
     * @return - <code>True</code> if there are active clients, <code>False</code> otherwise
     */
	public boolean clientsActive(){
        for(Thread t : clients ){
            if(t.isAlive())
                return true;
        }
        return false;
	}

    /**
     * Gets a <code>EvalTask</code> object from the <code>work</code> list. Can be called by multiple threads.
     * @return - the <code>EvalTask</code> from the <code>work</code> list.
     */
	public EvalTask getWork(){
        if( !init )
            throw new IllegalStateException();

        synchronized(workLock){
            if( hasWork() )
                return work.get(0);
            else
                return null;
        }
	}

    /**
     * Checks whether the <code>work</code> list is empty. Can be called by multiple threads.
     * @return - <code>True</code> if the list is not empty, <code>False</code> otherwise
     */
	public boolean hasWork(){
        if( !init )
            throw new IllegalStateException();
        synchronized(workLock){
            return work.size() != 0;
        }
	}

    /**
     * Initializes data structures for the server and opens the socket. Sets the timeout for listening per 
     * attempt to 10 seconds. Gracefully handles any exceptions that arise when attempting to start the server
     * socket, printing error messages.
     */
	public void initialize(){
        work = new LinkedList<EvalTask>();
        clients = new ArrayList<Thread>();
        results = new ArrayList<Object>();
        try {
            serv = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        init = true;
	}

    /**
     * Main method for running the server from command line. Creates a new server using the port and input 
     * file given at command line. Reads the data structures and methods from the input file and adds them to
     * the <code>work</code> list. Starts the server in a new <code>Thread</code>.
     * @param args
     */
	public static void main(java.lang.String[] args){
        String usage = "java cs671.eval.EvalServer [port number] [work file]";
        if( args.length != 3 ){
            System.out.println(usage);
            System.exit(1);
        }
        int port = Integer.parseInt(args[1]);
        File workFile = new File(args[2]);
        EvalServer server = new EvalServer(port);
        //TODO Figure out how to add work from work file
    }

    /**
     * Main loop of the server. While there is work to be done or clients are still connected, continues to 
     * listen for new clients. Will need to listen only for 10 seconds at a time so it can check for whether
     * it should keep running. When a new client connects, creates a new <code>EvalConnection</code> and 
     * starts a new thread to run it. Adds the thread to the <code>clients</code> list and continues to listen
     * for new clients.
     */
    @Override
    public void run() {
        if( !init )
            throw new IllegalStateException();

        while( hasWork() ){
            try {
                serv.setSoTimeout(10000);
                EvalConnection newE = new EvalConnection(serv.accept());
                Thread t = new Thread(newE);
                t.run();
            } catch (SocketException e) { //thrown by serv.setSoTimeout
                e.printStackTrace();
            } catch (IOException e) { //thrown by serv.accept
                e.printStackTrace();
            }
        }
    }

    /**
     * Inner class to handle communication with one specific client. Communicates to clients whether work is 
     * remaining, sends serialized <code>EvalTask</code> objects to be worked on, and receives results until a
     * new <code>String</code> object containing only "hasWork" is sent.
     */
    public class EvalConnection implements Runnable{
        public ObjectInputStream in;  //Input Stream from the client
        public ObjectOutputStream out;//Output Stream to the client
        private Socket socket; //Client Socket
        private boolean init;

        public EvalConnection(Socket s){
            socket = s;
            init = false;
        }

        /**
         * Adds an object to the <code>results</code> list in thread-safe manner.
         * @param o - object to be added
         */
        public void addResult(Object o){
            if( !init )
                throw new IllegalStateException();

            synchronized(resultsLock){
                results.add(o);
            }
        }

        /**
         * Initializes input and output streams. Gracefully handles any exceptions that arise when attempting
         * to connect and set up streams, printing error messages.
         */ 
        public void initialize(){
            try {
                in  = (ObjectInputStream) socket.getInputStream();
                out = (ObjectOutputStream) socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            init = true;
        }

        /**
         * Main loop of the connection. First, attempts to receive the <code>String</code> "hasWork" from the
         * client and sends <code>true</code> or <code>false</code> based on whether the server has remaining
         * work to send or not. Gets an <code>EvalTask</code> from the server and sends a serialized version
         * to the client over the socket. Then waits for result data to be sent and adds it to the 
         * <code>results</code> list. Continues listening for results until the <code>String</code> "hasWork" 
         * is sent again. If no work remains or the client disconnects, the method exits.
         */
        @Override
        public void run(){
            if( !init )
                throw new IllegalStateException();
        }
    }
}