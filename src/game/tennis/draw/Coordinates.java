/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis.draw;

import java.io.Serializable;

import android.graphics.Rect;

/**
 * Class hold data of object coordinates
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
        
        /**
         * returns x-axis value
         * @return
         */
        public double getX() {
            return x;
        }

        /**
         * sets x-axis value
         * @param value
         * @return
         */
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

        /**
         * returns y-axis value
         * @return
         */
        public double getY() {
            return y;
        }

        /**
         * sets Y-axis value
         * @param value
         * @return
         */
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

        /**
         * sets x,y-axis values
         * @param x
         * @param y
         */
        public void setXY(double x, double y){
            setY(y);
            setX(x);
        }

        /**
         * Adds to current values, new values given in parameter
         * @param x how much to add to x-axis
         * @param y how much to add to y-axis
         */
        public void updateXY(double x, double y){
            setY(y + getY());
            setX(x + getX());
        }

        /**
         * Sets Rectangular constraint of object(x,y values cant get out of this rectangle)
         * @param constraint
         */
        public void setConstraint(Rect constraint){
        	this.constraint = constraint;
        }
        
        @Override
        public String toString() {
            return "Coordinates: (" + x + "," + y + ")";
        }
}
