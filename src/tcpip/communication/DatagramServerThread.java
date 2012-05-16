/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tcpip.communication;

import java.io.*;
import java.net.*;
import java.util.*;

import android.util.Log;

public class DatagramServerThread extends Thread {

	private final String TAG = this.getClass().getSimpleName();
	protected final int PORT;
	protected String msg;
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
    private String clientIP = null;
    
    public DatagramServerThread(int port) throws IOException {
    	this("QuoteServerThread", port, "BROAD CAST");    	
    }

    public DatagramServerThread(String name, int port, String msg) throws IOException {
        super(name);
        PORT = port;
        this.msg = msg;
        socket = new DatagramSocket(PORT);        
    }

    public void run() {

            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                Log.d(TAG, " Packet recived");
                buf = msg.getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                clientIP = address.getHostAddress();
                int port = packet.getPort();
                Log.d(TAG, " got ip from client " + clientIP);
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        socket.close();
    }
    
    
	public String getClientIP() {
		return clientIP;
	}
	
}