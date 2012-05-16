/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.tennis;

import android.content.Context;
import android.graphics.Rect;
import android.view.Display;
import java.util.ArrayList;

/**
 *
 * @author Kieper
 */
public class GameData {
	
    private ArrayList<GraphicObject> graphicObjects;
    private final Background background;
    private Player player1;
    private Player player2;
    private Ball ball;
    
    public GameData(Context context, Display displayMetrics){
        graphicObjects = new ArrayList<GraphicObject>();        
        background = new Background(displayMetrics.getWidth(), displayMetrics.getHeight());
        player1 = new Player(background, PlayerType.PLAYER1);
        player1.getSpeed().setConstraint(new Rect(0,0,displayMetrics.getWidth(),displayMetrics.getHeight()));
        player2 = new Player(background, PlayerType.PLAYER2);
        player1.getSpeed().setConstraint(new Rect(0,0,displayMetrics.getWidth(),displayMetrics.getHeight()));
        ball = new Ball(background);
    }

    public ArrayList<GraphicObject> getObjects(){
        return graphicObjects;
    }

    public Background getBackground(){
        return background;
    }
    
    public Player getPlayer1(){
    	return player1;
    }
    
    public Player getPlayer2(){
    	return player2;
    }
    
    public Player getPlayer(PlayerType playerType){
    	switch(playerType){
    	case PLAYER1:
    		return player1;
    	case PLAYER2:
    		return player2;
    	default:
    		return  player1;
    	}
    }
    
    public Ball getBall(){
    	return ball;
    }
}
