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

        public double setX(double value) {
            x = value;
            double m=0;
            if(constraint != null){
            	if(x < constraint.left){
            		m = constraint.left - x;
            		x = constraint.left;
            	}
            	if(x > constraint.right){
            		m = constraint.right - x;
            		x = constraint.right;
            	}
            }
            return m;
        }

        public double getY() {
            return y;
        }

        public double setY(double value) {
            y = value;
            double m=0;
            if(constraint != null){
            	if(y < constraint.top){
            		m = constraint.top - y;
            		y = constraint.top;
            	}
            	if(y > constraint.bottom){
            		m = constraint.bottom - y;
            		y = constraint.bottom;
            	}
            }
            return m;
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
