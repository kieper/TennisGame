package game.tennis;

public class SoloCommunication implements Communication{

	private GameData gameData;
	private PlayerType playerType;
	
	
	/**
	 * Creates new instance of SoloCommunication class
	 * @param gameData GameData object
	 * @param playerType Player type of the opponent(this player moves will be simulates)
	 */
	public SoloCommunication(GameData gameData, PlayerType playerType){
		this.gameData = gameData;
		this.playerType = playerType;
	}
	
	@Override
	public void sendData(Packet packet) {
		try {
			Thread.sleep(100);//give me some sleep:)
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		simulatePlayer(packet);
	}

	@Override
	public Packet reciveData() {
		return new Packet(gameData, playerType);		
	}

	private void simulatePlayer(Packet packet){
		
		Player player = gameData.getPlayer(playerType);
		Ball ball = gameData.getBall();
		player.getSpeed().setYSpeed(0.3*(ball.getSpeed().getY() - player.getSpeed().getY()));
		
	}
}
