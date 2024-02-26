import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    Player p;
    public KeyboardInputs (Player q) {
        this.p = q;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
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

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}