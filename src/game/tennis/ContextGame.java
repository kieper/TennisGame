package game.tennis;

import android.app.Application;
import android.content.Context;


public class ContextGame extends Application {

    private static volatile Context context;

    public void onCreate(){
        super.onCreate();
        ContextGame.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ContextGame.context;
    }
}
