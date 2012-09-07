/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;


import game.tennis.draw.PlayerType;
import tcpip.communication.game.CommunicationType;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * It is main activity of the game, view of this activity does most of game related things.
 * @author Kieper
 */
public class GameActivity extends Activity {

	private final String TAG = this.getClass().getSimpleName();
    private static final boolean D = true;
	protected PowerManager.WakeLock wakeLock;
    
    private GameView gameView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();

        CommunicationType commType = null;
        PlayerType playerType = null;
        String ip = null;
        Bundle bundle = getIntent().getExtras();
        
        
        
        try{
        	commType = CommunicationType.valueOf((String) bundle.get(PreConfig.COMM_TYPE.toString())); 
        	playerType = PlayerType.valueOf((String) bundle.get(PreConfig.PLAYER.toString()));
        	ip = (String) bundle.get(PreConfig.IP.toString());
        	if(D) Log.d(TAG, "playertype 1 : "+ playerType.toString());	
        } catch (NullPointerException exception){
        	Log.e(TAG, "Error while specifying communication type");	
        } 
        
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.wakeLock.acquire();
        
        Log.d(TAG, "Creating GameView");
        gameView = new GameView(this, display, playerType, commType, ip); 
        setContentView(gameView);
        
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            
            
            //TODO show some acceptance screen
            final AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setIcon(android.R.drawable.ic_dialog_alert);
            b.setTitle("Attention");
            b.setMessage("Are you sure you want to leave?");
            b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(GameActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();                    
                }
                
            });
            b.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel();
                }
            });
            
            b.show();
            
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

    @Override
    public void finish(){
        if(gameView != null){
            gameView.dispose();           
        }
        super.finish();
    }
    
    
}
