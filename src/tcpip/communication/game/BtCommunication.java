package tcpip.communication.game;

import game.tennis.GameView;
import game.tennis.waitThread;
import game.tennis.draw.PlayerType;

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


public class BtCommunication extends Communication {

    private static final boolean D = true;

    private final BluetoothAdapter mAdapter;

    private String TAG = this.getClass().getSimpleName();

    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    // Name for the SDP record when creating server socket
    private static final String NAME = "GameTennis";

    public static BluetoothDevice device = null;

    private waitThread wt;


    public BtCommunication(Context context) {

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        wt = new waitThread();
        wt.setRunning(true);
        wt.start();
        if (D) {
            Log.d(TAG, "Player in bt comm " + GameView.getPlayerType().toString());
        }
        if (GameView.getPlayerType() == PlayerType.PLAYER2) {
            listenForClient();
        }
        else {
            if (device != null) {
                if (D)
                    Log.d(TAG, "Device to connect to: " + device.toString());
                connectToServer(device);
            }
        }

        synchronizeTime(GameView.getPlayerType());

        wt.setRunning(false);
        if (D)
            Log.d(TAG, "Constructor ended");
    }


    public void listenForClient() {
        BluetoothServerSocket serverSocket = null;
        // Create a new listening server socket
        if (D) {
            Log.d(TAG, "Start listen for clients");
        }
        try {
            serverSocket = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        }
        catch (IOException e) {
            Log.e(TAG, "listen() failed", e);
        }

        if (D) {
            Log.d(TAG, "UUID is created succesfuly" + this);
        }
        BluetoothSocket socket = null;
        // Listen to the server socket if we're not connected
        try {
            if (serverSocket != null) {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                if (D) {
                    Log.d(TAG, "Listening on, till client connects method");
                }
                socket = serverSocket.accept();
                streamInit(socket);
            }
            else {
                if (D) {
                    Log.d(TAG, "serverSocket is null cannot continue");
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "accept() failed", e);
        }

        if (D) {
            Log.d(TAG, "Client connected" + this);
        }
    }


    public void connectToServer(BluetoothDevice device) {
        if (D)
            Log.d(TAG, "Connecting to server");
        BluetoothSocket socket = null;

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            if (D) {
                Log.d(TAG, "connection to bt device");
            }
        }
        catch (IOException e) {
            Log.e(TAG, "create() failed", e);
        }

        mAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            if (socket != null) {

                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket.connect();
                if (D) {
                    Log.d(TAG, "Connected");
                }
                streamInit(socket);
            }
            else {
                if (D) {
                    Log.d(TAG, " socket is null cannot connect ");
                }

            }
        }
        catch (IOException e) {

            // Close the socket
            try {
                socket.close();
            }
            catch (IOException e2) {
                Log.e(TAG, "unable to close() socket during connection failure", e2);
            }
            // Start the service over to restart listening mode

            return;
        }
    }


    private void streamInit(BluetoothSocket socket) {
        // Get the BluetoothSocket input and output streams
        try {
            if (socket.getInputStream() != null && socket.getOutputStream() != null) {
                outStream = new ObjectOutputStream(socket.getOutputStream());
                inStream = new ObjectInputStream(socket.getInputStream());
            }
            else {
                if (D) {
                    Log.d(TAG, "Stream is null");
                }
            }
            if (D) {
                Log.d(TAG, "Stream initilized");
            }
        }
        catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
            e.printStackTrace();
        }

    }


    @Override
    public void close() {
        super.close();
        if (mAdapter != null) {
            mAdapter.disable();
        }
    }
}
