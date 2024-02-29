import javax.swing.JFrame;
import java.awt.*;

class Game {

    private int FPS = 120;

    public static void main(String[] args) {
        GamePanel panel = new GamePanel();
        GameWindow window = new GameWindow("Pacman", panel);

        panel.requestFocus();

        panel.startGameThread();
    }
}