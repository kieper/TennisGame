/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


/**
 *
 * @author Kieper
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameData gameData;
    private CalculateGame calcGame;
    private GameThread threadGame;
    private Controls gameControls;
    private double FPS = 0;
    private static PlayerType playerType;
    private Communication comm;
    private final String TAG = this.getClass().getSimpleName();
    
    public GameView(Context context, Display displayMetrics, PlayerType playerType, CommunicationType communicationType) {
        super(context);
        getHolder().addCallback(GameView.this);       
        
        gameData = new GameData(context, displayMetrics);
        calcGame = new CalculateGame();
        gameControls = new GameControls(context);     
        
        if(playerType != null ){
        	this.playerType = playerType;
        }else{        	
        	Toast.makeText(context,"Can not specify player type", Toast.LENGTH_LONG).show();
        	throw new NullPointerException();
        }
        
        Log.d(TAG, "Player set to " + this.playerType);
        if(communicationType != null ){
        	switch(communicationType){
        		case WIFI:
        			Log.d(TAG, "Starting WIFI communication");
        			comm = new WifiCommunication(context, this.playerType);
        			break;        		
        		case NONE:
        			Log.d(TAG, "Starting new solo communication");
        			comm = new SoloCommunication(gameData, PlayerType.PLAYER1);
        			break;
        		case BLUETOOTH:
        		default:
        			Toast.makeText(context,"Can not specify communication type", Toast.LENGTH_LONG).show();
        			throw new  UnsupportedOperationException();
        	}
        }else{
        	Toast.makeText(context,"Can not specify communication type", Toast.LENGTH_LONG).show();
        	throw new NullPointerException();
        }
        if(comm == null){
        	Background.drawMsg("Nie nawiazano polaczenia", 0);
        }
        threadGame = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    public void runCalculateGame(){
    	if(playerType == PlayerType.PLAYER2){
    		//calcGame.calculatePosition(gameData.getObjects());
        	
        	gameData.getBall().move(calcGame.collisionCheck(gameData.getPlayer1(), gameData.getBall()));
        	gameData.getBall().move(calcGame.collisionCheck(gameData.getPlayer2(), gameData.getBall()));    
    	
    	}
    	gameControls.controlPlayer(gameData.getPlayer(playerType));
        
    }

    @Override
    public void onDraw(Canvas canvas) {
        gameData.getBackground().draw(canvas);
        gameData.getPlayer1().draw(canvas);
        gameData.getPlayer2().draw(canvas);
        gameData.getBall().draw(canvas);
        
        gameControls.draw(canvas);
        
        //drawGame.draw(canvas, gameData.getObjects());
        
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(40);
        canvas.drawText("" + FPS, 0, 40, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        gameControls.dispose();
    	
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
    	Packet packet = new Packet(gameData, playerType);
    	if(playerType == PlayerType.PLAYER2){  //Server
    		comm.sendData(packet);  //Send data about this player and ball position
    		packet = comm.reciveData(); //recive data 
    		packet.setPlayerData(gameData); // <- set new position of player got from packet
    	}else{ //client
    		packet = comm.reciveData();
    		packet.setPlayerData(gameData); // <- nullpointer excep
    		packet.setBallData(gameData);
    		packet = new Packet(gameData, playerType);
    		comm.sendData(packet);
    	}
    }
    
    public static PlayerType getOppositePlayer(){
    	if(playerType == null) throw new NullPointerException();
    	return (playerType == PlayerType.PLAYER1)? PlayerType.PLAYER2: PlayerType.PLAYER1;
    }
}
