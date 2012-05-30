package tcpip.communication;

import java.io.*;
import java.net.*;

import android.util.Log;

public class DatagramClient {

	private final static String TAG = "SendBroadcast";

	public static String sendBroadcast(int port, InetAddress broadcastAdr) throws IOException {
		
		// get a datagram socket
		DatagramSocket socket = new DatagramSocket();

		// send request
		byte[] buf = new byte[256];

		Log.d(TAG, " ip to broadcast " + broadcastAdr);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, broadcastAdr,
				port);
		socket.send(packet);
		
		// get response
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		String ip = packet.getAddress().getHostAddress();
		
		// close socket
		socket.close();
		return ip;
	}

}