package huffmancodec;

import java.util.ArrayList;

/**
 *
 * @author karthik
 */
public class CacheOptimizedFourWayHeapPQ implements MyPriorityQueue {

    private static ArrayList<Node> ar = new ArrayList<>();

    public CacheOptimizedFourWayHeapPQ() {
        this.clear();
    }
    
    @Override
    public void add( Node newNode ) {
        
        ar.add(newNode);
        bubbleUp( ar.size()-1 );
    }
    
    public void clear() {
        ar.clear();
        ar.add(null);
        ar.add(null);
        ar.add(null);
    }
    
    @Override
    public Node extractMin() {
        
        Node temp = ar.get(3);
        ar.set( 3, ar.get( ar.size()-1 ) );
        ar.remove( ar.size()-1 );
        bubbleDown( 3 );
        return temp;
    }
    
    @Override
    public int size() {
        return ( ar.size() - 3 );
    }
    
    @Override
    public Node peek() {
        return ar.get(3);
    }
    
    public static int parent( int i ) {
        return ((i-4)/4)+3;
    }
    
    public static int child1( int i ) {
        return 4*(i-3)+4;
    }
    
    public static int child2( int i ) {
        return 4*(i-3)+5;
    }
    
    public static int child3( int i ) {
        return 4*(i-3)+6;
    }
    
    public static int child4( int i ) {
        return 4*(i-3)+7;
    }
    
    public static void bubbleDown( int p ) {
        
        int smallest;
        Node temp;
        int c1 = child1(p);
        int c2 = child2(p);
        int c3 = child3(p);
        int c4 = child4(p);
        
        if( c1 < ar.size() && ( ar.get(c1).freq < ar.get(p).freq ) )
            smallest = c1;
        else
            smallest = p;
        if( c2 < ar.size() && ( ar.get(c2).freq < ar.get(smallest).freq ) )
            smallest = c2;
        if( c3 < ar.size() && ( ar.get(c3).freq < ar.get(smallest).freq ) )
            smallest = c3;
        if( c4 < ar.size() && ( ar.get(c4).freq < ar.get(smallest).freq ) )
            smallest = c4;
        if( smallest != p )
        {
            temp = ar.get(p);
            ar.set( p, ar.get(smallest) );
            ar.set( smallest, temp );
            bubbleDown( smallest );
        }
    }
    
    protected void bubbleUp( int i ) {
        
        int p = parent(i);
        if( i <= 3 || ( ar.get(p).freq < ar.get(i).freq ) )
            return;
        Node parent = ar.get(p);
        ar.set( p, ar.get(i) );
        ar.set( i, parent );
        bubbleUp(p);
    }
}
