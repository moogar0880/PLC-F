package cs671.eval;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/19/13
 * Time: 11:13 PM
 */
public class SerialPrioQueueTest {
    SerialPrioQueue q;
    @Before
    public void setUp() throws Exception {
        q = new SerialPrioQueue();
    }

    @After
    public void tearDown() throws Exception {
        q = null;
    }

    @Test
    public void testAdd() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        q.add(one, oneString);
        q.add(two, twoString);
        assertTrue("Failed testAdd", q.remove().equals(oneString));
    }

    @Test
    public void testBalance() throws Exception {
        System.err.println("Balance not yet implemented");
    }

    @Test
    public void testIsEmpty() throws Exception {
        Integer one = new Integer(1);
        String oneString = "one";
        assertTrue(q.isEmpty());
        q.add(one, oneString);
        assertFalse(q.isEmpty());
    }

    @Test
    public void testRemove() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        q.add(one, oneString);
        q.add(two, twoString);
        assertTrue("Failed testAdd", q.remove().equals(oneString));
    }

    @Test
    public void testSize() throws Exception {
        Integer one = new Integer(1);
        Integer two = new Integer(2);
        String oneString = "one";
        String twoString = "two";
        q.add(one, oneString);
        q.add(two, twoString);
        assertTrue("Failed testAdd", q.size() == 2);
    }

    @Test
    public void testToString() throws Exception {
        System.err.println("toString not yet implemented");
    }
}
