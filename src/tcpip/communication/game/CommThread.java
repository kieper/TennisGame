package tcpip.communication.game;

import game.tennis.GameData;
import game.tennis.GameView;
import game.tennis.draw.DrawMsg;
import game.tennis.draw.PlayerType;
import android.content.Context;
import android.util.Log;


public class CommThread extends Thread {

    private GameView panel;

    private boolean run = false;

    private Context context;

    private Communication comm;

    private GameData gameData;

    private boolean connected = false;

    private String ip;

    private final String TAG = this.getClass().getSimpleName();


    /**
     * Constructor of CommThread, this thread takes care of communication, connecting, data exchange
     * 
     * @param panel
     * @param context
     * @param comm
     * @param gameData
     * @param ip
     */
    public CommThread(GameView panel, Context context, Communication comm, GameData gameData, String ip) {
        this.panel = panel;
        this.context = context;
        this.gameData = gameData;
        this.ip = ip;
    }


    /**
     * Turns on/off CommThread
     * 
     * @param run
     *            on-true/off-false
     */
    public void setRunning(boolean run) {
        this.run = run;
    }


    /**
     * Main loop in which data exchange is done
     */
    public void run() {
        initialize();

        while (run) {
            panel.transferData();
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }        
        finish();
    }


    /**
     * Initializes connection over specified connection type
     */
    public void initialize() {
        Log.d(TAG, "Player set to " + GameView.getPlayerType());
        switch (GameView.getCommunicationType()) {
        case WIFI:
            Log.d(TAG, "Starting WIFI communication, ip to connect is: " + ip);
            comm = new WifiCommunication(context, ip);
            break;
        case NONE:
            Log.d(TAG, "Starting new solo communication");
            comm = new SoloCommunication(gameData, PlayerType.PLAYER1);
            break;
        case BLUETOOTH:
            Log.d(TAG, "Starting BT communication");
            comm = new BtCommunication(context);
            break;

        default:
            throw new UnsupportedOperationException();
        }


        if (comm == null) {
            GameData.getInstance().changeDrawing(2);
            DrawMsg.drawMsg("Nie udalo sie nawiazac polaczenia", 2000);
        }

        panel.setCommunication(comm);
        connected = true;
    }


    /**
     * Returns wheter class is connected to any device
     * 
     * @return
     */
    public boolean isConnected() {
        return connected;
    }


    public void finish() {
        comm.close();
    }
}
