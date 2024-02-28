import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{


    private int FPS = 120;
    private Player p1 = new Player(32*35, 32*23, 28, 28, "./assets/pacman_animation.png");
    private int animTick = 0, animSpeed = 15, animIndex = 0;
    private final int TILE_WIDTH = 32, TILE_HEIGHT = 32;
    private BufferedImage tilesetParentImg;
    private BufferedImage[] tileset = new BufferedImage[17];
    private String gameState = "menu";
    private Sound s = new Sound();

    Level l1 = new Level("l1.txt");

    private Enemy blueEnemy = new Enemy(32*20, 32*12, 32, 32, "./assets/ghost/blue ghost/spr_ghost_blue_0.png");
    private Enemy orangeEnemy = new Enemy(32*21, 32*12, 32, 32, "./assets/ghost/orange ghost/spr_ghost_orange_0.png");
    private Enemy pinkEnemy = new Enemy(32*19, 32*12, 32, 32, "./assets/ghost/pink ghost/spr_ghost_pink_0.png");
    private Enemy redEnemy = new Enemy(32*18, 32*12, 32, 32, "./assets/ghost/red ghost/spr_ghost_red_0.png");

    private Enemy[] enemies = new Enemy[]{blueEnemy, orangeEnemy, pinkEnemy, redEnemy};

    InputStream is = getClass().getResourceAsStream("./assets/tileset.png");
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
        addKeyListener(new KeyboardInputs(p1, this));
        
    }

    public void render(Graphics g) {

        switch (gameState) {
            case "menu":
                if (animIndex <= 1) {
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
                    g.setColor(Color.WHITE);
                    g.drawString("Press Enter to start game", 400, 500);
                }
                InputStream is = getClass().getResourceAsStream("./assets/logo.png");
                BufferedImage logo = null;
                try {
                    logo = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                g.drawImage(logo, 350, 100, null);
                break;

            case "level":
            l1.drawMap(g, tileset, TILE_HEIGHT, TILE_WIDTH);
            p1.draw(g, animIndex);

            for (int i=0; i<4; i++) {
                enemies[i].draw(g);
            }
            break;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateAnimationTick();
        
        render(g);

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

    void playBG() {
        s.setFile(0);
        s.play();
        s.loop();
    }

    void playSE(int i) {
        s.setFile(i);
        s.play();
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gs) {
        gameState = gs;
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
