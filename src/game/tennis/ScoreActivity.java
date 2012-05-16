/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 * @author Kieper
 */
public class ScoreActivity extends Activity {

    ScoreView scoreView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        scoreView = new ScoreView(this);
        setContentView(scoreView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ScoreActivity.this, HomeActivity.class);
             scoreView.getScoreThread().setRunning(false);
             startActivity(intent);
             this.finish();
        }

        // Call super code so we dont limit default interaction
        super.onKeyDown(keyCode, event);

        return true;
    }
}
