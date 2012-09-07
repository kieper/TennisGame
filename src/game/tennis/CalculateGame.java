/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;
import game.tennis.draw.Background;
import game.tennis.draw.Ball;
import game.tennis.draw.DrawPoints;
import game.tennis.draw.GraphicObject;
import game.tennis.draw.Player;
import game.tennis.draw.PlayerType;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Rect;
import android.util.Log;

/**
 *
 * @author Kieper
 */
public class CalculateGame {

    //auxiliary variable to count if collision check should be perfomed
	private byte collisionCount = 0;
	
	private final String TAG = this.getClass().getSimpleName();
	
    public CalculateGame() {
    }

    /**
     * Currently unused, but used to calculate position of some random GraphicObjects in arraylist
     * @param go arraylist of graphic objects
     */
    
    public void calculatePosition(ArrayList<GraphicObject> go) {
        Iterator<GraphicObject> it = go.iterator();
 
        while (it.hasNext()) {
            GraphicObject i = it.next();
            i.getSpeed().UpdateXYPosition();
        }
    }

    /**
     * Checks wheter ball hit player or if ball hit the wall
     * @param g1 Player
     * @param g2 Ball
     * @return -1 if no collision, in other case rebound angel ratio
     */
    public double collisionCheck(Player g1, Ball g2){
    	boolean t = Rect.intersects(g1.getRect(), g2.getRect());

    	//Collision check algorithm
    	if(t  && (collisionCount <= 0)){
    		Log.d(TAG, "Kolizja");
    		double ratio = (g1.getRect().bottom - g2.getRect().centerY())/(double)g1.getRect().height();

    		collisionCount = 50;
    		return ratio;    		
    	}else{
    		collisionCount--;
    		if(collisionCount < 0) collisionCount = 0;
    	}
    	
    	//Check if need to add points for player 1
    	if(g2.getSpeed().getX() <= GameData.getInstance().getTableRect().left){
    	    DrawPoints.getInstance().addPoint(PlayerType.PLAYER1);
    	    Background bg = GameData.getInstance().getBackground();
    	    g2.getSpeed().setXY(GameData.getInstance().getTableRect().centerX(),bg.getPlayer2Rect().centerY());
    	    g2.getSpeed().toggleXDirection();
    	}
    	
    	//Check if need to add points for player 2
    	if(g2.getSpeed().getX() >= GameData.getInstance().getTableRect().right){
    	    DrawPoints.getInstance().addPoint(PlayerType.PLAYER2);
            Background bg = GameData.getInstance().getBackground();
            g2.getSpeed().setXY(GameData.getInstance().getTableRect().centerX(),bg.getPlayer1Rect().centerY());
            g2.getSpeed().toggleXDirection();
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
