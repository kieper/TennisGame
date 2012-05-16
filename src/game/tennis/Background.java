/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 *
 * @author Kieper
 */
public final class Background {

	private Paint paint, msgPaint;
    private Rect rectPlayer1Side, rectPlayer2Side; //two halfs of table
    private Rect msgRect;
    private static String msg;
    private final double BORDER  = 0.1, MSG_BORDER = 0.3; //Table distance from screen edges
    public final int  TABLE_WIDTH;
    public final int  TABLE_HEIGTH;
    private static boolean msgFlag = false;
    private static long msgTimeout = 0;
    private static long time = 0;
    
    public Background(int width, int height) {
    	
        TABLE_HEIGTH = (int)((1-2*BORDER)*height);
		TABLE_WIDTH = (int)((1-2*BORDER)*width);
		
		rectPlayer1Side = new Rect((int)(BORDER * width),(int)( BORDER * height), (int)( width/2), (int)((1-BORDER)*height));
        rectPlayer2Side = new Rect((int)( width/2),(int)( BORDER * height), (int)((1-BORDER) * width),(int)((1-BORDER)*height) );
        
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        
        msgRect = new Rect((int)(MSG_BORDER * width),(int)( MSG_BORDER * height),(int)((1-MSG_BORDER) * width),(int)((1-MSG_BORDER)*height));
        
        msgPaint = new Paint();
        msgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        msgPaint.setColor(Color.GREEN);
        msgPaint.getFontMetrics().ascent = msgRect.bottom;
        msgPaint.getFontMetrics().top = msgRect.top;
        
        
    }
    
    public void draw(Canvas canvas){
        canvas.drawColor(Color.BLACK);
    	canvas.drawRect(rectPlayer1Side, paint);
    	canvas.drawRect(rectPlayer2Side, paint);    	

    	if(msgFlag){
    		canvas.drawRect(msgRect, msgPaint);
    		canvas.drawText(msg, msgRect.left, msgRect.top, msgPaint);
    		if(msgTimeout < System.currentTimeMillis() &&( msgTimeout > 0)){
    			msgFlag = false;
    		}
    	}
    }
    
    public Rect getPlayer1Rect(){
    	return this.rectPlayer1Side;
    }
    
    public static void drawMsg(String new_msg, long new_time){
    	msg = new_msg;
    	time = System.currentTimeMillis();
    	msgTimeout = new_time+time;    	
    	msgFlag = true;
    }
    
    public static void cancelMsg(){
    	msgFlag = false;
    }
    public Rect getPlayer2Rect(){
    	return this.rectPlayer2Side;
    }

}
