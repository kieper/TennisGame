package game.tennis;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class WifiActivity extends Activity {

    private CommunicationType commType;
    private PlayerType playerType;
	
    // Debugging
    private static final String TAG = "BtActivity";
    private static final boolean D = true;
    
	private Button conBtn;
	private EditText edit;
	private TextView label;
    
	private String ip = null;
	
    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        if(D) Log.e(TAG, "+++ ON CREATE +++");

	        // Set up the window layout
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.wifi_layout);

	        conBtn = (Button) findViewById(R.id.con_btn);
	        edit = (EditText) findViewById(R.id.ip_edit);
	        label = (TextView) findViewById(R.id.ip_label);
	        commType = null;
	        playerType = null;
	        Bundle bundle = this.getIntent().getExtras();
	        try{
	        	commType = CommunicationType.valueOf((String) bundle.get(PreConfig.COMM_TYPE.toString())); 
	        	playerType = PlayerType.valueOf((String) bundle.get(PreConfig.PLAYER.toString()));
	        	if(D)Log.d(TAG, "playertype 1 : "+ playerType.toString());
	        } catch (NullPointerException exception){
	        	Log.e(TAG, "Error while specifying communication type");	
	        }         
	        
	        label.setText("Your ip is: " + WifiCommunication.getLocalIpAddress());
	        
	        conBtn.setOnClickListener(new ConClickListener(this));
	        
	        if(playerType == PlayerType.PLAYER2) edit.setVisibility(TextView.INVISIBLE);

	 }
    
    
    public static final boolean checkIPv4(final String ip) {
        boolean isIPv4;
        try {
        final InetAddress inet = InetAddress.getByName(ip);
        	isIPv4 = inet.getHostAddress().equals(ip) && (inet instanceof Inet4Address);
        } catch (final UnknownHostException e) {
        	isIPv4 = false;
        }
        return isIPv4;
    }
    
    private class ConClickListener implements OnClickListener{

    	private Activity activity;
    	
    	public ConClickListener(Activity act){
    		activity = act;
    	}
    	
		@Override
		public void onClick(View v) {
			if(checkIPv4(edit.getText().toString()) || PlayerType.PLAYER2 == playerType){
				Intent intent = new Intent(activity, GameActivity.class);
            	intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER2.toString());
            	intent.putExtra(PreConfig.COMM_TYPE.toString(), CommunicationType.NONE.toString());
            	intent.putExtra(PreConfig.IP.toString(), ip);
            	startActivity(intent);		
            	activity.finish();
			}
		}
    	
    }    

    
}
