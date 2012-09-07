/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis.draw;

import android.util.Log;

/**
 *
 * @author Kieper
 */
public class Speed extends Coordinates{

	private static final long serialVersionUID = 5717125320351063781L;
	private double xSpeed = 0; // x pixels per second
    private double ySpeed = 0; //y pixels per second
    private double xAccel = 0; // acceleration in X
    private double yAccel = 0; // acceleration in Y
    private double xSpeedConstraint = 20;
    private double ySpeedConstraint = 20;
    private long oldTime = System.currentTimeMillis();
    private long newTime;
    @SuppressWarnings("unused")
	private final String TAG = this.getClass().getSimpleName();
    private boolean D = false;
    
    public Speed(){    	
    }
    
    public Speed(double speedXY) {
        xSpeed = ySpeed = speedXY;
    }

    public Speed(double xSpeed, double ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }
   
    public Speed(double xSpeed, double ySpeed, Coordinates coordinates) { 
    	this.xSpeed  = xSpeed;
    	this.ySpeed = ySpeed;
    	this.setX(coordinates.getX());
    	this.setY(coordinates.getY());
    }

    /**
     * changes direction in X-axis 
     */
    public void toggleXDirection() {
    	setXSpeed(-getXSpeed());
    }

    /**
     * changes direction in X-axis and change X-axis speed value 
     */
    public void toggleXDirection(double xSpeed){
    	if(this.xSpeed >= 0){ this.xSpeed = -xSpeed;}
    	else{this.xSpeed = xSpeed;}
    }

    /**
     * changes direction in Y-axis and change Y-axis speed value 
     */
    public void toggleYDirection(double ySpeed){
    	if(this.ySpeed >= 0){ this.ySpeed = -ySpeed;}
    	else{this.ySpeed = ySpeed;}
    }
    
    /**
     * changes direction in Y-axis 
     */
    public void toggleYDirection() {
    	setYSpeed(-getYSpeed());
    }

    /** 
     * Returns x-axis speed value
     * @return x-axis speed value
     */
    public double getXSpeed() {
    	return xSpeed;
    }

    /**
     * Sets new x-axis speed value
     * @param xSpeed new x-axis speed value
     */
    public void setXSpeed(double xSpeed) {
    	this.xSpeed = xSpeed;
    	if(D)Log.d("Speed" , "TOGGLED X");
    }

    /**
     * return y-axis speed value
     * @return y-axis speed
     */
    public double getYSpeed() {
        return ySpeed;
    }
    
    /**
     * Sets new y-axis speed value
     * @param ySpeed new y-axis speed value
     */
    public void setYSpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    /**
     * Sets new x-axis and y-axis speed value
     * @param xSpeed new x-axis speed value
     * @param ySpeed new y-axis speed value
     */
    public void setSpeed(double xSpeed, double ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

	public void setData(double x, double y, double xSpeed, double ySpeed, long time){
		this.setX(x);
		this.setY(y);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.oldTime = time;
	}

	public long getTime(){
		return oldTime;
	}

    /**
     *	Updates Coordinates, based on time passed from previous execution and parameters like 
     *	axis speed or acceleration. 
     */
    public void UpdateXYPosition(){
        UpdateXYPosition(false, false);
    }
    /**
     *	Updates Coordinates, based on time passed from previous execution and parameters like 
     *	axis speed or acceleration. 
     */
    public void UpdateXYPosition(boolean bounceX, boolean bounceY){
    	newTime = System.currentTimeMillis();
    	double diff = (double) (newTime - oldTime);
    	diff /= 50;
    	double mx = setX( (getX() +(getXSpeed()*diff+xAccel*(diff*diff)/2)) );
    	double my = setY( (getY() +(getYSpeed()*diff+yAccel*(diff*diff)/2)) );
    	if(bounceX && mx != 0){
    		setX(getX() + mx);
    		toggleXDirection();
    	}
    	if(bounceY && my != 0){
    		setY(getY() + my);
    		toggleYDirection();
    	}
    	
    	double yNewSpeed = getYSpeed() + yAccel*diff;
    	double xNewSpeed = getXSpeed() + xAccel*diff;
    	//double yNewSpeed = yAccel*10;
    	//double xNewSpeed = xAccel*10;
    	
    	if(xNewSpeed == 0 || Math.abs(xNewSpeed) < Math.abs(xSpeedConstraint)){ setXSpeed( xNewSpeed );}    	
    	else if(xNewSpeed < 0) { setXSpeed(-this.xSpeedConstraint);}
    	else if(xNewSpeed > 0) { setXSpeed(this.xSpeedConstraint);}
    	
    	if(yNewSpeed == 0 || Math.abs(yNewSpeed) < Math.abs(ySpeedConstraint)) setYSpeed( yNewSpeed );    	
    	else if(yNewSpeed < 0) { setYSpeed(-this.ySpeedConstraint);}
    	else if(yNewSpeed > 0) { setYSpeed(this.ySpeedConstraint);}
    	oldTime = newTime; 	
    }

    /**
     * Sets new x-axis acceleration
     * @param xAccel new x-axis acceleration
     */
    public void setAcclerationX(double xAccel){
    	this.xAccel = xAccel;
    }
    
    /**
     * Sets new y-axis acceleration
     * @param yAccel new y-axis acceleration
     */
    public void setAccelerationY(double yAccel){
    	this.yAccel = yAccel;
    }

    /**
     * Sets constraints on max value of speed
     * @param xCon x-axis speed constraint value
     * @param yCon y-axis speed constraint value
     */
    public void setXYSpeedConstraint(double xCon, double yCon){
    	this.xSpeedConstraint = xCon;
    	this.ySpeedConstraint = yCon;
    }
    
    @Override
    public String toString() {
        return "Speed: x: " + xSpeed + " | y: " + ySpeed + " | xDirection: " ;
    }
    

}
