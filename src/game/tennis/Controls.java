package game.tennis;

import android.graphics.Canvas;

public interface Controls {
	public void controlPlayer(Player player);
	public void dispose();
	public void draw(Canvas canvas);
	public void callibrate(float calibration);
}
