import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player {
    private int[] position = new int[2];
    private int[] size = new int[2];
    private int[] speed = new int[]{0, 0};
    public int speedMultiplier = 2;
    private BufferedImage sprite;
    private BufferedImage animSprites[][] = new BufferedImage[4][4];
    private int lives = 5;
    private int score = 0;

    public Player (int x, int y, int sizex, int sizey, String imgname) {
        this.position[0] = x;
        this.position[1] = y;
        this.size[0] = sizex;
        this.size[1] = sizey;

        InputStream is = getClass().getResourceAsStream(imgname);
        try {
            this.sprite = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        for (int i=0; i<4; i++) {
            int j =0;
            for (j=0; j<3; j++) {
            animSprites[i][j] = sprite.getSubimage(4,  43*j + 43*i*3, 33, 43);
            }
            animSprites[i][j] = sprite.getSubimage(4,  43*(j-2) + 43*i*3, 33, 43);
        }

    }

    public int[] getSpeed () {
        return speed;
    }

    public void setSpeed(int[] s) {
        this.speed = s;
    }

    public void setPosition (int[] newPos) {
        position[0] = newPos[0];
        position[1] = newPos[1];
    }

    public int[] getPosition () {return position;}
    public int[] getSize() {return size;}
    public BufferedImage[] getSprite() {
        if ((speed[0] == 0 && speed[1] == 0) || (speed[0] == 1 && speed[1] == 0)) {
            return animSprites[0];
        } else if ((speed[0] == 0 && speed[1] == 1)) {
            return animSprites[1];
        } else if ((speed[0] == -1 && speed[1] == 0)) {
            return animSprites[2];
        } else {
            return animSprites[3];
        }
    }

    public void move () {
        this.position[0] += this.speed[0]*speedMultiplier; this.position[1] += this.speed[1]*speedMultiplier;
        if (this.position[0] < -32) {this.position[0] = 1280 + 30;}
        else if (this.position[0] > 1280 + 32) {this.position[0] = -30;}
    }

    public void move(int[] customSpeed) {
        this.position[0] += customSpeed[0]*speedMultiplier; this.position[1] += customSpeed[1]*speedMultiplier;
        if (this.position[0] < -32) {this.position[0] = 1280 + 30;}
        else if (this.position[0] > 1280 + 32) {this.position[0] = -30;}
    }

    public void moveBackwards() {
        this.position[0] -= this.speed[0] * speedMultiplier;
        this.position[1] -= this.speed[1] * speedMultiplier;
    }

    public void draw (Graphics g, int animIndex) {
        g.drawImage(getSprite()[animIndex], position[0], position[1], size[0], size[1], null);
    }

    public int getLives () {
        return lives;
    }

    public void loseLife () {
        lives--;
    }

    public void setLife (int i) {
        lives = i;
    }

    public int getScore () {
        return score;
    }

    public void setScore (int i) {
        score = i;
    }
}
