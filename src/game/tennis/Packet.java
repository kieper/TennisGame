package game.tennis;

import java.util.HashMap;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 
 * @author Kieper
 * In packet there is data about ball position(from server), and player position, these data is sent
 * to opponent via other classes.
 */
public class Packet implements Parcelable {		
	public final String TAG = this.getClass().getSimpleName();
	private HashMap<Keys,Point> Data;
	private PlayerType playerType;
	
	/**
	 * Creates new Packet object
	 * @param xp player x position
	 * @param yp player y position
	 * @param xb ball x position
	 * @param yb ball y position
	 */
	public Packet(int xp, int yp, int xb, int yb){
		Data = new HashMap<Keys,Point>();		
		Data.put(Keys.PLAYER, new Point(xp, yp));
		Data.put(Keys.BALL, new Point(xb, yb));		
	}
	
	public Packet(GameData gameData, PlayerType playerType){
		this.playerType = playerType;
		Data = new HashMap<Keys,Point>();
		Data.put(Keys.PLAYER, new Point((int) gameData.getPlayer(playerType).getSpeed().getX(), (int) gameData.getPlayer(playerType).getSpeed().getY()));
		Data.put(Keys.BALL, new Point((int) gameData.getBall().getSpeed().getX(), (int) gameData.getBall().getSpeed().getX()));			
	}
	
    public Packet(Parcel source){
    	Data = new HashMap<Keys,Point>();    	
    	for(Keys k:Keys.values()){
    		int x = source.readInt();
    		Log.d(TAG, "Read data x = " + x + " data position = " + source.dataPosition() );
    		int y = source.readInt();
    		Log.d(TAG, "Read data y = " + y + " data position = " + source.dataPosition() );
    		
        	Data.put(k, new Point(x, y));		      
        }
    	String player = source.readString();
    	Log.d(TAG, "Read data player =  " + player + " data position " + source.dataPosition());
		
    	if(PlayerType.PLAYER1.toString() == player){
    		playerType = PlayerType.PLAYER1;
    	}else{
    		playerType = PlayerType.PLAYER2;
    	}
   }

	public void setPlayerData(GameData gameData){
		Point p = Data.get(Keys.PLAYER); 
		gameData.getPlayer(playerType).getSpeed().setXY(p.x, p.y);
	}

	public void setBallData(GameData gameData){
		Point p = Data.get(Keys.BALL);
		gameData.getBall().getSpeed().setXY(p.x, p.y);
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	      Log.d(TAG, "writeToParcel..."+ flags);
	      for(Point i:Data.values()){
	    	  dest.writeInt(i.x);
	      	  dest.writeInt(i.y);
	      	  Log.d(TAG, "Written data x= "+ i.x + " y =" + i.y);		      
	      }
	      if(playerType != null) dest.writeString(playerType.toString());
      	  Log.d(TAG, "Written data playertype = " + playerType.toString());		      

	}
	
	private enum Keys{
		PLAYER, BALL
	}
}
