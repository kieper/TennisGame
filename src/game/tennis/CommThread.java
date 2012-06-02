package game.tennis;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class CommThread extends Thread {

    private GameView panel;
    private boolean run = false;
    private Context context;
    private Communication comm;
    private GameData gameData;
    private boolean connected = false;
    
	private final String TAG  = this.getClass().getSimpleName();
    
    public CommThread(GameView panel, Context context, Communication comm, GameData gameData) {
        this.panel = panel;
        this.context = context;
        this.gameData = gameData;
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    public void run() {
    	initialize();
    	
        while (run) {
        	panel.transferData();
        }
    }
    
    public void initialize(){
        Log.d(TAG, "Player set to " + GameView.getPlayerType());
    	switch( GameView.getCommunicationType()){
    		case WIFI:
    			Log.d(TAG, "Starting WIFI communication");
    			comm = new WifiCommunication(context, GameView.getPlayerType());
    			break;        		
    		case NONE:
    			Log.d(TAG, "Starting new solo communication");
    			comm = new SoloCommunication(gameData, PlayerType.PLAYER1);
    			break;
    		case BLUETOOTH:
    		default:
    			Toast.makeText(context,"Can not specify communication type", Toast.LENGTH_LONG).show();
    			throw new  UnsupportedOperationException();
    	}

    
    	if(comm == null){
    		GameData.getInstance().changeDrawing(2);
    		DrawMsg.drawMsg("Nie udalo sie nawiazac polaczenia", 2000);
    	}
    	
    	panel.setCommunication(comm);    
    	connected = true;
    }
    
    public boolean isConnected(){
    	return connected;
    }
}
