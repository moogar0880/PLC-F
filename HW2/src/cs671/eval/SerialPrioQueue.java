package cs671.eval;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/11/13
 * Time: 8:09 PM
 * A serializable priority queue implementation, using priority and value pairs. Implemented using a binary
 * heap. Insertion and head removal should be at most log(n).
 */
public class SerialPrioQueue implements Serializable {
    public transient Node head; //head of queue
    public transient int  size; //number of elements in queue
    private PriorityQueue<Node> q;

    public SerialPrioQueue(){
        head = null;
        size = 0;
        q = new PriorityQueue<Node>();
    }

    /**
     * Adds node with given priority and value to the correct place based on priority.
     * @param priority - priority of node to be added
     * @param val - value for node
     */
    public void add(Comparable priority, Object val){
        Node newNode = new Node(priority, val);
        if( q == null )
            q = new PriorityQueue<Node>();
        q.add(newNode);
        head = q.peek();
        size += 1;
    }

    /**
     * @return <code>True</code> if queue is empty, <code>False</code> otherwise
     */
	public boolean isEmpty(){
		return size == 0;
	}

    /**
     * Returns the data from the tree in order of root, left child and its children, right child and
     * its children. The children's descendants are given in the same pattern as described above.
     * @return An <code>Object</code> array containing the PreOrder output of the BinHeap
     */
    public Object[] preOrder(){
        Object[] toRet  = new Object[q.size()];
        Object[] cur    = q.toArray();
        recurisvePreOrderer(cur,0,toRet,0);
        return toRet;
    }

    /**
     * Recursively scan the heap to build Preorder array
     * @param cur - Current BinHeap array representation
     * @param i   - Index into cur Array
     * @param ary - Array representing PreOrder output
     * @param aryIndex - Index to be saved to in the PreOrder Array
     */
    private void recurisvePreOrderer(Object[] cur, int i, Object[] ary, int aryIndex){
        int left = 2*i + 1, right = 2*i + 2;
        ary[aryIndex] = ((Node)cur[i]).val;
        if (left < cur.length ){
            int index = findNextIndex(ary);
            recurisvePreOrderer(cur,left,ary,index);
            index = findNextIndex(ary);
            recurisvePreOrderer(cur,right,ary,index);
        }
    }

    /**
     * Returns the first index in an array where a null reference is found
     * @param ary - Array to be scanned for null reference
     * @return index into array with null reference
     */
    private int findNextIndex(Object[] ary){
        int i = 0;
        while(ary[i] != null && i < ary.length)
            i++;
        return i;
    }

    /**
     * Removes the highest priority element and returns the value from that node. Should also restore heap 
     * properties.
     * @return object removed
     */
	public Object remove(){
        Node toRet = (Node)q.poll();
        head = q.peek();
        size -= 1;
        return toRet.val;
	}

    /**
     * @return size of queue
     */
	public int size(){
		return size;
	}

    /**
     * Returns a string representation of the priority queue, with priority and value pairs. Should be given
     * in "pre-order" arrangement from the heap (root, left child and descendants, right child and descendants)
     * @return string representation of queue
     */
	public String toString(){
        String toRet = "";
        Object[] ary = preOrder();
        for(Object o : ary )
            toRet += o + ", ";
        return toRet;
	}

    /**
     * Overrides inherited serialization output
     * @param out - ObjectOutputStream to be written to
     */
    private void writeObject(ObjectOutputStream out){
        Object[] ary = q.toArray();
        try {
            out.writeInt(q.size());
            for(Object o : ary){
                out.writeObject(((Node)o).priority);
                out.writeObject(((Node)o).val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides inherited de-serialize behavior
     * @param in - ObjectInputStream to be read from
     */
    private void readObject(ObjectInputStream in){
        try {
            int s = in.readInt();
            for(int i = 0; i < s; i++){
                Comparable priority = (Comparable)in.readObject();
                Object val = in.readObject();
                add(priority,val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inner node class to store data. Stores reference to next node in the priority queue.
     */
    public static class Node implements Comparable{
    	public Comparable priority; //priority for this node
    	public Object val;			//Data for this node

    	public Node(Comparable k, Object v){
            priority = k;
            val = v;
    	}

        /**
         * Override compareTo functionality to allow for easy sorting into PrioQueue
         * @param o - Object for this Node to be compared to
         * @return integer representation of the comparison
         */
        @Override
        public int compareTo(Object o) {
            return -this.priority.compareTo(((Node)o).priority);
        }

        /**
         * Simple Node toString method
         * @return String representation of this Node
         */
        public String toString(){
            return val.toString();
        }
    }
}