package cs671.eval;

import java.io.*;
import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 6/11/13
 * Time: 7:31 PM
 *
 */
public class SerialBST implements Serializable {
	public transient Node root; //Root node of the tree
	public transient int  size; //number of elements in the tree
    private int index = 0;

	public SerialBST(){
        root = null;
        size = 0;
	}

    /**
     * Adds node with given key and value to the correct position in the tree based on key. Node will always 
     * be added as a leaf of the current tree.
     * @param key
     * @param val
     */
	public void add(Comparable key, Object val){
        Node newNode = new Node(key, val);
        newNode.left = newNode.right = null;
        if( root == null ){
            root = newNode;
            root.parent = null;
            size += 1;
        }
        else
            recursiveAdd(newNode,root);
	}

    private void recursiveAdd(Node n, Node cur){
        int result = n.compareTo(cur);

        if(result == 0){
            if(cur.left != null )
                recursiveAdd(n,cur.left);
            else{
                cur.left = n;
                n.parent = cur;
                size += 1;
            }
        }
        else if(result > 0 && cur.right != null){
            recursiveAdd(n,cur.right);
        }
        else if(result > 0){
            cur.right = n;
            n.parent  = cur;
            size += 1;
        }
        else if(result < 0 && cur.left != null){
            recursiveAdd(n,cur.left);
        }
        else{
            cur.left = n;
            n.parent = cur;
            size += 1;
        }
    }

    /**
     * Generic balance function, starts balancing at root
     */
    public void balance(){
        Node[] n = new Node[size];
        balanceHelper(n,root);
        List<Node> nodes = Arrays.asList(n);
        Collections.sort(nodes);
        int middle = nodes.size()/2;
        Node newRoot = nodes.get(middle);
        newRoot.left = newRoot.right = null;
        root = newRoot;
        int[] indexes = new int[size];
        indexes[middle] = middle;
        recursiveBalancer(nodes, middle, root, 0, nodes.size()-1, indexes);
        Node last = nodes.get(nodes.size()-1);
        add(last.key, last.val);
        size -= 1;
    }

    /**
     * Convert BST to an array for balancing
     * @param ary - array of Nodes
     * @param cur - Current node
     */
    private void balanceHelper(Node[] ary, Node cur){
        int index = findNextIndex(ary);
        ary[index] = cur;
        if(cur.left != null)
            balanceHelper(ary,cur.left);
        if(cur.right != null)
            balanceHelper(ary, cur.right);
    }

