package tcpip.communication;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import game.tennis.WifiCommunication;

import java.io.*;
import java.net.*;

import android.util.Log;

public class DatagramClient {

	private final static String TAG = " sendBroadcast";

	public static String sendBroadcast(int port, InetAddress broadcastAdr) throws IOException {
		// get a datagram socket
		DatagramSocket socket = new DatagramSocket();

		// send request
		byte[] buf = new byte[256];
/*
		Log.d(TAG, " getting ip of this device");
		String ip = WifiCommunication.getLocalIpAddress();

		Log.d(TAG, " device ip is " + ip);
		ip = ip.substring(0, ip.lastIndexOf("."))+ ".255";
		InetAddress address = InetAddress.getByName(ip.substring(0,
				ip.lastIndexOf("."))
				+ ".255");
*/		
//		InetAddress address = InetAddress.getByName(broadcastAdr);

		//String ip = broadcastAdr;
		Log.d(TAG, " ip to broadcast " + broadcastAdr);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, broadcastAdr,
				port);
		socket.send(packet);
		// get response
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		String ip = packet.getAddress().getHostAddress();
		// display response		
		socket.close();
		return ip;
	}

}