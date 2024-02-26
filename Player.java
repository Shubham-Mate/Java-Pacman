import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player {
    private int[] position = new int[2];
    private int[] size = new int[2];
    private int[] speed = new int[]{0, 0};
    private BufferedImage sprite;
    private BufferedImage animSprites[][] = new BufferedImage[4][4];

    public Player (int x, int y, int sizex, int sizey, String imgname) {
        this.position[0] = (x);
        this.position[1] = (y);
        this.size[0] = (sizex);
        this.size[1] = (sizey);

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

    public void setSpeed(int[] s) {
        this.speed = s;
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
        this.position[0] += this.speed[0]; this.position[1] += this.speed[1];
    }
}
