package game.tennis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;

public class WifiCommunication implements Communication {

	private Socket socket;
	private ServerSocket serverSocket;
	private DataOutputStream outStream;
	private DataInputStream inStream;
	private boolean isconnected;	
	private static final String TAG = "WifiCommunication";
	private final int PORT = 4000;
	private final int VALIDATION_CODE = 1234;
	private final int TIMEOUT = 700;
	private final int SERVER_TIMEOUT = 0; 
	
	public WifiCommunication(Context context, PlayerType playerType){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d(TAG, "Checking wifi Status");
		System.setProperty("java.net.preferIPv4Stack", "true");
	    if( ! cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting() ) {
	    	Log.d(TAG, "WIFI not connected");
	        Toast.makeText(context, "Turn on wifi, and try again",  Toast.LENGTH_LONG).show();
	    }else{	    	
	    	Log.d(TAG, "WIFI connected");
	    	initialize(playerType);
	    }
	}
	
	@Override
	public void sendData(Packet packet) {		
		try {
			Parcel parcel = Parcel.obtain();
			packet.writeToParcel(parcel, 0);
			
			byte data[] = parcel.marshall();   
			outStream.write(data);
			
		} catch (IOException e) {
			Log.e(TAG, "Error while sending DATA over WIFI");
		}
	}


	@Override
	public Packet reciveData() {
		try {
			Parcel parcel = Parcel.obtain();
			new Packet(0,0,0,0).writeToParcel(parcel, 0);
			byte data[] = parcel.marshall();
			inStream.read(data);			
			parcel.unmarshall(data, 0, data.length);			
			return	new CreatorParcelable().createFromParcel(parcel);
			
		} catch (IOException e) { 
			Log.e(TAG, "Error while reciving DATA over WIFI");
		}	
		return null;
	}

	private boolean connect(String ip, int port) {
	    try {	    	
	        this.socket = new Socket(ip, port);
	        Log.d(TAG, "Socket connected");
	        socket.setSoTimeout(TIMEOUT);
	        this.outStream = new DataOutputStream(socket.getOutputStream());
	        this.inStream = new DataInputStream(socket.getInputStream());
	        isconnected = true;
	    } catch (IOException e) {
	        Log.e(TAG, "connection failed on " + ip + ":" + port + " exception: " +e.toString());
	        isconnected = false;
	        return isconnected;
	    }
	    Log.d(TAG, "connection to " + ip + " sucessfull");
	    return isconnected;
	}
	
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
				ip = DatagramClient.sendBroadcast(PORT);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            /*String ip = getLocalIpAddress();
            ip = ip.substring(0, ip.lastIndexOf("."));
    		if(checkHosts(ip)){*/
    		if(ip != null){
    			connect(ip, PORT);
    			Background.drawMsg("Polaczony klient", -1);
    		}else{
    			Background.drawMsg("NiePolaczony klient", -1);
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
		        
		        this.outStream = new DataOutputStream(socket.getOutputStream());
		        this.inStream = new DataInputStream(socket.getInputStream());
		        Log.d(TAG, "Socket streams initialized");
		        
		        if( inStream.readInt() == VALIDATION_CODE){
		        	outStream.writeInt(VALIDATION_CODE);
		        }
		        Background.drawMsg("Polaczony Server", 0);
		        Log.d(TAG, "Client Connected");
			} catch (IOException e) {
				Log.e("TAG", "Server set to listen errror");
			}
    		break;
    	}
	}
	
}
