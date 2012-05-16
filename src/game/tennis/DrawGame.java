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
public class DrawGame {

    public DrawGame() {
    }


    public void drawBackground(Canvas canvas, Background bg) {
        bg.draw(canvas);
    }

    public void draw(Canvas canvas, ArrayList<GraphicObject> go) {
        for (GraphicObject i : go) {
            i.draw(canvas);
        }
    }

    public void draw(Canvas canvas, GraphicObject go) {
        go.draw(canvas);
    }
    
}
