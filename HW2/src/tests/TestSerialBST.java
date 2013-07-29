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
public class TestSerialBST {
    Random r;
    SerialBST sb;
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
        Tester tester = new Tester(TestSerialBST.class);
        tester.setConcurrencyLevel(1);
        tester.run();
    }

    private int nChildren(SerialBST.Node n){
        if(n==null)
            return 0;
        return nChildren(n.left) + nChildren(n.right) + 1;
    }

    @Test(timeout=2000,val=1) //SerialBST creation
    public void testCreate () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        assertEquals(sb.root.key, input[0]);
        assertEquals(sb.root.val, input[0]);
        assertEquals(sb.root.right.key, input[1]);
        assertEquals(sb.root.right.val, input[1]);
        assertEquals(sb.root.left.key, input[4]);
        assertEquals(sb.root.left.val, input[4]);
    }

    @Test(timeout=2000,val=1) //SerialBST size
    public void testSize () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        assertEquals(sb.size, input.length);

        assertEquals(nChildren(sb.root), input.length);
    }

    @Test(timeout=2000,val=1) //SerialBST isEmpty
    public void testIsEmpty () {
        sb = new SerialBST();
        assertTrue(sb.isEmpty());
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        assertFalse(sb.isEmpty());
    }

    @Test(timeout=2000,val=1) //SerialBST inOrder
    @SuppressWarnings("unchecked")
    public void testInOrder () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        Object[] result = sb.inOrder();
        assertEquals(result.length, input.length);
        ArrayList<Integer> sortedInput = new ArrayList(Arrays.asList(input));
        Collections.sort(sortedInput);
        for(int i=0; i<result.length; i++){
            assertEquals(sortedInput.get(i), result[i]);
        }
    }

    @Test(timeout=2000,val=1) //SerialBST postOrder
    @SuppressWarnings("unchecked")
    public void testPostOrder () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        Object[] result = sb.postOrder();
        assertEquals(result.length, input.length);
        Integer[] postOrderInput = new Integer[]{1,0,10,5,18,99,67,12,3};
        for(int i=0; i<result.length; i++){
            assertEquals(postOrderInput[i], result[i]);
        }
    }

    @Test(timeout=2000,val=1) //SerialBST preOrder
    @SuppressWarnings("unchecked")
    public void testPreOrder () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        Object[] result = sb.preOrder();
        assertEquals(result.length, input.length);
        Integer[] preOrderInput = new Integer[]{3,0,1,12,5,10,67,18,99};
        for(int i=0; i<result.length; i++){
            assertEquals(preOrderInput[i], result[i]);
        }
    }

    @Test(timeout=2000,val=2) //SerialBST find
    public void testFind () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        sb.find(67);
        try{
            sb.find(100);
            throw new AssertionError("Expected NoSuchElementException.");
        }
        catch(NoSuchElementException ex){
        }
    }

    @Test(timeout=2000,val=2) //SerialBST remove
    public void testRemove () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        assertEquals(sb.remove(67), 67);
        try{
            sb.find(67);
            throw new AssertionError("Expected NoSuchElementException.");
        }
        catch(NoSuchElementException ex){
        }
        
        try{
            sb.remove(100);
            throw new AssertionError("Expected NoSuchElementException.");
        }
        catch(NoSuchElementException ex){
        }
    }

    @Test(timeout=2000,val=2) //SerialBST balance
    public void testBalance () {
        sb = new SerialBST();
        
        Integer[] input = new Integer[]{3,12,67,5,0,10,99,18,1};
        for(Integer i: input)
            sb.add(i,i);
        
        sb.balance();
        
        assertEquals(sb.size, input.length);

        assertEquals(nChildren(sb.root.left), nChildren(sb.root.right));
    }

    @Test(timeout=3000,val=3) //SerialBST efficient serialization
    public void testSerialization () {
        SerialBST sb1 = new SerialBST();
        SerialBST sb2 = new SerialBST();
        for(int i=0; i<1000; i++){
            for(Long x: new Long[] {1L,2L,3L,0L,4L,5L}){
                sb1.add(x, x);
                sb2.add(r.nextLong(), x);
            }
        }
        
        int copyLength=0;
        int uniqueLength = 0;
        try{
            out.writeObject(sb1);
            copyLength = bos.size();
            out.writeObject(sb2);
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
            SerialBST inBST = (SerialBST)(in.readObject());
            assertEquals(inBST.size(), 6000);
            assertEquals(inBST.root.val, 1L);
        }
        catch(Exception ex){
            System.err.println(ex.toString());
            assertTrue(false);
        } 
    }
}