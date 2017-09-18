import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TwoWaySerialComm
{
    public TwoWaySerialComm()
    {
        super();
    }
    
    /** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while (true)
                {
                	byte[] test = new byte[1];
                	test[0] = (byte)this.in.read();
                	System.out.println("Response:0x" + Communicator.bytesToHex(test));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
            	int c = 0;
            	
            	System.out.print("\n1.Open\n2.LED ON\n3.LED OFF\nEnter Choice:");
            	c = System.in.read();
            	while (c!= 99)
            	{
            		byte[] req;
            		switch(c)
            		{
	            		case 49: // 1
	            			req = FPS.Open();
	            			this.out.write(req);
	        			break;
	            		case 50:  // 2
	            			req = FPS.LEDON();
	            			this.out.write(req);
	        			break;
	            		case 51: // 3
	            			req = FPS.LEDOFF();
	            			this.out.write(req);
	        			break;
	            		case 52: // 4
	            			req = FPS.GetEnrollCount();
	            			this.out.write(req);
	            		break;
	            		case 48: // 5
	            			req = FPS.Close();
	            			this.out.write(req);
	        			break;
            		}
            		this.out.write("\n".getBytes());
            		System.out.print("\n1.Open\n2.LED ON\n3.LED OFF\nEnter Choice:");
            		c = System.in.read();
            	}
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    public static void main ( String[] args )
    {
        try
        {
            (new TwoWaySerialComm()).connect("COM7");
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}