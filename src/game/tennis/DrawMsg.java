package game.tennis;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class DrawMsg implements Draw{
	
	private Paint msgPaint;
    private Rect msgRect;
    private final double MSG_BORDER = 0.3; //Table distance from screen edges
    private int width = 250, height = 200;
    private static boolean msgFlag = false;
    private static long msgTimeout = 0;
    private static long time = 0;
    private static String msg;
    
	public DrawMsg(){
        msgRect = new Rect((int)(MSG_BORDER * width),(int)( MSG_BORDER * height),(int)((1-MSG_BORDER) * width),(int)((1-MSG_BORDER)*height));
	
        msgPaint = new Paint();
        msgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        msgPaint.setColor(Color.GREEN);
        msgPaint.getFontMetrics().ascent = msgRect.bottom;
        msgPaint.getFontMetrics().top = msgRect.top;
	}
	
	@Override
	public void draw(Canvas canvas) {
    	if(msgFlag){
    		canvas.drawRect(msgRect, msgPaint);
    		canvas.drawText(msg, msgRect.left, msgRect.top, msgPaint);
    		if(msgTimeout < System.currentTimeMillis() &&( msgTimeout > 0)){
    			msgFlag = false;
    			GameData.getInstance().changeDrawing(1);
    		}
    	}
		
	}
	
    public static void drawMsg(String new_msg, long new_time){
    	msg = new_msg;
    	time = System.currentTimeMillis();
    	msgTimeout = new_time+time;    	
    	msgFlag = true;
    }
    
    public static void cancelMsg(){
    	msgFlag = false;
    	GameData.getInstance().changeDrawing(1);
    }

}