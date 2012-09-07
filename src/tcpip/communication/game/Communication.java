package tcpip.communication.game;

import game.tennis.draw.PlayerType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import android.util.Log;


public abstract class Communication {

    protected ObjectOutputStream outStream;

    protected ObjectInputStream inStream;

    protected static final String TAG = "Communication";

    protected static final boolean D = true;

    protected long timeCorrection = 0;

    public void sendData(Packet packet) throws IOException {
            if (D){
                Log.d(TAG, "in function send data");}
            if (D){
                Log.d(TAG, packet.toString());}
            packet.send(outStream);
            if (D)
                Log.d(TAG, "Data  sent");

    }

    public Packet reciveData() throws IOException, ClassNotFoundException {

        if (D) {
            Log.d(TAG, "in function recive data");
        }
        if (D) {
            Log.d(TAG, "set packet time correction");
        }
        Packet packet = null;

        packet = Packet.creatorPacket(inStream);

        packet.setTimeCorrection(timeCorrection);
        if (D) {
            Log.d(TAG, packet.toString());
        }


        return packet;
    }


    /**
     * Sends packet to synchornize to devices for game
     * 
     * @param time
     *            current time on deivce
     */
    private void sendTimeSynchronizationPacket(long time) {
        try {
            Ping ping = new Ping(1, time);
            outStream.writeObject(ping);
        }
        catch (IOException e) {
            Log.e(TAG, "Error while sending DATA over WIFI");
        }
    }


    /**
     * returns synchornization time packet
     * 
     * @return synchornization packet
     */
    private long recvTimeSynchronizationPacket() {
        Ping ping;
        try {
            ping = (Ping) inStream.readObject(); // read data sent from other device
            return ping.getTime();
        }
        catch (OptionalDataException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * Synchornize time of device for game
     * 
     * @param playerType
     *            PLAYER1 or PLAYER2
     */
    protected void synchronizeTime(PlayerType playerType) {
        long time0, time1, time2;
        switch (playerType) {
        case PLAYER1:
            // on player1 time0 -> send player1 time0
            // on player1 time2 -> recv player2 time1
            // on player1 time2 -> send player1 time2
            time0 = System.currentTimeMillis();
            sendTimeSynchronizationPacket(time0);
            time1 = recvTimeSynchronizationPacket();
            time2 = System.currentTimeMillis();
            sendTimeSynchronizationPacket(time2);

            // (time2 + time0)/2 -> player1 time
            // recived time1 -> player2 time
            timeCorrection = (time2 + time0) / 2 - time1;
            break;
        case PLAYER2:
        default:
            // on player2 time1 -> recv player1 time0
            // on player2 time1 -> send player2 time1
            // on player2 time3 -> recv player1 time2
            // Packet packet = Packet.creatorPacket(inStream);
            time0 = recvTimeSynchronizationPacket();
            time1 = System.currentTimeMillis();
            sendTimeSynchronizationPacket(time1);
            time2 = recvTimeSynchronizationPacket();

            // (time2 + time0)/2 -> player1 time
            // recived time1 -> player2 time
            timeCorrection = -((time2 + time0) / 2 - time1);
            break;
        }
    }


    /**
     * Closes streams
     */
    public void close() {
        try {
            if (outStream != null) {
                outStream.close();
            }
            if (inStream != null) {
                inStream.close();
            }
        }
        catch (IOException e) {
            // closee sliently
        }
    }
}
