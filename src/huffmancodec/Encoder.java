package huffmancodec;

import java.io.BufferedReader;
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
    
    static HashMap<Integer,Integer> freq_table = new LinkedHashMap<>();
    static HashMap<Integer,String> code_table = new LinkedHashMap<>();
    static SampleQueue sq;
    static HuffmanTree ht;
    
    public static void main(String[] args) throws IOException {
        
        buildFrequencyTable();
        buildHuffmanTree();
        generateCodeTable();
        encode();
    }
    
    public static void buildFrequencyTable() throws IOException {
        
        //Read input file and generate frequency table
        try( BufferedReader br = new BufferedReader( new FileReader( "sample_input_large.txt" ) ) )
        {
            String line;
            int value;
            while ( ( line = br.readLine() ) != null )
            {
                try{
                    value = Integer.parseInt(line);
                }
                catch( NumberFormatException e ){
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
        
        //Create priority queue
        sq = new SampleQueue();
        Iterator it = freq_table.entrySet().iterator();
        while( it.hasNext() )
        {
            Map.Entry me = (Map.Entry) it.next();
            Node newNode = new Node( (int) me.getKey(), (int) me.getValue(), null, null );
            sq.add(newNode);
        }
        
        //Build Huffman Tree
        ht = new HuffmanTree(sq);
    }
    
    public static void generateCodeTable() {
        
        //Generate Code Table
        StringBuilder sb = new StringBuilder("");
        InOrderTree( ht.root, sb );
    }
    
    public static void InOrderTree( Node ptr, StringBuilder sb ) {
        
        if( ptr == null )
            return;
        
        if( ptr.data != -1 )
        {
            code_table.put( ptr.data , sb.toString() );
            return;
        }
        
        InOrderTree( ptr.left, sb.append('0') );
        sb.deleteCharAt( sb.length() - 1 );
        InOrderTree( ptr.right, sb.append('1') );
        sb.deleteCharAt( sb.length() - 1 );
    }
    
    public static void encode() throws IOException {
        
        //Read input file, encode data and write to binary file
        File f1 = new File( "encoded.bin" );
        if (f1.exists())    
            f1.delete();
        f1.createNewFile();
        
        try( 
            BufferedReader br = new BufferedReader( new FileReader( "sample_input_large.txt" ) );
            FileOutputStream fos = new FileOutputStream( "encoded.bin" )
        )
        {
            StringBuilder sb = new StringBuilder("");
            String line;
            int value;
            while ( ( line = br.readLine() ) != null )
            {
                try{
                    value = Integer.parseInt(line);
                    sb.append( code_table.get(value) );
                }
                catch( NumberFormatException e ){
                    //Do nothing
                }
            }
            br.close();
            
            BitSet bitSet = new BitSet( sb.length() );
            int bitcounter = 0;
            for( int i = 0; i < sb.length(); i++ )
            {
                if( sb.charAt(i) == '1' )
                    bitSet.set(bitcounter);
                bitcounter++;
            }
            byte[] buffer = bitSet.toByteArray();
            fos.write(buffer);
            fos.close();
        }
        
        //Write code table
        f1 = new File( "code_table.txt" );
        if (f1.exists())
            f1.delete();
        
        try( PrintWriter out = new PrintWriter( new FileWriter("code_table.txt", true) ) )
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
