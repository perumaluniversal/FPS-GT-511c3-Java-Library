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
        			System.out.print("\n\nFingerPrint Example Program\n1.LED ON\n2.LED OFF\n3.Finger Pressed\n4.GetEnrollCount\n5.Enroll New Fingerprint\n6.Identity\n");
        			System.out.print("Enter Input:");
        			
        			int input = Integer.parseInt(br.readLine());
        			
        			switch(input)
        			{
        				case 1:
        					Serialconnector.LEDON();
    					break;
        				case 2:
        					Serialconnector.LEDOFF();
    					break;
        				case 3:
        					System.out.println("Finger Pressed:" + Serialconnector.IsPressFinger());
        					break;
        				case 4:
        					System.out.println("Enroll Count:" + Serialconnector.GetEnrollCount());
        					break;
        				case 5:
        					System.out.println("*******EnrollMent*******\nEnter Fingerprint ID:");
        					int enrollid  = Integer.parseInt(br.readLine());
        					//Serialconnector.LEDON();
        					
        					Boolean usedid = true;
        					while (usedid == true)
        					{
        						usedid = Serialconnector .CheckEnrolled(enrollid);
        						if (usedid==true) enrollid++;
        					}
        					Serialconnector.EnrollStart(enrollid);

        					// enroll
        					System.out.print("Press finger to Enroll #");
        					System.out.println(enrollid);
        					while(Serialconnector.IsPressFinger() == false) Thread.sleep(100);
        					Boolean bret = Serialconnector.CaptureFinger(true);
        					int iret = 0;
        					if (bret != false)
        					{
        						System.out.println("Remove finger");
        						Serialconnector.Enroll1(); 
        						while(Serialconnector.IsPressFinger() == true) Thread.sleep(100);
        						System.out.println("Press same finger again");
        						while(Serialconnector.IsPressFinger() == false) Thread.sleep(100);
        						bret = Serialconnector.CaptureFinger(true);
        						if (bret != false)
        						{
        							System.out.println("Remove finger");
        							Serialconnector.Enroll2();
        							while(Serialconnector.IsPressFinger() == true) Thread.sleep(100);
        							System.out.println("Press same finger yet again");
        							while(Serialconnector.IsPressFinger() == false) Thread.sleep(100);
        							bret = Serialconnector.CaptureFinger(true);
        							if (bret != false)
        							{
        								System.out.println("Remove finger");
        								iret = Serialconnector.Enroll3();
        								if (iret == 0)
        								{
        									System.out.println("Enrolling Successfull");
        								}
        								else
        								{
        									System.out.println("Enrolling Failed with error code:");
        									System.out.println(iret);
        								}
        							}
        							else System.out.println("Failed to capture third finger");
        						}
        						else System.out.println("Failed to capture second finger");
        					}
        					else System.out.println("Failed to capture first finger");
        					
        					Thread.sleep(2000);
        					
        					break;
        				case 6:
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