    /**
     * Perform balancing on BST Nodes
     * @param nodes - <code>ArrayList</code> of Nodes
     * @param index - Index of next Node to add
     * @param parent - Parent Node
     * @param max - Max value for right recursion
     */
    private void recursiveBalancer(List<Node> nodes, int index, Node parent, int min, int max, int[] indexes){
        int leftSubTreeMiddle  = (index + min)/2;
        int rightSubTreeMiddle = (index + max)/2;
        Node left = null;
        if(leftSubTreeMiddle >= 0 && indexes[leftSubTreeMiddle] == 0){
            left = nodes.get(leftSubTreeMiddle);
            parent.left = left;
            left.parent = parent;
            left.right = left.left = null;
            indexes[leftSubTreeMiddle] = leftSubTreeMiddle;
        }
        Node right = null;
        if(rightSubTreeMiddle < nodes.size() && indexes[rightSubTreeMiddle] == 0){
            right = nodes.get(rightSubTreeMiddle);
            parent.right = right;
            right.parent = parent;
            right.left = right.right = null;
            indexes[rightSubTreeMiddle] = rightSubTreeMiddle;
        }

        if(left != null && leftSubTreeMiddle - 1 >= 0 && (indexes[leftSubTreeMiddle-1] == 0 || indexes[rightSubTreeMiddle+1] == 0))
            recursiveBalancer(nodes, leftSubTreeMiddle, left, min, index, indexes);
        if(right != null && rightSubTreeMiddle + 1 < nodes.size() && (indexes[rightSubTreeMiddle-1] == 0 || indexes[rightSubTreeMiddle+1] == 0))
            recursiveBalancer(nodes, rightSubTreeMiddle, right, index, max, indexes);
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
     * Retrieves the element with the given key.
     * @param key - key to be searched for
     * @return the object found
     * @throws NoSuchElementException if the key is not found in the tree
     */
	public Object find(Comparable key){
        Node scan = root;
        while( scan != null ){
            int result = key.compareTo(scan.key);
            if( result == 0 ){
                return scan.val;
            }
            else if( result < 0 ){
                scan = scan.left;
            }
            else{
                scan = scan.right;
            }
        }
        //if not found in tree throw exception
        throw new NoSuchElementException();
	}

    /**
     * Returns the data from the tree in order of keys, from lowest to highest.
     * @return the data from the tree in order of keys, from lowest to highest.
     */
	public Object[] inOrder(){
        Object[] toRet = new Object[size];
        index = 0;
        inOrderHelper(toRet, root);
        return toRet;
	}

    /**
     * Recursive inorder helper
     * @param nodes - nodes in BST
     * @param n - current node
     */
    private void inOrderHelper(Object[] nodes, Node n){
        if( n == null )
            return;
        inOrderHelper(nodes, n.left);
        nodes[index++] = n.val;
        inOrderHelper(nodes, n.right);
    }

    /**
     * @return <code>True</code> if the the tree is empty, <code>False</code> otherwise
     */
	public boolean isEmpty(){
		return size == 0;
	}

    /**
     * Returns the data from the tree in order of left child and its children, right child and its children,
     * root. The children's descendants are given in the same pattern as described above.
     * @return postOrdered form of the tree
     */
	public Object[] postOrder(){
        Object[] toRet = new Object[size];
        index = 0;
        postOrderHelper(toRet, root);
        return toRet;
	}

    /**
     * Recursive Postorder helper method
     * @param nodes - array of nodes in BST
     * @param n - current node
     */
    private void postOrderHelper(Object[] nodes, Node n){
        if( n == null )
            return;
        postOrderHelper(nodes, n.left);
        postOrderHelper(nodes, n.right);
        nodes[index++] = n.val;
    }

    /**
     * Returns the data from the tree in order of root, left child and its children, right child and its 
     * children. The children's descendants are given in the same pattern as described above.
     * @return preOrdered form of the tree
     */
	public Object[] preOrder(){
        Node[] nodes = new Node[size];
        Object[] toRet = new Object[size];
        index = 0;
        toStringHelper(nodes, root);
        int i = 0;
        for( Node n : nodes )
            toRet[i++] = n.val;
        return toRet;
	}

    /**
     * Finds and returns the node with the corresponding key
     * @param key - key to be searched for
     * @return - the node with the corresponding key
     * @throws NoSuchElementException if the key is not found
     */
    public Node findNode(Comparable key){
        Node scan = root;
        while( scan != null ){
            int result = key.compareTo(scan.key);
            if( result == 0 ){
                return scan;
            }
            else if( result < 0 ){
                scan = scan.left;
            }
            else{
                scan = scan.right;
            }
        }
        //if not found in tree throw exception
        throw new NoSuchElementException();
    }

    /**
     * Removes the element with the given key and returns the value from that node.
     * @param key - key to search the tree for
     * @return the object found, or null
     * @throws NoSuchElementException - if the key is not found in the tree
     */
	public Object remove(Comparable key){
        Node toRemove = findNode(key);
        //Exception will be thrown if element not found
        if( toRemove.right != null && toRemove.left != null ){
            Node rightmostChild = next(toRemove.right);
            if(rightmostChild.parent != null ){
                if(rightmostChild.equals(rightmostChild.parent.right))
                    rightmostChild.parent.right = null;
                else
                    rightmostChild.parent.left = null;
            }
            else
                root = rightmostChild;
            rightmostChild.parent = toRemove.parent;
            rightmostChild.left = toRemove.left;
            rightmostChild.right = toRemove.right;
            if( toRemove.parent != null ){
                if(toRemove.equals(toRemove.parent.left))
                    toRemove.parent.left = rightmostChild;
                else
                    toRemove.parent.right = rightmostChild;
            }
        }
        size -= 1;
        return toRemove.val;
	}

    /**
     * Return the rightmost child of a BST subtree
     * @param n - current node
     * @return - the rightmost child after it's been removed
     */
    private Node next(Node n){
        if(n.left != null)
            next(n.left);
        return n;
    }

    /**
     * @return size of the tree
     */
	public int size(){
		return size;
	}

    /**
     * Returns a string representation of the tree, with key, value pairs. The nodes are given in the same
     * order as the preOrder method.
     * @return <code>String</code> representation of the tree
     */
    @Override
	public String toString(){
        String toRet = "";
        Node[] nodes = new Node[size];
        toStringHelper(nodes, root);
        for( Node n : nodes )
            toRet += n + ",";
        return toRet.substring(0, toRet.length()-1);
	}

    /**
     * Recursive helper method for printing tree
     * @param nodes - array of nodes in Tree
     * @param n - current node
     */
    private void toStringHelper(Node[] nodes, Node n){
        int index = findNextIndex(nodes);
        nodes[index] = n;
        if( n.left != null )
            toStringHelper(nodes, n.left);
        if(n.right != null )
            toStringHelper(nodes, n.right);
    }

    /**
     * Reads the entire tree from <code>out</code> in as efficient a way as possible, re-calculating any
     * necessary values.
     * @param in - input stream
     */
    private void readObject(ObjectInputStream in){
        try {
            in.defaultReadObject();
            int s = in.readInt();
            for(int i = 0; i < s; i++){
                Comparable key = (Comparable)in.readObject();
                Object val = in.readObject();
                add(key,val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the entire tree to <code>in</code> in as efficient a way as possible.
     * @param out - output stream
     */
	private void writeObject(ObjectOutputStream out){
        Node[] nodes = new Node[size];
        toArray(nodes, root);
        try{
            out.defaultWriteObject();
            out.writeInt(size);
            for(Node n : nodes){
                out.writeObject(n.key);
                out.writeObject(n.val);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
	}

    private void toArray(Node[] ary, Node cur){
        int index = findNextIndex(ary);
        ary[index] = cur;
        if(cur.left != null)
            balanceHelper(ary,cur.left);
        if(cur.right != null)
            balanceHelper(ary, cur.right);
    }

	/**
	 * Inner node class to store data. Stores reference to left and right children.
	 */
    public static class Node implements Comparable{
        public Comparable key; 	 //Key for this node
        public Node left, right; //child Nodes
        public Node parent;      //Parent Node
        public Object val;		 //data for this node

        public Node(Comparable k, Object v){
            key = k;
            val = v;
        }

        /**
         * to String method
         * @return String representation of this Node
         */
        public String toString(){
            String toRet = "";
            toRet += "[key: " + key + ", " + val + "]";
            return toRet;
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
         * equals method
         * @param o - object to be checked against
         * @return true if objects are equal, false otherwise
         */
        @Override
        public boolean equals(Object o) {
            if( o == null )
                return false;
            Node toCheck = (Node) o;
            if( key.compareTo(toCheck.key) == 0 ){
                if(val.equals(toCheck.val))
                    return true;
            }
            return false;
        }
    }
}