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
    public Player p1 = new Player(32*35, 32*23, 28, 28, "./assets/pacman_animation.png");

    // Define variables for the animation
    private int animTick = 0, animSpeed = 15, animIndex = 0;
    private int aiTimer = 0;

    // Define the width and height of each tile as number of pixels on screen
    public final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

    // Variable to manage what part of game we are in (Eg: menu, level, game over screen etc...)
    private String gameState = "level";

    // Create Sound object to manage sounds
    private Sound s = new Sound();

    // Create the level
    static Level l1 = new Level("l1.txt");
    public static int[][] lvlMap = l1.getLevel();
    
    // create the coins
    Coins cns =new Coins("cns.txt");

    // Create the enemies
    private Enemy blueEnemy = new Enemy(32*19 - 10, 32*10, 32, 32, "./assets/ghost/blue ghost/spr_ghost_blue_0.png", new int[]{0, 0});
    private Enemy orangeEnemy = new Enemy(32*19 - 10, 32*10, 32, 32, "./assets/ghost/orange ghost/spr_ghost_orange_0.png", new int[]{39, 25});
    private Enemy pinkEnemy = new Enemy(32*19 - 10, 32*10, 32, 32, "./assets/ghost/pink ghost/spr_ghost_pink_0.png", new int[]{39, 0});
    private Enemy redEnemy = new Enemy(32*19 - 10, 32*10, 32, 32, "./assets/ghost/red ghost/spr_ghost_red_0.png", new int[]{0, 25});
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
                break;
            
            case "completed":
            g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
            g.setColor(Color.WHITE);
            g.drawString("You won, Congrats!!!!", 275, 200);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            if (animIndex <= 1) {
                g.drawString("Press Enter to continue", 400, 700);
            }
            g.drawString("Your score: " + p1.getScore(), 400, 500);
            break;
        }
    }

    public void update() {
        updateAnimationTick();
        updateAITick();
        p1.move();
        int[] playerTile = Collision.getPlayerTile(p1, TILE_HEIGHT, TILE_WIDTH); // Approximate location of player in 40x25 grid
        for (int i=0; i<4; i++) {
            if (enemies[i].chase) {
                enemies[i].setTargetTile(playerTile);
            }
            enemies[i].move();
            int touchedEnemy = Collision.playerEnemyCollision(p1, enemies[i]);
            if (touchedEnemy == 1) {
                p1.loseLife();
                respawn();

                if (p1.getLives() == 0) {
                    setGameState("game over");
                }
                break;
            }
        }
        if (p1.getLives() == 0) {
            setGameState("game over");
        }
        
        
        // Check collision of player with a coin with functionality to collect coins.
        int[][] cmap = cns.getCoin();
        for (int i=Math.max(0, playerTile[1] - 2); i<Math.min(25, playerTile[1] + 2); i++) {
            for (int j=Math.max(0, playerTile[0] - 2); j<Math.min(40, playerTile[0] + 2); j++) {
                if(cmap[i][j]==1){
                    int collided = Collision.playerCoinCollision(p1, i, j, TILE_WIDTH, TILE_HEIGHT);
                    if (collided == 1) {
                        cns.changeCoin(i, j, 0);
                        p1.setScore(p1.getScore() + 200);
                        break;
                    }
                }
            }
        }

        boolean coinExists = false;
        cmap = cns.getCoin();
        for (int i=0; i<25; i++) {
            for (int j=0; j<40; j++) {
                if (cmap[i][j] == 1) {
                    coinExists = true;
                }
            }
        }
        if (!coinExists) {
            setGameState("completed");
        }

        // Check collision of player with tiles
        
        for (int i=Math.max(0, playerTile[1] - 2); i<Math.min(25, playerTile[1] + 2); i++) {
            for (int j=Math.max(0, playerTile[0] - 2); j<Math.min(40, playerTile[0] + 2); j++) {
                if(lvlMap[i][j] != 8){
                    int collided = Collision.playerTileCollision(p1, i, j, TILE_WIDTH, TILE_HEIGHT);
                    if (collided == 1) {
                        //p1.move(new int[]{p1.getSpeed()[0] * -1, p1.getSpeed()[1] * -1});
                        p1.moveBackwards();
                        //p1.setSpeed(new int[]{0, 0});
                    }
                }
            }
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

    private void updateAITick() {
        aiTimer++;
        if (aiTimer >= 120*10) {
            for (int i=0; i<4; i++) {
                if (enemies[i].scatter) {
                    enemies[i].scatter = false;
                    enemies[i].chase = true;
                    aiTimer = 0;
                }
            }
        } else if (aiTimer >= 120*5) {
            for (int i=0; i<4; i++) {
            if (enemies[i].chase) {
                enemies[i].chase = false;
                enemies[i].scatter = true;
                aiTimer = 0;
            }
        }
        }
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

    public void respawn() {
        p1.setPosition(new int[]{32*35, 32*23});
        for (int i=0; i<4; i++) {
            enemies[i].setPosition(new int[]{32*19 - 10, 32*10});
        }
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
                repaint();
                last = now;
            }
        }
    }
}
