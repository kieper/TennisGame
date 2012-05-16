package game.tennis;

import java.util.HashMap;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Packet implements Parcelable {		
	public final String TAG = this.getClass().getSimpleName();
	private HashMap<Keys,Point> Data;
	private PlayerType playerType;
	
	public Packet(int xp, int yp, int xb, int yb){
		Data = new HashMap<Keys,Point>();		
		Data.put(Keys.PLAYER, new Point(xp, yp));
		Data.put(Keys.BALL, new Point(xb, yb));		
	}
	
	public Packet(GameData gameData, PlayerType playerType){
		Data = new HashMap<Keys,Point>();
		Data.put(Keys.PLAYER, new Point((int) gameData.getPlayer(playerType).getSpeed().getX(), (int) gameData.getPlayer(playerType).getSpeed().getY()));
		Data.put(Keys.BALL, new Point((int) gameData.getBall().getSpeed().getX(), (int) gameData.getBall().getSpeed().getX()));			
	}
	
    public Packet(Parcel source){
    	Data = new HashMap<Keys,Point>();    	
    	for(Keys k:Keys.values()){
        	Data.put(k, new Point(source.readInt(), source.readInt()));
        }
   }

	public void setGameData(GameData gameData){
		Point p = Data.get(Keys.PLAYER); 
		PlayerType pt;
		if(playerType == PlayerType.PLAYER1){
			pt = PlayerType.PLAYER2;
		}else{
			pt = PlayerType.PLAYER1;
		}
		gameData.getPlayer(pt).getSpeed().setXY(p.x, p.y);
		p = Data.get(Keys.BALL);
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
	      }
	}
	
	private enum Keys{
		PLAYER, BALL
	}
}
