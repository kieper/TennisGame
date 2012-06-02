package game.tennis;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This is universal touch listener, used along with GameView class, we can intercept all touch event, and react to them by using
 * Objects implementing TouchEvent class.
 * @author Kieper
 *
 */
public class TouchListener implements OnTouchListener {
	private static TouchListener touchListener = new TouchListener();
	private static TouchEvent touchEvent = null;
	private static String TAG = "TouchListener";
	/**
	 * Just private to be singleton
	 */
	private TouchListener(){
		
	}
	
	/**
	 * Returns instance of this object(Singleton Pattern)
	 * @return instance of TouchListener
	 */
	public static TouchListener getInstance(){
		return touchListener;
	}
	
	/**
	 * Responds touch events 
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.e(TAG, "Touch in listener status of touchevent = " + touchEvent);
		if(touchEvent != null){
			touchEvent.touch(event);
		}
		return false;
	}

	/*
	 * Sets object from which touch method will be used
	 */
	public void setTouchEvent(TouchEvent touchClass){
		touchEvent = touchClass;
	}
	
}
