/**
 *
 * @author karthik
 */
public class Node implements Comparable<Node> {
    
    int data, freq;
    Node left, right;
    
    Node( int data, int freq, Node left, Node right ) {
        
        this.data = data;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public int compareTo( Node other ) {
        return ( this.freq - other.freq );
    }
}
