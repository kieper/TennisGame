package game.tennis;

public class Table {
	private static int x,y;
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

	public static void setWIDTH(double wIDTH) {
		WIDTH = wIDTH;
	}

	public static void setHEIGHT(double hEIGHT) {
		HEIGHT = hEIGHT;
	}
	
	
}
