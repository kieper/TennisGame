package game.tennis;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class HomeActivity extends Activity {

    /** Called when the activity is first created. */
    Button playBtn;
    Button multiBtn;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        playBtn = (Button) findViewById(R.id.btnPlay);
        playBtn.setOnClickListener(new onPlayClick(this));

        multiBtn = (Button) findViewById(R.id.multiBtn);
        multiBtn.setOnClickListener(new onMultiClick(this));



    }

    private class onPlayClick implements OnClickListener {

        private Activity activity;

        public void onClick(View v) {
            Intent intent = new Intent(activity, GameActivity.class);
            intent.putExtra(PreConfig.PLAYER.toString(), PlayerType.PLAYER1.toString());
            intent.putExtra(PreConfig.COMM_TYPE.toString(), CommunicationType.NONE.toString());
            startActivity(intent);
            activity.finish();
        }

        public onPlayClick(Activity activity) {
            this.activity = activity;
        }
    }
    
    private class onMultiClick implements OnClickListener {

        private Activity activity;

        public void onClick(View v) {
            Intent intent = new Intent(activity, MultiActivity.class);
            startActivity(intent);
            activity.finish();
        }

        public onMultiClick(Activity activity) {
            this.activity = activity;
        }
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        // Call super code so we dont limit default interaction
        super.onKeyDown(keyCode, event);

        return true;
    }
}
