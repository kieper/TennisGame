/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis.draw;

import android.graphics.Canvas;
import game.tennis.GameData;

import java.util.ArrayList;

/**
 * Implements Draw interface, class responsible for drawing game view
 * @author Kieper
 */
public class DrawGame implements Draw{

    public void draw(Canvas canvas, ArrayList<GraphicObject> go) {
        for (GraphicObject i : go) {
            i.draw(canvas);
        }
    }

    public void draw(Canvas canvas) {
        GameData gameData = GameData.getInstance();
        //Draw background
        gameData.getBackground().draw(canvas);
        //Draw player 1
        gameData.getPlayer1().draw(canvas);
        //Draw player 2
        gameData.getPlayer2().draw(canvas);
        //Draw ball
        gameData.getBall().draw(canvas);
        //Draw points
        DrawPoints.getInstance().draw(canvas);
    }
    
}
