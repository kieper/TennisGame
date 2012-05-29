/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 *
 * @author Kieper
 */
public class GameThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameView panel;
    private boolean run = false;
    private final double STATIC_FPS = 20;
    private final String TAG  = this.getClass().getSimpleName();
    
    public GameThread(SurfaceHolder surfaceHolder, GameView panel) {
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    @Override
    public void run() {
        Canvas c;

        long beginTime=0;     // the time when the cycle begun
        long prevBeginTime=0;
        long timeDiff=0;      // the time it took for the cycle to execute
        double fps = 0;

        while (run) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                
                synchronized (surfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    timeDiff = beginTime - prevBeginTime;
                    fps = (fps + 1000/timeDiff)/2;
                    panel.transferData();                  
                    panel.setFPS(fps);
                    panel.runCalculateGame();
                    panel.onDraw(c);

                    
                    if(timeDiff < (1/STATIC_FPS)*1000){
                    	try{
                    		sleep((long) ((1/STATIC_FPS)*1000-timeDiff));
                    	}catch(Exception e){
                    		Log.e(TAG, "[Sleep] GameThread: " + e.getMessage() );
                    	}
                    }
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            prevBeginTime = beginTime;
        }
    }
}
