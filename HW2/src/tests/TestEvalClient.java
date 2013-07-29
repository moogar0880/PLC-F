package tests;

import cs671.eval.*;

import charpov.grader.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

//@Test(val=25)
public class TestEvalClient {
    private EvalServer es;
    private EvalClient ec;
    private EvalTask stringTask;
    private Object stringTaskResult;
    private EvalTask badMethodTask;
    private EvalTask badArgsTask;
    private EvalTask voidTask;
    private Object voidTaskResult;
    private EvalTask primitiveTask;
    private Object primitiveTaskResult;

    public void BEFORE() {
        try{
            stringTask = new EvalTask("this is a long string", 
                                      new String[]{"indexOf"}, 
                                      new Object[][]{{"a l"}});
            stringTaskResult = "this is a long string".indexOf("a l");
            badMethodTask = new EvalTask(new Integer("122326348"), 
                                      new String[]{"substring"}, 
                                         new Object[][]{{new Integer("3")}});
            badArgsTask = new EvalTask(new Integer("122326348"), 
                                      new String[]{"parseInt"}, 
                                       new Object[][]{{new Boolean(true)}});
            voidTask = new EvalTask(new StringBuilder("cats"), 
                                      new String[]{"setCharAt"},
                                      new Object[][]{{0, 'b'}});
            voidTaskResult = "bats";
            primitiveTask = new EvalTask("this is a long string", 
                                      new String[]{"substring"}, 
                                      new Object[][]{{2, 7}});
            primitiveTaskResult = "this is a long string".substring(2,7);
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
        Tester tester = new Tester(TestEvalClient.class);
        tester.setConcurrencyLevel(1);
        tester.run();
    }

    @Test(timeout=11000,val=1) //client start before initialize
    public void testRun () {
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

    @Test(timeout=2000,val=1) //valid task
    public void testCheckMethod () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = stringTask;
        try{
            for(Method m: t.target.getClass().getMethods()){
                if(ec.checkMethod(t.methods[0], m, t.args[0])){
                    assertEquals(m.getName(), t.methods[0]);
                    return;
                }
            }
        }
        catch(Exception ex){
            throw new AssertionError("Error while running.");
        }
        
        assertTrue(false);
    }

    @Test(timeout=2000,val=2) //task with bad method name
    public void testCheckMethodBadMethod () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = badMethodTask;
        try{
            for(Method m: t.target.getClass().getMethods()){
                if(ec.checkMethod(t.methods[0], m, t.args[0])){
                    assertTrue(false);
                    return;
                }
            }
        }
        catch(Exception ex){
            throw new AssertionError("Error while running.");
        }
    }

    @Test(timeout=2000,val=2) //task with bad argument types
    public void testCheckMethodBadArgs () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = badArgsTask;
        try{
            for(Method m: t.target.getClass().getMethods()){
                if(ec.checkMethod(t.methods[0], m, t.args[0])){
                    assertTrue(false);
                    return;
                }
            }
        }
        catch(Exception ex){
            throw new AssertionError("Error while running.");
        }
    }

    @Test(timeout=2000,val=2) //task with void method
    public void testCheckMethodVoid () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = voidTask;
        try{
            for(Method m: t.target.getClass().getMethods()){
                if(ec.checkMethod(t.methods[0], m, t.args[0])){
                    assertEquals(m.getName(), t.methods[0]);
                    assertEquals(m.getReturnType(), Void.TYPE);
                    return;
                }
            }
        }
        catch(Exception ex){
            throw new AssertionError("Error while running.");
        }
    }

    @Test(timeout=2000,val=2) //task with primitive arguments
    public void testCheckMethodPrimitive () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = primitiveTask;
        try{
            for(Method m: t.target.getClass().getMethods()){
                if(ec.checkMethod(t.methods[0], m, t.args[0])){
                    assertEquals(m.getName(), t.methods[0]);
                    assertEquals(m, String.class.getMethod("substring", int.class, int.class));
                    return;
                }
            }
        }
        catch(Exception ex){
            throw new AssertionError("Error while running.");
        }
    }

    @Test(timeout=2000,val=2) //valid task
    public void testDoTask () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = stringTask;
        Object res = ec.doTask(t.target.getClass(), t.target, 
                               t.methods[0], t.args[0]);
        assertEquals(res.getClass(), Integer.class);
        assertEquals(res, stringTaskResult);
    }

    
    @Test(timeout=2000,val=3) //task with bad method name
    public void testDoTaskBadMethod () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = badMethodTask;
        Object res = ec.doTask(t.target.getClass(), t.target, 
                               t.methods[0], t.args[0]);
        assertEquals(res.getClass(), String.class);
    }

    @Test(timeout=2000,val=3) //task with bad argument types
    public void testDoTaskBadArgs () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = badArgsTask;
        Object res = ec.doTask(t.target.getClass(), t.target, 
                               t.methods[0], t.args[0]);
        assertEquals(res.getClass(), String.class);
    }

    @Test(timeout=2000,val=3) //task with void method
    public void testDoTaskVoid () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = voidTask;
        Object res = ec.doTask(t.target.getClass(), t.target, 
                               t.methods[0], t.args[0]);
        assertEquals(res.getClass(), StringBuilder.class);
        assertEquals(res.toString(), voidTaskResult);
    }

    @Test(timeout=2000,val=3) //task with primitive arguments
    public void testDoTaskPrimitive () {
        ec = new EvalClient("localhost",1024);
        EvalTask t = primitiveTask;
        Object res = ec.doTask(t.target.getClass(), t.target, 
                               t.methods[0], t.args[0]);
        assertEquals(res.getClass(), String.class);
        assertEquals(res, primitiveTaskResult);
    }

}