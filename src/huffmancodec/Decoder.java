package huffmancodec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;

/**
 *
 * @author karthik
 */
public class Decoder {
    
    private static String encodedString;
    private static Node root;
    
    public static void main(String[] args) throws IOException {
        
        try {
            File f1 = new File( args[0] );
            if( !f1.exists() )
            {
                System.out.println( args[0] + " not found!!");
                return;
            }
            f1 = new File( args[1] );
            if( !f1.exists() )
            {
                System.out.println( args[1] + " not found!!");
                return;
            }
        }
        catch( ArrayIndexOutOfBoundsException e ) {
            System.out.println("Command line arguments not proper!!");
        }
        
        root = buildDecodeTree( args[1] );
        encodedString = getEncodedString( args[0] );
        decode( root, encodedString );
    }
    
    public static Node buildDecodeTree( String fileName ) throws IOException {
        
        //Read input file and generate frequency table
        Node root = null, ptr = null;
        try( BufferedReader br = new BufferedReader( new FileReader( fileName ) ) )
        {
            String line;
            String[] input;
            int data;
            while ( ( line = br.readLine() ) != null )
            {
                try {
                    input = line.split(" ");
                    data = Integer.parseInt( input[0] );
                    
                    if( root == null )
                        root = new Node( -1, -1, null, null );
                    if( input[1].isEmpty() )
                        root.data = data;
                    else
                    {
                        ptr = root;
                        for( int i = 0; i < input[1].length(); i++ )
                        {
                            switch( input[1].charAt(i) )
                            {
                                case '0':
                                    if( ptr.left == null )
                                        ptr.left = new Node( -1, -1, null, null );
                                    ptr = ptr.left;
                                    break;
                                case '1':
                                    if( ptr.right == null )
                                        ptr.right = new Node( -1, -1, null, null );
                                    ptr = ptr.right;
                                    break;
                            }
                        }
                        ptr.data = data;
                    }
                    
                }
                catch( Exception e ) {
                    //Do Nothing
                }
            }
            br.close();
        }
        
        return root;
    }
    
    public static String getEncodedString( String fileName ) throws IOException {
        
        //Read encoded file
        File f1 = new File( fileName );
        StringBuilder sb = new StringBuilder("");
        try( FileInputStream fis = new FileInputStream( fileName ) )
        {
            byte buffer[] = new byte[ (int)f1.length() ];
            fis.read(buffer);
            BitSet bs = BitSet.valueOf(buffer);
            for( int i = 0; i <= bs.length(); i++ )
            {
                if( bs.get(i) )
                    sb.append('1');
                else
                    sb.append('0');
            }
            fis.close();
        }
        
        return sb.toString();
    }
    
    public static void decode( Node root, String encodedString ) throws IOException {
       
        //Write decoded file
        File f1 = new File( "decoded.txt" );
        if (f1.exists())
            f1.delete();
        
        Node ptr = root;
        try( PrintWriter out = new PrintWriter( new FileWriter("decoded.txt", true) ) )
        {
            for( int i = 0; i < encodedString.length(); i++ )
            {
                if( ptr.left == null && ptr.right == null )
                {
                    out.println( ptr.data );
                    ptr = root;
                }
                switch( encodedString.charAt(i) )
                {
                    case '0':
                        ptr = ptr.left;
                        break;
                    case '1':
                        ptr = ptr.right;
                        break;
                }
            }
            if( ptr.left == null && ptr.right == null )
                out.println( ptr.data );
            
            out.close();
        }
    }
}
