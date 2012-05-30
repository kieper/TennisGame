package game.tennis;


public class CommThread extends Thread {

    private GameView panel;
    private boolean run = false;

    @SuppressWarnings("unused")
	private final String TAG  = this.getClass().getSimpleName();
    
    public CommThread(GameView panel) {
        this.panel = panel;
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    public void run() {

        while (run) {
        	panel.transferData();
        }
    }
}
