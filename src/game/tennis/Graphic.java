package game.tennis;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface Graphic {
	public void draw(Canvas canvas);
	public Speed getSpeed();
	public int getWidth();
	public int getHeight();
	public Rect getRect();
	public void move(double ratio);
}
