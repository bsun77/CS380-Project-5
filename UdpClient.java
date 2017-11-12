package Project5;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UdpClient {

	public static void main(String[] args) throws Exception {
		try (Socket socket = new Socket("18.221.102.182", 38005)) {
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			byte[] deadbeef = {(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF};
			IPv4Packet handshake = new IPv4Packet(deadbeef);
			os.write(handshake.header);
			System.out.print("Handshake response: 0x");
			for(int j = 0; j < 4; j++){
				System.out.printf("%02X", is.read());
			}
			System.out.println();
			int destPort = is.read()*256+is.read();

			System.out.println("Port number received: " + destPort );
			System.out.println();
			
			long then, now;
			for(int i = 1; i < 13; i++){
				int dataSize = (int)Math.pow(2, i);
				System.out.println("Sending packet with " + dataSize +" bytes of data");
				byte[] udpPacketBytes = IPv4Packet.generateUDPPacket(destPort, dataSize);
				IPv4Packet packet = new IPv4Packet( udpPacketBytes );
				os.write(packet.header);
				then = System.currentTimeMillis();
				System.out.print("Response: 0x");
				for(int j = 0; j < 4; j++){
					System.out.printf("%02X", is.read());
				}
				System.out.println();
				now = System.currentTimeMillis();
				System.out.println("RTT: " +(now-then) + "ms\n");
			}
		}
	}
	
}
