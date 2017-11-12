package Project5;

import java.util.Random;

public class IPv4Packet {

	public byte[] header;
	private byte version = 4; // 4 bits
    private byte HLen = 5; // 4 bits
    
	public IPv4Packet(byte[] payload){
		int payloadSize = payload.length;
		header = new byte[20+payloadSize];
		
		//Version + IHL(Header Length)
		header[0] = (byte) ((byte) (version << 4) + HLen);
		//Total Length
		header[2] = (byte) (20+payloadSize >> 8);
		header[3] = (byte) (20+payloadSize);
		//Flag
		header[6] = (byte) (1 << 6);
		//TTL
		header[8] = 50;
		//TCP Protocol
		header[9] = 17;
		
		//18,221,102,182
		header[16] = (byte) 18;
		header[17] = (byte) 221;
		header[18] = (byte) 102;
		header[19] = (byte) 182;
		
		short checksum = checksum(header);
		//checksum
		header[10] = (byte)(checksum >> 8);
		header[11] = (byte)(checksum);
		
		for ( int i=0; i<payload.length;i++){
			header[i+20] = (byte) payload[i];
		}
	}
	
	public static byte[] generateUDPPacket( int destPort, int dataLength ){
		byte[] UDP = new byte[8+dataLength];
		
		UDP[2] = (byte)(destPort >> 8);
		UDP[3] = (byte)(destPort);
		UDP[4] = (byte)(UDP.length >> 8);
		UDP[5] = (byte)(UDP.length);
		fill( UDP, 9, UDP.length);
		short checksum = checksum(getPseudoHeader(UDP));
		UDP[6] = (byte)(checksum >> 8);
		UDP[7] = (byte)(checksum);
		return UDP;
	}
	
	private static byte[] getPseudoHeader( byte[] udpDatagram ) {
		byte[] pseudoHeader = new byte[ udpDatagram.length + 12 ];
		pseudoHeader[4] = (byte) 18;
		pseudoHeader[5] = (byte) 221;
		pseudoHeader[6] = (byte) 102;
		pseudoHeader[7] = (byte) 182;
		pseudoHeader[8] = 0; 
		pseudoHeader[9] = 17; //Protocol Number
		pseudoHeader[10] = (byte)( udpDatagram.length >> 8 );
		pseudoHeader[11] = (byte)udpDatagram.length;
		for ( int i=0; i<udpDatagram.length;i++){
			pseudoHeader[i+12] = udpDatagram[i];
		}
		return pseudoHeader;
	}

	public static short checksum(byte[] b){
        long sum = 0;
        int count = 0;
        while (count < b.length) {
            sum += ((  (b[count]&0xFF) << 8) | (++count<b.length ? (b[count]&0xFF) : 0 ) & 0xFF);
            if ((sum & 0xFFFF0000) != 0) {
                sum &= 0xFFFF;
                sum++;
            }
            count++;
        }
        return (short)~(sum & 0xFFFF );
    }
	
	public static void fill(byte[] array, int start, int end){
		Random rand = new Random();
		for(int i = start; i < end; i++){
			array[i] = (byte)rand.nextInt();
		}
	}
	
//	public byte[] fuse(byte[] a, byte[] b){
//		byte[] g = new byte[a.length + b.length];
//		for(int i = 0; i < a.length; i++){
//			g[i] = a[i];
//		}
//		for(int i = 0; i < b.length; i++){
//			g[i+a.length] = b[i];
//		}
//		return g;
//	}
}
