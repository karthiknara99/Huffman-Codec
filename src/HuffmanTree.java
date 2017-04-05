/**
 *
 * @author karthik
 */
public class HuffmanTree {
    
    Node root;

    HuffmanTree( MyPriorityQueue pq ) {
        
        while( pq.size() > 1 )
        {
            Node left = pq.extractMin();
            Node right = pq.extractMin();
            Node newNode = new Node( -1, left.freq + right.freq, left, right );
            pq.add(newNode);
        }
        root = pq.peek();
    }
    
}
