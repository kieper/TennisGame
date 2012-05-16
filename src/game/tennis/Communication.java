package game.tennis;

public interface Communication {
	void sendData(Packet packet);
	Packet reciveData();
}
