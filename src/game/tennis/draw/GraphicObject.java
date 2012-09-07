/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 *
 * @author Kieper
 */
public class GraphicObject {

    private Bitmap bitmap;
    private Speed speed;

    public GraphicObject(Bitmap bmp) {
    	this.bitmap = bmp;
    	speed = new Speed();
    }

    public GraphicObject(Bitmap bmp, Speed speed) {
    	this.bitmap = bmp;
    	this.speed = speed;
    }
    
    public Bitmap getGraphic() {
        return bitmap;
    }

    public void setGraphic(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public int getWidth() {
       return  bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public Speed getSpeed(){
    	return speed;
    }

    public void draw(Canvas canvas){
    	canvas.drawBitmap(bitmap, (int) speed.getX(), (int) speed.getY(), null);
    }
}
