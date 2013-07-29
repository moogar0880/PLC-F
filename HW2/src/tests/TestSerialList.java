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

//@Test(val=10)
public class TestSerialList {
    Random r;
    SerialList sl;
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
        Tester tester = new Tester(TestSerialList.class);
        tester.setConcurrencyLevel(1);
        tester.run();
    }

    @Test(timeout=1000,val=1) //SerialList add/get
    public void testAdd () {
        sl = new SerialList();
        for(int i=0; i<5; i++){
            sl.add(5-i, "test"+i);
        }
        for(int i=0; i<5; i++){
            assertEquals(sl.get(i), "test"+(4-i)); //test by index
            assertEquals(sl.get(new Integer(5-i)), "test"+i); //test by key
        }
        try{
            sl.get(7); //getting item out of range fails
            throw new AssertionError("Expected IndexOutOfBoundsException.");
        }
        catch(IndexOutOfBoundsException ex){}
        try{
            sl.get(new Integer(6)); //getting non-existent item fails
            throw new AssertionError("Expected NoSuchElementException.");
        }
        catch(NoSuchElementException ex){ return; }
    }

    @Test(timeout=1000,val=1) //SerialList add/get
    public void testAddError () {
        sl = new SerialList();
        for(int i=0; i<5; i++){
            sl.add(5-i, "test"+i);
        }
        try{
            sl.get(7); //getting item out of range fails
            throw new AssertionError("Expected IndexOutOfBoundsException.");
        }
        catch(IndexOutOfBoundsException ex){}
        try{
            sl.get(new Integer(6)); //getting non-existent item fails
            throw new AssertionError("Expected NoSuchElementException.");
        }
        catch(NoSuchElementException ex){ return; }
    }

    @Test(timeout=1000,val=1) //SerialList isEmpty
    public void testIsEmpty () {
        sl = new SerialList();
        assertTrue(sl.isEmpty());
        sl.add(0, "test");
        assertFalse(sl.isEmpty());
    }

    @Test(timeout=1000,val=1) //SerialList size
    public void testSize () {
        sl = new SerialList();
        assertEquals(sl.size(), 0);
        sl.add(0, "test");
        assertEquals(sl.size(), 1);
        for(int i=0; i<100; i++){
            sl.add(0, "test");
        }
        assertEquals(sl.size(), 101);
    }

    @Test(timeout=1000,val=1) //SerialList remove
    public void testRemove () {
        sl = new SerialList();
        for(int i=0; i<5; i++){
            sl.add(5-i, "test"+i);
        }
        sl.remove(2); //remove by index
        assertEquals(sl.get(2), "test"+1);
        try{
            sl.remove(4);
            throw new AssertionError("Expected IndexOutOfBoundsException.");
        } //removing item out of range fails
        catch(IndexOutOfBoundsException ex){ }
        sl.remove(new Integer(1)); //remove by key
        assertEquals(sl.get(0), "test"+3);
    }

    @Test(timeout=1000,val=1) //SerialList remove
    public void testRemoveError () {
        sl = new SerialList();
        for(int i=0; i<5; i++){
            sl.add(5-i, "test"+i);
        }
        try{
            sl.remove(5);
            throw new AssertionError("Expected IndexOutOfBoundsException.");
        } //removing item out of range fails
        catch(IndexOutOfBoundsException ex){ }
        try{
            sl.remove(new Integer(100));
            throw new AssertionError("Expected IndexOutOfBoundsException.");
        } //removing item not in list fails
        catch(NoSuchElementException ex){ }
        sl.remove(new Integer(1)); //remove by key
        try{
            sl.remove(new Integer(1));  //removing removed item fails
            throw new AssertionError("Expected NoSuchElementException."); 
        }
        catch(NoSuchElementException ex){ }
    }

    @SuppressWarnings("unchecked")
    @Test(timeout=2000,val=2) //SerialList sort
    public void testSort () {
        sl = new SerialList();
        Integer[] sorted = new Integer[] {1,2,3,5,5,7,8};
        for(Integer x: new Integer[] {5,2,5,7,3,8,1}){
            sl.add(x, x);
        }
        int origSize = sl.size();
        sl.sort();
        assertEquals(sl.size(), origSize);
        SerialList.Node curr = sl.head;
        SerialList.Node next = sl.head.next;
        int i=0;
        while(next!=null){
            assertTrue(curr.key.compareTo(next.key) <= 0);
            assertEquals(curr.key, curr.val);
            assertEquals(curr.key, sorted[i]);
            curr = curr.next;
            next = next.next;
            i++;
        }
    }

    @Test(timeout=3000,val=2) //SerialList efficient serialization
    public void testSerialization () {
        SerialList sl1 = new SerialList();
        SerialList sl2 = new SerialList();
        for(int i=0; i<1000; i++){
            for(Long x: new Long[] {1L,2L,3L,0L,4L,5L}){
                sl1.add(x, x);
                sl2.add(r.nextLong(), x);
            }
        }
        
        int copyLength=0;
        int uniqueLength=0;
        try{
            out.writeObject(sl1);
            copyLength = bos.size();
            out.writeObject(sl2);
            uniqueLength = bos.size()-copyLength;
        }
        catch(Exception ex){
            System.err.println(ex.toString());
            assertTrue(false);
        }

        assertTrue(copyLength < uniqueLength);

        try{
            bis = new ByteArrayInputStream(bos.toByteArray());
            in = new ObjectInputStream(bis);
            SerialList inList = (SerialList)(in.readObject());
            assertEquals(inList.size(), 6000);
            assertEquals(inList.get(0), 5L);
        }
        catch(Exception ex){
            System.err.println(ex.toString());
            assertTrue(false);
        } 
    }
}