import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;
import java.awt.Graphics;

public class KeyboardInputs implements KeyListener {
    Player p;
    GamePanel g;
    public KeyboardInputs (Player q, GamePanel gp) {
        this.p = q;
        this.g = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int[] originalSpeed = this.p.getSpeed();
        int[] playerTile = Collision.getPlayerTile(p, g.TILE_HEIGHT, g.TILE_WIDTH);
        int[][] lvlMap = g.l1.getLevel();
        switch (this.g.getGameState()) {
        case "level":
        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
            if (originalSpeed[0] == 0) {
                this.p.setSpeed(new int[]{0, -1});
                break;
            }

                if (lvlMap[playerTile[1] - 1][playerTile[0]] == 8) {
                    p.setPosition(new int[]{playerTile[0] * g.TILE_HEIGHT, playerTile[1] * g.TILE_WIDTH});
                    this.p.setSpeed(new int[]{0, -1});
                }
                
                break;
            
            case KeyEvent.VK_S:
            if (originalSpeed[0] == 0) {
                this.p.setSpeed(new int[]{0, 1});
                break;
            }
            if (lvlMap[playerTile[1] + 1][playerTile[0]] == 8) {
                p.setPosition(new int[]{playerTile[0] * g.TILE_HEIGHT, playerTile[1] * g.TILE_WIDTH});
                this.p.setSpeed(new int[]{0, 1});
            }
                break;

            case KeyEvent.VK_A:
            if (originalSpeed[1] == 0) {
                this.p.setSpeed(new int[]{-1, 0});
                break;
            }
            if (lvlMap[playerTile[1]][playerTile[0] - 1] == 8) {
                p.setPosition(new int[]{playerTile[0] * g.TILE_HEIGHT, playerTile[1] * g.TILE_WIDTH});
                this.p.setSpeed(new int[]{-1, 0});
            }
                break;

            case KeyEvent.VK_D:
            if (originalSpeed[1] == 0) {
                this.p.setSpeed(new int[]{1, 0});
                break;
            }
            if (lvlMap[playerTile[1]][playerTile[0] + 1] == 8) {
                p.setPosition(new int[]{playerTile[0] * g.TILE_HEIGHT, playerTile[1] * g.TILE_WIDTH});
                this.p.setSpeed(new int[]{1, 0});
            }
                break;
            
        }
        case "menu" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                this.g.setGameState("starting");
            }
            break;
        case "game over" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                this.g.setGameState("menu");
            }
            break;
        case "completed" :
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                this.g.setGameState("menu");
            }
            break;
    }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}