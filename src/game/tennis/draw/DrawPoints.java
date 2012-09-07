package game.tennis.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class DrawPoints implements Draw {

    private int p1Points;
    private int p2Points;
    
    private int x1;
    private int x2;
    
    private static DrawPoints drawPoints;
    
    public static DrawPoints getInstance(){
        if(drawPoints == null) {
            synchronized(DrawPoints.class) { 
                drawPoints = new DrawPoints();
            }
         }
        return drawPoints;
    }
    
    
    //for singleton pattern
    private DrawPoints(){
    }
    
    @Override
    public void draw(Canvas canvas) {
    
        x1 = canvas.getWidth();
        
        //position to draw points for each player
        x2 = (int)(0.05*x1);
        x1 = (int)(0.95*x1);
        
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(40);
        
        canvas.drawText("" + p1Points, x1, 40, p);
        canvas.drawText("" + p2Points, x2, 40, p);
        
    }

    public int getP1Points() {
        return p1Points;
    }

    public void addPoint(PlayerType pt) {
        if( pt == null){ return;}
        if(pt == PlayerType.PLAYER1){
            this.p1Points++;
        }else{
            this.p2Points++;
        }
    }

    public int getP2Points() {
        return p2Points;
    }

    
    public static void dispose(){
        drawPoints = null;
    }
}
