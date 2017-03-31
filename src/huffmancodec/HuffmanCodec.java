package huffmancodec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author karthik
 */
public class HuffmanCodec {

    public static void main(String[] args) throws IOException {
        
        Map<Integer,Integer> freq_table = new LinkedHashMap<>();
        Map<Integer,String> code_table = new LinkedHashMap<>();
        
        //Read input file and generate frequency table
        try( BufferedReader br = new BufferedReader( new FileReader("sample_input_small.txt") ) )
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
        
        //Fill code table
        code_table.put(2,"000");
        code_table.put(999999,"001");
        code_table.put(0,"01");
        code_table.put(2245,"10");
        code_table.put(446,"110");
        code_table.put(34,"111");
        
        //Read input file, encode data and write to binary file
        File f1 = new File( "encoded.bin" );
        if (f1.exists())    
            f1.delete();
        f1.createNewFile();
        try( 
            BufferedReader br = new BufferedReader( new FileReader("sample_input_small.txt") );
            FileOutputStream fos = new FileOutputStream( "encoded.bin" )
        )
        {
            StringBuilder sb = new StringBuilder("");
            String line;
            int value;
            byte buffer;
            while ( ( line = br.readLine() ) != null )
            {
                try{
                    value = Integer.parseInt(line);
                    sb.append( code_table.get(value) );
                    if( sb.length() >= 8 )
                    {
                        //Cut off string at a multiple of 8 ( = 1byte )
                        buffer = (byte) Integer.parseInt( sb.substring(0, 8), 2 );
                        fos.write(buffer);
                        sb.delete(0, 8);
                    }
                }
                catch( NumberFormatException e ){
                    //Do nothing
                }
            }
            /*
            if( sb.length() > 0 )
            {
                buffer = (byte) Integer.parseInt( sb.substring(0, sb.length()), 2 );
                fos.write(buffer);
            }
            */
            fos.close();
            br.close();
        }
        
        //Write code table
        f1 = new File( "code_table.txt" );
        if (f1.exists())
            f1.delete();
        try( PrintWriter out = new PrintWriter( new FileWriter("code_table.txt", true) ) )
        {
            Iterator i = code_table.entrySet().iterator();
            while( i.hasNext() )
            {
               Map.Entry me = (Map.Entry) i.next();
               out.print( me.getKey() + " " );
               out.println( me.getValue() );
            }
            out.close();
        }
        
        //Read encoded file
        f1 = new File( "encoded.bin" );
        try( FileInputStream fis = new FileInputStream( "encoded.bin" ) )
        {
            byte buffer[] = new byte[ (int)f1.length() ];
            fis.read(buffer);
            for( int i = 0; i < buffer.length; i++ )
                System.out.print( String.format( "%8s", Integer.toBinaryString( buffer[i] & 0xFF ) ).replace(' ', '0') );
            System.out.println("");
            fis.close();
        }
    }
   
}
