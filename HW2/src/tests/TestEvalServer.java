package tests;

import cs671.eval.*;

import charpov.grader.*;
import static org.testng.Assert.*;

import java.net.Socket;
import java.io.*;

//@Test(val=20)
public class TestEvalServer {
    EvalServer es;
    EvalClient ec;
    Socket s;
    EvalTask stringTask;
    Object stringTaskResult;
    ObjectOutputStream out;
    ObjectInputStream in;
    ByteArrayOutputStream bos;
    ByteArrayInputStream bis;
    EvalTask[] smallInputTasks;
    EvalTask[] largeInputTasks;

    public void BEFORE() {
        try{
            
            stringTask = new EvalTask("this is a long string", 
                                      new String[]{"indexOf"}, 
                                      new Object[][]{{"a l"}});
            stringTaskResult = "this is a long string".indexOf("a l");
            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            smallInputTasks = new EvalTask[2];
            smallInputTasks[0] = 
                new EvalTask(new SerialList(),
                             new String[]{"remove","get","remove","sort","add"},
                             new Object[][]{{new Long(48)},
                                            {new Integer(45)},
                                            {new Long(50)},
                                            {},
                                            {new Long(19), 
                                             new String("discreeter")}});
            smallInputTasks[1] = 
                new EvalTask(new SerialPrioQueue(),
                             new String[]{"remove","toString","add","preOrder"},
                             new Object[][]{{},
                                            {},
                                            {new Short("56"), 
                                             new Float(36.9200141602)},
                                            {}});
            largeInputTasks = new EvalTask[5];
            largeInputTasks[0] = 
                new EvalTask(new SerialBST(),
                             new String[]{"add",
                                          "postOrder",
                                          "remove",
                                          "add",
                                          "balance",
                                          "remove",
                                          "inOrder"},
                             new Object[][]{{new Float(95.9262133332),
                                             new Short("51")},
                                            {},
                                            {new Integer(52)},
                                            {new Float(82.0601852704), 
                                             new Short("51")},
                                            {},
                                            {new Integer(15)},
                                            {}});
            largeInputTasks[1] = 
                new EvalTask(new SerialBST(),
                             new String[]{"remove",
                                          "postOrder",
                                          "toString",
                                          "find",
                                          "balance",
                                          "toString"},
                             new Object[][]{{new Integer(94)},
                                            {},
                                            {},
                                            {new Byte("6")},
                                            {},
                                            {}});
            largeInputTasks[2] = 
                new EvalTask(new SerialList(),
                             new String[]{"remove",
                                          "remove",
                                          "toString",
                                          "get",
                                          "get"},
                             new Object[][]{{new Integer(79)},
                                            {new Float(89.5717707021)},
                                            {},
                                            {new Integer(42)},
                                            {new Integer(31)}});
            largeInputTasks[3] = 
                new EvalTask(new SerialPrioQueue(),
                             new String[]{"preOrder",
                                          "add",
                                          "preOrder",
                                          "add",
                                          "toString",
                                          "remove",
                                          "add",
                                          "add"},
                             new Object[][]{{},
                                            {new Byte("3"), new Short("54")},
                                            {},
                                            {new Byte("28"), new Short("70")},
                                            {},
                                            {},
                                            {new Byte("31"), new Short("14")},
                                            {new Byte("20"), new Short("31")}});
            largeInputTasks[4] = 
                new EvalTask(new SerialPrioQueue(),
                             new String[]{"add",
                                          "remove",
                                          "add",
                                          "remove",
                                          "toString"},
                             new Object[][]{{"redounding", "unwrapped"},
                                            {},
                                            {"slumlords", "deportation"},
                                            {},
                                            {}});
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
            ec.out.close();
            ec.connection.close();
        }
        catch(Exception ex){
        }
        try{
            out.close();
            in.close();
            s.close();
        }
        catch(Exception ex){
        }
    }

    public static void main (String[] args) throws Exception {
        java.util.logging.Logger.getLogger("charpov.grader")
            .setLevel(java.util.logging.Level.WARNING);
        Tester tester = new Tester(TestEvalServer.class);
        tester.setConcurrencyLevel(1);
        tester.run();
    }

    @Test(timeout=1000,val=1) //server with no work and no clients
    public void testNoWorkNoClients () {
        es = new EvalServer(1024);
        es.initialize();
        Thread t1 = new Thread(es);
        t1.start();
        try{
            t1.join();
        }
        catch(Exception ex){}
    }

