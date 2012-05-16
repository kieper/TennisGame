/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 *
 * @author Kieper
 */
public class ScoreView extends SurfaceView implements SurfaceHolder.Callback {

    private ScoreThread scoreThread;

    public ScoreView(Context context) {
        super(context);
        getHolder().addCallback(ScoreView.this);
        scoreThread = new ScoreThread(getHolder(), this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.MAGENTA);
        Paint pTxt = new Paint();
        pTxt.setColor(Color.CYAN);
        pTxt.setTextSize(60);
        canvas.drawText("Ujajiłeś", 200, 200, pTxt);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Context context = this.getContext();
            Intent intent = new Intent(context, HomeActivity.class);
            scoreThread.setRunning(false);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
        return true;
    }

    public ScoreThread getScoreThread(){
        return this.scoreThread;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        scoreThread.setRunning(true);
        scoreThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        scoreThread.setRunning(false);
        while (retry) {
            try {
                scoreThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }

    public class ScoreThread extends Thread {

        private SurfaceHolder surfaceHolder;
        private ScoreView scoreView;
        private boolean run = false;

        public ScoreThread(SurfaceHolder surfaceHolder, ScoreView panel) {
            this.surfaceHolder = surfaceHolder;
            scoreView = panel;
        }

        public void setRunning(boolean run) {
            this.run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (run) {
                c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        scoreView.onDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
