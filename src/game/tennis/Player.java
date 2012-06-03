/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 *
 * @author Kieper
 */
public class Player implements Graphic{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7663827760428826978L;
	private Speed speed;
	private Paint paint;
	private Rect rectPlayer;
	private int playerWidth =10;
	private int playerHeight;
	private PlayerType playerType;
	public final double PLAYER_SIZE_RATIO = 0.2;
	private final int DST_FROM_TABLE = -15;
	private String TAG = this.getClass().getSimpleName();
	
	private Rect sideRect;
	
    public Player(Background bg, PlayerType playerType) {
    	this.playerType = playerType;
    	rectPlayer = new Rect();
    	        
    	if(playerType == PlayerType.PLAYER1){
    		speed = new Speed(0, 0, new Coordinates(bg.getPlayer1Rect().left - DST_FROM_TABLE,(int)(bg.getPlayer1Rect().bottom/2+10)));
    		playerHeight = (int)(PLAYER_SIZE_RATIO*(bg.getPlayer1Rect().bottom - bg.getPlayer1Rect().top));
    		speed.setConstraint(bg.getPlayer1Rect());
            sideRect = bg.getPlayer1Rect();
    	}else{
    		speed = new Speed(0, 0, new Coordinates(bg.getPlayer2Rect().right + DST_FROM_TABLE,(int)(bg.getPlayer2Rect().bottom/2+10)));
    		playerHeight = (int)(PLAYER_SIZE_RATIO*(bg.getPlayer2Rect().bottom - bg.getPlayer2Rect().top));
            speed.setConstraint(bg.getPlayer2Rect());
            sideRect = bg.getPlayer2Rect();
    	}
    	
    	updatePlayerRect();
    	speed.setXYSpeedConstraint(0, 20);
    	paint= new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        
    }
    
    
    public void move(double ratio){
    	//speed.setAccelerationY(ratio);
    	double newSpeed = ratio*10;
    	speed.setYSpeed((speed.getYSpeed() + newSpeed)/2);
    }

    public void draw(Canvas canvas){
    	speed.UpdateXYPosition();
    	updatePlayerRect();
    	canvas.drawRect(rectPlayer, paint);
    	Log.d(TAG, "drawPlayer position (" +speed.getX() + ", " +speed.getY()+ ")");
    }
    
    public Rect getRect(){
    	return rectPlayer;
    }
    
    public PlayerType getPlayerType(){
    	return playerType;
    }
    
    private void updatePlayerRect(){
    	rectPlayer.top = (int) (speed.getY() - playerHeight/2);
    	rectPlayer.left = (int) (speed.getX() - playerWidth/2);
    	rectPlayer.right = (int) (speed.getX() + playerWidth/2);
    	rectPlayer.bottom = (int) (speed.getY() + playerHeight/2);
    }

	@Override
	public Speed getSpeed() {
		return speed;
	}

	@Override
	public int getWidth() {
		return this.rectPlayer.width();
	}

	@Override
	public int getHeight() {
		return this.rectPlayer.height();
	}

	@Override
	public byte getType() {
		return (byte) GraphicTypes.Player.ordinal();		
	}
    
	public Rect getSideRect(){
		return sideRect;
	}
	
    public String toString(){
		return TAG + " x = " + getSpeed().getX() + " y = " + getSpeed().getY();    	
    }
}
