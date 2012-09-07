package game.tennis.draw;

import game.tennis.GameData;

public class Table {
	private static double WIDTH = 880;
	private static double HEIGHT = 420;
	
	public static void convertToTableCordinates(Graphic graphic){
		graphic.getSpeed().setX(graphic.getSpeed().getX()/WIDTH);
		graphic.getSpeed().setY(graphic.getSpeed().getY()/HEIGHT);
	}
	
	public static void convertToDisplayCordinates(Graphic graphic){
		graphic.getSpeed().setX(graphic.getSpeed().getX()*WIDTH);
		graphic.getSpeed().setY(graphic.getSpeed().getY()*HEIGHT);
	}
	
	public static void actualizeDisplayMetrics(){
		WIDTH = GameData.getInstance().getDisplayMaterics().getWidth();
		HEIGHT = GameData.getInstance().getDisplayMaterics().getHeight();
	}
}
