/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;
import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Rect;
import android.util.Log;

/**
 *
 * @author Kieper
 */
public class CalculateGame {

	private byte collisionCount = 0;
	private final String TAG = this.getClass().getSimpleName();
	
    public CalculateGame() {
    }

    public void calculatePosition(ArrayList<GraphicObject> go) {
        Iterator<GraphicObject> it = go.iterator();
 
        while (it.hasNext()) {
            GraphicObject i = it.next();
            i.getSpeed().UpdateXYPosition();
        }
    }

    public double collisionCheck(Player g1, Ball g2){
    	boolean t = Rect.intersects(g1.getRect(), g2.getRect());

    	Log.d(TAG, "Player " + g1.getRect().toString());
    	Log.d(TAG, "BALL " + g2.getRect().toString());
    	Log.d(TAG, "T value = " + t + " collision count value = " + collisionCount);
    	if(t  && (collisionCount <= 0)){
    		Log.d(TAG, "Kolizja");
    		double ratio = (g1.getRect().bottom - g2.getRect().centerY())/(double)g1.getRect().height();

    		collisionCount = 50;
    		return ratio;    		
    	}else{
    		collisionCount--;
    		if(collisionCount < 0) collisionCount = 0;
    	}
    	return -1;
    }
 
    @SuppressWarnings("unused")
	private boolean collision(Rect r1, Rect r2){    	 
        int left1, left2;
        int right1, right2;
        int top1, top2;
        int bottom1, bottom2;

        left1 = r1.left;
        left2 = r2.left;
        right1 = left1 + r1.width();
        right2 = left2 + r2.width();
        top1 = r1.top;
        top2 = r2.top;
        bottom1 = top1 + r1.height();
        bottom2 = top2 + r2.height();

        if (bottom1 <= top2) return false;
        if (top1 >= bottom2) return false;
  
        if (right1 <= left2) return false;
        if (left1 >= right2) return false;

        return true;    	 
    }
}
