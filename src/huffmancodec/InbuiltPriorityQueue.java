package huffmancodec;

import java.util.PriorityQueue;

/**
 *
 * @author karthik
 */
public class InbuiltPriorityQueue implements MyPriorityQueue {
    
    PriorityQueue<Node> pq = new PriorityQueue<>();
    
    @Override
    public void add( Node newNode ) {
        pq.add(newNode);
    }
    
    @Override
    public Node extractMin() {
        return pq.poll();
    }
    
    @Override
    public int size() {
        return pq.size();
    }
    
    @Override
    public Node peek() {
        return pq.peek();
    }
}
