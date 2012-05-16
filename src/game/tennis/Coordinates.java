/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.tennis;

import android.graphics.Rect;

/**
 *
 * @author Kieper
 */
public class Coordinates {
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

        public void setXY(int x, int y){
            setY(y);
            setX(x);
        }

        public void updateXY(int x, int y){
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
