/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.tennis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Display;
import java.util.ArrayList;

/**
 *
 * @author Kieper
 */
public class GameData {
	
    private static GameData gameData = new GameData();
	private ArrayList<GraphicObject> graphicObjects;
    private Background background;
    private Player player1;
    private Player player2;
    private Ball ball;
    private Draw drawGame;
    private Draw drawMsg;
    private int drawFlag = 1; // keeps state of what should be drawn on view 1 - game, 2 - msg
    
    private GameData(){
        graphicObjects = new ArrayList<GraphicObject>();        
        drawGame = new DrawGame(this);
        drawMsg = new DrawMsg();
    }

    public void initialize(Context context, Display displayMetrics){
        background = new Background(displayMetrics.getWidth(), displayMetrics.getHeight());
        player1 = new Player(background, PlayerType.PLAYER1);
        player2 = new Player(background, PlayerType.PLAYER2);
        ball = new Ball(background);
        ((DrawMsg)drawMsg).setDisplayMetrics(displayMetrics);
    }
    
    public static GameData getInstance(){
    	return gameData;
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
    
    /**
     * Draws elements of game or msg depending on current state value
     * @param canvas canvas on which elements should be drawn
     */
    public void draw(Canvas canvas){
    	switch(drawFlag){
    		case 1:
    			drawGame.draw(canvas);
    			break;
    		case 2:
    			drawMsg.draw(canvas);
    			break;
    		default:
    			drawGame.draw(canvas);	
    	}
    }
    
    /**
     * Sets state for what should be drawn
     * @param state 1 - game, 2 - msg
     */
    public void changeDrawing(int state){
    	drawFlag = state;
    }
}
