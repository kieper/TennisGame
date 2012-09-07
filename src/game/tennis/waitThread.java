package game.tennis;

import game.tennis.draw.DrawMsg;
import android.util.Log;
import android.view.MotionEvent;

public class waitThread extends Thread implements TouchEvent{

	private boolean run = false;

	private final String TAG  = this.getClass().getSimpleName();

    public void setRunning(boolean run) {
        this.run = run;
    }

    public void run() {
    	TouchListener.getInstance().addTouchEvent(this);
        while (run) {
    		GameData.getInstance().changeDrawing(2); //Change drawing style to msg
    		DrawMsg.drawMsg("Nie udalo sie nawiazac polaczenia", 10);
        	try{
        		sleep(15);
        	}catch(Exception e){
        		Log.e(TAG, "[Sleep] WaitThread: " + e.getMessage() );
        	}
        }
    }

    /**
     * What should be done when touch happens in GameView
     */
	@Override
	public void touch(MotionEvent e) {
		Log.e(TAG, "Touch in waitThread");
		if(e.getAction() == MotionEvent.ACTION_DOWN){

		}
	}
}
