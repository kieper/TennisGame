/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;


import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.widget.Toast;

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
    private GameData gameData;
    private CalculateGame calcGame;
    private Controls controls;
    private CommThread commThread;
    private Communication comm;
    private Context context;
    private Display displayMetrics;
    
    
    public GameThread(SurfaceHolder surfaceHolder, GameView panel, Context context, Display displayMetrics) {
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
        this.context = context;
        this.displayMetrics = displayMetrics;
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    @Override
    public void run() {
        Canvas c;

        long beginTime=0;     // the time when the cycle begun
        long prevBeginTime=0;
        long timeDiff=0;      // the time it took for the cycle to execute
        double fps = 0;

        this.initialize();
        panel.setGameData(gameData);
        panel.setControls(controls);
        
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
    }
    
    public void initialize(){
    
    	gameData = GameData.getInstance();
    	gameData.initialize(context, displayMetrics);
    	calcGame = new CalculateGame();
    	controls = new GameControls(context);    
    	
    	commThread = new CommThread(panel, context, comm, gameData);
    	commThread.setRunning(true);
    	commThread.start();
    }
    
    public void runCalculateGame(){
    	//if(GameView.getPlayerType() == PlayerType.PLAYER2){
    		//calcGame.calculatePosition(gameData.getObjects());
        gameData.getBall().move(calcGame.collisionCheck(gameData.getPlayer1(), gameData.getBall()));
        gameData.getBall().move(calcGame.collisionCheck(gameData.getPlayer2(), gameData.getBall()));    
    	//}
    	controls.controlPlayer(gameData.getPlayer(GameView.getPlayerType()));
        
    }
    

}
