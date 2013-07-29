package tests;

import cs671.eval.*;

import charpov.grader.*;
import static org.testng.Assert.*;

import java.io.IOException;

public class SampleClientServerTests {
    private EvalServer es;
    private EvalClient ec;
    private EvalTask stringTask;
    private String stringTaskResult;
    private EvalTask badMethodTask;
    private EvalTask badArgsTask;
    private EvalTask voidTask;
    private String voidTaskResult;

    public void BEFORE() {
        try{
            stringTask = new EvalTask("this is a long string", 
                                      new String[]{"substring"}, 
                                      new Object[][]{{2, 7}});
            stringTaskResult = "this is a long string".substring(2,7);
            badMethodTask = new EvalTask(new Integer("122326348"), 
                                      new String[]{"compareTo"}, 
                                      new Object[][]{{"not an Integer"}});
            badArgsTask = new EvalTask(new Integer("122326348"), 
                                      new String[]{""}, 
                                      new Object[][]{{2, 7}});
            voidTask = new EvalTask(new StringBuilder("cats"), 
                                      new String[]{"setCharAt"},
                                      new Object[][]{{0, 'b'}});
            voidTaskResult = "bats";
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
    }

    public static void main (String[] args) throws Exception {
        java.util.logging.Logger.getLogger("charpov.grader")
            .setLevel(java.util.logging.Level.WARNING);
        Tester tester = new Tester(SampleClientServerTests.class);
        tester.setConcurrencyLevel(1);
        tester.run();
    }

    @Test(timeout=1000,val=1) //server with no work and no clients
    public void sample01 () {
        es = new EvalServer(1024);
        es.initialize();
        Thread t1 = new Thread(es);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test(timeout=1000,val=1) //server start before initialize
    public void sample02 () {
        try{
            es = new EvalServer(1024);
            es.run();
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=11000,val=1) //client start before initialize
    public void sample03 () {
        try{
            es = new EvalServer(1024);
            es.initialize();
            es.addWork(stringTask);
            new Thread(es).start();
            ec = new EvalClient("localhost",1024);
            es.getWork();
            ec.run();
        }
        catch(IllegalStateException ex){return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=1) //server hasWork before initialize
    public void sample04 () {
        try{
            es = new EvalServer(1024);
            es.hasWork();
        }
        catch(IllegalStateException ex){ return; }
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=1000,val=1) //server getWork when hasWork not true
    public void sample05 () {
        try{
            es = new EvalServer(1024);
            es.initialize();
            es.getWork();
        }
        catch(IllegalStateException ex){ return; }
        catch(Exception ex){}
        throw new AssertionError("expected IllegalStateException");
    }

    @Test(timeout=20000,val=1) //client and server with no work
    public void sample06 () {
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
    }

    @Test(timeout=20000,val=1) //client and server with one task
    public void sample07 () {
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

    @Test(timeout=20000,val=1) //task with bad method name
    public void sample08 () {
        es = new EvalServer(1024);
        es.initialize();
        es.addWork(badMethodTask);
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
        assertEquals(es.results.get(0).getClass(), String.class);
    }

    @Test(timeout=20000,val=1) //task with bad argument types
    public void sample09 () {
        es = new EvalServer(1024);
        es.initialize();
        es.addWork(badArgsTask);
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
        assertEquals(es.results.get(0).getClass(), String.class);
    }

    @Test(timeout=20000,val=1) //task with void method
    public void sample10 () {
        es = new EvalServer(1024);
        es.initialize();
        es.addWork(voidTask);
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
        assertEquals(es.results.get(0).getClass(), StringBuilder.class);
        assertEquals(es.results.get(0).toString(), voidTaskResult);
    }

    @Test(timeout=60000,val=1) //multiple clients
    public void sample11 () {
        for(int i=0; i<5; i++){
            es = new EvalServer(1024);
            es.initialize();
            for(int j=0; j<100; j++){
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
            assertEquals(es.results.size(), 100);
            for(Object o: es.results)
                assertEquals(o, stringTaskResult);
            AFTER();
        }
    }

}