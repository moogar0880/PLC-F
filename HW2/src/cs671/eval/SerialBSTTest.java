package cs671.eval;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/19/13
 * Time: 11:13 PM
 */
public class SerialBSTTest {
    SerialBST bst;
    @Before
    public void setUp() throws Exception {
        bst = new SerialBST();
    }

    @After
    public void tearDown() throws Exception {
        bst = null;
    }

    @Test
    public void testAdd() throws Exception {
        Integer one = new Integer(1);
        String oneString = "one";
        bst.add(one, oneString);
        assertTrue("Failed testAdd", bst.find(one).equals(oneString));
    }

    @Test
    public void testBalance() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        bst.add(one, oneString);
        bst.add(two, twoString);
        bst.balance();
        assertTrue("testBalance Broke EVERYTHING", bst.size() == 2);
    }

    @Test
    public void testFind() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        bst.add(one, oneString);
        bst.add(two, twoString);
        assertTrue("Failed testAdd", bst.find(two).equals(twoString));
    }

    @Test
    public void testInOrder() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        bst.add(one, oneString);
        bst.add(two, twoString);
        Object[] objs = bst.inOrder();
        assertTrue("Failed InORder", true);
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertTrue("Failed ISEMPTY", bst.isEmpty());
    }

    @Test
    public void testPostOrder() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
    }

    @Test
    public void testPreOrder() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
    }

    @Test
    public void testReadObject() throws Exception {

    }

    @Test
    public void testRemove() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        bst.add(one, oneString);
        bst.add(two, twoString);
        bst.remove(one);
        assertTrue("Failed testRemove", bst.size() == 1);
    }

    @Test
    public void testSize() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        bst.add(one, oneString);
        bst.add(two, twoString);
        assertTrue("Failed testRemove", bst.size() == 2);
    }

    @Test
    public void testToString() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        bst.add(one, oneString);
        bst.add(two, twoString);
    }

    @Test
    public void testWriteObject() throws Exception {

    }
}
