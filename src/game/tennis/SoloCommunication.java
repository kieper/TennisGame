package game.tennis;

public class SoloCommunication implements Communication{

	private Packet packet;
	
	@Override
	public void sendData(Packet packet) {
		this.packet = packet;
	}

	@Override
	public Packet reciveData() {
		
		return null;
	}

}
