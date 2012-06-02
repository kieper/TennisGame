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

    public final int  TABLE_WIDTH;
    public final int  TABLE_HEIGTH;
	private Paint paint;
    private Rect rectPlayer1Side, rectPlayer2Side; //two halfs of table
    private final double BORDER  = 0.1; //Table distance from screen edges

    
    public Background(int width, int height) {
    	
        TABLE_HEIGTH = (int)((1-2*BORDER)*height);
		TABLE_WIDTH = (int)((1-2*BORDER)*width);
		
		rectPlayer1Side = new Rect((int)(BORDER * width),(int)( BORDER * height), (int)( width/2), (int)((1-BORDER)*height));
        rectPlayer2Side = new Rect((int)( width/2),(int)( BORDER * height), (int)((1-BORDER) * width),(int)((1-BORDER)*height) );
        
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);      
        
    }
    
    public void draw(Canvas canvas){
    	
        canvas.drawColor(Color.BLACK);
    	canvas.drawRect(rectPlayer1Side, paint);
    	canvas.drawRect(rectPlayer2Side, paint);  

    }
    
    public Rect getPlayer1Rect(){
    	return this.rectPlayer1Side;
    }
    

    public Rect getPlayer2Rect(){
    	return this.rectPlayer2Side;
    }

}
