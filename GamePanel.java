import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{


    private int FPS = 120;
    private Player p1 = new Player(0, 0, 10, 10, "pacman_animation.png");
    private int animTick = 0, animSpeed = 15, animIndex = 0;
    
    public GamePanel () {
        this.setFrameSize();
        setBackground(Color.BLACK);
        setOpaque(true);
        addKeyListener(new KeyboardInputs(p1));
        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[] pos = p1.getPosition();
        updateAnimationTick();
        g.drawImage(p1.getSprite()[animIndex], pos[0], pos[1], null);
        

    }

    private void updateAnimationTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
        }

        animIndex %= 4;
    }

    private void setFrameSize() {
        Dimension d = new Dimension(1280, 720);
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }

    public void startGameThread () {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS;
        double last = System.nanoTime();

        while (true) {
            double now = System.nanoTime();
            if (now - last >= timePerFrame) {
                p1.move();
                repaint();
                last = now;
            }
        }
    }
}
