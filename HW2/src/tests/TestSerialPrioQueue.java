package tests;

import cs671.eval.*;

import charpov.grader.*;
import static org.testng.Assert.*;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

//@Test(val=15)
public class TestSerialPrioQueue {
    Random r;
    SerialPrioQueue spq;
    ObjectOutputStream out;
    ObjectInputStream in;
    ByteArrayOutputStream bos;
    ByteArrayInputStream bis;

    public void BEFORE() {
        try{
            r = new Random();
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
            out.close();
            bos.close();
        }
        catch(Exception ex){ }
    }

    public static void main (String[] args) throws Exception {
        java.util.logging.Logger.getLogger("charpov.grader")
            .setLevel(java.util.logging.Level.WARNING);
        Tester tester = new Tester(TestSerialPrioQueue.class);
        tester.setConcurrencyLevel(1);
        tester.run();
    }

    @Test(timeout=2000,val=1) //SerialPrioQueue adding
        @SuppressWarnings("unchecked")
    public void testAdd () {
        spq = new SerialPrioQueue();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            spq.add(i,i);
        
        assertEquals(spq.size, input.length);

        assertEquals(spq.head.priority, 99);
    }

    @Test(timeout=2000,val=1) //SerialPrioQueue remove
        @SuppressWarnings("unchecked")
    public void testRemove () {
        spq = new SerialPrioQueue();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            spq.add(i,i);

        assertEquals(spq.remove(), 99);
        assertEquals(spq.size, input.length-1);
        assertEquals(spq.head.priority, 67);
    }

    @Test(timeout=2000,val=5) //SerialPrioQueue ordering
        @SuppressWarnings("unchecked")
    public void testOrder () {
        spq = new SerialPrioQueue();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            spq.add(i,i);

        assertEquals(spq.remove(), 99);

        Comparable prevPrio = new Integer(99);
        while(spq.size>0){
            Comparable currPrio = spq.head.priority;
            assertTrue(prevPrio.compareTo(currPrio)>0);
            spq.remove();
            prevPrio = currPrio;
        }
    }

    @Test(timeout=4000,val=5) //SerialPrioQueue preOrder
    @SuppressWarnings("unchecked")
    public void testPreOrder () {
        spq = new SerialPrioQueue();
        
        Integer[] input = 
            new Integer[]{3,12,67,5,0,10,99,18,1,12,2,22,30,4,11};
        for(Integer i: input)
            spq.add(i,i);
        
        Object[] result = spq.preOrder();

        assertEquals(result.length, input.length);

        //check heap property
        Integer rootLeft = (Integer)result[1];
        int i;
        for(i=2; i<=result.length/2; i++){
            assertTrue(rootLeft.compareTo((Integer)result[i])>=0);
        }
        
        Integer rootRight = (Integer)result[i];
        for(int j=i+1; j<result.length; j++){
            assertTrue(rootRight.compareTo((Integer)result[j])>=0);
        }
    }

    @Test(timeout=3000,val=3) //SerialPrioQueue efficient serialization
    @SuppressWarnings("unchecked")
    public void testSerialization () {
        SerialPrioQueue spq1 = new SerialPrioQueue();
        SerialPrioQueue spq2 = new SerialPrioQueue();
        for(int i=0; i<1000; i++){
            for(Long x: new Long[] {1L,2L,3L,0L,4L,5L}){
                spq1.add(x, x);
                spq2.add(r.nextLong(), x);
            }
        }
        
        int copyLength=0;
        int uniqueLength = 0;
        try{
            out.writeObject(spq1);
            copyLength = bos.size();
            out.writeObject(spq2);
            uniqueLength = bos.size()-copyLength;
        }
        catch(Exception ex){
            System.err.println(ex.toString());
            assertTrue(false);
        }

        assertTrue(copyLength < uniqueLength);

        SerialPrioQueue inPrioQueue = null;
        try{
            bis = new ByteArrayInputStream(bos.toByteArray());
            in = new ObjectInputStream(bis);
            inPrioQueue = (SerialPrioQueue)(in.readObject());
            assertEquals(inPrioQueue.size(), 6000);
            assertEquals(inPrioQueue.head.val, 5L);
        }
        catch(Exception ex){
            System.err.println(ex.toString());
            assertTrue(false);
        }     

        Comparable prevPrio = (Long)inPrioQueue.remove();
        while(inPrioQueue.size>0){
            Comparable currPrio = inPrioQueue.head.priority;
            assertTrue(prevPrio.compareTo(currPrio)>=0);
            inPrioQueue.remove();
            prevPrio = currPrio;
        }
    }
}