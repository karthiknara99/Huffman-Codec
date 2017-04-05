package huffmancodec;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author karthik
 */

class PairNode {
    
    Node data;
    PairNode leftChild;
    PairNode nextSibling;
    PairNode prevSibling;
    
    public PairNode( Node data ) {
        
        this.data = data;
        leftChild = null;
        nextSibling = null;
        prevSibling = null;
    }
}

public class PairingHeapPQ implements MyPriorityQueue {

    private PairNode root;
    private int heapSize;
    
    public PairingHeapPQ() {
        this.clear();
    }
    
    @Override
    public void add( Node newNode ) {
        
        PairNode newPairNode = new PairNode(newNode);
        if( root == null )
            root = newPairNode;
        else
            root = compareAndLink( root, newPairNode );
        heapSize++;
    }
    
    public PairNode compareAndLink( PairNode one, PairNode two ) {
        
        if( two == null )
            return one;
        if( two.data.freq < one.data.freq )
        {
            //Attach one as leftmost child of two
            two.prevSibling = one.prevSibling;
            one.prevSibling = two;
            one.nextSibling = two.leftChild;
            if( one.nextSibling != null )
                one.nextSibling.prevSibling = one;
            two.leftChild = one;
            return two;
        }
        else
        {
            //Attach two as leftmost child of one
            two.prevSibling = one;
            one.nextSibling = two.nextSibling;
            if( one.nextSibling != null )
                one.nextSibling.prevSibling = one;
            two.nextSibling = one.leftChild;
            if( two.nextSibling != null )
                two.nextSibling.prevSibling = two;
            one.leftChild = two;
            return one;
        }
    }
    
    public void clear() {
       
        root = null;
        heapSize = 0;
    }
    
    @Override
    public Node extractMin() {
        
        if( size() < 1 )
            return null;
        Node temp = root.data;
        if( root.leftChild == null )
            root = null;
        else
            root = combineSiblings( root.leftChild );
        heapSize--;
        return temp;
    }
    
    public PairNode combineSiblings( PairNode firstSibling ) {
        if( firstSibling.nextSibling == null )
            return firstSibling;
        
        Queue<PairNode> q = new LinkedList<>();
        while( firstSibling != null )
        {
            q.add(firstSibling);
            firstSibling.prevSibling.nextSibling = null;
            firstSibling = firstSibling.nextSibling;
        }
        
        //Multi-pass scheme
        while( q.size() > 1 )
        {
            PairNode one = q.remove();
            PairNode two = q.remove();
            q.add( compareAndLink(one, two) );
        }
        
        return q.poll();
    }
    
    @Override
    public int size() {
        return heapSize;
    }
    
    @Override
    public Node peek() {
        return root.data;
    }
}
