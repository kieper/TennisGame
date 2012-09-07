package tcpip.communication.game;

import game.tennis.GameData;
import game.tennis.GameView;
import game.tennis.draw.GraphicTypes;
import game.tennis.draw.PlayerType;
import game.tennis.draw.Speed;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;


/**
 * 
 * @author Kieper In packet there is data about ball position(from server), and player position, these data is sent to opponent
 *         via other classes. First element of ArrayList is graphic of Ball, second is Player.
 */
public class Packet implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2050680319755447574L;

    public final String TAG = this.getClass().getSimpleName();

    private ArrayList<Buffer> data;

    private PlayerType playerType;

    long timeCorrection;


    /**
     * Creates instance of packet
     * 
     * @param gameData
     *            From this class data to send is taken
     * @param playerType
     *            Type of player on current device
     */
    public Packet(GameData gameData, PlayerType playerType) {
        this.playerType = playerType;
        this.timeCorrection = 0;
        Speed ballSpeed = gameData.getBall().getSpeed();
        Speed playerSpeed = gameData.getPlayer(playerType).getSpeed();
        data = new ArrayList<Buffer>();
        data.add(new Buffer(ballSpeed.getX(), ballSpeed.getY(), ballSpeed.getXSpeed(), ballSpeed.getYSpeed(),
                gameData.getBall().getType(), ballSpeed.getTime()));
        data.add(new Buffer(playerSpeed.getX(), playerSpeed.getY(), playerSpeed.getXSpeed(), playerSpeed.getYSpeed(),
                gameData.getPlayer(playerType).getType(), playerSpeed.getTime()));
    }


    public Packet(ArrayList<Buffer> data, PlayerType playerType) {
        this.playerType = playerType;
        this.data = data;
    }


    public void setTimeCorrection(long timeCorrection) {
        this.timeCorrection = timeCorrection;
    }


    public void setPlayerData(GameData gameData) {
        for (Buffer g : data) {
            if (g.type == GraphicTypes.Player.ordinal()) {
                gameData.getPlayer(playerType).getSpeed().setData(g.x, g.y, g.vx, g.vy, g.time + timeCorrection);
                gameData.getPlayer(playerType).getSpeed().UpdateXYPosition();
                return;
            }
        }
    }


    public void setBallData(GameData gameData) {
        for (Buffer g : data) {
            if (g.type == GraphicTypes.Ball.ordinal()) {
                if (gameData.getPlayer(playerType).getSideRect().contains((int) Math.round(g.x), (int) Math.round(g.y))) {

                    Speed ballSpeed = gameData.getBall().getSpeed();
                    ballSpeed.setData(g.x, g.y, g.vx, g.vy, g.time + timeCorrection);
                    ballSpeed.UpdateXYPosition(false, true);
                    return;
                }
            }
        }
    }


    public void send(ObjectOutputStream oos) throws IOException {
        oos.writeByte(data.size()); // write how much elements will be sent
        oos.flush();
        for (Buffer g : data) {
            oos.writeObject(g); // write all elements from array list
            oos.flush();
        }

    }


    public static Packet creatorPacket(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ArrayList<Buffer> data = new ArrayList<Buffer>();
        String TAG = "Packet.CreatorPacket"; // create tag(need to be done manualy because of static)
        PlayerType playerType = GameView.getOppositePlayer(); // set playerType to type of opponent


        int n = 0;

        n = ois.readByte(); // how many objects to read?


        Log.d(TAG, " elements to read is " + n);
        data.clear();
        for (int i = 0; i < n; i++) {
            Buffer g = null;

            g = (Buffer) ois.readObject(); // read data sent from other device

            if (g != null) {
                data.add(g);
                Log.d(TAG, g.toString());
            }
            else {
                Log.d(TAG, "null packet");
            }
        }


        Log.d(TAG, "Done ");
        return new Packet(data, playerType);
    }


    public String toString() {
        String txt = null;
        for (Buffer g : data) {
            txt += g.toString() + " ";
        }
        return txt;
    }


    /**
     * @author Kieper This class has all variables to create object whose ancestor is Graphic interface
     */
    private class Buffer implements Serializable {

        private static final long serialVersionUID = -5394687784242022095L;

        private double x, y;

        private double vx, vy;

        private long time; // czas w ktorym pakiet zostal wyslany (wedlug czasu drugiego urzadzenia)

        private byte type;


        private Buffer(double x, double y, double vx, double vy, byte type, long time) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.type = type;
            this.time = time;
        }


        public String toString() {
            return "x = " + x + " y = " + y + " vx = " + vx + " vy = " + vy + " type = " + type;
        }

    }
}
