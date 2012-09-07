package game.tennis;


import game.tennis.draw.PlayerType;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import tcpip.communication.game.CommunicationType;
import tcpip.communication.game.WifiCommunication;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class WifiActivity extends Activity {

    private PlayerType playerType;

    // Debugging
    private static final String TAG = "WifiActivity";

    private static final boolean D = true;

    private Button conBtn;

    private EditText edit;

    private TextView label;

    private String ip = null;

    private RadioButton radioAuto;
    
    private RadioButton radioIp;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D){
            Log.e(TAG, "+++ ON CREATE +++");
        }
        // Set up the window layout
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.wifi_layout);

        conBtn = (Button) findViewById(R.id.con_btn);
        edit = (EditText) findViewById(R.id.ip_edit);
        label = (TextView) findViewById(R.id.ip_label);

        playerType = null;

        Bundle bundle = this.getIntent().getExtras();

        try {
            playerType = PlayerType.valueOf((String) bundle.get(PreConfig.PLAYER.toString()));
            if (D)
                Log.d(TAG, "playertype 1 : " + playerType.toString());
        }
        catch (NullPointerException exception) {
            Log.e(TAG, "Error while specifying communication type");
        }

        label.setText("Your ip is: " + WifiCommunication.getLocalIpAddress());

        conBtn.setOnClickListener(new ConClickListener(this));

        if (playerType == PlayerType.PLAYER2)
            edit.setVisibility(TextView.INVISIBLE);

        radioAuto = (RadioButton) findViewById(R.id.radioAuto);        
        radioIp = (RadioButton) findViewById(R.id.radioIp);
        
        radioIp.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
               edit.setEnabled(true); 
            }
            
        });

        radioAuto.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
               edit.setEnabled(false); 
            }
            
        });

        
    }


    public static final boolean checkIPv4(final String ip) {
        boolean isIPv4;
        try {
            final InetAddress inet = InetAddress.getByName(ip);
            isIPv4 = inet.getHostAddress().equals(ip) && (inet instanceof Inet4Address);
        }
        catch (final UnknownHostException e) {
            isIPv4 = false;
        }
        return isIPv4;
    }


    private class ConClickListener implements OnClickListener {

        private Activity activity;


        public ConClickListener(Activity act) {
            activity = act;
        }


        @Override
        public void onClick(View v) {

            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            // NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            // wifi
            State wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (wifi != NetworkInfo.State.CONNECTED ) {
                if (D){
                    Log.d(TAG, "WIFI is not enabled, turn on wifi");}
                Intent intent = new Intent(WifiActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            
            else {
                if (PlayerType.PLAYER1 == playerType) {

                    if (D){
                        Log.d(TAG, "Validiating ip : " + checkIPv4(edit.getText().toString()));}
                    Toast msg = Toast.makeText(activity, edit.getText().toString(), Toast.LENGTH_LONG);
                    msg.show();
                    if (checkIPv4(edit.getText().toString()) && radioAuto.isChecked()) {
                        ip = edit.getText().toString();
                        if (D){
                            Log.d(TAG, "Validiating ip in if statment: " + edit.getText().toString());}
                        Intent intent = new Intent(activity, GameActivity.class);
                        intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER1.toString());
                        intent.putExtra(PreConfig.COMM_TYPE.toString(), CommunicationType.WIFI.toString());
                        intent.putExtra(PreConfig.IP.toString(), ip);
                        startActivity(intent);
                        activity.finish();
                    }
                }
                else {
                    Intent intent = new Intent(activity, GameActivity.class);
                    intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER2.toString());
                    intent.putExtra(PreConfig.COMM_TYPE.toString(), CommunicationType.WIFI.toString());
                    intent.putExtra(PreConfig.IP.toString(), ip);
                    startActivity(intent);
                    activity.finish();
                }
            }
        }

    }


}
