package tcpip.communication.game;

import java.io.Serializable;

public class Ping implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private long time;
	
	public Ping(int id, long time){
		this.id = id;
		this.time = time;
	}
	
	public int getId(){
		return id;
	}
	
	public long getTime(){
		return time;
	}
	
	public String toString(){
		return "id = " + id + " time = "+ time;			
	}
}
