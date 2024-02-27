import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{


    private int FPS = 120;
    private Player p1 = new Player(0, 0, 10, 10, "pacman_animation.png");
    private int animTick = 0, animSpeed = 15, animIndex = 0;
    private final int TILE_WIDTH = 32, TILE_HEIGHT = 32;
    private BufferedImage tilesetParentImg;
    private BufferedImage[] tileset = new BufferedImage[17];
    Level l1 = new Level("level1.txt");

    InputStream is = getClass().getResourceAsStream("tileset.png");
    {
    try {
        this.tilesetParentImg = ImageIO.read(is);
    } catch (IOException e) {
            e.printStackTrace();
    } finally {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    for (int i=0; i<17; i++) {
        tileset[i] = tilesetParentImg.getSubimage(i*16, 0, 16, 14);
    }
}

    
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
        int[][] currentLevel = l1.getLevel();
        

        for (int i=0; i<25; i++) {
            for (int j=0; j<40; j++) {
                g.drawImage(tileset[currentLevel[i][j]], TILE_WIDTH*j, TILE_HEIGHT*i, TILE_WIDTH, TILE_HEIGHT, null);
            }
        }
        g.drawImage(p1.getSprite()[animIndex], pos[0], pos[1], 30, 30, null);
        

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
        Dimension d = new Dimension(1280, 800);
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
