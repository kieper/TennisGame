package game.tennis;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MultiActivity extends Activity {

	Button btBtn;
    Button wifiBtn;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.comm_type_layout);

        btBtn = (Button) findViewById(R.id.btBtn);
        btBtn.setOnClickListener(new onBtClick(this));

        wifiBtn = (Button) findViewById(R.id.wifiBtn);
        wifiBtn.setOnClickListener(new onWifiClick(this));
    }
    
    private class onWifiClick implements OnClickListener {

        private Activity activity;

        public void onClick(View v) {
            Intent intent = new Intent(activity, HostTypeActivity.class);
            intent.putExtra(PreConfig.COMM_TYPE.toString(), CommunicationType.WIFI.toString());
            startActivity(intent);
            activity.finish();
        }

        public onWifiClick(Activity activity) {
            this.activity = activity;
        }
    }
    
    private class onBtClick implements OnClickListener {

        private Activity activity;

        public void onClick(View v) {
            Intent intent = new Intent(activity, HostTypeActivity.class);
            intent.putExtra(PreConfig.COMM_TYPE.toString(), CommunicationType.BLUETOOTH.toString());
            startActivity(intent);
            activity.finish();
        }

        public onBtClick(Activity activity) {
            this.activity = activity;
        }
    }

}
