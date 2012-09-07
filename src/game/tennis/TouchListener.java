package game.tennis;

import java.util.ArrayList;

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
	
	//Singleton instance of this class(thread safe)
	private static TouchListener touchListener = new TouchListener();
	
	//List of objects implementing TouchEvent interface
	private ArrayList<TouchEvent> touchEventList = new ArrayList<TouchEvent>();
	
	//Debuging Tag
	private String TAG = "TouchListener";
	
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
		Log.e(TAG, "Touch in listener status of touchevent = " );
		if(!touchEventList.isEmpty()){
			for(TouchEvent t:touchEventList){
				t.touch(event);
			}
		}
		return false;
	}

	/**
	 * Adds to list object for which touch method will be used
	 * @param touchClass class implementing TouchEvent interface
	 */
	public void addTouchEvent(TouchEvent touchClass){
		touchEventList.add(touchClass);
	}
	
	/**
	 * Remove object from list
	 * @param touchClass class implementing TouchEvent interface
	 */
	public void removeTouchEvent(TouchEvent touchClass){
		touchEventList.remove(touchClass);
	}
}
