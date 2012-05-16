package game.tennis;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Ball implements Graphic {
	private Speed speed;
	private Paint paint;
	private int radius = 5;
	private final double NORMAL_SPEED = 5;
	private final double beta;
	private final String TAG = this.getClass().getSimpleName();
	private double alpha = 0, b;
	
	public Ball(Background background) {
		Coordinates coordinates = new Coordinates(background.getPlayer1Rect().right, background.getPlayer1Rect().bottom / 2);
		speed = new Speed(NORMAL_SPEED, 0, coordinates);
		paint = new Paint();
		b = speed.getY() - alpha*speed.getX();		
		beta = background.TABLE_HEIGTH /(double) background.TABLE_WIDTH;
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.WHITE);		
	}

	public void draw(Canvas canvas) {
		speed.UpdateXYPosition(alpha, b);
		canvas.drawCircle(Math.round(speed.getX()), Math.round(speed.getY()), radius, paint);
		Log.d(TAG, "drawBall position (" + speed.getX() + ", " + speed.getY()
				+ ")");
	}

	public void move(double ratio) {
		Log.d(TAG, "got : " + ratio);
		if(ratio >= 0 && ratio <= 1 ){
			alpha = Math.abs(2* beta * ratio - beta);
			if (ratio < 0.49 || ratio > 0.51) {
					//double sy = Math.sqrt((NORMAL_SPEED * NORMAL_SPEED)/ (1 + beta * beta));
					//double sx = (beta * sy);
					//speed.toggleXDirection(sx);
					//if(ratio >= 0.51) speed.setYSpeed(-sy);
					//if(ratio <= 0.49) speed.setYSpeed(sy);
			b = speed.getY() - alpha*speed.getX();		
			speed.toggleXDirection();
		/*	}else{
				speed.setYSpeed(0);
				speed.toggleXDirection();
			}*/
			}
			
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

}
