package huffmancodec;

import java.util.ArrayList;

/**
 *
 * @author karthik
 */
public class BinaryHeapPriorityQueue implements MyPriorityQueue {
    
    private static int heapSize = -1;
    private static ArrayList<Node> ar = new ArrayList<>();
    
    @Override
    public void add( Node newNode ) {
        
        ar.add(newNode);
        heapSize++;
        bubbleUp( heapSize );
    }
    
    @Override
    public Node extractMin() {
        
        Node temp = ar.get(0);
        ar.set( 0, ar.get(heapSize) );
        ar.remove(heapSize);
        heapSize--;
        bubbleDown( 0 );
        return temp;
    }
    
    @Override
    public int size() {
        return ( heapSize + 1 );
    }
    
    @Override
    public Node peek() {
        return ar.get(0);
    }
    
    public static int parent( int i ) {
        return i/2;
    }
    
    public static int left( int i ) {
        return 2*i;
    }
    
    public static int right( int i ) {
        return (2*i)+1;
    }
    
    public static void bubbleDown( int i ) {
        
        int smallest;
        Node temp;
        int l = left(i);
        int r = right(i);
        if( l <= heapSize && ( ar.get(l).freq < ar.get(i).freq ) )
            smallest = l;
        else
            smallest = i;
        if( r <= heapSize && ( ar.get(r).freq < ar.get(smallest).freq ) )
            smallest = r;
        if( smallest != i )
        {
            temp = ar.get(i);
            ar.set( i, ar.get(smallest) );
            ar.set( smallest, temp );
            bubbleDown( smallest );
        }
    }
    
    protected void bubbleUp( int i ) {
        
        int p = parent(i);
        if( i <= 0 || ( ar.get(p).freq < ar.get(i).freq ) )
            return;
        Node parent = ar.get(p);
        ar.set( p, ar.get(i) );
        ar.set( i, parent );
        bubbleUp(p);
    }
}
