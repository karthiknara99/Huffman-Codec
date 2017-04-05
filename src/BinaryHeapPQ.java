import java.util.ArrayList;

/**
 *
 * @author karthik
 */
public class BinaryHeapPQ implements MyPriorityQueue {
    
    private static ArrayList<Node> ar = new ArrayList<>();

    public BinaryHeapPQ() {
        this.clear();
    }
    
    @Override
    public void add( Node newNode ) {
        
        ar.add(newNode);
        bubbleUp( ar.size()-1 );
    }
    
    public void clear() {
        ar.clear();
    }
    
    @Override
    public Node extractMin() {
        
        if( ar.size() < 1 )
            return null;
        Node temp = ar.get(0);
        ar.set( 0, ar.get( ar.size()-1 ) );
        ar.remove( ar.size()-1 );
        bubbleDown( 0 );
        return temp;
    }
    
    @Override
    public int size() {
        return ar.size();
    }
    
    @Override
    public Node peek() {
        return ar.get(0);
    }
    
    public static int parent( int i ) {
        return (i-1)/2;
    }
    
    public static int left( int i ) {
        return (2*i)+1;
    }
    
    public static int right( int i ) {
        return (2*i)+2;
    }
    
    public static void bubbleDown( int p ) {
        
        int smallest;
        Node temp;
        int l = left(p);
        int r = right(p);
        if( l < ar.size() && ( ar.get(l).freq < ar.get(p).freq ) )
            smallest = l;
        else
            smallest = p;
        if( r < ar.size() && ( ar.get(r).freq < ar.get(smallest).freq ) )
            smallest = r;
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
        if( i <= 0 || ( ar.get(p).freq < ar.get(i).freq ) )
            return;
        Node parent = ar.get(p);
        ar.set( p, ar.get(i) );
        ar.set( i, parent );
        bubbleUp(p);
    }
}
