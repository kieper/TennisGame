package game.tennis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;


public class BtCommunication implements Communication {

    private static final boolean D = true;
    private final BluetoothAdapter mAdapter;
    private String TAG = this.getClass().getSimpleName();
    //private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    // Name for the SDP record when creating server socket
    private static final String NAME = "GameTennis";
    public static BluetoothDevice device = null;
	private waitThread wt;
	ObjectInputStream inStream = null;
    ObjectOutputStream outStream = null;
    
	public BtCommunication(Context context){

		mAdapter = BluetoothAdapter.getDefaultAdapter();
		wt = new waitThread();
		wt.setRunning(true);
		wt.start();
		if (D) Log.d(TAG, "Player in bt comm " + GameView.getPlayerType().toString()); 
		if(GameView.getPlayerType() == PlayerType.PLAYER1){
			listenForClient();
		}else{
			if(device != null){
				connectToServer(device);
			}
		}
	    wt.setRunning(false);
		if (D) Log.d(TAG, "Constructor ended"); 
	}
	
	@Override
	public void sendData(Packet packet) {
		try {
			if(D)Log.d(TAG, "in function send data");
			if(D)Log.d(TAG, packet.toString());
			packet.send(outStream);
			if(D)Log.d(TAG, "Data  sent");
			
		} catch (IOException e) {
			Log.e(TAG, "Error while sending DATA over WIFI");
		}
		
	}

	@Override
	public Packet reciveData() {
		if(D)Log.d(TAG, "in function recive data");

		Packet packet = Packet.creatorPacket(inStream);
		if(D)Log.d(TAG, "Packet recived, " + packet.toString());
		return packet;
	}

    public void listenForClient(){
        BluetoothServerSocket tmp = null;
        BluetoothServerSocket mmServerSocket;
        // Create a new listening server socket
        if (D) Log.d(TAG, "Start listen for clients"); 
        try {
            tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "listen() failed", e);
        }
        mmServerSocket = tmp;
        
        if (D) Log.d(TAG, "UUID is created succesfuly" + this);
        BluetoothSocket socket = null;
        // Listen to the server socket if we're not connected

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
            	if (D) Log.d(TAG, "Listening on, till client connects method"); 
                socket = mmServerSocket.accept();
                streamInit(socket);
            } catch (IOException e) {
                Log.e(TAG, "accept() failed", e);
            }            

            if (D) Log.d(TAG, "Client connected" + this);    
    }
    
    public void connectToServer(BluetoothDevice device){
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        
    }
    
    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
            streamInit(mmSocket);
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
            	
                mmSocket.connect();
            } catch (IOException e) {

                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode

                return;
            }

        }


    }
    
    private void streamInit(BluetoothSocket socket){
        // Get the BluetoothSocket input and output streams
        try {
        	inStream = (ObjectInputStream) socket.getInputStream();
        	outStream = (ObjectOutputStream) socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }
      
    }
}
