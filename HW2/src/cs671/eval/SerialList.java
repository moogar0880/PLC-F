package cs671.eval;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/11/13
 * Time: 7:50 PM
 * A serializable linked-list implementation, using key and value pairs.
 */
public class SerialList implements Serializable {
	public transient Node head, tail; //head and tail of the list
	public transient int size; //number of elements in the list

	public SerialList(){
        head = tail = null;
        size = 0;
	}

    /**
     * Adds node with given key and value to the head of the list.
     * @param key - key of value to be stored
     * @param val - value to be stored
     */
	public void add(Comparable key, Object val){
        if( head == null ){
            head = new Node(key, val);
            head.next = null;
            tail = head;
        }
        else{
            Node tmp = new Node(key, val);
            tmp.next = head;
            head = tmp;
        }
        size += 1;
	}

    /**
     * Retrieves the data of the element with the given key.
     * @param key - key to search for the object with
     * @return the Object found with the specified key
     * @throws NoSuchElementException
     */
	public Object get(Comparable key){
        Node scan = head;

        while( scan != null && scan.key.compareTo(key) != 0 )
            scan = scan.next;

        if(scan == null)
            throw new NoSuchElementException();
        return scan.val;
	}

    /**
     * Retrieves the data of the element at the given index.
     * @param index - index to return the object from
     * @return the Object found with the specified key
     * @throws IndexOutOfBoundsException - if the index is invalid for this list
     */
	public Object get(int index){
        if(index > size || index < 0)
            throw new IndexOutOfBoundsException();

        Node scan = head;
        int i = 0;
        while( i < index ){
            scan = scan.next;
            i++;
        }

        return scan.val;
	}

    /**
     * Return <code>True</code> if list is empty, <code>False</code> otherwise
     * @return <code>True</code> if list is empty, <code>False</code> otherwise
     */
	public boolean isEmpty(){
		return size == 0;
	}

    /**
     * Removes the first element with the given key and returns the value from that node.
     * @param key - key for object to be removed
     * @return the object removed
     * @throws NoSuchElementException - if the key is not found in the list.
     */
	public Object remove(Comparable key){
        Node scan1 = null, scan2 = head;
        if( scan2.key.compareTo(key) == 0 ){
            if( scan2.next == null ){
                head = tail = null;
            }
            else{
                head = scan2.next;
                size -= 1;
                return scan2.val;
            }
        }
        while( scan2 != null && scan2.key.compareTo(key) != 0 ){
            scan1 = scan2;
            scan2 = scan2.next;
        }
        if( scan2 == null )
            throw new NoSuchElementException();

        if( scan2.key.compareTo(head.key) == 0 ){
            if (scan2.next == null )
                head = tail = null;
            else{
                head = scan2.next;
            }
        }
        else if( scan2.equals(tail)){
            scan1.next = null;
            tail = scan1;
        }
        else{
            scan1.next = scan2.next;
        }
        size -= 1;

        return scan2.val;
	}

    /**
     * Removes the element at the given index and returns the value from that node.
     * @param index - index to remove the element from
     * @return the object removed
     * @throws NoSuchElementException - if the key is not found in the list.
     */
	public Object remove(int index){
        if(index > (size - 1) || index < 0)
            throw new IndexOutOfBoundsException();

        Node scan1 = null, scan2 = head;
        int i = 0;
        while( i < index ){
            scan1 = scan2;
            scan2 = scan2.next;
            i++;
        }
        if( scan2.equals(head) ){
            if (scan2.next == null )
                head = tail = null;
            else{
                head = scan2.next;
            }
        }
        else if( scan2.equals(tail)){
            scan1.next = null;
            tail = scan1;
        }
        else{
            scan1.next = scan2.next;
        }

        size -= 1;
        return scan2.val;
	}

    /**
     *	Return size of list
     * @return size of list
     */
	public int size(){
		return size;
	}

    /**
     * Sorts the list on each node's key, from lowest to highest.
     */
    public void sort(){
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node scan = head;
        while (scan != null){
            nodes.add(scan);
            scan = scan.next;
        }
        Collections.sort(nodes);
        head = nodes.get(0);
        for(int i = 0; i < nodes.size()-1; i++){
            nodes.get(i).next = nodes.get(i+1);
        }
        nodes.get(nodes.size()-1).next = null;
    }

    /**
     * Returns a string representation of the list, with key and value pairs.
     * @return a string representation of the list, with key and value pairs
     */
	public String toString(){
        String toRet = "";
        Node scan = head;
        while(scan != null){
            toRet += "key: " + scan.key + ", value: " + scan.val + "\n";
            scan = scan.next;
        }
        return toRet;
	}

    /**
     * Reads the entire list from <code>out</code> in as efficient a way as possible, re-calculating any
     * necessary values.
     * @param in - input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in){
        try {
            int size = in.readInt();
            for(int i = 0; i < size; i++){
                Comparable k = (Comparable)in.readObject();
                Object v = in.readObject();
                addToTail(k,v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Because sorting works counter-intuitively this method is needed to de-serialize appropriately
     * as indicated by tests
     * @param key - key of Node
     * @param val - val of Node
     */
    private void addToTail(Comparable key, Object val){
        if( head == null ){
            head = new Node(key, val);
            head.next = null;
            tail = head;
        }
        else{
            Node tmp = new Node(key, val);
            tail.next = tmp;
            tail = tail.next;
        }
        size += 1;
    }

    /**
     * Writes the entire list to out in as efficient a way as possible. Do not store any duplicate keys or
     * values, but instead multiple references to the same object.
     * @param out - output stream
     * @throws IOException - if something goes wrong with writing to the stream
     */
	private void writeObject(ObjectOutputStream out){
        Node scan = head;
            try {
                out.writeInt(size);
                while( scan != null ){
                    out.writeObject(scan.key);
                    out.writeObject(scan.val);
                    scan = scan.next;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
	 * Inner node class to store data. Stores reference to next node in the list.
	 */
    public static class Node implements Comparable{
        public Comparable key; //Key for this node
        public Node next; 	   //next item in the list
        public Object val;	   //data for this node

        public Node(Comparable k, Object v){
            key = k;
            val = v;
        }

        /**
         * equals method
         * @param o - object to be compared against
         * @return true if nodes are equal, false otherwise
         */
        @Override
        public boolean equals(Object o) {
            Node toCheck = (Node) o;
            if( this.key.compareTo(toCheck.key) == 0 ){
                if(this.val.equals(toCheck.val))
                    return true;
            }
            return false;
        }

        /**
         * Override compareTo functionality to allow for easy sorting into PrioQueue
         * @param o - Object for this Node to be compared to
         * @return integer representation of the comparison
         */
        @Override
        public int compareTo(Object o) {
            return this.key.compareTo(((Node)o).key);
        }

        /**
         * toString method
         * @return String representation of this node
         */
        public String toString(){
            String toRet = "";
            toRet += "[key: " + key + ", " + val + "]";
            return toRet;
        }
    }
}
