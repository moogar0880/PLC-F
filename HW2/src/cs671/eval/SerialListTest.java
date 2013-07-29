package cs671.eval;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/19/13
 * Time: 10:32 PM
 */
public class SerialListTest {
    SerialList list;

    @org.junit.Before
    public void setUp() throws Exception {
        list = new SerialList();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        list = null;
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        list.add(one, oneString);
        list.add(two, twoString);
        assertTrue("Failed ", list.size() == 2);
    }

    @org.junit.Test
    public void testGet1() throws Exception {
        Integer one = new Integer(1);
        String oneString = "one";
        list.add(one, oneString);
        assertTrue("Failed testGet1", list.get(0).equals(oneString));
    }

    @org.junit.Test
    public void testGet2() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        list.add(one, oneString);
        list.add(two, twoString);
        assertTrue("Failed testGet2", list.get(1).equals(twoString));
    }

    @org.junit.Test
    public void testIsEmpty() throws Exception {
        //System.out.println(list.isEmpty());
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        list.add(one, oneString);
        list.add(two, twoString);
        assertFalse("Failed testIsEmpty", list.isEmpty());
    }

    @org.junit.Test
    public void testRemove1() throws Exception {
        Integer one = new Integer(1);
        String oneString = "one";
        list.add(one, oneString);
        list.remove(0);
        assertTrue("Failed testRemove1",list.isEmpty());
    }

    @org.junit.Test
    public void testRemove2() throws Exception {
        Integer one = new Integer(1);
        String oneString = "one";
        list.add(one, oneString);
        list.remove(one);
        assertTrue("Failed testRemove2",list.isEmpty());
    }

    @org.junit.Test
    public void testSize() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        list.add(one, oneString);
        list.add(two, twoString);
        assertTrue("Failed testSize",list.size() == 2);
    }

    @org.junit.Test
    public void testSort() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        list.add(two, twoString);
        list.add(one, oneString);
        list.sort();
        System.out.println(list.get(0));
        assertTrue("Failed testSort",list.get(0).equals(oneString));
    }

    @org.junit.Test
    public void testToString() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        String correctAnswer = "key: " + one + ", value: " + oneString + "\nkey: " + two + ", value: " + twoString + "\n";
        list.add(one, oneString);
        list.add(two, twoString);
        assertTrue("Failed testToString", correctAnswer.equals(list.toString()));
    }

    @org.junit.Test
    public void testReadObject() throws Exception {
        System.err.println("testReadObject not written yet");
    }

    @org.junit.Test
    public void testWriteObject() throws Exception {
        System.err.println("testWriteObject not written yet");
    }
}
