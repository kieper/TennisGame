package game.tennis;

//import game.tennis.Packet.Buffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
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
 * Class responsible for connection over wifi network
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
	
	private long timeCorrection = 0;
	
	private String preConfIP = null; //got ip from Activity?
	private boolean D = true;
	
	/**
	 * Konsturktor
	 * @param context got from View or Activity
	 * @param playerType PLAYER1 or PLAYER2
	 * @param ip optional, connects to this ip
	 */
	public WifiCommunication(Context context, PlayerType playerType, String ip){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d(TAG, "Checking wifi Status");
		System.setProperty("java.net.preferIPv4Stack", "true");
		wt = new waitThread();
		wt.setRunning(true);
		wt.start();
		
		this.preConfIP = ip;
		
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
	    	synchronizeTime(playerType);
	    }
	    wt.setRunning(false);
	}
	
	/**
	 * Send data in packet
	 */
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

	/**
	 * Recive packet Data
	 */
	@Override
	public Packet reciveData() {
			if(D )Log.d(TAG, "in function recive data");
			if(D )Log.d(TAG, "set packet time correction");

			Packet packet = Packet.creatorPacket(inStream);
			packet.setTimeCorrection(timeCorrection);
			if(D )Log.d(TAG, packet.toString());
			return packet;
	}

	/**
	 * Connects to server
	 * @param ip of server
	 * @param port to which we should connect
	 * @return true if connection succeded
	 */
	private boolean connect(String ip, int port) {
	    try {	    
	    	if(D )Log.d(TAG, "connecting ");
	        this.socket = new Socket(ip, port);
	        if(D )Log.d(TAG, "Socket connected client");
	        socket.setSoTimeout(TIMEOUT); 
	        this.outStream = new ObjectOutputStream(socket.getOutputStream());
	        this.inStream = new ObjectInputStream(socket.getInputStream());
	        isconnected = true;
	    } catch (IOException e) {
	    	if(D )Log.e(TAG, "connection failed on " + ip + ":" + port + " exception: " +e.toString());
	        isconnected = false;
	        return isconnected;
	    }
	    if(D )Log.d(TAG, "connection to " + ip + " sucessfull");
	    return isconnected;
	}
	
	@SuppressWarnings("unused")
	private boolean checkHosts(String subnet){
	   int timeout=1000;
	   for (int i=1;i<254;i++){
	       String host=subnet + "." + i;
	       try {
	    	   if (!InetAddress.getByName(host).isLoopbackAddress() && InetAddress.getByName(host).isReachable(timeout)){
	    		   if(D )Log.d(TAG, "addres is reachable " +host);
			       if(connect(host, PORT)){
			    	   outStream.writeInt(VALIDATION_CODE);
			       	   if(inStream.readInt() == VALIDATION_CODE){
			       		   if(D )Log.d(TAG, "Validiation confirmed");
			       		   return true;
			       	   }
			       }
			   }else{
				   if(D )Log.d(TAG, "addres is NOT reachable " +host);
			   }
	       } catch (UnknownHostException e) {
	    	   e.printStackTrace();
	       } catch (IOException e) {
	    	   e.printStackTrace();
	       }
	   }
	   return false;
	}	
	
	/**
	 * Gets lolcal ip addres
	 * @return lolcal ip
	 */
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
	
	/**
	 * Initializes connection
	 * @param playerType set wheter class should listen for client or try to connect
	 */
	private void initialize(PlayerType playerType){
    	switch(playerType){
    	case PLAYER1: 
    		String ip = null;
    		if(preConfIP != null){
    			ip = preConfIP;
    			try {
					broadCastAddr = InetAddress.getByName(preConfIP);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}else{
    			try {
    				ip = DatagramClient.sendBroadcast(PORT+1, broadCastAddr);
				} catch (IOException e1) {
					Log.e(TAG, "Cannot broadcast datagram to server");
					e1.printStackTrace();
				}
    		}	
    		if(ip != null){
    			connect(ip, PORT);
        		GameData.getInstance().changeDrawing(2);
        		DrawMsg.drawMsg("Udalo sie nawiazac polaczenie", 2000);
        		if(D )Log.d(TAG, "Client connected");
    		}else{
        		GameData.getInstance().changeDrawing(2);
        		DrawMsg.drawMsg("Nie udalo sie nawiazac polaczenia", 2000);
        		if(D )Log.d(TAG, "Client not connected");
    		}break;
    		
    	case PLAYER2:
        default:
        	try {	
        		if(D )Log.d(TAG, "Starting up Server");
        		DatagramServerThread s = new DatagramServerThread("asd", PORT+1, "zom");
        		s.run();

        		if(D) Log.d(TAG, "Waiting for connection");
        		serverSocket = new ServerSocket(PORT);
	        	serverSocket.setSoTimeout(SERVER_TIMEOUT);
				socket = serverSocket.accept();
				socket.setSoTimeout(TIMEOUT);
				if(D )Log.d(TAG, "Connection estabilished");
		        
		        this.outStream = new ObjectOutputStream(socket.getOutputStream());
		        this.inStream = new ObjectInputStream(socket.getInputStream());
		        if(D )Log.d(TAG, "Socket streams initialized");

	    		GameData.getInstance().changeDrawing(2);
	    		DrawMsg.drawMsg("Wszystko smiga", 2000);
	    		if(D )Log.d(TAG, "Client Connected");
			} catch (IOException e) {
				Log.e("TAG", "Server set to listen errror");
			}
    		break;
    	}
	}
	
	/**
	 * Return broadcast addres
	 * @param context from activity or View
	 * @return broadcast addres of network
	 * @throws IOException
	 */
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

	/**
	 * Sends packet to synchornize to devices for game
	 * @param time current time on deivce
	 */
	private void sentTimeSynchronizationPacket(long time){
		try {
			Ping ping = new Ping(1, time);
			outStream.writeObject(ping);
		}catch (IOException e) {
			Log.e(TAG, "Error while sending DATA over WIFI");
		}
	}
	
	/**
	 * returns synchornization time packet
	 * @return synchornization packet
	 */
	private long recvTimeSynchronizationPacket(){
		Ping ping;
		try {
			ping = (Ping) inStream.readObject(); //read data sent from other device
			return ping.getTime();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Synchornize time of device for game
	 * @param playerType PLAYER1 or PLAYER2
	 */
	private void synchronizeTime(PlayerType playerType){
		long time0, time1, time2;
    	switch(playerType){
    	case PLAYER1:
    		//on player1 time0 -> send player1 time0
    		//on player1 time2 -> recv player2 time1
    		//on player1 time2 -> send player1 time2
    		time0 = System.currentTimeMillis();
    		sentTimeSynchronizationPacket(time0);
    		time1 = recvTimeSynchronizationPacket();
    	    time2 = System.currentTimeMillis();
    		sentTimeSynchronizationPacket(time2);
    		
    		//(time2 + time0)/2 -> player1 time
    		//recived time1     -> player2 time
    		timeCorrection = (time2 + time0)/2 - time1;
    		break;
    	case PLAYER2:
    	default:
    		//on player2 time1 -> recv player1 time0
    		//on player2 time1 -> send player2 time1
    		//on player2 time3 -> recv player1 time2
			//Packet packet = Packet.creatorPacket(inStream);
    		time0 = recvTimeSynchronizationPacket();
    		time1 = System.currentTimeMillis();
    		sentTimeSynchronizationPacket(time1);
    		time2 = recvTimeSynchronizationPacket();

    		//(time2 + time0)/2 -> player1 time
    		//recived time1     -> player2 time
    		timeCorrection = -((time2 + time0)/2 - time1);
    		break;
    	}
	}
}
