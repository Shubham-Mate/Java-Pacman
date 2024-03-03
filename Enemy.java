import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Enemy {
    private int[] position = new int[2];
    private int[] size = new int[2];
    private int[] speed = new int[]{0, 1};
    private BufferedImage sprite;
    private BufferedImage scaredSprite;
    public boolean scared = false;
    public boolean scatter = true;
    public boolean chase = false;
    private int[] targetTile;
    private int[] scatterTargetTile;
    public int speedMultiplier = 2;

    public Enemy (int x, int y, int sizex, int sizey, String imgname, int[] target) {
        this.position[0] = x;
        this.position[1] = y;
        this.size[0] = sizex;
        this.size[1] = sizey;
        targetTile = target;
        scatterTargetTile = target;

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

        InputStream is2 = getClass().getResourceAsStream("./assets/ghost/gost afraid/spr_afraid_1.png");
        try {
            this.scaredSprite = ImageIO.read(is2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    BufferedImage getSprite() {
        if (!scared) {
        return this.sprite;
        } else {
            return this.scaredSprite;
        }
    }
    public int[] getPosition () {return position;}
    public int[] getSpeed () {return speed;}

    public void draw (Graphics g) {
        g.drawImage(getSprite(), position[0], position[1], size[0], size[1], null);
    }

    public void setPosition (int[] newPos) {
        position[0] = newPos[0];
        position[1] = newPos[1];
    }

    public int[] getSize() {return size;}

    public void setSpeed(int[] s) {
        this.speed = s;
    }

    public void move() {
        int[] copyOfSpeed = speed;
        int[] possibleDirections = new int[]{1, 1, 1, 1};
        int[] enemyTile = Collision.getEnemyTile(this, 32, 32);

        if (scatter) {
            targetTile = scatterTargetTile;
        }
        
        if (speed[0] == 0 && speed[1] == 1) {possibleDirections[0] = 0;}
        else if (speed[0] == 0 && speed[1] == -1) {possibleDirections[1] = 0;}
        else if (speed[0] == 1 && speed[1] == 0) {possibleDirections[2] = 0;}
        else if (speed[0] == -1 && speed[1] == 0) {possibleDirections[3] = 0;}

        for (int i=Math.max(0, enemyTile[1] - 2); i<Math.min(25, enemyTile[1] + 2); i++) {
            for (int j=Math.max(0, enemyTile[0] - 2); j<Math.min(40, enemyTile[0] + 2); j++) {
                if(GamePanel.lvlMap[i][j] != 8){
                    speed = new int[]{0, 1};
                    if (Collision.enemyTileCollision(this, i, j, 32, 32) == 1) {possibleDirections[1] = 0;}
                    speed = new int[]{0, -1};
                    if (Collision.enemyTileCollision(this, i, j, 32, 32) == 1) {possibleDirections[0] = 0;}
                    speed = new int[]{1, 0};
                    if (Collision.enemyTileCollision(this, i, j, 32, 32) == 1) {possibleDirections[3] = 0;}
                    speed = new int[]{-1, 0};
                    if (Collision.enemyTileCollision(this, i, j, 32, 32) == 1) {possibleDirections[2] = 0;}
                }
            }
        }


        float min_distance = 500000;
        int fin_direction = 0;

        if (possibleDirections[0] == 1) {
            float d = Collision.distance(position[0], position[1] - 1*speedMultiplier, targetTile[0] * 32, targetTile[1] * 32);
            if (d < min_distance) {
                min_distance = d;
                fin_direction = 0;
            }
        }
        if (possibleDirections[1] == 1) {
            float d = Collision.distance(position[0], position[1] + 1*speedMultiplier, targetTile[0] * 32, targetTile[1] * 32);
            if (d < min_distance) {
                min_distance = d;
                fin_direction = 1;
            }
        }
        if (possibleDirections[2] == 1) {
            float d = Collision.distance(position[0] - 1*speedMultiplier, position[1] , targetTile[0] * 32, targetTile[1] * 32);
            if (d < min_distance) {
                min_distance = d;
                fin_direction = 2;
            }
        }
        if (possibleDirections[3] == 1) {
            float d = Collision.distance(position[0] + 1*speedMultiplier, position[1], targetTile[0] * 32, targetTile[1] * 32);
            if (d < min_distance) {
                min_distance = d;
                fin_direction = 3;
            }
        }

        if (fin_direction == 0) {
            speed = new int[]{0, -1};
        } else if (fin_direction == 1) {
            speed = new int[]{0, 1};
        } else if (fin_direction == 2) {
            speed = new int[]{-1, 0};
        } else if (fin_direction == 3) {
            speed = new int[]{1, 0};
        }
        position[0] += speed[0]*speedMultiplier;
        position[1] += speed[1]*speedMultiplier;

        if (this.position[0] < -32) {this.position[0] = 1280 + 30;}
        else if (this.position[0] > 1280 + 32) {this.position[0] = -30;}
    }

    public void setTargetTile(int[] tt) {
        targetTile = tt;
    }
}
