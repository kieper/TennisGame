/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;


import game.tennis.draw.Controls;
import game.tennis.draw.DrawPoints;
import tcpip.communication.game.CommThread;
import tcpip.communication.game.Communication;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;

/**
 *
 * @author Kieper
 */
public class GameThread extends Thread   {

    
	private SurfaceHolder surfaceHolder;
    private GameView panel;
    private boolean run = false;
    private final double STATIC_FPS = 20;
    private final String TAG  = this.getClass().getSimpleName();
    private boolean D = true;
    private GameData gameData;
    private CalculateGame calcGame;
    private Controls controls;
    private CommThread commThread;
    private Communication comm;
    private Context context;
    private Display displayMetrics;
    private String ip;
    
    /**
     * Constructor of GameThread, createœ needed data and fires functions needed for game in loop
     * @param surfaceHolder of the view
     * @param panel instance of the view
     * @param context of the view
     * @param displayMetrics of the view
     * @param ip to connect to server if communication is over wifi
     */
    public GameThread(SurfaceHolder surfaceHolder, GameView panel, Context context, Display displayMetrics, String ip) {
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
        this.context = context;
        this.displayMetrics = displayMetrics;
        this.ip = ip;
    }

    /**
     * turns on/off game thread 
     * @param run on-true/off-false
     */
    public void setRunning(boolean run) {
        this.run = run;
    }

    /**
     * returns surface holder
     * @return
     */
    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    /**
     * Main loop executing actions needed for game
     */
    @Override
    public void run() {
        Canvas c;

        long beginTime=0;     // the time when the cycle begun
        long prevBeginTime=0;
        long timeDiff=0;      // the time it took for the cycle to execute
        double fps = 0;

        this.initialize();
        panel.setGameData(gameData);
        
        //allows to draw on view while sockets block other threads
        while(!commThread.isConnected()){
        	try{
        		sleep(10);
        	}catch(Exception e){
        		Log.e(TAG, "[Sleep] GameThread, while waiting for connection: " + e.getMessage() );
        	}
        	
            c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                
                synchronized (surfaceHolder) {
                    panel.onDraw(c);
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
        if(D)Log.d(TAG, "Starting gamming loop, communication estabilished");
        
        //Run the game loop
        while (run) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                
                synchronized (surfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    timeDiff = beginTime - prevBeginTime;
                    fps = (fps + 1000/timeDiff)/2;           
                    panel.setFPS(fps);
                    runCalculateGame();
                    panel.onDraw(c);

                    
                    if(timeDiff < (1/STATIC_FPS)*1000){
                    	try{
                    		sleep((long) ((1/STATIC_FPS)*1000-timeDiff));
                    	}catch(Exception e){
                    		Log.e(TAG, "[Sleep] GameThread: " + e.getMessage() );
                    	}
                    }
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            prevBeginTime = beginTime;
        }
        
        finish();
    }
    
    /**
     * Initializes classes needed for game
     */
    public void initialize(){
    	gameData = GameData.getInstance();
    	gameData.initialize(context, displayMetrics);
    	calcGame = new CalculateGame();  
    	
    	controls = new GameControls(context);
    	
    	commThread = new CommThread(panel, context, comm, gameData, ip);
    	commThread.setRunning(true);
    	commThread.start();
    }
    
    /**
     * Runs functions to check for collisions
     */
    public void runCalculateGame(){
        gameData.getBall().move(calcGame.collisionCheck(gameData.getPlayer1(), gameData.getBall()));
        gameData.getBall().move(calcGame.collisionCheck(gameData.getPlayer2(), gameData.getBall()));    
    	controls.controlPlayer(gameData.getPlayer(GameView.getPlayerType()));        
    }
    
    /**
     * Cleanup
     */
    public void finish(){
        commThread.setRunning(false);
        controls.dispose();        
        gameData.dispose();
        DrawPoints.dispose();
    }

}
