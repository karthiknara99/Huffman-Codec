package huffmancodec;

/**
 *
 * @author karthik
 */
public interface MyPriorityQueue {
    
    public void add( Node newNode ); 
    public Node extractMin();
    public int size();
    public Node peek();
}
