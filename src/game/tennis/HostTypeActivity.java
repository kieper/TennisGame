package game.tennis;

import game.tennis.draw.PlayerType;
import tcpip.communication.game.CommunicationType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HostTypeActivity extends Activity {

    Button hostBtn;
    Button joinBtn;
    CommunicationType commType;
    String TAG = this.getClass().getSimpleName();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.host_type);

        hostBtn = (Button) findViewById(R.id.hostBtn);
        hostBtn.setOnClickListener(new onHostClick(this));

        joinBtn = (Button) findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new onJoinClick(this));
        
        Bundle bundle = getIntent().getExtras();
        try{
        	commType = CommunicationType.valueOf((String) bundle.get(PreConfig.COMM_TYPE.toString()));  
        	Log.d(TAG, "Recived Comm type is " + commType);
        }    	
        catch (NullPointerException exception){
        	Toast.makeText(this,"Can not specify communication type", Toast.LENGTH_LONG).show();
        	Log.e(TAG, "Error while specifying communication type");	
        }
    }

    private class onJoinClick implements OnClickListener {

        private Activity activity;

        public void onClick(View v) {
        	Intent intent;
        	if(commType == CommunicationType.BLUETOOTH){
        		intent = new Intent(activity, BtActivity.class);
        	}else{
        		intent = new Intent(activity, WifiActivity.class);            	
        	}
            intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER1.toString());
            intent.putExtra(PreConfig.COMM_TYPE.toString(), commType.toString());
            startActivity(intent); 
            activity.finish();
            
        }

        public onJoinClick(Activity activity) {
            this.activity = activity;
        }
    }
    
    private class onHostClick implements OnClickListener {

        private Activity activity;

        public void onClick(View v) {
        	Intent intent;
        	if(commType == CommunicationType.BLUETOOTH){
        		intent = new Intent(activity, BtActivity.class);
        	}else{
        		intent = new Intent(activity, WifiActivity.class);            	
        	}
        	intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER2.toString());
            intent.putExtra(PreConfig.COMM_TYPE.toString(), commType.toString());
            startActivity(intent);
            activity.finish();
        }

        public onHostClick(Activity activity) {
            this.activity = activity;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, HomeActivity.class );
            startActivity(intent);
            this.finish();
        }
        // Call super code so we dont limit default interaction
        super.onKeyDown(keyCode, event);

        return true;
    } 
    
}
