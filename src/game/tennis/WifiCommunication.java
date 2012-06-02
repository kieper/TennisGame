package game.tennis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import tcpip.communication.DatagramClient;
import tcpip.communication.DatagramServerThread;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Kieper
 * 
 */
public class WifiCommunication implements Communication {

	private Socket socket;
	private ServerSocket serverSocket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private boolean isconnected;	
	private static final String TAG = "WifiCommunication";
	private final int PORT = 4000;
	private final int VALIDATION_CODE = 1234;
	private final int TIMEOUT = 100000;
	private final int SERVER_TIMEOUT = 0; 
	private InetAddress broadCastAddr;
	private waitThread wt;
	
	public WifiCommunication(Context context, PlayerType playerType){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d(TAG, "Checking wifi Status");
		System.setProperty("java.net.preferIPv4Stack", "true");
		wt = new waitThread();
		wt.setRunning(true);
		wt.start();
	    if( ! cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting() ) {
	    	Log.d(TAG, "WIFI not connected");
	        Toast.makeText(context, "Turn on wifi, and try again",  Toast.LENGTH_LONG).show();
	    }else{	    	
	    	Log.d(TAG, "WIFI connected");
	    	try {
				broadCastAddr = getBroadcastAddress(context);
				Log.d(TAG, "Broadcast addr is " +broadCastAddr.toString() );
			} catch (IOException e) {
				Log.e(TAG, "Could not find broadcast addr");
			}
	    	initialize(playerType);
	    }
	    wt.setRunning(false);
	}
	
	@Override
	public void sendData(Packet packet) {		
		try {
			Log.d(TAG, "in function send data");
			Log.d(TAG, packet.toString());
			packet.send(outStream);
			Log.d(TAG, "Data  sent");
			
		} catch (IOException e) {
			Log.e(TAG, "Error while sending DATA over WIFI");
		}
	}


	@Override
	public Packet reciveData() {
		
			Log.d(TAG, "in function recive data");
			 
			Packet packet = Packet.creatorPacket(inStream);
			Log.d(TAG, packet.toString());
			return packet;
	}

	private boolean connect(String ip, int port) {
	    try {	    	
	        this.socket = new Socket(ip, port);
	        Log.d(TAG, "Socket connected client");
	        socket.setSoTimeout(TIMEOUT); 
	        this.outStream = new ObjectOutputStream(socket.getOutputStream());
	        this.inStream = new ObjectInputStream(socket.getInputStream());
	        isconnected = true;
	    } catch (IOException e) {
	        Log.e(TAG, "connection failed on " + ip + ":" + port + " exception: " +e.toString());
	        isconnected = false;
	        return isconnected;
	    }
	    Log.d(TAG, "connection to " + ip + " sucessfull");
	    return isconnected;
	}
	
	@SuppressWarnings("unused")
	private boolean checkHosts(String subnet){
	   int timeout=1000;
	   for (int i=1;i<254;i++){
	       String host=subnet + "." + i;
	       try {
	    	   if (!InetAddress.getByName(host).isLoopbackAddress() && InetAddress.getByName(host).isReachable(timeout)){
			       Log.d(TAG, "addres is reachable " +host);
			       if(connect(host, PORT)){
			    	   outStream.writeInt(VALIDATION_CODE);
			       	   if(inStream.readInt() == VALIDATION_CODE){
			       		   Log.d(TAG, "Validiation confirmed");
			       		   return true;
			       	   }
			       }
			   }else{
				   Log.d(TAG, "addres is NOT reachable " +host);
			   }
	       } catch (UnknownHostException e) {
	    	   e.printStackTrace();
	       } catch (IOException e) {
	    	   e.printStackTrace();
	       }
	   }
	   return false;
	}	
	
	public static String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e(TAG, ex.toString());
	    }
	    return null;
	}
	
	private void initialize(PlayerType playerType){
    	switch(playerType){
    	case PLAYER1: 
    		String ip = null;
    		try {
				ip = DatagramClient.sendBroadcast(PORT, broadCastAddr);
			} catch (IOException e1) {
				Log.e(TAG, "Cannot broadcast datagram to server");
				e1.printStackTrace();
			}

    		if(ip != null){
    			connect(ip, PORT);
        		GameData.getInstance().changeDrawing(2);
        		DrawMsg.drawMsg("Udalo sie nawiazac polaczenie", 2000);
    			Log.d(TAG, "Client connected");
    		}else{
        		GameData.getInstance().changeDrawing(2);
        		DrawMsg.drawMsg("Nie udalo sie nawiazac polaczenia", 2000);
    			Log.d(TAG, "Client not connected");
    		} break;
    		
    	case PLAYER2:
        default:
        	try {	
        		Log.d(TAG, "Starting up Server");
        		DatagramServerThread s = new DatagramServerThread("asd", PORT, "zom");
        		s.run();

        		serverSocket = new ServerSocket(PORT);
	        	serverSocket.setSoTimeout(SERVER_TIMEOUT);
				socket = serverSocket.accept();
				socket.setSoTimeout(TIMEOUT);
		        Log.d(TAG, "Connection estabilished");
		        
		        this.outStream = new ObjectOutputStream(socket.getOutputStream());
		        this.inStream = new ObjectInputStream(socket.getInputStream());
		        Log.d(TAG, "Socket streams initialized");

	    		GameData.getInstance().changeDrawing(2);
	    		DrawMsg.drawMsg("Wszystko smiga", 2000);
		        Log.d(TAG, "Client Connected");
			} catch (IOException e) {
				Log.e("TAG", "Server set to listen errror");
			}
    		break;
    	}
	}
	
	
	private InetAddress getBroadcastAddress(Context context) throws IOException {
		WifiManager myWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo myDhcpInfo = myWifiManager.getDhcpInfo();
		if (myDhcpInfo == null) {
			System.out.println("Could not get broadcast address");
			return null;
		}
		int broadcast = (myDhcpInfo.ipAddress & myDhcpInfo.netmask)
					| ~myDhcpInfo.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
		quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}
}
