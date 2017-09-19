package FP;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Example
{   
    public static void main ( String[] args )
    {
    	FPS Serialconnector = new FPS();
    	BufferedReader br;
        try
        {
        	br = new BufferedReader(new InputStreamReader(System.in));
        	 
        	if(Serialconnector.open("COM7"))
        	{
        		while(true)
        		{
        			System.out.print("\n\nFingerPrint Example Program\n1.LED ON\n2.LED OFF\n3.GetEnrollCount\n");
        			System.out.print("Enter Input:");
        			
        			int input = Integer.parseInt(br.readLine());
        			
        			switch(input)
        			{
        				case 1:
        					Serialconnector.LEDOFF();
    					break;
        				case 2:
        					Serialconnector.LEDON();
    					break;
    					default:
    						System.out.println("Invaild Input");
    						break;
        			}
        			
        		}
        	}
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}