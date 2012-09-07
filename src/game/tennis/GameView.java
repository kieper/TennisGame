/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import game.tennis.draw.DrawMsg;
import game.tennis.draw.PlayerType;
import tcpip.communication.game.Communication;
import tcpip.communication.game.CommunicationType;
import tcpip.communication.game.Packet;
import android.content.Context;
import android.graphics.Canvas;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


/**
 * Main View of the game, responsible for showing game and pre-initalization of the game data
 * 
 * @author Kieper
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private double FPS = 0;

    private static PlayerType playerType;

    private static CommunicationType communicationType;

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().getSimpleName();

    private GameThread threadGame;

    private GameData gameData;

    private Communication comm;


    /**
     * Constructor
     * 
     * @param context
     *            of the Activity
     * @param displayMetrics
     *            of display
     * @param playerType
     *            specifies if game acts as server or client
     * @param communicationType
     *            bluetooth/wifi/solo game(none)
     * @param ip
     *            of device if communication type is wifi, and class should connect to some server
     */
    @SuppressWarnings("static-access")
    public GameView(Context context, Display displayMetrics, PlayerType playerType, CommunicationType communicationType, String ip) {
        super(context);
        getHolder().addCallback(GameView.this);

        if (playerType != null && communicationType != null) {
            this.playerType = playerType;
            this.communicationType = communicationType;
        }
        else {
            Toast.makeText(context, "Can not specify player or communication type", Toast.LENGTH_LONG).show();
            throw new NullPointerException();
        }

        // sets main touch listener
        this.setOnTouchListener(TouchListener.getInstance());

        // Creates gameThread responsible for maintaining game
        threadGame = new GameThread(getHolder(), this, context, displayMetrics, ip);
        setFocusable(true);


    }


    /**
     * Draws whole game
     */
    @Override
    public void onDraw(Canvas canvas) {
        gameData.draw(canvas);


        // Paint p = new Paint();
        // p.setColor(Color.RED);
        // p.setTextSize(40);
        // canvas.drawText("" + FPS, 0, 40, p);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadGame.setRunning(true);
        threadGame.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        threadGame.setRunning(false);
        while (retry) {
            try {
                threadGame.join();
                retry = false;
            }
            catch (InterruptedException e) {
                // we will try it again and again...
            }
        }


    }


    public void setFPS(double FPS) {
        this.FPS = FPS;
    }


    /**
     * fire functions to exchange data beetwen devices
     */
    public void transferData() {
        Packet packet = new Packet(gameData, getPlayerType());

        try {
            if (getPlayerType() == PlayerType.PLAYER2) { // Server
                comm.sendData(packet); // Send data about this player and ball position
                packet = comm.reciveData(); // recive data
                packet.setPlayerData(gameData); // <- set new position of player got from packet
                packet.setBallData(gameData);
            }
            else { // client
                packet = comm.reciveData();
                packet.setPlayerData(gameData);
                packet.setBallData(gameData);
                packet = new Packet(gameData, getPlayerType());
                comm.sendData(packet);
            }
        }
        catch (Exception e) {
                DrawMsg.drawMsg("Other player disconnected! \n Please restart game.", -1);
        }
    }


    /**
     * return PlayerType of opponent
     * 
     * @return
     */
    public static PlayerType getOppositePlayer() {
        if (getPlayerType() == null)
            throw new NullPointerException();
        return (getPlayerType() == PlayerType.PLAYER1) ? PlayerType.PLAYER2 : PlayerType.PLAYER1;
    }


    /**
     * return playerType on current device
     * 
     * @return
     */
    public static PlayerType getPlayerType() {
        return playerType;
    }


    /**
     * returns Communication type of current game
     * 
     * @return
     */
    public static CommunicationType getCommunicationType() {
        return communicationType;
    }


    /**
     * sets new GameData object
     * 
     * @param gameData
     */
    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }


    /**
     * Sets new class used for communication
     * 
     * @param comm
     */
    public void setCommunication(Communication comm) {
        this.comm = comm;
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }


    public void dispose() {
        threadGame.setRunning(false);
    }

}
