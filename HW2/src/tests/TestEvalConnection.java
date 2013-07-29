package tests;

import cs671.eval.*;

import charpov.grader.*;
import static org.testng.Assert.*;

import java.net.Socket;
import java.io.*;

//@Test(val=15)
public class TestEvalConnection {
    EvalServer es;
    EvalClient ec;
    EvalServer.EvalConnection eco;
    EvalTask stringTask;
    Object stringTaskResult;
    EvalTask badMethodTask;
    EvalTask badArgsTask;
    EvalTask voidTask;
    Object voidTaskResult;
    ObjectOutputStream out;
    ObjectInputStream in;
    ByteArrayOutputStream bos;
    ByteArrayInputStream bis;

    public void BEFORE() {
        try{
            stringTask = new EvalTask("this is a long string", 
                                      new String[]{"indexOf"}, 
                                      new Object[][]{{"a l"}});
            stringTaskResult = "this is a long string".indexOf("a l");
            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
        }
        catch(Exception ex){
            System.err.println("Trouble setting up the tests. This is bad.");
            System.err.println(ex);
        }
    }

    public void AFTER() {
        try{
            es.serv.close();
        }
        catch(Exception ex){
        }
        try{
            ec.connection.close();
        }
        catch(Exception ex){
        }
        try{
            eco.connection.close();
        }
        catch(Exception ex){
        }
    }

    public static void main (String[] args) throws Exception {
        java.util.logging.Logger.getLogger("charpov.grader")
            .setLevel(java.util.logging.Level.WARNING);
        Tester tester = new Tester(TestEvalConnection.class);
        tester.setConcurrencyLevel(1);
        tester.run();
    }

    @Test(timeout=1000,val=2) //run before initialize
    public void testRun () {
        try{
            es = new EvalServer(1024);
            Socket s = new Socket();
            eco = es.new EvalConnection(s);
            eco.run();
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=2) //addResult before initialize
    public void testAddResult () {
        try{
            es = new EvalServer(1024);
            es.initialize();
            FakeSocket s = new FakeSocket();
            s.withInputBytes(bos.toByteArray());
            eco = es.new EvalConnection(s);
            eco.addResult("test");
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=2) //addResult with multiple connections
        public void testAddResultThreadsafe () {
        es = new EvalServer(1024);
        es.initialize();
        Thread[] threads = new Thread[100];
        final EvalServer.EvalConnection[] ecs = 
            new EvalServer.EvalConnection[100];
        for(int i=0; i<100; i++){
            final int c = i;
            FakeSocket s = new FakeSocket();
            s.withInputBytes(bos.toByteArray());
            ecs[i] = es.new EvalConnection(s);
            ecs[i].initialize();
            threads[i] = new Thread(){
                    public void run(){
                        for(int j=0; j<10; j++){
                            ecs[c].addResult("test");
                        }
                    }
                };
        }
        System.out.println("here");
        for(int i=0; i<100; i++){
            threads[i].start();
        }
    }

    @Test(timeout=1000,val=3) //connection handles "hasWork" correctly
    public void testHasWork () {
        try{
            es = new EvalServer(1024);
            es.initialize();
            FakeSocket s = new FakeSocket();
            out.writeObject("hasWork");
            s.withInputBytes(bos.toByteArray());
            eco = es.new EvalConnection(s);
            eco.initialize();
            Thread t1 = new Thread(eco);
            t1.start();
            bis = new ByteArrayInputStream(s.getOutputBytes());
            in = new ObjectInputStream(bis);
            Object res = in.readObject();
            assertEquals(res, "false");
            s.close();
            try{
                t1.join();
            }
            catch(Exception ex){}
        }
        catch(Exception ex){ return; }
    }

    @Test(timeout=1000,val=3) //connection sends work correctly
    public void testWorkSent () {
        try{
            es = new EvalServer(1024);
            es.initialize();
            es.addWork(stringTask);
            FakeSocket s = new FakeSocket();
            out.writeObject("hasWork");
            out.writeObject("fake result");
            out.writeObject("hasWork");
            s.withInputBytes(bos.toByteArray());
            eco = es.new EvalConnection(s);
            eco.initialize();
            Thread t1 = new Thread(eco);
            t1.start();
            bis = new ByteArrayInputStream(s.getOutputBytes());
            in = new ObjectInputStream(bis);
            Object res = in.readObject();
            assertEquals(res, stringTask);
            try{
                t1.join();
            }
            catch(Exception ex){}
        }
        catch(Exception ex){}
    }

    @Test(timeout=1000,val=3) //connection handles result correctly
    public void testGetsResult () {
        try{
            es = new EvalServer(1024);
            es.initialize();
            es.addWork(stringTask);
            FakeSocket s = new FakeSocket();
            out.writeObject("hasWork");
            out.writeObject(stringTaskResult);
            out.writeObject("hasWork");
            s.withInputBytes(bos.toByteArray());
            eco = es.new EvalConnection(s);
            eco.initialize();
            Thread t1 = new Thread(eco);
            t1.start();
            s.getOutputBytes();
            
            try{
                t1.join();
            }
            catch(Exception ex){}
        }
        catch(Exception ex){ assertTrue(false); }
    }

}