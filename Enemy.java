import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Enemy {
    private int[] position = new int[2];
    private int[] size = new int[2];
    private int[] speed = new int[]{0, 0};
    private BufferedImage sprite;
    private boolean scared = false;
    private boolean scatter = false;
    private boolean chase = false;
    private int[] targetTile;

    public Enemy (int x, int y, int sizex, int sizey, String imgname) {
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
    }

    BufferedImage getSprite() {return this.sprite;}
    public int[] getPosition () {return position;}

    public void draw (Graphics g) {
        g.drawImage(getSprite(), position[0], position[1], size[0], size[1], null);
    }
}
