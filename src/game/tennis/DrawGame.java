/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.graphics.Canvas;
import java.util.ArrayList;

/**
 *
 * @author Kieper
 */
public class DrawGame implements Draw{

	private GameData gameData;
	
    public DrawGame(GameData gameData) {
    	this.gameData = gameData;
    }


    public void drawBackground(Canvas canvas, Background bg) {
        bg.draw(canvas);
    }

    public void draw(Canvas canvas, ArrayList<GraphicObject> go) {
        for (GraphicObject i : go) {
            i.draw(canvas);
        }
    }

    public void draw(Canvas canvas) {
        gameData.getBackground().draw(canvas);
        gameData.getPlayer1().draw(canvas);
        gameData.getPlayer2().draw(canvas);
        gameData.getBall().draw(canvas);
    }
    
}
