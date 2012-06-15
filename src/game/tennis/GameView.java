/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import java.util.Observable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Toast;


/**
 *
 * @author Kieper
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private double FPS = 0;
    private static PlayerType playerType;
    private static CommunicationType communicationType;
    private final String TAG = this.getClass().getSimpleName();
    private GameThread threadGame;
    private GameData gameData;
    private Communication comm;
    
    
    public GameView(Context context, Display displayMetrics, PlayerType playerType, CommunicationType communicationType, String ip) {
        super(context);
        getHolder().addCallback(GameView.this);       
        
        if(playerType != null && communicationType != null ){
	        	this.playerType = playerType;
	        	this.communicationType = communicationType;
        }else{        	
        	Toast.makeText(context,"Can not specify player or communication type", Toast.LENGTH_LONG).show();
        	throw new NullPointerException();
        }        
        
        this.setOnTouchListener(TouchListener.getInstance());        
        threadGame = new GameThread(getHolder(), this, context, displayMetrics, ip);
        setFocusable(true);
    }



    @Override
    public void onDraw(Canvas canvas) {
    	gameData.draw(canvas);
                        
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(40);
        canvas.drawText("" + FPS, 0, 40, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	Log.e(TAG, "Touch Happend" );
        synchronized (threadGame.getSurfaceHolder()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

            }
            return true;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
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
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
        
        
    }

    public void setFPS(double FPS) {
        this.FPS = FPS;
    }
    
    public void transferData(){
    	Packet packet = new Packet(gameData, getPlayerType());
    	if(getPlayerType() == PlayerType.PLAYER2){  //Server
    		comm.sendData(packet);  //Send data about this player and ball position
    		packet = comm.reciveData(); //recive data 
    		packet.setPlayerData(gameData); // <- set new position of player got from packet
       		packet.setBallData(gameData);
    	}else{ //client
    		packet = comm.reciveData();
    		packet.setPlayerData(gameData); 
    		packet.setBallData(gameData);
    		packet = new Packet(gameData, getPlayerType());
    		comm.sendData(packet);
    	}
    }
    
    public static PlayerType getOppositePlayer(){
    	if( getPlayerType() == null) throw new NullPointerException();
    	return (getPlayerType() == PlayerType.PLAYER1)? PlayerType.PLAYER2: PlayerType.PLAYER1;
    }

	public static PlayerType getPlayerType() {
		return playerType;
	}

	public static CommunicationType getCommunicationType(){
		return communicationType;
	}
	
	public void setGameData(GameData gameData){
		this.gameData = gameData;
	}
	
	public void setCommunication(Communication comm){
		this.comm = comm;
	}

}
