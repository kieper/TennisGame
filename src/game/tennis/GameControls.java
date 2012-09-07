package game.tennis;

import game.tennis.draw.Controls;
import game.tennis.draw.Player;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GameControls implements Controls{
	private SensorManager sensorManager;
	private float accelX, accelY, accelZ;
	private float calibration = 0;
	private final double RATIO = 3;
	
	public GameControls(Context context){
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		//sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		sensorManager.registerListener(accelerationListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        
	}
	
	
	private SensorEventListener accelerationListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int acc) {
		}
 
		@Override
		public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {            	
                accelX = event.values[0];
    			accelY = event.values[1];
    			accelZ = event.values[2];
            }					
		} 
	};
	
	public void controlPlayer(Player player){
		if(accelX  > calibration-0.15 && accelX < calibration+0.15){ //strefa nieczuloœci 10%
			player.move(0);
		}else{
			player.move((accelX-calibration)/RATIO);
		}
	}
	
	public void dispose(){
		sensorManager.unregisterListener(accelerationListener);
	}
	
	public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(35);
        
        canvas.drawText("x " + accelX, 0, 60, p);
        canvas.drawText("y " + accelY, 0, 95, p);
        canvas.drawText("z : " + accelZ, 0, 130, p);

	}

	@Override
	public void callibrate(float calibration) {
		this.calibration = calibration;		
	}
	
}
