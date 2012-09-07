package game.tennis.draw;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface Graphic extends Serializable{
	public void draw(Canvas canvas);
	public Speed getSpeed();
	public int getWidth();
	public int getHeight();
	public Rect getRect();
	public void move(double ratio);
	public byte getType(); //return type 1 - player, 2 - ball
	public String toString();
}
