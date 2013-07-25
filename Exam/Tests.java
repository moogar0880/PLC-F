import charpov.grader.*;
import static org.testng.Assert.*;

import java.util.*;
import java.io.StringWriter;
import java.io.File;
import java.io.IOException;

public class Tests{
    StringWriter w;

    public void BEFORE() {
    }

    public void AFTER() {
    }

    boolean isPrime(long n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n%2 == 0 || n%3 == 0) return false;
        long sqrtN = (long)Math.sqrt(n)+1;
        for(long i = 6L; i <= sqrtN; i += 6) {
            if(n%(i-1) == 0 || n%(i+1) == 0) return false;
        }
        return true;
    }

    public static void main (String[] args) throws Exception {
        java.util.logging.Logger.getLogger("charpov.grader")
            .setLevel(java.util.logging.Level.WARNING);
        Tester tester = new Tester(Tests.class);
        tester.setConcurrencyLevel(Runtime.getRuntime().availableProcessors());
        tester.run();
    }

    @Test(val=2)
    public void testSortString(){
        String[] a = {"sat","at","fat","cat"};
        String[] sorted = {"at", "cat", "fat", "sat"};
        assertEquals(Exam.sortGeneric(a), sorted);
    }

    @Test(val=3)
    public void testSortInteger(){
        Integer[] a = {3,2,4,1,6,5};
        Integer[] sorted = {1,2,3,4,5};
        assertEquals(Exam.sortGeneric(a), sorted);
    }

    @Test(val=5)
    public void testProducer(){
        Exam.Producer p = new Exam.Producer(999);
        p.start();
        
        try{
            Thread.currentThread().wait(100);
        }
        catch(InterruptedException ex){
        }
        p.done = true;
        assertTrue(p.work.size() > 0);
    }

    @Test(val=5)
    public void testProducerConsumer(){
        Exam.Producer p = new Exam.Producer(999);
        Exam.Consumer c = new Exam.Consumer(p);
        c.start();
        try{
            Thread.currentThread().wait(1000);
        }
        catch(InterruptedException ex){
        }
        p.done = true;
        assertTrue(p.results.size() > 1);

        for(Integer i: p.results){
            assertTrue(isPrime(i));
        }
    }

    @Test(val=5)
    public void testProducerConsumers(){
        Exam.Producer p = new Exam.Producer(999);
        Exam.Consumer[] c = new Exam.Consumer[4];
        for(int i = 0; i < 4; i++){
            c[i] = new Exam.Consumer(p);
        }
        for(int i = 0; i < 4; i++){
            c[i].start();
        }

        try{
            Thread.currentThread().wait(1000);
        }
        catch(InterruptedException ex){
        }
        p.done = true;
        assertTrue(p.results.size() > 1);

        for(Integer i: p.results){
            assertTrue(isPrime(i));
        }
    }

    @Test(val=5)
    public void testNoMethods(){
        w = new StringWriter(4096);
        class A{
            int x = 3;
        }

        Exam.printMethods(new A(), w);

        assertTrue(w.toString().equals(""));
    }

    @Test(val=5)
    public void testOneMethod(){
        w = new StringWriter(4096);
        class A{
            int x = 3;
            public void foo(){}
        }
        class B extends A{
            public void bar(int x){}
        }

        Exam.printMethods(new B(), w);

        assertTrue(w.toString().contains("bar"));
    }

    @Test(val=5)
    public void testParams(){
        w = new StringWriter(4096);
        class A{
            int x = 3;
            public void foo(){}
        }
        class B extends A{
            public void bar(int x){}
        }

        Exam.printMethods(new B(), w);

        assertTrue(w.toString().contains("bar(int)"));
    }

    @Test(val=5)
    public void testNoInherited(){
        w = new StringWriter(4096);
        class A{
            int x = 3;
            public void foo(){}
        }
        class B extends A{
            public void bar(int x){}
        }

        Exam.printMethods(new B(), w);

        assertTrue(!(w.toString().contains("foo")));
    }
}
