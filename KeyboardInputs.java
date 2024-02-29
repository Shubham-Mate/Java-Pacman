import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;
import java.awt.Graphics;

public class KeyboardInputs implements KeyListener {
    Player p;
    GamePanel g;
    public KeyboardInputs (Player q, GamePanel gp) {
        this.p = q;
        this.g = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (this.g.getGameState()) {
        case "level":
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    this.p.setSpeed(new int[]{0, -1});
                    System.out.println('W');
                    break;
                
                case KeyEvent.VK_S:
                    this.p.setSpeed(new int[]{0, 1});
                    break;

                case KeyEvent.VK_A:
                    this.p.setSpeed(new int[]{-1, 0});
                    break;

                case KeyEvent.VK_D:
                    this.p.setSpeed(new int[]{1, 0});
                    break;
                
            }
            break;
        case "menu" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                this.g.setGameState("starting");
            }
            break;
        case "game over" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                this.g.setGameState("menu");
            }
            break;
    }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}