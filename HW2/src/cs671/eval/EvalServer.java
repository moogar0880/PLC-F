package cs671.eval;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
     * @param p - port number to listen on
     */
    public EvalServer( int p ){
        init = false;
        port = p;
    }

    /**
     * Initializes data structures for the server and opens the socket. Sets the timeout for listening per
     * attempt to 10 seconds. Gracefully handles any exceptions that arise when attempting to start the server
     * socket, printing error messages.
     */
    public void initialize(){
        work    = new LinkedList<EvalTask>();
        clients = new ArrayList<Thread>();
        results = new ArrayList<Object>();
        try {
            serv = new ServerSocket(port);
            serv.setSoTimeout(1000);
        } catch (IOException e) {
            System.err.println("EvalServer.initialize():" + getLineNumber() + ": " + e );
        }
        init = true;
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
        synchronized(workLock){
            if( !init || !hasWork())
                throw new IllegalStateException();
            return work.remove(0);
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

        Socket s = null;
        while( hasWork() || clientsActive() ){
            try {
                s = serv.accept();
                EvalConnection newE = new EvalConnection(s);
                newE.initialize();
                Thread t = new Thread(newE);
                t.start();
                clients.add(t);
            } catch (IOException e) { //thrown by serv.accept
                System.err.println("EvalServer.run():" + getLineNumber() + ": " + e );
                break;
            }
        }
        try {
            serv.close();
        } catch (IOException e) {
            System.err.println("EvalServer.run():" + getLineNumber() + ": " + e );
        }
    }

    /** Get the current line number.
     * @return int - Current line number.
     */
    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    /**
     * Inner class to handle communication with one specific client. Communicates to clients whether work is 
     * remaining, sends serialized <code>EvalTask</code> objects to be worked on, and receives results until a
     * new <code>String</code> object containing only "hasWork" is sent.
     */
    public class EvalConnection implements Runnable{
        public ObjectInputStream in;  //Input Stream from the client
        public ObjectOutputStream out;//Output Stream to the client
        public Socket connection; //Client Socket
        private boolean init;

        public EvalConnection(Socket s){
            connection = s;
            init       = false;
        }

        /**
         * Initializes input and output streams. Gracefully handles any exceptions that arise when attempting
         * to connect and set up streams, printing error messages.
         */
        public void initialize(){
            try {
                in  = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
            } catch (IOException e) {
                System.err.println("EvalConnection.initialize():" + getLineNumber() + ": " + e );
            }
            init = true;
        }

        /**
         * Adds an object to the <code>results</code> list in thread-safe manner.
         * @param o - object to be added
         * @throws IllegalStateException - if called before EvalConnection.initialize
         */
        public void addResult(Object o){
            if( !init )
                throw new IllegalStateException();

            synchronized(resultsLock){
                results.add(o);
            }
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
            while(!connection.isClosed()){
                try {
                    if( in.readUTF().equals("hasWork")){
                        out.writeBoolean(hasWork());
                        out.flush();
                    }
                    if(!hasWork())
                        break;
                    else{
                        out.writeObject(getWork());
                        out.flush();
                        Object[] results = (Object[]) in.readObject();
                        for( Object o : results )
                            addResult(o);
                    }
                } catch (IOException e) {
                    System.err.println("EvalConnection.run():" + getLineNumber() + ": " + e );
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("EvalConnect.run():" + getLineNumber() + ": " + e );
                }
            }
        }
    }

    private static Class<?>[] buildHeaders(String[] headers){
        Class<?>[] toRet = new Class<?>[3];
        Class dataStructure = null, keyType = null, dataType = null;
        for(String s : headers){
            try{
                if( dataStructure == null )
                    dataStructure = Class.forName("cs671.eval." + s);
                else if(keyType == null)
                    keyType = Class.forName("cs671.eval." + s);
                else if(dataType == null)
                    dataType = Class.forName("cs671.eval." + s);
            } catch(ClassNotFoundException e) {
                try{
                    if( dataStructure == null )
                        dataStructure = Class.forName("java.lang." + s);
                    else if(keyType == null)
                        keyType = Class.forName("java.lang." + s);
                    else if(dataType == null)
                        dataType = Class.forName("java.lang." + s);
                }
                catch (ClassNotFoundException ex){
                    System.err.println(e);
                }
            }
        }
        toRet[0] = dataStructure;
        toRet[1] = keyType;
        toRet[2] = dataType;
        return toRet;
    }

    private static Method getAddMethod(Class ds){
        try {
            return ds.getMethod("add", Comparable.class, Object.class);
        } catch (NoSuchMethodException e) {
            System.err.println(e);
        }
        return null;
    }

    private static Object getNewInstance(Class c){
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            System.err.println(e);
        } catch (IllegalAccessException e) {
            System.err.println(e);
        }
        return null;
    }

    private static Constructor<?>[] getTypeCons(Class key, Class data){
        Constructor<?>[] toRet = new Constructor<?>[2];
        Class[] argTypes = {String.class};
        try {
            toRet[0] = key.getDeclaredConstructor(argTypes);
            toRet[1] = data.getDeclaredConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            System.err.println(e);
        }
        return toRet;
    }

    private static ArrayList<Object> getDSData(String[] raw, Constructor<?> key, Constructor<?> data){
        ArrayList<Object> toRet = new ArrayList<Object>();
        int i = 0;
        while( i + 1 < raw.length ){
            try {
                toRet.add(key.newInstance(raw[i++]));
                toRet.add(data.newInstance(raw[i++]));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return toRet;
    }

    private static Object[][] parseMakeParams(ArrayList<Object[]> params){
        Object[][] toRet = new Object[params.size()][];
        for(int i = 0; i < toRet.length; i++){
            Object[] scan = params.get(i);
            toRet[i] = new Object[scan.length];
            for(int j = 0; j < scan.length; j++){
                String[] parsed = ((String)scan[j]).split("&");
                int k = 0;
                while(k < parsed.length){
                    String type = parsed[k++];
                    String val  = parsed[k++];
                    toRet[i][j] = null;
                    //Class<?> c = Class.forName(type).getDeclaredConstructor(Class.forName(val)).
                }
                toRet[i][j] = scan[j];
            }
        }
        return toRet;
    }

    private static EvalTask buildTask(List<String> lines){
        Class<?>[] headers = buildHeaders(lines.remove(0).split(" "));
        Class dataStructure = headers[0], keyType = headers[1], dataType = headers[2];
        //Instantiate DS, keyType and dataType Constructors
        Object target    = getNewInstance(dataStructure);
        Method targetAdd = getAddMethod(dataStructure);

        Constructor<?>[] tyCons = getTypeCons(keyType,dataType);
        Constructor<?> keyConst = tyCons[0], dataConst = tyCons[1];

        //Build DS from data lines
        ArrayList<Object> data = getDSData(lines.remove(0).split("&"), keyConst, dataConst );
        int i = 0;
        while(i + 1 < data.size()){
            try {
                targetAdd.invoke(target,keyType.cast(data.get(i++)),dataType.cast(data.get(i++)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        //Build EvalTasks
        String cur = lines.remove(0);
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Object[]> params = new ArrayList<Object[]>();
        while(true){
            String[] fields = cur.split(" ");
            names.add(fields[0]);
            if(fields.length > 1){
                Object[] p = new Object[fields.length - 1];
                i = 1;
                while(i < fields.length)
                    p[i-1] = fields[i++];
                params.add(p);
            }
            else
                params.add(new Object[0]);
            if(lines.isEmpty())
                break;
            cur = lines.remove(0);
        }
        String[]   finalNames = new String[names.size()];
        for(i = 0; i < names.size(); i++)
            finalNames[i] = names.get(i);

        Object[][] finalParams = parseMakeParams(params);

        String out = "=============\n";
        for(i = 0; i < finalParams.length; i++){
            out += i +": " + finalNames[i] + ": ";
            for(int j = 0; j < finalParams[i].length; j++){
                out += finalParams[i][j] +",";
            }
            out += "\n";
        }
        System.out.println(out);
        return new EvalTask(target,finalNames,finalParams);
    }

    /**
     * Main method for running the server from command line. Creates a new server using the port and input
     * file given at command line. Reads the data structures and methods from the input file and adds them to
     * the <code>work</code> list. Starts the server in a new <code>Thread</code>.
     * @param args - Command line arguments in the form java cs671.eval.EvalServer [port number] [work file]
     */
    public static void main(java.lang.String[] args){
        String usage = "java cs671.eval.EvalServer [port number] [work file]";
        if( args.length != 2 ){
            System.out.println(usage);
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        EvalServer server = new EvalServer(port);
        server.initialize();
        Thread t = new Thread(server);
        t.start();
        String file = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(args[1]));
            String line;
            while ((line = in.readLine()) != null)
                file += line + "\n";
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Get list of EvalTask data
        String[] tasks = file.split("\n\n");
        for(String s : tasks){
            server.addWork(buildTask(new ArrayList(Arrays.asList(s.split("\n")))));
        }
    }
}