    @Test(timeout=1000,val=1) //server start before initialize
    public void testRun () {
        try{
            es = new EvalServer(1024);
            es.run();
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=1) //addWork before initialize
    public void testAddWork () {
        try{
            es = new EvalServer(1024);
            es.addWork(stringTask);
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=1) //addWork before initialize
    public void testGetWork () {
        try{
            es = new EvalServer(1024);
            es.getWork();
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=1) //server hasWork before initialize
    public void testHasWork () {
        try{
            es = new EvalServer(1024);
            es.hasWork();
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=1) //server getWork when hasWork not true
    public void testGetWorkNoWork () {
        try{
            es = new EvalServer(1024);
            es.initialize();
            es.getWork();
        }
        catch(IllegalStateException ex){ return; }
        catch(Exception ex){}
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=20000,val=1) //client and server with one task
    public void testClientsActive () {
        es = new EvalServer(1024);
        es.initialize();
        for(int i=0; i<5; i++)
            es.addWork(stringTask);
        Thread ts = new Thread(es);
        assertFalse(es.clientsActive());
    }

    @Test(timeout=20000,val=1) //client and server with no work
    public void testNoWork () {
        es = new EvalServer(1024);
        es.initialize();
        es.addWork(stringTask);
        Thread ts = new Thread(es);
        ts.start();
        ec = new EvalClient("localhost",1024);
        ec.initialize();
        es.getWork();
        Thread tc = new Thread(ec);
        tc.start();
        try{
            tc.join();
            ts.join();
        }
        catch(Exception ex){}
    }

    @Test(timeout=20000,val=2) //client and server with one task
    public void testOneTask () {
        es = new EvalServer(1024);
        es.initialize();
        es.addWork(stringTask);
        Thread ts = new Thread(es);
        ts.start();
        ec = new EvalClient("localhost",1024);
        ec.initialize();
        Thread tc = new Thread(ec);
        tc.start();
        try{
            tc.join();
            ts.join();
        }
        catch(Exception ex){ 
            throw new AssertionError("Error while running.");
        }
        assertEquals(es.results.size(), 1);
        assertEquals(es.results.get(0), stringTaskResult);
    }

    @Test(timeout=30000,val=3) //multiple clients
    public void testTwoClients () {
        for(int i=0; i<5; i++){
            es = new EvalServer(1024);
            es.initialize();
            for(int j=0; j<50; j++){
                es.addWork(stringTask);
            }
            Thread ts = new Thread(es);
            ts.start();
            Thread[] tc = new Thread[2];
            for(int j=0; j<2; j++){
                ec = new EvalClient("localhost",1024);
                ec.initialize();
                tc[j] = new Thread(ec);
                tc[j].start();
            }
            try{
                tc[0].join();
                tc[1].join();
                ts.join();
            }
            catch(Exception ex){ 
                throw new AssertionError("Error while running.");
            }
            assertEquals(es.results.size(), 50);
            for(Object o: es.results)
                assertEquals(o, stringTaskResult);
            AFTER();
        }
    }

    @Test(timeout=5000,val=3) //small file input
    public void testMainSmall () {
        Thread t = new Thread(){
                public void run(){
                    EvalServer.main(new String[]{"1025","inputSmall.txt"});
                }
            };
        t.start();

        Socket s = null;
        //connect a fake client
        try{
            s = new Socket("localhost", 1025);
            out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            in = new ObjectInputStream(s.getInputStream());
        }
        catch(Exception ex){ assertTrue(false); }
        //check the data structures, methods, and arguments
        try{
            out.writeObject("hasWork");
            Boolean work = (Boolean)(in.readObject());
            assertTrue(work);
            int j = 0;
            while(work){
                EvalTask realTask = smallInputTasks[j];
                EvalTask task = (EvalTask)(in.readObject());
                //check that the target type is correct
                assertEquals(task.target.getClass(),
                             realTask.target.getClass());
                assertEquals(task.methods.length, realTask.methods.length);
                assertEquals(task.args.length, realTask.args.length);
                for(int i=0; i < task.methods.length; i++){
                    //check method name
                    assertEquals(task.methods[i], realTask.methods[i]);
                    //check args
                    assertEquals(task.args[i].length, realTask.args[i].length);
                    for(int k=0; k < task.args[i].length; k++){
                        assertEquals(task.args[i][k], realTask.args[i][k]);
                    }
                }
                out.writeObject("fake result");
                out.writeObject("hasWork");
                work = (Boolean)(in.readObject());
                j++;
            }
            t.join();
        }
        catch(Exception ex){ assertTrue(false); }
    }

    @Test(timeout=10000,val=4) //large file input
    public void testMainLarge () {
        Thread t = new Thread(){
                public void run(){
                    EvalServer.main(new String[]{"1024","inputLarge.txt"});
                }
            };
        t.start();
        
        Socket s = null;
        //connect a fake client
        try{
            s = new Socket("localhost", 1024);
            out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            in = new ObjectInputStream(s.getInputStream());
        }
        catch(Exception ex){ assertTrue(false); }
        //check the data structures, methods, and arguments
        try{
            out.writeObject("hasWork");
            Boolean work = (Boolean)(in.readObject());
            assertTrue(work);
            int j = 0;
            while(work){
                EvalTask realTask = largeInputTasks[j];
                EvalTask task = (EvalTask)in.readObject();
                //check that the target type is correct
                assertEquals(task.target.getClass(),
                             realTask.target.getClass());
                assertEquals(task.methods.length, realTask.methods.length);
		assertEquals(task.args.length, realTask.args.length);
                for(int i=0; i < task.methods.length; i++){
                    //check method name
                    assertEquals(task.methods[i], realTask.methods[i]);
                    //check args
                    assertEquals(task.args[i].length, realTask.args[i].length);
                    for(int k=0; k < task.args[i].length; k++){
                        assertEquals(task.args[i][k], realTask.args[i][k]);
                    }
                }
                out.writeObject("fake result");
                out.writeObject("hasWork");
                work = (Boolean)(in.readObject());
                j++;
            }
            t.join();
        }
        catch(Exception ex){ assertTrue(false); }
    }

}