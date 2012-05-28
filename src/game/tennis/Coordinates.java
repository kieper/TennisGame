/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import java.io.Serializable;

import android.graphics.Rect;

/**
 *
 * @author Kieper
 */
public class Coordinates implements Serializable{
        /**
	 * 
	 */
	private static final long serialVersionUID = 1294596921394570899L;
		private double x = 0;
        private double y = 0;
        private Rect constraint = null;

        public Coordinates(){

        }

        public Coordinates( double x, double y){
            setX(x);
            setY(y);
        }
        
        public double getX() {
            return x;
        }

        public void setX(double d) {
            x = d;
            if(constraint != null){
            	if(x < constraint.left) x = constraint.left;
            	if(x > constraint.right) x = constraint.right;
            }
        }

        public double getY() {
            return y;
        }

        public void setY(double value) {
            y = value;
            if(constraint != null){
            	if(y < constraint.left) y = constraint.top;
            	if(y > constraint.right) y = constraint.bottom;
            }
        }

        public void setXY(double x, double y){
            setY(y);
            setX(x);
        }

        public void updateXY(double x, double y){
            setY(y + getY());
            setX(x + getX());
        }

        public void setConstraint(Rect constraint){
        	this.constraint = constraint;
        }
        
        @Override
        public String toString() {
            return "Coordinates: (" + x + "," + y + ")";
        }
}
