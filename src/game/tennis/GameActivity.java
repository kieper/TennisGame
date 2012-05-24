/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 * @author Kieper
 */
public class GameActivity extends Activity {

	private final String TAG = this.getClass().getSimpleName();
	protected PowerManager.WakeLock wakeLock;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();

        CommunicationType commType = null;
        PlayerType playerType = null;
        Bundle bundle = this.getIntent().getExtras();
        try{
        	commType = CommunicationType.valueOf((String) bundle.get(PreConfig.COMM_TYPE.toString())); 
        	playerType = PlayerType.valueOf((String) bundle.get(PreConfig.PLAYER.toString()));
        } catch (NullPointerException exception){
        	Log.e(TAG, "Error while specifying communication type");	
        } 
        
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.wakeLock.acquire();
        
        Log.d(TAG, "Creating GameView");
        setContentView(new GameView(this, display, playerType, commType));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }

        // Call super code so we dont limit default interaction
        super.onKeyDown(keyCode, event);

        return true;
    }
    
    @Override
    public void onDestroy() {
        this.wakeLock.release();
        super.onDestroy();
    }
    
}
