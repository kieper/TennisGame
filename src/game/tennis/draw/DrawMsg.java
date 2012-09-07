package game.tennis.draw;

import game.tennis.ContextGame;
import game.tennis.GameData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View.MeasureSpec;
import android.widget.TextView;

/**
 * Class responsible for drawing messages with text
 * @author Kieper
 *
 */
public class DrawMsg implements Draw{
	
	//Variables used for drawing
	private Paint msgRectPaint;
	private Paint msgTextPaint;
	

    @SuppressWarnings("unused")
    private static final String TAG = "Draw"; 

    //if set true show msg
    private static boolean msgFlag = false;
    //time till when msg should be showing
    private static long msgTimeout = 0;
    //time when msg started showing
    private static long time = 0;
    //String containing our msg
    private static String msg;
    
	public DrawMsg(){
        
        msgRectPaint = new Paint();
        msgRectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        msgRectPaint.setColor(Color.GREEN);
        msgTextPaint = new Paint();
        msgTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        msgTextPaint.setColor(Color.RED);
        msgTextPaint.setTextSize(20);

	}
	
	@Override
	public void draw(Canvas canvas) {
    	if(msgFlag){
         
    	    //some new way to do this
    	    drawTextOnCanvas(canvas, msg);
    	    
    		//Check time current time
    		if(msgTimeout < System.currentTimeMillis() &&( msgTimeout > 0)){
    			msgFlag = false;
    			GameData.getInstance().changeDrawing(1);
    		}
    		
    	}
		
	}
	
	/**
	 * Draws msg on screen.
	 * @param new_msg text to be shown.
	 * @param new_time time in ms for how long msg should me shown( -1 means forever).
	 */
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

    private void drawTextOnCanvas(Canvas canvas, String text) {
        // Setup a textview like you normally would with your activity context
        TextView tv = new TextView(ContextGame.getAppContext());

        // setup text
        tv.setText(text);

        // maybe set textcolor
        tv.setTextColor(Color.RED);

        // you have to enable setDrawingCacheEnabled, or the getDrawingCache will return null
        tv.setDrawingCacheEnabled(true);

        // we need to setup how big the view should be..which is exactly as big as the canvas
        tv.measure(MeasureSpec.makeMeasureSpec((int)(canvas.getWidth()*0.6), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec((int)(canvas.getHeight()*0.6), MeasureSpec.AT_MOST));

        // assign the layout values to the textview
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        // draw the bitmap from the drawingcache to the canvas
        canvas.drawBitmap(tv.getDrawingCache(), (int)(canvas.getWidth()*0.2), (int)(canvas.getWidth()*0.2), msgRectPaint);

        // disable drawing cache
        tv.setDrawingCacheEnabled(false);
    }
    
    
}
