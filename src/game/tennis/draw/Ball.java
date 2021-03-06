package game.tennis.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * @author Kieper
 *
 */


public class Ball implements Graphic {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8629159685210672332L;
	private Speed speed;
	private Paint paint;
	private int radius = 5;
	
	private final double NORMAL_SPEED = 20;
	private final boolean D = false;
	
	private final String TAG = this.getClass().getSimpleName();
	
	public Ball(Background background) {
		Coordinates coordinates = new Coordinates(background.getPlayer2Rect().centerX(), background.getPlayer2Rect().centerY());
		speed = new Speed(NORMAL_SPEED, 0, coordinates);
		speed.toggleXDirection();
		speed.setConstraint(background.getTableRect());
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.WHITE);		
	}

	public void draw(Canvas canvas) {
		speed.UpdateXYPosition(false, true);
		canvas.drawCircle(Math.round(speed.getX()), Math.round(speed.getY()), radius, paint);
		if(D)Log.d(TAG, "drawBall position (" + speed.getX() + ", " + speed.getY()
				+ ")");
	}

	public void move(double ratio) {
		if(D)Log.d(TAG, "got : " + ratio);
		if(ratio >= 0 && ratio <= 1 ){
			double alfa = ((0.5-ratio) * Math.PI);
			if(speed.getXSpeed() > 0){
				alfa = - alfa + Math.PI; //alfa: -pi/2; 3/2pi
			}

			speed.setYSpeed( Math.sin(alfa)*NORMAL_SPEED );
			speed.setXSpeed( Math.cos(alfa)*NORMAL_SPEED );
		}
	}
	@Override
	public Speed getSpeed() {
		return speed;
	}

	@Override
	public int getWidth() {
		return 2 * radius;
	}

	@Override
	public int getHeight() {
		return 2 * radius;
	}

	@Override
	public Rect getRect() {
		return new Rect((int)(speed.getX()-radius), (int) (speed.getY()-radius), 
				(int)(speed.getX()+radius), (int) (speed.getY()+radius));
	}

	@Override
	public byte getType() {
		return (byte) GraphicTypes.Ball.ordinal();
	}

	public String toString(){
		return TAG + " x = " + getSpeed().getX() + " y = " + getSpeed().getY();    	
    }
}
