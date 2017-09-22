FPS GT-511c3 / GT-511c5  JAVA Library
=========================
![FingerPrint Scanner](https://dlnmh9ip6v2uc.cloudfront.net/images/products/1/1/7/9/2/11792-01_medium.jpg)  
*Fingerprint Scanner - TTL (GT-511C3 / GT-511C5)*

This is a great fingerprint module from ADH-Tech that communicates over USB to TTL Serial so you can easily embed it into your next java project.

This repository contains example code and binaries to work with it.

Repository Contents
-------------------
Java Library to interface with the sensor using USB TO TTL Module. It works for any System / IOT (Raspberry,Pine64,.etc) that supports java.

Example Program
--------------
        FPS Serialconnector = new FPS();
    	BufferedReader br;
        try
        {
        	br = new BufferedReader(new InputStreamReader(System.in));
        	 
        	if(Serialconnector.open("COM7")) // Change appropriate COM PORT here(USB TO TTL).
        	{
        		while(true)
        		{
        			System.out.print("\n\nFingerPrint Example Program\n1.LED ON\n2.LED OFF\n3.Finger Pressed\n4.GetEnrollCount\n5.Enroll New Fingerprint\n6.Identity\n");
        			System.out.print("Enter Input:");
        			
        			int input = Integer.parseInt(br.readLine());
        			
        			switch(input)
        			{
        				case 1: // Cmos LED ON
        					Serialconnector.LEDON();
    					break;
        				case 2: // Cmos LED OFF
        					Serialconnector.LEDOFF();
    					break;
        				case 3: // Check Finger Pressed
        					System.out.println("Finger Pressed:" + Serialconnector.IsPressFinger());
        					break;
        				case 4: // Get Enrollment Count
        					System.out.println("Enroll Count:" + Serialconnector.GetEnrollCount());
        					break;
        				case 5: // New Enrollment
        					System.out.println("*******EnrollMent*******\nEnter Fingerprint ID:");
        					int enrollid  = Integer.parseInt(br.readLine());
        					
        					Serialconnector.NewEnrollment(enrollid);
        					
        					Thread.sleep(2000);
        					
        					break;
        				case 6: // Identify fingerprint test
        					System.out.println("Please place your finger");
        					Boolean result= false;
        					while(result==false)
        					{
        						Serialconnector.CaptureFinger(false);
        						int id = Serialconnector.Identify1_N();
        						if (id <200)
        						{
        							System.out.println("Verified ID:" + id);
        							result =true;
        						}
        						else
        						{
        							System.out.println("Finger not found");
        							result =false;
        						}
        					}
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


License Information
-------------------

The original library license is as follows:

"Created by Perumal Raj Narayanasamy, September 18th 2017
	Licensed for non-commercial use only."
