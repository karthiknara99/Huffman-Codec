package huffmancodec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author karthik
 */
public class Encoder {
    
    private static HashMap<Integer,Integer> freq_table = new LinkedHashMap<>();
    private static HashMap<Integer,String> code_table = new LinkedHashMap<>();
    private static CacheOptimizedFourWayHeapPQ pq;
    private static HuffmanTree ht;
    
    public static void main(String[] args) throws IOException {
        
        try {
            File f1 = new File( args[0] );
            if( !f1.exists() )
            {
                System.out.println( args[0] + " not found!!");
                return;
            }
        }
        catch( ArrayIndexOutOfBoundsException e ) {
            System.out.println("Command line arguments not proper!!");
        }
        
        buildFrequencyTable( args[0] );
        buildHuffmanTree();
        generateCodeTable();
        encode( args[0] );
    }
    
    public static void buildFrequencyTable( String fileName ) throws IOException {
        
        //Read input file and generate frequency table
        try( BufferedReader br = new BufferedReader( new FileReader( fileName ) ) )
        {
            String line;
            int value;
            while ( ( line = br.readLine() ) != null )
            {
                try {
                    value = Integer.parseInt(line);
                }
                catch( NumberFormatException e ) {
                    continue;
                }
                
                if( freq_table.containsKey(value) )
                    freq_table.put( value, freq_table.get(value) + 1 );
                else
                    freq_table.put(value,1);
            }
            br.close();
        }
    }
    
    public static void buildHuffmanTree() {
        
        /*
        //Testing
        BinaryHeapPQ pq1;
        CacheOptimizedFourWayHeapPQ pq2;
        PairingHeapPQ pq3;
        int num_it = 10;
        long a = System.currentTimeMillis();
        for( int i = 0; i < num_it; i++ )
        {
            pq1 = new BinaryHeapPQ();
            Iterator it = freq_table.entrySet().iterator();
            while( it.hasNext() )
            {
                Map.Entry me = (Map.Entry) it.next();
                Node newNode = new Node( (int) me.getKey(), (int) me.getValue(), null, null );
                pq1.add(newNode);
            }

            //Build Huffman Tree
            ht = new HuffmanTree(pq1);
        }
        long b = System.currentTimeMillis();
        System.out.println( "Binary Heap: " + (b-a)/(float)(num_it*1000) + " s" );
        
        a = System.currentTimeMillis();
        for( int i = 0; i < num_it; i++ )
        {
            pq2 = new CacheOptimizedFourWayHeapPQ();
            Iterator it = freq_table.entrySet().iterator();
            while( it.hasNext() )
            {
                Map.Entry me = (Map.Entry) it.next();
                Node newNode = new Node( (int) me.getKey(), (int) me.getValue(), null, null );
                pq2.add(newNode);
            }

            //Build Huffman Tree
            ht = new HuffmanTree(pq2);
        }
        b = System.currentTimeMillis();
        System.out.println( "4-ary Heap: " + (b-a)/(float)(num_it*1000) + " s" );
        
        a = System.currentTimeMillis();
        for( int i = 0; i < num_it; i++ )
        {
            pq3 = new PairingHeapPQ();
            Iterator it = freq_table.entrySet().iterator();
            while( it.hasNext() )
            {
                Map.Entry me = (Map.Entry) it.next();
                Node newNode = new Node( (int) me.getKey(), (int) me.getValue(), null, null );
                pq3.add(newNode);
            }

            //Build Huffman Tree
            ht = new HuffmanTree(pq3);
        }
        b = System.currentTimeMillis();
        System.out.println( "Pairing Heap: " + (b-a)/(float)(num_it*1000) + " s" );
        */
        
        //Build Priority Queue
        pq = new CacheOptimizedFourWayHeapPQ();
        Iterator it = freq_table.entrySet().iterator();
        while( it.hasNext() )
        {
            Map.Entry me = (Map.Entry) it.next();
            Node newNode = new Node( (int) me.getKey(), (int) me.getValue(), null, null );
            pq.add(newNode);
        }

        //Build Huffman Tree
        ht = new HuffmanTree(pq);
    }
    
    public static void generateCodeTable() {
        
        //Generate Code Table
        StringBuilder sb = new StringBuilder("");
        InOrderTree( ht.root, sb );
    }
    
    public static void InOrderTree( Node ptr, StringBuilder sb ) {
        
        if( ptr == null )
            return;
        
        if( ptr.left == null && ptr.right == null )
        {
            code_table.put( ptr.data , sb.toString() );
            return;
        }
        
        InOrderTree( ptr.left, sb.append('0') );
        sb.deleteCharAt( sb.length() - 1 );
        InOrderTree( ptr.right, sb.append('1') );
        sb.deleteCharAt( sb.length() - 1 );
    }
    
    public static void encode( String fileName ) throws IOException {
        
        //Read input file, encode data and write to binary file
        File f1 = new File( "encoded.bin" );
        if (f1.exists())    
            f1.delete();
        f1.createNewFile();
        
        try( 
            BufferedReader br = new BufferedReader( new FileReader( fileName ) );
            FileOutputStream fos = new FileOutputStream( "encoded.bin" )
        )
        {
            StringBuilder sb = new StringBuilder("");
            String line;
            int value;
            while ( ( line = br.readLine() ) != null )
            {
                try {
                    value = Integer.parseInt(line);
                    sb.append( code_table.get(value) );
                }
                catch( NumberFormatException e ) {
                    //Do nothing
                }
            }
            br.close();
            
            BitSet bs = new BitSet( sb.length() );
            int bitcounter = 0;
            for( int i = 0; i < sb.length(); i++ )
            {
                if( sb.charAt(i) == '1' )
                    bs.set(bitcounter);
                bitcounter++;
            }
            byte[] buffer = bs.toByteArray();
            fos.write(buffer);
            fos.close();
        }
        
        //Write code table
        f1 = new File( "code_table.txt" );
        if (f1.exists())
            f1.delete();
        
        try( PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter("code_table.txt", true) ) ) )
        {
            Iterator it = code_table.entrySet().iterator();
            while( it.hasNext() )
            {
                Map.Entry me = (Map.Entry) it.next();
                out.print( me.getKey() + " " );
                out.println( me.getValue() );
            }
            out.close();
        }
    }
}
