import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class FPS {
	
	InputStream in;
	OutputStream out;
	CommPortIdentifier portIdentifier; 
	 CommPort commPort;
	public void open (String portName) throws Exception
    {
        portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned())
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            if (commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                
                //(new Thread(new SerialReader(in))).start();
                //(new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
	
	public void close()
	{
		if(commPort!=null)
		{
			commPort.close();
			commPort = null;
		}
		
		if(portIdentifier!=null)
			portIdentifier=null;
	
	}
	
	

	byte[] command = new byte[2];	
	byte[] Parameter = new byte[4];								// Parameter 4 bytes, changes meaning depending on command							
	
	private final char[] hexArray = "0123456789ABCDEF".toCharArray();
	public String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	
	public static class Commands
	{
		public static final short NotSet				= 0x00,		// Default value for enum. Scanner will return error if sent this.
		Open				= 0x01,		// Open Initialization
		Close				= 0x02,		// Close Termination
		UsbInternalCheck	= 0x03,		// UsbInternalCheck Check if the connected USB device is valid
		ChangeEBaudRate		= 0x04,		// ChangeBaudrate Change UART baud rate
		SetIAPMode			= 0x05,		// SetIAPMode Enter IAP Mode In this mode, FW Upgrade is available
		CmosLed				= 0x12,		// CmosLed Control CMOS LED
		GetEnrollCount		= 0x20,		// Get enrolled fingerprint count
		CheckEnrolled		= 0x21,		// Check whether the specified ID is already enrolled
		EnrollStart			= 0x22,		// Start an enrollment
		Enroll1				= 0x23,		// Make 1st template for an enrollment
		Enroll2				= 0x24,		// Make 2nd template for an enrollment
		Enroll3				= 0x25,		// Make 3rd template for an enrollment, merge three templates into one template, save merged template to the database
		IsPressFinger		= 0x26,		// Check if a finger is placed on the sensor
		DeleteID			= 0x40,		// Delete the fingerprint with the specified ID
		DeleteAll			= 0x41,		// Delete all fingerprints from the database
		Verify1_1			= 0x50,		// Verification of the capture fingerprint image with the specified ID
		Identify1_N			= 0x51,		// Identification of the capture fingerprint image with the database
		VerifyTemplate1_1	= 0x52,		// Verification of a fingerprint template with the specified ID
		IdentifyTemplate1_N	= 0x53,		// Identification of a fingerprint template with the database
		CaptureFinger		= 0x60,		// Capture a fingerprint image(256x256) from the sensor
		MakeTemplate		= 0x61,		// Make template for transmission
		GetImage			= 0x62,		// Download the captured fingerprint image(256x256)
		GetRawImage			= 0x63,		// Capture & Download raw fingerprint image(320x240)
		GetTemplate			= 0x70,		// Download the template of the specified ID
		SetTemplate			= 0x71,		// Upload the template of the specified ID
		GetDatabaseStart	= 0x72,		// Start database download, obsolete
		GetDatabaseEnd		= 0x73,		// End database download, obsolete
		UpgradeFirmware		= 0x80,		// Not supported
		UpgradeISOCDImage	= 0x81,		// Not supported
		Ack					= 0x30,		// Acknowledge.
		Nack				= 0x31;	// Non-acknowledge
	}
	
	final byte COMMAND_START_CODE_1 = 0x55;	// Static byte to mark the beginning of a command packet	-	never changes
	final byte COMMAND_START_CODE_2 = (byte)0xAA;	// Static byte to mark the beginning of a command packet	-	never changes
	final byte COMMAND_DEVICE_ID_1 = 0x01;	// Device ID Byte 1 (lesser byte)							-	theoretically never changes
	final byte COMMAND_DEVICE_ID_2 = 0x00;	// Device ID Byte 2 (greater byte)							-	theoretically never changes
	
	public static class ErrorCodes
	{
				public static final int NO_ERROR					= 0x0000,	// Default value. no error
				NACK_TIMEOUT				= 0x1001,	// Obsolete, capture timeout
				NACK_INVALID_BAUDRATE		= 0x1002,	// Obsolete, Invalid serial baud rate
				NACK_INVALID_POS			= 0x1003,	// The specified ID is not between 0~199
				NACK_IS_NOT_USED			= 0x1004,	// The specified ID is not used
				NACK_IS_ALREADY_USED		= 0x1005,	// The specified ID is already used
				NACK_COMM_ERR				= 0x1006,	// Communication Error
				NACK_VERIFY_FAILED			= 0x1007,	// 1:1 Verification Failure
				NACK_IDENTIFY_FAILED		= 0x1008,	// 1:N Identification Failure
				NACK_DB_IS_FULL				= 0x1009,	// The database is full
				NACK_DB_IS_EMPTY			= 0x100A,	// The database is empty
				NACK_TURN_ERR				= 0x100B,	// Obsolete, Invalid order of the enrollment (The order was not as: EnrollStart -> Enroll1 -> Enroll2 -> Enroll3)
				NACK_BAD_FINGER				= 0x100C,	// Too bad fingerprint
				NACK_ENROLL_FAILED			= 0x100D,	// Enrollment Failure
				NACK_IS_NOT_SUPPORTED		= 0x100E,	// The specified command is not supported
				NACK_DEV_ERR				= 0x100F,	// Device Error, especially if Crypto-Chip is trouble
				NACK_CAPTURE_CANCELED		= 0x1010,	// Obsolete, The capturing is canceled
				NACK_INVALID_PARAM			= 0x1011,	// Invalid parameter
				NACK_FINGER_IS_NOT_PRESSED	= 0x1012,	// Finger is not pressed
				INVALID						= 0XFFFF;	// Used when parsing fails
	}
	
	byte[] GetPacketBytes(short cmd)
	{
		byte[] packetbytes= new byte[12];

		// update command before calculating checksum (important!)
		//short cmd = Command;
		command[0] = GetLowByte(cmd);
		command[1] = GetHighByte(cmd);

		short checksum = _CalculateChecksum();
		
		packetbytes[0] = COMMAND_START_CODE_1;
		packetbytes[1] = COMMAND_START_CODE_2;
		packetbytes[2] = COMMAND_DEVICE_ID_1;
		packetbytes[3] = COMMAND_DEVICE_ID_2;
		packetbytes[4] = Parameter[0];
		packetbytes[5] = Parameter[1];
		packetbytes[6] = Parameter[2];
		packetbytes[7] = Parameter[3];
		packetbytes[8] = command[0];
		packetbytes[9] = command[1];
		
		packetbytes[10] = GetLowByte(checksum);
		packetbytes[11] = 0x01; /*GetHighByte(checksum);*/
		//packetbytes[12] = "\n".getBytes()[0];

		for(int i=0;i<12;i++)
		{
			byte[] test = new byte[1];
			test[0] = packetbytes[i];
			System.out.print("0x" + bytesToHex(test) + ",");
		}
		System.out.println("");
		
		
		return packetbytes;
	} 
	
	short _CalculateChecksum()
	{
		short w = 0;
		w += COMMAND_START_CODE_1;
		w += COMMAND_START_CODE_2;
		w += COMMAND_DEVICE_ID_1;
		w += COMMAND_DEVICE_ID_2;
		w += Parameter[0];
		w += Parameter[1];
		w += Parameter[2];
		w += Parameter[3];
		w += command[0];
		w += command[1];

		return w;
	}
	
	
	byte GetHighByte(short w)
	{
		return (byte) ((byte)(w>>8)&0x00FF);
	}

	// Returns the low byte from a word
	byte GetLowByte(short w)
	{
		return (byte) (w&0x00FF);
	}
	
	String SendToSerial(byte data[], int length)
	{
	  String res = "";
	  boolean first=true;                                                                                                                                  
	  res = res + "\"";
	  for(int i=0; i<length; i++)
	  {
		if (first) first=false; else  res = res + " "; // Serial.print(" ");
		res = res + serialPrintHex(data[i]);
	  }
	  res = res + "\"";
	  return res;
	}
	
	String serialPrintHex(byte data)
	{
	  char[] tmp = new char[16];
	  return new String(tmp).format("%.2X", data);
	  //sprintf(tmp, "%.2X",);
	  //Serial.print(tmp);
	  //return tmp;
	}
	
	/*static byte[] Open()
	{
		//if (UseSerialDebug) Serial.println("FPS - Open");
		//Command_Packet* cp = new Command_Packet();
		Parameter = new byte[4];	
		Parameter[0] = 0x00;
		Parameter[1] = 0x00;
		Parameter[2] = 0x00;
		Parameter[3] = 0x00;
		return GetPacketBytes(Commands.Open);
		//packetbytes;
	}*/
	
	/*byte[] Close()
	{
		Parameter = new byte[4];
		Parameter[0] = 0x00;
		Parameter[1] = 0x00;
		Parameter[2] = 0x00;
		Parameter[3] = 0x00;
		return GetPacketBytes(Commands.Close);
	}*/
	
	public byte[] LEDON()
	{
		Parameter = new byte[4];
		Parameter[0] = 0x01;
		Parameter[1] = 0x00;
		Parameter[2] = 0x00;
		Parameter[3] = 0x00;
		return GetPacketBytes(Commands.CmosLed);
		
		//return true;
		
	}
	
	public byte[] LEDOFF()
	{
		Parameter = new byte[4];
		Parameter[0] = 0x00;
		Parameter[1] = 0x00;
		Parameter[2] = 0x00;
		Parameter[3] = 0x00;
		return GetPacketBytes(Commands.CmosLed);
		
	}
	
	public byte[] GetEnrollCount()
	{
		Parameter = new byte[4];
		Parameter[0] = 0x00;
		Parameter[1] = 0x00;
		Parameter[2] = 0x00;
		Parameter[3] = 0x00;
		return GetPacketBytes(Commands.GetEnrollCount);
	}
	
	public byte[] IsPressFinger()
	{
		return null;
	}
	
}
