import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{


    private int FPS = 120;

    // Create the player 
    private Player p1 = new Player(32*35, 32*23, 28, 28, "./assets/pacman_animation.png");

    // Define variables for the animation
    private int animTick = 0, animSpeed = 15, animIndex = 0;

    // Define the width and height of each tile as number of pixels on screen
    private final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

    // Variable to manage what part of game we are in (Eg: menu, level, game over screen etc...)
    private String gameState = "menu";

    // Create Sound object to manage sounds
    private Sound s = new Sound();

    // Create the level
    Level l1 = new Level("l1.txt");
    
    // create the coins
    Coins cns =new Coins("cns.txt");

    // Create the enemies
    private Enemy blueEnemy = new Enemy(32*20, 32*12, 32, 32, "./assets/ghost/blue ghost/spr_ghost_blue_0.png");
    private Enemy orangeEnemy = new Enemy(32*21, 32*12, 32, 32, "./assets/ghost/orange ghost/spr_ghost_orange_0.png");
    private Enemy pinkEnemy = new Enemy(32*19, 32*12, 32, 32, "./assets/ghost/pink ghost/spr_ghost_pink_0.png");
    private Enemy redEnemy = new Enemy(32*18, 32*12, 32, 32, "./assets/ghost/red ghost/spr_ghost_red_0.png");
    private Enemy[] enemies = new Enemy[]{blueEnemy, orangeEnemy, pinkEnemy, redEnemy};

    // Load the tileset images
    private BufferedImage tilesetParentImg;
    private BufferedImage[] tileset = new BufferedImage[17];
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

            case "starting":
                l1.drawMap(g, tileset, TILE_HEIGHT, TILE_WIDTH);
                p1.draw(g, animIndex);

                for (int i=0; i<4; i++) {
                    enemies[i].draw(g);
                }
                playSE(1);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                setGameState("level");
                p1.setLife(3);
                p1.setScore(0);
                playBG();

            case "level":
                l1.drawMap(g, tileset, TILE_HEIGHT, TILE_WIDTH); // Draw all the tiles on the screen
                cns.drawMap(g, TILE_HEIGHT, TILE_WIDTH);
                p1.draw(g, animIndex); // Draw the player on the screen
                

                // Draw Enemies on screen
                for (int i=0; i<4; i++) {
                    enemies[i].draw(g);
                }

                // Draw UI on screen (Eg: Lives remaining, score, etc...)
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                g.setColor(Color.WHITE);
                g.drawString("Lives: " + p1.getLives(), 32*0, 32*25 - 5);
                g.drawString("Score: " + p1.getScore(), 32*35, 32*25 - 5);

            break;

            case "game over":
                g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
                g.setColor(Color.WHITE);
                g.drawString("Shameful Honestly", 275, 200);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
                if (animIndex <= 1) {
                    g.drawString("Press Enter to continue", 400, 700);
                }
                g.drawString("Your score: " + p1.getScore(), 400, 500);
        }
    }

    public void update() {
        updateAnimationTick();
        if (p1.getLives() == 0) {
            setGameState("game over");
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        update();
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
