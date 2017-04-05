import java.io.BufferedReader;
import java.io.BufferedWriter;
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
public class decoder {
    
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
        decode( root, args[0] );
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
    
    public static void decode( Node root, String fileName ) throws IOException {
        
        //Write decoded file
        File f1 = new File( "decoded.txt" );
        if (f1.exists())
            f1.delete();
        f1.createNewFile();
        
        //Read encoded file
        f1 = new File( fileName );
        byte buffer[] = new byte[ (int)f1.length() ];
        try( FileInputStream fis = new FileInputStream( fileName ) )
        {
            fis.read(buffer);
            fis.close();
        }
        
        BitSet bs = BitSet.valueOf(buffer);
        char c;
        Node ptr = root;
        
        try( PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter("decoded.txt", true) ) ) )
        {
            for( int i = 0; i < buffer.length * 8; i++ )
            {
                if( ptr.left == null && ptr.right == null )
                {
                    out.println( ptr.data );
                    ptr = root;
                }
             
                c = ( bs.get(i) == true ) ? '1' : '0';
                
                switch( c )
                {
                    case '0':   ptr = ptr.left;     break;
                    case '1':   ptr = ptr.right;    break;
                }
            }
            
            if( ptr.left == null && ptr.right == null )
                out.println( ptr.data );
            
            out.close();
        }
    }
}
